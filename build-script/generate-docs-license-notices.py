#!/usr/bin/env python3
"""Generate the documentation site's notices from its reviewed browser inventory."""

import argparse
from concurrent.futures import ThreadPoolExecutor
import hashlib
import importlib.util
import json
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parent.parent
INVENTORY = ROOT / "licenses/docs-frontend-inventory.json"
OUTPUT = ROOT / "docs/src/.vuepress/public/THIRD-PARTY-NOTICES.txt"
spec = importlib.util.spec_from_file_location("frontend_notices", ROOT / "build-script/generate-frontend-license-notices.py")
frontend = importlib.util.module_from_spec(spec)
sys.dont_write_bytecode = True
spec.loader.exec_module(frontend)


def local_asset_notice(asset):
    for record in [*asset["files"], *asset["licenseFiles"]]:
        if hashlib.sha256((ROOT / record["path"]).read_bytes()).hexdigest() != record["sha256"]:
            raise ValueError(f"Local browser asset or license changed; review {record['path']}")
    licenses = "\n\n".join((ROOT / record["path"]).read_text() for record in asset["licenseFiles"])
    return "\n".join([
        f"{asset['name']} {asset['version']}",
        f"License for the included SVG icons: {asset['license']}",
        asset["attribution"],
        f"Project: {asset['projectUrl']}",
        f"Original archive: {asset['sourceArchive']['url']}",
        asset["changes"],
        "The upstream license below also describes fonts and code; this subset includes only SVG icons.",
        "Original file paths, archive integrity and per-file hashes: licenses/docs-frontend-inventory.json",
        "", licenses,
    ])


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true")
    args = parser.parse_args()
    inventory = json.loads(INVENTORY.read_text())
    if hashlib.sha256((ROOT / inventory["lockfile"]).read_bytes()).hexdigest() != inventory["lockfileSha256"]:
        raise ValueError("Docs lockfile changed; review the browser inventory before regenerating notices")
    with ThreadPoolExecutor(max_workers=6) as executor:
        entries = list(executor.map(frontend.fetch_package, inventory["packages"]))
    entries.extend(local_asset_notice(asset) for asset in inventory.get("localAssets", []))
    header = """Third-party notices for the jt-framework documentation site
==========================================================

Project-authored site code is provided under MIT; see LICENSE.txt.
Third-party components and SVG icons retain their own licenses and notices.

This inventory covers packages contributing browser JavaScript or CSS to the
reviewed VuePress production build. It excludes the server renderer and build
utilities, and includes local SVG icons, the copied Search Pro worker and its
embedded SlimSearch and VuePress helper code. The browser build checks local
asset and license hashes, package versions and
the reviewed worker's source map against this
inventory and publishes license-build-provenance.json beside these notices.
See licenses/docs-frontend-inventory.json in the repository for locked versions,
archive integrity values and additional notices for bundled code.

"""
    content = (header + "\n\n".join(entries) + "\n").encode("utf-8")
    digest = hashlib.sha256(content).hexdigest()
    if args.check:
        if not OUTPUT.exists() or OUTPUT.read_bytes() != content or inventory.get("noticesSha256") != digest:
            print("Docs license materials are stale; run python3 build-script/generate-docs-license-notices.py", file=sys.stderr)
            return 1
        print(f"Checked docs notices for {len(inventory['packages'])} browser packages and {len(inventory.get('localAssets', []))} local asset sets")
    else:
        OUTPUT.write_bytes(content)
        inventory["noticesSha256"] = digest
        INVENTORY.write_text(json.dumps(inventory, ensure_ascii=False, indent=2) + "\n")
        print(f"Wrote docs notices for {len(inventory['packages'])} browser packages and {len(inventory.get('localAssets', []))} local asset sets")
    return 0


if __name__ == "__main__":
    sys.exit(main())
