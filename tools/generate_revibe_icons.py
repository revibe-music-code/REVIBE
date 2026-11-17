#!/usr/bin/env python3
"""Generate legacy launcher PNGs matching REVIBE's adaptive vector icon."""
from pathlib import Path
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[1]
SIZES = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192,
}
BG_TOP = (34, 27, 52)
BG_BOTTOM = (18, 16, 26)
MARK = (255, 255, 255, 255)


def icon(size: int) -> Image.Image:
    scale = 4
    canvas = Image.new("RGBA", (size * scale, size * scale), (0, 0, 0, 255))
    pixels = canvas.load()
    for y in range(size * scale):
        t = y / max(1, size * scale - 1)
        color = tuple(round(BG_TOP[i] * (1 - t) + BG_BOTTOM[i] * t) for i in range(3)) + (255,)
        for x in range(size * scale):
            pixels[x, y] = color

    draw = ImageDraw.Draw(canvas)
    # Three softly rounded bars reproduce the adaptive vector's compact sound-wave mark.
    for x, y0, y1, width in ((31, 48, 60, 10), (54, 30, 78, 11), (77, 42, 66, 10)):
        cx, top, bottom, stroke = x * size / 108, y0 * size / 108, y1 * size / 108, width * size / 108
        cx *= scale
        top *= scale
        bottom *= scale
        stroke *= scale
        draw.line((cx, top, cx, bottom), fill=MARK, width=max(1, round(stroke)))
        r = stroke / 2
        draw.ellipse((cx - r, top - r, cx + r, top + r), fill=MARK)
        draw.ellipse((cx - r, bottom - r, cx + r, bottom + r), fill=MARK)

    return canvas.resize((size, size), Image.Resampling.LANCZOS)


def main() -> None:
    for directory, size in SIZES.items():
        target = ROOT / "app" / "src" / "main" / "res" / directory
        target.mkdir(parents=True, exist_ok=True)
        image = icon(size)
        image.save(target / "ic_launcher.png", optimize=True)
        image.save(target / "ic_launcher_round.png", optimize=True)
        print(f"wrote {target} ({size}x{size})")


if __name__ == "__main__":
    main()
