#!/usr/bin/env python3
"""Rebuild the published, unmodified jump3r Java sources with a JDK."""

import argparse
from pathlib import Path
import subprocess
import tempfile
import zipfile


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("sources", type=Path)
    parser.add_argument("--output", type=Path, default=Path("jump3r-rebuilt.jar"))
    args = parser.parse_args()
    output = args.output.resolve()
    output.parent.mkdir(parents=True, exist_ok=True)
    with tempfile.TemporaryDirectory(prefix="jump3r-rebuild-") as work:
        work = Path(work)
        src, classes = work / "src", work / "classes"
        src.mkdir()
        classes.mkdir()
        with zipfile.ZipFile(args.sources) as archive:
            for entry in archive.infolist():
                target = (src / entry.filename).resolve()
                if not target.is_relative_to(src.resolve()):
                    raise ValueError("Unsafe archive entry: " + entry.filename)
                if not entry.is_dir() and entry.filename.endswith(".java"):
                    target.parent.mkdir(parents=True, exist_ok=True)
                    target.write_bytes(archive.read(entry))
        sources = sorted(src.rglob("*.java"))
        if not sources:
            raise ValueError("No Java source files in " + str(args.sources))
        argfile = work / "sources.args"
        argfile.write_text("\n".join('"' + str(p) + '"' for p in sources), encoding="utf-8")
        subprocess.run([
            "javac", "--release", "8", "-encoding", "UTF-8", "-d", str(classes),
            "@" + str(argfile),
        ], check=True)
        subprocess.run(["jar", "--create", "--file", str(output), "-C", str(classes), "."], check=True)
        print("Rebuilt:", output)


if __name__ == "__main__":
    main()
