from mcresources import ResourceManager
from assets import *

import shutil

JO_PATH = '../src/main/resources/trees/dttfc/jo_codes/'


def generate(rm: ResourceManager):
    for name in ALL_SPECIES:
        if name == 'acacia':
            species(rm, name, family_override='dttfc:tfc_acacia', tapering=0.15, signal_energy=12, up_probability=0, lowest_branch_height=3, growth_rate=0.7)
        elif name == 'birch':
            species(rm, name, family_override='dttfc:tfc_birch', tapering=0.1, signal_energy=14, up_probability=4, lowest_branch_height=4, growth_rate=1.25),
        elif name == 'oak':
            species(rm, name, family_override='dttfc:tfc_oak')
        elif name == 'sequoia':
            species(rm, name, tapering=0.25, signal_energy=24, up_probability=3, lowest_branch_height=4, growth_rate=1.2, growth_logic_kit='conifer')
        elif name == 'spruce':
            species(rm, name, family_override='dttfc:tfc_spruce', tapering=0.25, signal_energy=16, up_probability=3, lowest_branch_height=3, growth_rate=0.9, growth_logic_kit='conifer')
        elif name == 'palm':
            species(rm, name, tapering=0.2, signal_energy=10, growth_rate=0.8, soil_str=2, growth_logic_kit='dynamictrees:palm', soils=['dirt_like', 'sand_like'], spec_type='dynamictrees:palm', leaves_override='dttfc:palm')
        elif name == 'kapok':
            species(rm, name, tapering=0.2, signal_energy=24, up_probability=3, lowest_branch_height=2, growth_rate=1, growth_logic_kit='jungle')
        elif name == 'mangrove':
            species(rm, name, family_override='dttfc:tfc_mangrove', tapering=0.25, signal_energy=20, up_probability=8, lowest_branch_height=2, growth_rate=0.6, spec_type='mangrove', soils=['dirt_like', 'mud_like', 'water_like'], extra_data={'root_tapering': 0.2, 'root_signal_energy': 20.0, 'roots_growth_logic_kit': 'mangrove_roots', 'primitive_sapling': 'mangrove_propagule', 'model_overrides': {'sapling': 'dynamictrees:block/smartmodel/water_sapling_thin'}, 'plantable_on_fluid': True})
        else:
            species(rm, name)

        if name == 'birch':
                family(rm, 'tfc_birch', conifer_variants=True, extra_data={})
        elif name == 'acacia':
                family(rm, 'tfc_acacia', extra_data={})
        elif name == 'oak':
                family(rm, 'tfc_oak', extra_data={})
        elif name == 'sequoia' or name == 'spruce':
            family_name = 'tfc_spruce' if name == 'spruce' else name
            family_extra_data = {} if name == 'spruce' else {}
            family(rm, family_name, max_branch_radius=24, conifer_variants=True, extra_data=family_extra_data)
        elif name == 'palm':
            family(rm, name, thickness1=3, thickness2=4, fam_type='dynamictrees:palm')
        elif name == 'kapok':
            family(rm, name, max_branch_radius=24, roots=True, max_signal=64)
        elif name == 'mangrove':
            family(rm, 'tfc_mangrove', fam_type='underground_roots', max_branch_radius=8, extra_data={'default_soil': 'dttfc:mangrove_aerial_roots', 'primitive_root': 'tfc:tree_roots', 'primitive_filled_root': 'tfc:muddy_roots/entisol', 'primitive_covered_root': 'tfc:mud/entisol', 'root_system_acceptable_soils': ['dirt_like', 'mud_like', 'sand_like']})
        else:
            family(rm, name)

        if name == 'acacia':
            leaves_properties(rm, name, cell_kit='dynamictrees:acacia', smother=2)
        elif name == 'sequoia' or name == 'spruce':
            leaves_properties(rm, name, cell_kit='dynamictrees:conifer', smother=3)
        elif name == 'palm':
            leaves_properties(rm, name, cell_kit='dttfc:palm', leaf_type='dynamictrees:palm')
        elif name == 'kapok':
            leaves_properties(rm, name, light=12)
        elif name == 'mangrove':
            leaves_properties(rm, name, leaf_type='scruffy', smother=6)
        else:
            leaves_properties(rm, name)

        if name not in NO_BUSHES:
            jo_code('undergrowth', '%s_undergrowth' % name)
            leaves_properties(rm, name + '_undergrowth', leaf_type='scruffy', ground=True, light=4, scruff_chance=0.66, scruff_hydro_max=1, leaves_override='tfc:wood/leaves/%s' % name)
            undergrowth_family = 'dttfc:tfc_%s' % name if name in ('acacia', 'birch', 'oak', 'spruce', 'mangrove') else 'dttfc:%s' % name
            species(rm, name + '_undergrowth', family_override=undergrowth_family, up_probability=0, lowest_branch_height=0, signal_energy=2, soil_str=1, leaves_override='dttfc:%s_undergrowth' % name)

    for soil in DIRT_TYPES:
        soil_properties(rm, soil, 'mud')
        soil_properties(rm, soil, 'dirt')
        soil_properties(rm, soil, 'farmland', ident('%s_dirt' % soil))
        soil_properties(rm, soil, 'rooted_dirt', ident('%s_dirt' % soil))
        soil_properties(rm, soil, 'grass', properties='dttfc:grass')
    for color in SAND_COLORS:
        soil_properties(rm, color, 'sand', soil_type='sand_like')

    write(rm, 'soil_properties', 'mangrove_aerial_roots', {
        'type': 'dttfc:aerial_roots',
        'primitive_soil': 'tfc:wood/log/mangrove',
        'acceptable_soils': []
    })
    rm.blockstate('rooty_mangrove_aerial_roots', variants=dict(('radius=%s' % i, {'model': 'dynamictrees:block/rooty_mangrove_aerial_roots_radius%s' % i}) for i in range(1, 9))).with_lang(lang('rooty mangrove aerial roots'))
    write(rm, 'soil_properties', 'salt_water', {
        'type': 'dttfc:fluid',
        'primitive_soil': 'tfc:fluid/salt_water',
        'acceptable_soils': ['water_like']
    })
    rm.blockstate('rooty_salt_water', model='dynamictrees:block/roots_water').with_lang(lang('rooty salt water'))
    rm.item_model('rooty_salt_water', parent='dynamictrees:block/roots_water', no_textures=True)


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


def species(rm: ResourceManager, name: str, family_override: str = None, leaves_override: str = None, spec_type: str = None, tapering: float = None, signal_energy: float = None, up_probability: float = None, lowest_branch_height: float = None, growth_rate: float = None, growth_logic_kit: str = None, soil_str: int = None, soils: List[str] = None, extra_data: Dict[str, Any] = {}):
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
        **extra_data
    })


def family(rm: ResourceManager, name: str, fam_type: str = None, max_branch_radius: int = None, conifer_variants: bool = None, thickness1: int = None, thickness2: int = None, roots: bool = None, max_signal: int = None, extra_data: Dict[str, Any] = {}):
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
        **extra_data
    })


def leaves_properties(rm: ResourceManager, name: str, leaves_override: str = None, cell_kit: str = None, smother: int = None, leaf_type: str = None, light: int = None, ground: bool = None, scruff_chance: float = None, scruff_hydro_max: int = None):
    write(rm, 'leaves_properties', name, {
        'primitive_leaves': 'tfc:wood/leaves/%s' % name if leaves_override is None else leaves_override,
        'block_registry_name': 'dynamictrees:dttfc_%s_leaves' % name,
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
