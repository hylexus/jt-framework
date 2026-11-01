#!/usr/bin/env python3
"""Check license material packaging and synchronization; this is not license approval."""

import argparse
from collections import Counter
import hashlib
import json
from pathlib import Path, PurePosixPath
import sys
import zipfile

ROOT = Path(__file__).resolve().parent.parent
UI = ROOT / "dashboard/jt-dashboard-server-ui-vue3"
SERVER_UI = ROOT / "dashboard/jt-dashboard-server/src/main/resources/static/client"
# Keep aligned with the repository-only exclusions in build.gradle.kts.
REPOSITORY_ONLY_LICENSE_RECORDS = {
    "source-audit.json", "frontend-inventory.json", "docs-frontend-inventory.json",
}
PROJECT_MATERIALS = {
    "META-INF/LICENSE.txt": ROOT / "LICENSE",
    "META-INF/NOTICE": ROOT / "NOTICE",
    "META-INF/THIRD-PARTY-SOURCE-NOTICES.txt": ROOT / "THIRD-PARTY-SOURCE-NOTICES.txt",
    "META-INF/THIRD-PARTY-FRONTEND-NOTICES.txt": ROOT / "THIRD-PARTY-FRONTEND-NOTICES.txt",
}
UI_MATERIALS = {
    "LICENSE.txt": UI / "LICENSE",
    "NOTICE.txt": UI / "public/NOTICE.txt",
    "THIRD-PARTY-NOTICES.txt": ROOT / "THIRD-PARTY-FRONTEND-NOTICES.txt",
}


def sha256(data):
    return hashlib.sha256(data).hexdigest()


def material_files(directory):
    return sorted(path for path in directory.rglob("*") if path.is_file()
                  and "__pycache__" not in path.parts and path.suffix != ".pyc")


class Checks:
    def __init__(self):
        self.errors = []
        self.summaries = []

    def require(self, condition, message):
        if not condition:
            self.errors.append(message)
        return condition

    def dashboard(self, directory):
        for filename, source in UI_MATERIALS.items():
            target = directory / filename
            if self.require(target.is_file(), f"{directory}: missing {filename}"):
                self.require(target.read_bytes() == source.read_bytes(),
                             f"{target}: stale license material; rebuild and sync the Dashboard UI")
        manifest = directory / "license-build-provenance.json"
        if not self.require(manifest.is_file(), f"{directory}: missing {manifest.name}"):
            return
        inventory_path = ROOT / "licenses/frontend-inventory.json"
        inventory_data = inventory_path.read_bytes()
        inventory = json.loads(inventory_data)
        lock_hash = sha256((ROOT / inventory["lockfile"]).read_bytes())
        provenance = json.loads(manifest.read_bytes())
        self.require(lock_hash == inventory["lockfileSha256"], "Frontend lockfile differs from reviewed inventory")
        for key, expected in {
            "lockfile": inventory["lockfile"], "lockfileSha256": lock_hash,
            "inventorySha256": sha256(inventory_data),
            "thirdPartyNoticesSha256": sha256(UI_MATERIALS["THIRD-PARTY-NOTICES.txt"].read_bytes()),
        }.items():
            self.require(provenance.get(key) == expected, f"{manifest}: stale {key}")
        recorded = set()
        for asset in provenance.get("assets", []):
            name = asset["file"]
            relative = PurePosixPath(name)
            if not self.require(not relative.is_absolute() and ".." not in relative.parts,
                                f"{manifest}: invalid asset path {name}"):
                continue
            self.require(name not in recorded, f"{manifest}: duplicate asset {name}")
            recorded.add(name)
            target = directory / name
            if self.require(target.is_file(), f"{directory}: missing recorded asset {name}"):
                self.require(sha256(target.read_bytes()) == asset["sha256"], f"{target}: asset hash differs from build record")
        required = {"index.html", "favicon.svg", *UI_MATERIALS}
        self.require(required <= recorded, f"{manifest}: missing required asset records {sorted(required - recorded)}")
        actual = {str(path.relative_to(directory).as_posix()) for path in directory.rglob("*")
                  if path.is_file() and path != manifest}
        self.require(actual == recorded, f"{directory}: files and build record differ {sorted(actual ^ recorded)}")
        self.summaries.append(f"Checked Dashboard output: {directory} ({len(recorded)} assets)")

    def jar(self, path):
        if not self.require(path.is_file(), f"Missing JAR: {path}"):
            return
        expected = dict(PROJECT_MATERIALS)
        for source in material_files(ROOT / "licenses"):
            relative = source.relative_to(ROOT / "licenses").as_posix()
            if relative not in REPOSITORY_ONLY_LICENSE_RECORDS:
                expected["META-INF/licenses/" + relative] = source
        expected["META-INF/licenses/checkstyle/jt-framework-checkstyle.xml"] = ROOT / "build-script/checkstyle/checkstyle.xml"
        module_build = path.parent.parent
        expected["META-INF/THIRD-PARTY-NOTICES.txt"] = module_build / "reports/dependency-license/THIRD-PARTY-NOTICES.txt"
        prepared = module_build / "generated/third-party-sources"
        for source in material_files(prepared):
            expected["META-INF/third-party-sources/" + source.relative_to(prepared).as_posix()] = source
        with zipfile.ZipFile(path) as archive:
            entries = Counter(archive.namelist())
            duplicates = [name for name, count in entries.items() if count > 1]
            self.require(not duplicates, f"{path}: duplicate ZIP entries {duplicates}")
            for relative in sorted(REPOSITORY_ONLY_LICENSE_RECORDS):
                name = "META-INF/licenses/" + relative
                self.require(name not in entries, f"{path}: repository-only record included: {name}")
            binary_jar = not path.name.endswith(("-sources.jar", "-javadoc.jar"))
            resource_prefix = "BOOT-INF/classes/" if "BOOT-INF/classes/" in entries else ""
            if binary_jar and module_build.parent.name == "jt-dashboard-server":
                for source in material_files(SERVER_UI):
                    expected[resource_prefix + "static/client/" + source.relative_to(SERVER_UI).as_posix()] = source
            # Standalone sample pages use their directory's MIT and third-party notices.
            sample_static = module_build.parent / "src/main/resources/static"
            if binary_jar and (sample_static / "LICENSE.txt").is_file():
                for name in ("LICENSE.txt", "THIRD-PARTY-NOTICES.txt"):
                    expected[resource_prefix + "static/" + name] = sample_static / name
            for name, source in expected.items():
                if not self.require(source.is_file(), f"{path}: missing reference material {source}"):
                    continue
                if self.require(name in entries, f"{path}: missing {name}"):
                    self.require(archive.read(name) == source.read_bytes(), f"{path}: stale {name}")
        self.summaries.append(f"Checked JAR: {path} ({len(expected)} materials/assets)")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--report", type=Path, help="Write the check log (CI uses build/reports/)")
    commands = parser.add_subparsers(dest="command", required=True)
    dashboard = commands.add_parser("dashboard", help="Check a fresh UI build and the server's committed UI")
    dashboard.add_argument("directory", type=Path)
    jars = commands.add_parser("jars", help="Check the exact JAR paths supplied by Gradle")
    jars.add_argument("archives", type=Path, nargs="+")
    args = parser.parse_args()
    checks = Checks()
    targets = [args.directory.resolve(), SERVER_UI] if args.command == "dashboard" else args.archives
    for target in targets:
        try:
            if args.command == "dashboard":
                checks.dashboard(target)
            else:
                checks.jar(target)
        except (OSError, ValueError, KeyError, TypeError, zipfile.BadZipFile) as error:
            checks.errors.append(f"{target}: {error}")
    lines = checks.summaries + ["ERROR: " + error for error in checks.errors]
    lines.append(f"License material checks {'FAILED' if checks.errors else 'PASSED'} ({len(targets)} outputs, {len(checks.errors)} errors)")
    log = "\n".join(lines) + "\n"
    print(log, end="")
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(log, encoding="utf-8")
    return 1 if checks.errors else 0


if __name__ == "__main__":
    sys.exit(main())
