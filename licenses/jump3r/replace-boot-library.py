#!/usr/bin/env python3
"""Copy an unsigned Spring Boot JAR with its nested jump3r library replaced."""

import argparse
import copy
from pathlib import Path
import re
import zipfile


def replace_library(application, replacement, output):
    application, replacement, output = (
        Path(path).resolve() for path in (application, replacement, output)
    )
    if output in (application, replacement) or output.exists():
        raise ValueError("Choose a new output path; input files are preserved")
    with application.open("rb") as stream:
        if stream.read(4) != b"PK\x03\x04":
            raise ValueError("Shell-prefixed executable JARs are not supported")
    replacement_data = replacement.read_bytes()
    with zipfile.ZipFile(replacement) as library:
        required = {"de/sciss/jump3r/lowlevel/LameEncoder.class", "de/sciss/jump3r/mp3/Lame.class"}
        if not required.issubset(library.namelist()):
            raise ValueError("Replacement does not contain the required jump3r classes")
        if library.testzip() is not None:
            raise ValueError("Replacement JAR failed its ZIP integrity check")
    with zipfile.ZipFile(application) as original:
        entries = original.infolist()
        names = [entry.filename for entry in entries]
        if len(names) != len(set(names)):
            raise ValueError("Application JAR contains duplicate ZIP entries")
        signatures = [name for name in names if re.fullmatch(r"META-INF/[^/]+\.(SF|RSA|DSA|EC)", name, re.I)]
        # Spring Boot 3 adds an empty BOOT.SF marker; it is not a JAR signature.
        if any(name != "META-INF/BOOT.SF" or original.getinfo(name).file_size != 0 for name in signatures):
            raise ValueError("Signed application JARs require a separate rebuild/signing workflow")
        matches = [name for name in names if re.fullmatch(r"BOOT-INF/lib/jump3r-[^/]+\.jar", name)]
        if len(matches) != 1:
            raise ValueError("Expected exactly one BOOT-INF/lib/jump3r-*.jar entry")
        target = matches[0]
        output.parent.mkdir(parents=True, exist_ok=True)
        # Exclusive creation prevents accidentally replacing an existing artifact.
        with output.open("xb") as stream:
            try:
                with zipfile.ZipFile(stream, "w") as updated:
                    updated.comment = original.comment
                    for entry in entries:
                        info = copy.copy(entry)
                        data = replacement_data if entry.filename == target else original.read(entry)
                        if entry.filename == target:
                            # Boot's nested JAR loader requires an uncompressed library entry.
                            info.compress_type = zipfile.ZIP_STORED
                        updated.writestr(info, data)
            except BaseException:
                stream.close()
                output.unlink()
                raise
    return target


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("application", type=Path)
    parser.add_argument("replacement", type=Path)
    parser.add_argument("--output", required=True, type=Path)
    args = parser.parse_args()
    target = replace_library(args.application, args.replacement, args.output)
    print(f"Replaced {target}; wrote {args.output}")


if __name__ == "__main__":
    main()
