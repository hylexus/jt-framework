#!/usr/bin/env python3
"""Prepare a companion license/source bundle from an actual Spring Boot JAR.

Called only by the optional Gradle prepareDistributionLicenses task. Generated
texts, downloaded release files and checksums belong in build/, not the repo.
The output requires review before distribution; it is not license approval.
"""

import argparse
import hashlib
import io
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


def original_notice(name):
    return (not name.endswith("/") and PurePosixPath(name).suffix.lower() not in
            (".class", ".java", ".js", ".css") and
            re.fullmatch(r"(?:license|licence|notice|copying|copyright)(?:[._-].*)?",
                         PurePosixPath(name).name, re.IGNORECASE))


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
    materials, records, warnings = {}, [], []

    def save(relative, data, source):
        path = PurePosixPath(relative)
        if path.is_absolute() or ".." in path.parts:
            raise ValueError(f"Unsafe material path: {relative}")
        target = output / path
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_bytes(data)
        materials[str(path)] = {"path": str(path), "sha256": hashlib.sha256(data).hexdigest(), "source": source}

    def standard_terms(license_id):
        relative = f"spdx/{license_id}.txt"
        url = f"https://raw.githubusercontent.com/spdx/license-list-data/v3.27.0/text/{license_id}.txt"
        save(f"licenses/{license_id}.txt", fetch(args.cache, relative, url, args.offline), url)

    archive_path = Path(config["archive"])
    with zipfile.ZipFile(archive_path) as boot:
        entries = set(boot.namelist())
        for artifact in config["artifacts"]:
            nested = "BOOT-INF/lib/" + artifact["filename"]
            if nested not in entries:
                continue  # Boot omits some metadata-only starter JARs.
            coordinate = artifact["coordinate"]
            group, module, version = coordinate.split(":")
            prefix = f"licenses/{group}/{module}/{version}"
            url = f"https://repo.maven.apache.org/maven2/{group.replace('.', '/')}/{module}/{version}/"
            data = boot.read(nested)
            record = {"coordinate": coordinate, "binary": artifact["filename"],
                      "sha256": hashlib.sha256(data).hexdigest(), "materials": []}
            with zipfile.ZipFile(io.BytesIO(data)) as library:
                for name in library.namelist():
                    if original_notice(name):
                        relative = prefix + "/" + name
                        save(relative, library.read(name), url + artifact["filename"] + "!/" + name)
                        record["materials"].append(relative)
                if group == "org.apache.tomcat.embed" and module == "tomcat-embed-core":
                    for name in library.namelist():
                        if name.endswith((".xsd", ".dtd")):
                            relative = f"sources/{module}-{version}-schemas/{name}"
                            save(relative, library.read(name), url + artifact["filename"] + "!/" + name)
            source_path = Path(artifact["source"]) if artifact.get("source") else None
            is_jsr305 = group == "com.google.code.findbugs" and module == "jsr305"
            if is_jsr305 and source_path is None:
                raise ValueError(f"Matching sources are required to retain jsr305's original CC-BY notices: {coordinate}")
            if source_path:
                source_url = url + source_path.name
                if group in ("ch.qos.logback", "jakarta.annotation", "net.jcip"):
                    relative = "sources/" + source_path.name
                    save(relative, source_path.read_bytes(), source_url)
                    record["sources"] = relative
                with zipfile.ZipFile(source_path) as source:
                    if is_jsr305:
                        notices = ["Original concurrent-annotation notices in " + coordinate,
                                   "Source archive: " + source_url,
                                   "The library's Apache-2.0 metadata does not replace these file-level notices.", ""]
                        for annotation in ("GuardedBy", "Immutable", "NotThreadSafe", "ThreadSafe"):
                            name = f"javax/annotation/concurrent/{annotation}.java"
                            header = re.match(rb"\s*(/\*[\s\S]*?\*/)", source.read(name))
                            if header is None or b"creativecommons.org/licenses/by/2.5" not in header.group(1):
                                raise ValueError(f"Review changed jsr305 source notice: {coordinate}!/{name}")
                            notices.extend([name, header.group(1).decode("utf-8"), ""])
                        relative = prefix + "/original-CC-BY-2.5-source-notices.txt"
                        save(relative, "\n".join(notices).encode("utf-8"), source_url)
                        record["materials"].append(relative)
                    if "org/bouncycastle/LICENSE.java" in source.namelist():
                        relative = prefix + "/LICENSE.java.txt"
                        save(relative, source.read("org/bouncycastle/LICENSE.java"), source_url + "!/org/bouncycastle/LICENSE.java")
                        record["materials"].append(relative)
                    if group == "org.slf4j":
                        headers = sorted({match.group(0) for name in source.namelist() if name.endswith(".java")
                                          for match in re.finditer(rb"/\*[\s\S]*?\*/", source.read(name))
                                          if b"Permission is hereby granted" in match.group(0)})
                        if headers:
                            relative = prefix + "/original-MIT-source-notices.txt"
                            save(relative, ("Source archive: " + source_url + "\n\n").encode() + b"\n\n".join(headers) + b"\n", source_url)
                            record["materials"].append(relative)
            if group == "ch.qos.logback":
                standard_terms("EPL-1.0")
                notice_url = f"https://raw.githubusercontent.com/qos-ch/logback/v_{version}/LICENSE.txt"
                relative = prefix + "/LICENSE.txt"
                save(relative, fetch(args.cache, f"logback/{version}/LICENSE.txt", notice_url, args.offline), notice_url)
                record["materials"].append(relative)
                record["selectedLicense"] = "EPL-1.0"
                if version not in ("1.2.12", "1.5.6"):
                    warnings.append(f"Review Logback {version}'s actual license alternatives before choosing EPL-1.0.")
            if group == "jakarta.annotation":
                record["selectedLicense"] = "EPL-2.0; original conditional secondary license retained"
            if group == "net.jcip":
                standard_terms("CC-BY-2.5")
                record["selectedLicense"] = "CC-BY-2.5; attribution is preserved in the supplied source"
            if is_jsr305:
                standard_terms("CC-BY-2.5")
                record["selectedLicense"] = "Apache-2.0; four concurrent annotations retain their original CC-BY-2.5 notices"
            if group == "io.netty" and version.startswith("4.1."):
                release_url = f"https://raw.githubusercontent.com/netty/netty/netty-{version}/"
                notice = fetch(args.cache, f"netty/{version}/NOTICE.txt", release_url + "NOTICE.txt", args.offline)
                paths = {"LICENSE.txt", "NOTICE.txt"} | set(re.findall(r"license/[\w.-]+\.txt", notice.decode()))
                for name in sorted(paths):
                    relative = f"licenses/netty/{version}/{name}"
                    save(relative, fetch(args.cache, f"netty/{version}/{name}", release_url + name, args.offline), release_url + name)
                    record["materials"].append(relative)
            if coordinate == "de.sciss:jump3r:1.0.5":
                warnings.append("jump3r 1.0.5: the upstream POM declares LGPL-2.1-or-later. FFT.java retains header comments concerning GPL origins and other historical rights matters. The reviewed materials do not establish how those comments relate to the upstream LGPL declaration for this file. See repository licenses/jump3r/UPSTREAM-NOTICES.txt for the original text and docs/license-1078-review.md for the reviewed evidence and unresolved questions.")
            if not record["materials"]:
                warnings.append(f"{coordinate}: no separate original notice extracted; review dependency report and actual license scope.")
            records.append(record)
        # Existing LGPL/MPL delivery remains intact; copy it into the companion bundle too.
        for name in boot.namelist():
            if name.startswith("META-INF/third-party-sources/") and not name.endswith("/"):
                save("sources/" + name.removeprefix("META-INF/third-party-sources/"), boot.read(name), archive_path.name + "!/" + name)
    save("THIRD-PARTY-NOTICES.txt", Path(config["report"]).read_bytes(), "Generated module dependency report; may include compile-only entries")
    notes = ["Third-party materials for " + archive_path.name,
             "Distribute this directory/ZIP alongside the unchanged application JAR.",
             "The embedded original library notices remain intact. This bundle is additional material.",
             "Review licenses, bundled components and unresolved items before release; generation is not authorization.", ""]
    for record in records:
        notes += [record["coordinate"], "Original materials: " + ", ".join(record["materials"])]
        if "selectedLicense" in record:
            notes.append("Distribution preparation: " + record["selectedLicense"])
        if "sources" in record:
            notes.append("Matching source supplied: " + record["sources"])
        notes.append("")
    notes += ["EPL programs are supplied AS IS, without contributor warranties or conditions, including title,",
              "non-infringement, merchantability and fitness for a particular purpose.",
              "All contributor liability for damages is excluded to the extent permitted by applicable law and license.",
              "Additional warranties/support/different terms are the distributor's responsibility, not the contributors'.",
              "Retain the original terms, notices and matching-source availability when redistributing.", "", "Review items:"] + warnings
    (output / "README.txt").write_text("\n".join(notes) + "\n")
    (output / "inventory.json").write_text(json.dumps({"application": archive_path.name,
        "applicationSha256": hashlib.sha256(archive_path.read_bytes()).hexdigest(),
        "scope": "Actual embedded external libraries; preparation only, not release approval",
        "records": records, "materials": sorted(materials.values(), key=lambda m: m["path"]),
        "reviewItems": warnings}, ensure_ascii=False, indent=2) + "\n")
    shutil.make_archive(str(output), "zip", output)
    print(f"Prepared {len(records)} embedded library records in {output}; {len(warnings)} items need review.")


if __name__ == "__main__":
    main()
