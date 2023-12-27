#!/bin/python3

# 需要当前文件夹下存在需要处理的图块转角素材
# 例如 a_01.png a_02.png
#
# a 表示应当在 右下角 的转角
# b 表示应当在 左下角 的转角
# c 表示应当在 右上角 的转角
# d 表示应当在 左上角 的转角

from PIL import Image

import os
import glob
import sys


def process(t):
    if t == "a":
        x = 16
        y = 16
    elif t == "b":
        x = 0
        y = 16
    elif t == "c":
        x = 16
        y = 0
    elif t == "d":
        x = 0
        y = 0

    directory = sys.argv[1]

    file_pattern = os.path.join(directory, f"{t}*.png")
    ld_files = glob.glob(file_pattern)

    for file in ld_files:
        base_image = Image.open("main.png").convert("RGBA")
        overlay_image = Image.open(file).convert("RGBA")

        result_image = base_image.copy()

        result_image.paste(overlay_image, (x, y), overlay_image)

        # result_image.show()
        result_image.save(file)


for odr in ["a", "b", "c", "d"]:
    process(odr)
