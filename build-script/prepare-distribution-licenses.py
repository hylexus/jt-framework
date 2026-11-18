#!/usr/bin/env python3
"""Prepare a companion license/source bundle from an actual Spring Boot JAR.

Called only by the optional Gradle prepareDistributionLicenses task. Generated
materials belong in build/, not the repo. Original library notices stay in their
unmodified nested JARs; supplemental source archives retain their original headers.
The output requires review before distribution; it is not license approval.
"""

import argparse
import json
from pathlib import Path, PurePosixPath
import re
import shutil
import subprocess
import tempfile
import zipfile


def fetch(cache, relative, url, offline):
    path = cache / relative
    if not path.exists():
        if offline:
            raise RuntimeError(f"Missing release material in offline cache: {url}")
        path.parent.mkdir(parents=True, exist_ok=True)
        with tempfile.NamedTemporaryFile(dir=path.parent, suffix=".tmp", delete=False) as file:
            temporary = Path(file.name)
        try:
            subprocess.run(["curl", "--fail", "--silent", "--show-error", "--location",
                            "--max-time", "30", "--retry", "2", url, "--output", str(temporary)], check=True)
            temporary.replace(path)
        finally:
            temporary.unlink(missing_ok=True)
    return path.read_bytes()


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--input", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    parser.add_argument("--cache", type=Path, required=True)
    parser.add_argument("--offline", action="store_true")
    args = parser.parse_args()
    config = json.loads(args.input.read_text())
    output = args.output
    if (output.name, output.parent.name, output.parent.parent.name) != ("distribution-licenses", "reports", "build"):
        raise ValueError("Output must be a module's build/reports/distribution-licenses directory")
    if output.exists():
        shutil.rmtree(output)
    output.mkdir(parents=True)

    def save(relative, data):
        path = PurePosixPath(relative)
        if path.is_absolute() or ".." in path.parts:
            raise ValueError(f"Unsafe material path: {relative}")
        target = output / path
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_bytes(data)

    def release_material(relative, url):
        save("licenses/" + relative, fetch(args.cache, relative, url, args.offline))

    source_count, releases = 0, set()
    archive_path = Path(config["archive"])
    with zipfile.ZipFile(archive_path) as boot:
        entries = set(boot.namelist())
        for artifact in config["artifacts"]:
            if "BOOT-INF/lib/" + artifact["filename"] not in entries:
                continue
            group, module, version = artifact["coordinate"].split(":")
            releases.add((group, version))
            if artifact.get("source"):
                source = Path(artifact["source"])
                is_jump3r = (group, module) == ("de.sciss", "jump3r")
                # Complete sources preserve jsr305, SLF4J and Bouncy Castle notices
                # without scanning individual Java files or rewriting their headers.
                save("sources/" + ("jump3r/" if is_jump3r else "") + source.name, source.read_bytes())
                source_count += 1
                if is_jump3r:
                    root = Path(__file__).resolve().parent.parent
                    for material in sorted((root / "licenses/jump3r").iterdir()):
                        if material.is_file():
                            save("sources/jump3r/" + material.name, material.read_bytes())
                    save("licenses/LGPL-2.1.txt", (root / "licenses/LGPL-2.1.txt").read_bytes())

    # These releases do not include all original terms in their Maven JARs.
    for group, version in sorted(releases):
        if group == "ch.qos.logback":
            release_material(f"logback/{version}/LICENSE.txt",
                             f"https://raw.githubusercontent.com/qos-ch/logback/v_{version}/LICENSE.txt")
        if group == "io.netty":
            url = f"https://raw.githubusercontent.com/netty/netty/netty-{version}/"
            notice = fetch(args.cache, f"netty/{version}/NOTICE.txt", url + "NOTICE.txt", args.offline)
            for name in sorted({"LICENSE.txt", "NOTICE.txt"} | set(re.findall(r"license/[\w.-]+\.txt", notice.decode()))):
                release_material(f"netty/{version}/{name}", url + name)

    for license_id, group in [("EPL-1.0", "ch.qos.logback"),
                              ("CC-BY-2.5", "com.google.code.findbugs"), ("CC-BY-2.5", "net.jcip")]:
        if any(release_group == group for release_group, _ in releases):
            url = f"https://raw.githubusercontent.com/spdx/license-list-data/v3.27.0/text/{license_id}.txt"
            save(f"licenses/{license_id}.txt", fetch(args.cache, f"spdx/{license_id}.txt", url, args.offline))

    save("THIRD-PARTY-NOTICES.txt", Path(config["report"]).read_bytes())
    notes = ["Supplemental materials for " + archive_path.name,
             "Distribute this directory/ZIP alongside the unchanged application JAR.",
             "Original notices remain in the nested libraries; complete matching source archives",
             "and release notices supplement them. Project JARs retain their own covered sources.",
             "The dependency report may include compile-only entries; review the actual distribution.",
             "If sources/jump3r/ is present, read its README.md and UPSTREAM-NOTICES.txt.",
             "Review applicable licenses and unresolved items before release; generation is not authorization.", "",
             "EPL programs are supplied AS IS, without contributor warranties or conditions, including title,",
             "non-infringement, merchantability and fitness for a particular purpose.",
             "All contributor liability for damages is excluded to the extent permitted by applicable law and license.",
             "Additional warranties/support/different terms are the distributor's responsibility, not the contributors'.",
             "Retain applicable original terms and matching-source availability when redistributing."]
    save("README.txt", ("\n".join(notes) + "\n").encode("utf-8"))
    shutil.make_archive(str(output), "zip", output)
    print(f"Prepared {source_count} matching source archives and supplemental release notices in {output}.")


if __name__ == "__main__":
    main()
