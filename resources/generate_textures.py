from PIL import Image, ImageDraw, ImageEnhance, ImageOps
from PIL.Image import Transpose

import assets

PATH = '../src/main/resources/assets/dttfc/textures/'


def main():
    for dirt in assets.DIRT_TYPES:
        overlay_image('texture_templates/roots', 'texture_templates/mud/%s' % dirt, PATH + 'block/rooty_%s_mud' % dirt)
        overlay_image('texture_templates/roots', 'texture_templates/dirt/%s' % dirt, PATH + 'block/rooty_%s_dirt' % dirt)


def overlay_image(front_file_dir, back_file_dir, result_dir):
    foreground = Image.open(front_file_dir + '.png')
    background = Image.open(back_file_dir + '.png').convert('RGBA')
    background.paste(foreground, (0, 0), foreground.convert('RGBA'))
    background.save(result_dir + '.png')


if __name__ == '__main__':
    main()
