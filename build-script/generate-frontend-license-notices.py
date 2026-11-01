#!/usr/bin/env python3
"""Refresh frontend notices from the reviewed, integrity-pinned inventory."""

import argparse
import base64
from concurrent.futures import ThreadPoolExecutor
import hashlib
import io
import json
from pathlib import Path
import re
import sys
import tarfile
import urllib.parse
import urllib.request

ROOT = Path(__file__).resolve().parent.parent
INVENTORY = ROOT / "licenses/frontend-inventory.json"
SAMPLE_FRONTENDS = (
    "samples/jt-1078-server-sample-webflux-boot3/src/main/resources/static",
    "samples/jt-1078-server-sample-webmvc-boot3/src/main/resources/static",
    "samples/jt-808-server-sample-annotation/src/main/resources/static",
)


def read_local_notice(source):
    material = (ROOT / source["noticeFile"]).resolve()
    if not material.is_relative_to((ROOT / "licenses").resolve()):
        raise ValueError("Supplemental notice must be in licenses/")
    data = material.read_bytes()
    if hashlib.sha256(data).hexdigest() != source["sha256"]:
        raise ValueError(f"Supplemental notice changed: {source['noticeFile']}")
    return data.decode("utf-8")


def read_license_source(source):
    if "localFile" in source:
        return read_local_notice({"noticeFile": source["localFile"], "sha256": source["sha256"]})
    with urllib.request.urlopen(source["url"], timeout=45) as response:
        data = response.read()
    if hashlib.sha256(data).hexdigest() != source["sha256"]:
        raise ValueError(f"Upstream license changed: {source['url']}")
    return data.decode("utf-8")


def fetch_package(package):
    name, version = package["name"], package["version"]
    encoded_name = urllib.parse.quote(name, safe="@/")
    url = f"https://registry.npmjs.org/{encoded_name}/-/{name.split('/')[-1]}-{version}.tgz"
    cache = ROOT / "build/frontend-license-cache" / (encoded_name.replace("/", "_") + "-" + version + ".tgz")
    cache.parent.mkdir(parents=True, exist_ok=True)
    if cache.exists():
        data = cache.read_bytes()
    else:
        with urllib.request.urlopen(url, timeout=45) as response:
            data = response.read()
    algorithm, expected = package["integrity"].split("-", 1)
    actual = base64.b64encode(hashlib.new(algorithm, data).digest()).decode()
    if actual != expected:
        raise ValueError(f"Integrity mismatch: {name}@{version}")
    cache.write_bytes(data)
    texts = []
    with tarfile.open(fileobj=io.BytesIO(data), mode="r:gz") as archive:
        # Older @types packages use their package name as the archive root.
        candidates = [entry for entry in archive.getmembers() if entry.isfile()
                      and Path(entry.name).name == "package.json"]
        metadata_entry = min(candidates, key=lambda entry: len(Path(entry.name).parts))
        package_root = Path(metadata_entry.name).parent
        metadata = json.load(archive.extractfile(metadata_entry))
        for entry in archive.getmembers():
            path = Path(entry.name)
            normalized = path.name.lower()
            # Keep the publisher's root license and notice files, including
            # notices for bundled code (e.g. TypeScript's third-party notices).
            if entry.isfile() and path.parent == package_root and (
                normalized.startswith(("license", "licence", "notice", "thirdpartynotice", "copyright"))
            ):
                texts.append((entry.name, archive.extractfile(entry).read().decode("utf-8")))
    if not texts and "licenseSource" in package:
        source = package["licenseSource"]
        texts.append((source["url"], read_license_source(source)))
    if not texts and "licenseSupplement" in package:
        source = package["licenseSupplement"]
        if metadata.get("license") != source["declaredLicense"]:
            raise ValueError(f"Publisher license differs from reviewed supplement: {name}@{version}")
        texts.append(("Supplemental MIT terms: " + source["noticeFile"],
                      read_local_notice(source)))
    if not texts and "missingLicenseText" in package:
        texts.append(("Publisher metadata; original license text unresolved",
                      package["missingLicenseText"]))
    if not texts:
        raise ValueError(f"No license text in published archive: {name}@{version}")
    for source in package.get("additionalLicenseSources", []):
        texts.append((source["description"] + ": " + source["url"], read_license_source(source)))
    license_id = metadata.get("license", metadata.get("licenses", "Unknown"))
    if not isinstance(license_id, str):
        license_id = json.dumps(license_id, ensure_ascii=False)
    lines = [f"{name}@{version}", f"Declared license: {license_id}", f"Source: {url}"]
    for filename, content in sorted(texts):
        lines.extend(["", "----- " + filename + " -----", content.rstrip()])
    return "\n".join(lines)


def sample_notices(packages, entries):
    outputs = {}
    player_version = next(package["version"] for package in packages if package["name"] == "mpegts.js")
    for relative in SAMPLE_FRONTENDS:
        directory = ROOT / relative
        lines = [
            "jt-framework 示例前端第三方声明",
            "Third-party notices for the jt-framework sample frontend", "=" * 57, "",
            "本目录中维护者有权授权的原创 HTML/JS 按 MIT 提供；见 LICENSE.txt。",
            "Original HTML/JS in this directory that the maintainer has authority to license is provided under MIT; see LICENSE.txt.", "",
            "其他贡献按其原有许可及明确授权范围处理。",
            "Other contributions remain subject to their existing licenses and the scope of their express grants.", "",
            "第三方文件保留其原始许可和版权声明。",
            "Third-party files retain their original licenses and copyright notices.", "",
        ]
        external = sorted({url for page in directory.glob("*.html")
                           for url in re.findall(r'(?:src|href)=[\"\'](https?://[^\"\']+)', page.read_text())})
        if external:
            lines.append("这些页面加载的外部脚本／样式（未捆绑在本目录中）：")
            lines.append("External scripts/styles loaded by these pages (not bundled here):")
            lines.extend(external)
            lines.append("")
        player = directory / "js/mpegts.js"
        if player.exists():
            # The sample distributes the published UMD bundle, including its dependencies.
            with tarfile.open(ROOT / f"build/frontend-license-cache/mpegts.js-{player_version}.tgz", "r:gz") as archive:
                original = archive.extractfile("package/dist/mpegts.js").read()
            if player.read_bytes() != original:
                raise ValueError(f"Sample player differs from reviewed mpegts.js {player_version}: {relative}")
            lines.extend([
                f"本目录捆绑的 mpegts.js {player_version} 及其依赖声明：",
                f"Bundled mpegts.js {player_version} and its dependency notices:", "",
            ])
            lines.extend(entries[name] + "\n" for name in ("mpegts.js", "es6-promise", "webworkify-webpack"))
        outputs[directory / "THIRD-PARTY-NOTICES.txt"] = "\n".join(lines).rstrip() + "\n"
    return outputs


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true",
                        help="Check generated notices without changing them (downloads use build/ cache)")
    args = parser.parse_args()
    inventory = json.loads(INVENTORY.read_text())
    lock = ROOT / inventory["lockfile"]
    if hashlib.sha256(lock.read_bytes()).hexdigest() != inventory["lockfileSha256"]:
        raise ValueError("Frontend lockfile changed; review and update the inventory first")
    with ThreadPoolExecutor(max_workers=6) as executor:
        futures = [executor.submit(fetch_package, package) for package in inventory["packages"]]
        entries, errors = [], []
        for future in futures:
            try:
                entries.append(future.result())
            except Exception as error:
                errors.append(str(error))
        if errors:
            raise ValueError("\n".join(errors))
    source_entries = []
    for source in inventory.get("copiedSources", []):
        content = read_local_notice(source)
        source_entries.append("\n".join([
            "复制来源：" + source["name"],
            "Copied source: " + source["name"],
            "对照源码：" + source["source"],
            "Comparison source: " + source["source"],
            "本地文件：" + ", ".join(source["localFiles"]),
            "Local files: " + ", ".join(source["localFiles"]),
            "声明文件：" + source["noticeFile"],
            "Notice: " + source["noticeFile"],
            "", content.rstrip(),
        ]))
    player_version = next(package["version"] for package in inventory["packages"] if package["name"] == "mpegts.js")
    header = f"""jt-framework 前端第三方声明
Third-party frontend notices for jt-framework
==============================================

维护者有权授权的原创前端代码按 MIT 提供；其他贡献按其原有许可及明确授权范围处理。
Original frontend code that the maintainer has authority to license is provided under MIT; other contributions remain subject to their existing licenses and the scope of their express grants.

第三方组件保留下文收录的各自许可和声明。
Third-party components retain the licenses and notices reproduced below.

本清单覆盖 Dashboard 的直接及传递依赖、UnoCSS 图标数据、复制的模板，以及独立示例页面使用的 mpegts.js {player_version}。
This list includes the Dashboard dependency closure, UnoCSS icon data, copied templates and mpegts.js {player_version} used by the standalone sample pages.

版本、完整性校验依据及来源核查说明保存在 licenses/frontend-inventory.json 和 docs/license-source-audit.md 中。
Version/integrity evidence and source review notes are maintained in licenses/frontend-inventory.json and docs/license-source-audit.md.
"""
    outputs = {ROOT / "THIRD-PARTY-FRONTEND-NOTICES.txt":
               header + "\n\n" + "\n\n".join(entries + source_entries) + "\n"}
    outputs.update(sample_notices(inventory["packages"], {
        package["name"]: entry for package, entry in zip(inventory["packages"], entries)
    }))
    if args.check:
        stale = [str(path.relative_to(ROOT)) for path, content in outputs.items()
                 if not path.is_file() or path.read_bytes() != content.encode("utf-8")]
        if stale:
            print("Frontend notices are missing or stale:\n" + "\n".join(stale), file=sys.stderr)
            print("Run python3 build-script/generate-frontend-license-notices.py, then rebuild/sync the Dashboard UI.",
                  file=sys.stderr)
            return 1
        print(f"Checked {len(outputs)} notice files for {len(entries)} frontend packages and {len(source_entries)} copied-source records")
    else:
        for path, content in outputs.items():
            path.write_bytes(content.encode("utf-8"))
        print(f"Wrote notices for {len(entries)} frontend packages and {len(source_entries)} copied-source records")
    return 0


if __name__ == "__main__":
    sys.exit(main())
