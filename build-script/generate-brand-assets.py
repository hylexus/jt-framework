#!/usr/bin/env python3
"""Generate the project's geometric JT mark, PNG sizes and multi-size ICO.

The design uses only paths created here, without fonts or external icon assets.
Python's standard library and the system Cairo shared library are required.
"""

import ctypes
import ctypes.util
import hashlib
import json
from pathlib import Path
import re
import struct

ROOT = Path(__file__).resolve().parent.parent
BRAND = ROOT / "assets/brand"
NAVY = "#111E37"
BLUE = "#60A5FA"
MINT = "#2DD4BF"
BACKGROUND = (
    "M128 16 H384 C446 16 496 66 496 128 V384 "
    "C496 446 446 496 384 496 H128 C66 496 16 446 16 384 "
    "V128 C16 66 66 16 128 16 Z"
)
LETTER_J = (
    "M188 112 H248 V304 C248 366 212 402 152 402 "
    "C116 402 88 390 64 370 L100 324 C114 338 130 346 150 346 "
    "C174 346 188 332 188 304 Z"
)
LETTER_T = (
    "M276 112 H416 Q432 112 432 128 V156 Q432 172 416 172 "
    "H382 V386 Q382 402 366 402 H338 Q322 402 322 386 "
    "V172 H276 Q260 172 260 156 V128 Q260 112 276 112 Z"
)
SHAPES = [(BACKGROUND, NAVY), (LETTER_J, BLUE), (LETTER_T, MINT)]


def svg():
    paths = "\n".join(f'  <path fill="{color}" d="{path}"/>' for path, color in SHAPES)
    return (
        '<?xml version="1.0" encoding="UTF-8"?>\n'
        '<!-- SPDX-License-Identifier: Apache-2.0; project geometric mark, 2026-10-03. -->\n'
        '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" '
        'width="512" height="512" role="img" aria-labelledby="title desc">\n'
        '  <title id="title">jt-framework</title>\n'
        '  <desc id="desc">Blue J and mint T on a rounded navy square.</desc>\n'
        + paths + '\n</svg>\n'
    )


class Cairo:
    """Render the same path geometry used by the SVG, without font lookup."""

    def __init__(self):
        name = ctypes.util.find_library("cairo")
        candidates = [name, "/opt/homebrew/lib/libcairo.2.dylib", "/usr/local/lib/libcairo.2.dylib"]
        for candidate in filter(None, candidates):
            try:
                self.lib = ctypes.CDLL(candidate)
                break
            except OSError:
                continue
        else:
            raise RuntimeError("Cairo shared library is required to render PNG assets")
        pointer, integer, double = ctypes.c_void_p, ctypes.c_int, ctypes.c_double
        signatures = {
            "image_surface_create": ([integer, integer, integer], pointer),
            "surface_status": ([pointer], integer),
            "create": ([pointer], pointer),
            "status": ([pointer], integer),
            "scale": ([pointer, double, double], None),
            "set_source_rgb": ([pointer, double, double, double], None),
            "move_to": ([pointer, double, double], None),
            "line_to": ([pointer, double, double], None),
            "curve_to": ([pointer] + [double] * 6, None),
            "close_path": ([pointer], None),
            "fill": ([pointer], None),
            "surface_write_to_png": ([pointer, ctypes.c_char_p], integer),
            "destroy": ([pointer], None),
            "surface_destroy": ([pointer], None),
        }
        for function, (arguments, result) in signatures.items():
            method = getattr(self.lib, "cairo_" + function)
            method.argtypes, method.restype = arguments, result
            setattr(self, function, method)

    def path(self, context, data):
        tokens = re.findall(r"[A-Z]|-?\d+(?:\.\d+)?", data)
        index, x, y = 0, 0.0, 0.0
        while index < len(tokens):
            command = tokens[index]
            index += 1
            count = {"M": 2, "L": 2, "H": 1, "V": 1, "C": 6, "Q": 4, "Z": 0}[command]
            values = [float(value) for value in tokens[index:index + count]]
            index += count
            if command in ("M", "L"):
                x, y = values
                (self.move_to if command == "M" else self.line_to)(context, x, y)
            elif command == "H":
                x = values[0]
                self.line_to(context, x, y)
            elif command == "V":
                y = values[0]
                self.line_to(context, x, y)
            elif command == "C":
                self.curve_to(context, *values)
                x, y = values[-2:]
            elif command == "Q":
                control_x, control_y, end_x, end_y = values
                self.curve_to(context,
                              x + (control_x - x) * 2 / 3, y + (control_y - y) * 2 / 3,
                              end_x + (control_x - end_x) * 2 / 3, end_y + (control_y - end_y) * 2 / 3,
                              end_x, end_y)
                x, y = end_x, end_y
            else:
                self.close_path(context)

    def png(self, size, destination):
        # CAIRO_FORMAT_ARGB32. New surfaces have transparent pixels.
        surface = self.image_surface_create(0, size, size)
        context = self.create(surface)
        try:
            if self.surface_status(surface) or self.status(context):
                raise RuntimeError("Failed to allocate Cairo rendering surface")
            self.scale(context, size / 512, size / 512)
            for path, color in SHAPES:
                rgb = [int(color[offset:offset + 2], 16) / 255 for offset in (1, 3, 5)]
                self.set_source_rgb(context, *rgb)
                self.path(context, path)
                self.fill(context)
            if self.surface_write_to_png(surface, str(destination).encode()):
                raise RuntimeError(f"Failed to write {destination}")
        finally:
            self.destroy(context)
            self.surface_destroy(surface)


def write_ico(images, destination):
    offset = 6 + 16 * len(images)
    entries, payloads = [], []
    for size, data in images:
        entries.append(struct.pack("<BBBBHHII", size % 256, size % 256, 0, 0, 1, 32, len(data), offset))
        payloads.append(data)
        offset += len(data)
    destination.write_bytes(struct.pack("<HHH", 0, 1, len(images)) + b"".join(entries + payloads))


def main():
    BRAND.mkdir(parents=True, exist_ok=True)
    outputs = []
    vector = svg().encode()
    for relative in [
        "assets/brand/jt-framework.svg",
        "dashboard/jt-dashboard-server-ui-vue3/public/favicon.svg",
        "dashboard/jt-dashboard-server/src/main/resources/static/client/favicon.svg",
        "docs/src/.vuepress/public/logo.svg",
        "docs/src/.vuepress/public/favicon.svg",
    ]:
        (ROOT / relative).write_bytes(vector)
        outputs.append((relative, "svg", 512))
    renderer = Cairo()
    images = {}
    for size in [16, 32, 48, 64, 128, 256, 512]:
        destination = BRAND / f"jt-framework-{size}.png"
        renderer.png(size, destination)
        images[size] = destination.read_bytes()
        outputs.append((destination.relative_to(ROOT).as_posix(), "png", size))
    targets = {
        "docs/src/.vuepress/public/logo.png": 512,
        "docs/src/.vuepress/public/img/logo.png": 512,
        "docs/src/.vuepress/public/img/hero.png": 512,
        ".idea/icon.png": 128,
        "docs/src/.vuepress/public/img/icons/favicon-16x16.png": 16,
        "docs/src/.vuepress/public/img/icons/favicon-32x32.png": 32,
    }
    for relative, size in targets.items():
        (ROOT / relative).write_bytes(images[size])
        outputs.append((relative, "png", size))
    relative = "docs/src/.vuepress/public/favicon.ico"
    write_ico([(size, images[size]) for size in [16, 32, 48, 64, 128, 256]], ROOT / relative)
    outputs.append((relative, "ico", None))
    assets = [{"path": path, "format": format_, "size": size,
               "sha256": hashlib.sha256((ROOT / path).read_bytes()).hexdigest()}
              for path, format_, size in sorted(outputs)]
    (BRAND / "manifest.json").write_text(json.dumps({
        "designDate": "2026-10-03", "license": "Apache-2.0",
        "source": "build-script/generate-brand-assets.py",
        "sourceSha256": hashlib.sha256(Path(__file__).read_bytes()).hexdigest(),
        "viewBox": [0, 0, 512, 512],
        "colors": {"navy": NAVY, "blue": BLUE, "mint": MINT},
        "assets": assets,
    }, indent=2) + "\n")
    print(f"Generated {len(assets)} brand assets from the geometric JT design")


if __name__ == "__main__":
    main()
