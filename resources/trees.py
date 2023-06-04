from mcresources import ResourceManager
from assets import *

import shutil

JO_PATH = '../src/main/resources/trees/dttfc/jo_codes/'


def generate(rm: ResourceManager):
    for name in ALL_SPECIES:
        if name == 'acacia':
            species(rm, name, tapering=0.15, signal_energy=12, up_probability=0, lowest_branch_height=3, growth_rate=0.7)
        elif name == 'birch':
            species(rm, name, tapering=0.1, signal_energy=14, up_probability=4, lowest_branch_height=4, growth_rate=1.25),
        elif name == 'sequoia' or name == 'spruce':
            species(rm, name, tapering=0.25, signal_energy=16, up_probability=3, lowest_branch_height=3, growth_rate=0.9, growth_logic_kit='conifer')
        elif name == 'palm':
            species(rm, name, tapering=0.2, signal_energy=10, growth_rate=0.8, soil_str=2, growth_logic_kit='dttfc:diagonal_palm', soils=['dirt_like', 'sand_like'], spec_type='palm')
        elif name == 'kapok':
            species(rm, name, tapering=0.2, signal_energy=24, up_probability=3, lowest_branch_height=2, growth_rate=1, growth_logic_kit='jungle')
        else:
            species(rm, name)

        if name == 'birch':
            family(rm, name, conifer_variants=True)
        elif name == 'sequoia' or name == 'spruce':
            family(rm, name, max_branch_radius=24, conifer_variants=True)
        elif name == 'palm':
            family(rm, name, thickness1=3, thickness2=4, fam_type='dttfc:diagonal_palm')
        elif name == 'kapok':
            family(rm, name, max_branch_radius=24, roots=True, max_signal=64)
        else:
            family(rm, name)

        if name == 'acacia':
            leaves_properties(rm, name, cell_kit='dynamictrees:acacia', smother=2)
        elif name == 'sequoia' or name == 'spruce':
            leaves_properties(rm, name, cell_kit='dynamictrees:conifer', smother=3)
        elif name == 'palm':
            leaves_properties(rm, name, cell_kit='dttfc:palm', leaf_type='palm')
        elif name == 'kapok':
            leaves_properties(rm, name, light=12)
        else:
            leaves_properties(rm, name)

        if name not in NO_BUSHES:
            jo_code('undergrowth', '%s_undergrowth' % name)
            leaves_properties(rm, name + '_undergrowth', leaf_type='scruffy', ground=True, light=4, scruff_chance=0.66, scruff_hydro_max=1, leaves_override='tfc:wood/leaves/%s' % name)
            species(rm, name + '_undergrowth', family_override='dttfc:%s' % name, up_probability=0, lowest_branch_height=0, signal_energy=2, soil_str=1, leaves_override='dttfc:%s_undergrowth' % name)

    for soil in DIRT_TYPES:
        soil_properties(rm, soil, 'mud')
        soil_properties(rm, soil, 'dirt')
        soil_properties(rm, soil, 'farmland', ident('%s_dirt' % soil))
        soil_properties(rm, soil, 'rooted_dirt', ident('%s_dirt' % soil))
        soil_properties(rm, soil, 'grass', properties='dttfc:grass')
    for color in SAND_COLORS:
        soil_properties(rm, color, 'sand', soil_type='sand_like')



def soil_properties(rm: ResourceManager, name: str, tfc_soil: str, sub: str = None, soil_type: str = 'dirt_like', properties: str = None):
    if sub is None:
        if tfc_soil != 'grass':
            rm.blockstate('rooty_%s_%s' % (name, tfc_soil)).with_lang(lang('rooty %s %s', name, tfc_soil)).with_block_model().with_block_loot('tfc:%s/%s' % (tfc_soil, name)).with_item_model()
        else:
            rm.blockstate_multipart('rooty_%s_%s' % (name, tfc_soil), *grass_multipart('dttfc:block/rooty_%s_%s' % (name, tfc_soil))).with_lang(lang('rooty %s %s', name, tfc_soil)).with_block_loot('tfc:dirt/%s' % name)
            grass_models(rm, 'rooty_%s_%s' % (name, tfc_soil), 'tfc:block/dirt/%s' % name)
            # multipart handles display of grass on sides when grassy, this handles display of dirt+roots when dirty
            rm.block_model(('rooty_%s_%s' % (name, tfc_soil), 'side'), {'texture': 'dttfc:block/rooty_%s_dirt' % name}, parent='tfc:block/grass_side')
            rm.item_model('rooty_%s_%s' % (name, tfc_soil), {'block': 'tfc:block/dirt/%s' % name}, parent='tfc:item/grass_inv')
    write(rm, 'soil_properties', name + '_' + tfc_soil, {
        'primitive_soil': 'tfc:%s/%s' % (tfc_soil, name),
        'acceptable_soils': [soil_type],
        'substitute_soil': sub,
        'type': properties
    })

def grass_multipart(model: str):
    return [
        {'model': model + '/bottom', 'x': 90},
        {'model': model + '/top', 'x': 270},
        ({'north': True}, {'model': model + '/top'}),
        ({'east': True}, {'model': model + '/top', 'y': 90}),
        ({'south': True}, {'model': model + '/top', 'y': 180}),
        ({'west': True}, {'model': model + '/top', 'y': 270}),
        ({'north': False}, {'model': model + '/side'}),
        ({'east': False}, {'model': model + '/side', 'y': 90}),
        ({'south': False}, {'model': model + '/side', 'y': 180}),
        ({'west': False}, {'model': model + '/side', 'y': 270}),
    ]

def grass_models(rm: ResourceManager, name: utils.ResourceIdentifier, texture: str):
    for variant in ('top', 'bottom'):
        rm.block_model((name, variant), {'texture': texture}, parent='tfc:block/grass_%s' % variant)


def species(rm: ResourceManager, name: str, family_override: str = None, leaves_override: str = None, spec_type: str = None, tapering: float = None, signal_energy: float = None, up_probability: float = None, lowest_branch_height: float = None, growth_rate: float = None, growth_logic_kit: str = None, soil_str: int = None, soils: List[str] = None):
    res = ident(name)
    write(rm, 'species', name, {
        'type': spec_type,
        'leaves_properties': leaves_override,
        'family': res if family_override is None else family_override,
        'can_bone_meal_tree': False,
        'tapering': tapering,
        'signal_energy': signal_energy,
        'up_probability': up_probability,
        'lowest_branch_height': lowest_branch_height,
        'growth_rate': growth_rate,
        'growth_logic_kit': growth_logic_kit,
        'soil_longevity': soil_str,
        'acceptable_soils': soils,
    })


def family(rm: ResourceManager, name: str, fam_type: str = None, max_branch_radius: int = None, conifer_variants: bool = None, thickness1: int = None, thickness2: int = None, roots: bool = None, max_signal: int = None):
    res = ident(name)
    write(rm, 'families', name, {
        'type': fam_type,
        'common_species': res,
        'common_leaves': res,
        'primitive_log': 'tfc:wood/log/%s' % name,
        'primitive_stripped_log': 'tfc:wood/stripped_log/%s' % name,
        'max_branch_radius': max_branch_radius,
        'conifer_variants': conifer_variants,
        'primary_thickness': thickness1,
        'secondary_thickness': thickness2,
        'generate_surface_root': roots,
        'max_signal_depth': max_signal,
    })


def leaves_properties(rm: ResourceManager, name: str, leaves_override: str = None, cell_kit: str = None, smother: int = None, leaf_type: str = None, light: int = None, ground: bool = None, scruff_chance: float = None, scruff_hydro_max: int = None):
    write(rm, 'leaves_properties', name, {
        'primitive_leaves': 'tfc:wood/leaves/%s' % name if leaves_override is None else leaves_override,
        'cell_kit': cell_kit,
        'smother': smother,
        'type': leaf_type,
        'light_requirement': light,
        'can_grow_on_ground': ground,
        'scruffy_leaf_chance': scruff_chance,
        'scruffy_max_hydro': scruff_hydro_max,
    })

def jo_code(template: str, to: str):
    shutil.copy('codes/%s.txt' % template, JO_PATH + to + '.txt')

def write(rm: ResourceManager, folder: str, path: str, data):
    rm.write((*rm.resource_dir, 'trees', rm.domain, folder, path), data)
