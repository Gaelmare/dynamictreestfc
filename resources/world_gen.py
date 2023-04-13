from typing import get_args, Literal, Union, Optional

from mcresources import ResourceManager
from mcresources.type_definitions import Json

from assets import *


def generate(rm: ResourceManager):
    forest_config(rm, 30, 210, 17, 40, 'acacia', True)
    forest_config(rm, 60, 240, 1, 15, 'ash', True)
    forest_config(rm, 350, 500, -18, 5, 'aspen', False)
    forest_config(rm, 125, 310, -11, 7, 'birch', False)
    forest_config(rm, 0, 180, 12, 35, 'blackwood', True)
    forest_config(rm, 180, 370, -4, 17, 'chestnut', False)
    forest_config(rm, 290, 500, -16, -1, 'douglas_fir', True)
    forest_config(rm, 210, 400, 7, 15, 'hickory', True)
    forest_config(rm, 270, 500, 17, 40, 'kapok', False)
    forest_config(rm, 270, 500, -1, 15, 'maple', True)
    forest_config(rm, 240, 450, -9, 11, 'oak', False)
    forest_config(rm, 180, 470, 20, 35, 'palm', False)
    forest_config(rm, 60, 270, -18, -4, 'pine', True)
    forest_config(rm, 140, 310, 8, 31, 'rosewood', False)
    forest_config(rm, 250, 420, -14, 2, 'sequoia', True, old_growth_chance=3)
    forest_config(rm, 110, 320, -17, 1, 'spruce', True)
    forest_config(rm, 230, 480, 15, 29, 'sycamore', True)
    forest_config(rm, 10, 220, -13, 9, 'white_cedar', True)
    forest_config(rm, 330, 500, 11, 35, 'willow', True)

    forest_config(rm, 0, 150, 5, 20, 'saguaro_cactus', not_a_tree=True)
    forest_config(rm, 0, 170, 9, 22, 'pillar_cactus', not_a_tree=True)
    forest_config(rm, 0, 120, 0, 18, 'pipe_cactus', not_a_tree=True)
    forest_config(rm, 0, 80, 5, 15, 'mega_cactus', not_a_tree=True)

    rm.configured_feature_tag('dynamic_forest_trees', *[ident(s) for s in ALL_SPECIES], *['%s_cactus' % c for c in ('saguaro', 'pillar', 'pipe', 'mega')])

    rm.domain = 'tfc'
    rm.configured_feature('forest', 'dttfc:forest', {
        'entries': '#dttfc:dynamic_forest_trees',
        'types': {
            'none': {
                'per_chunk_chance': 0
            },
            'sparse': {
                'tree_count': uniform_int(1, 4),
                'groundcover_count': 10,
                'per_chunk_chance': 0.08,
                'bush_count': 0,
                'has_spoiler_old_growth': True
            },
            'edge': {
                'tree_count': 4,
                'groundcover_count': 15
            },
            'normal': {
                'tree_count': 8,
                'groundcover_count': 30,
                'has_spoiler_old_growth': True
            },
            'old_growth': {
                'tree_count': 12,
                'groundcover_count': 40,
                'allows_old_growth': True
            }
        }
    })
    rm.domain = 'dttfc'

    #place_species(rm, 'saguaro_cactus', 'dynamictreesplus:saguaro_cactus', 3, 10, 2, decorate_chance(8), decorate_square(), decorate_climate(5, 20, 0, 150, fuzzy=True))
    #place_species(rm, 'pillar_cactus', 'dynamictreesplus:pillar_cactus', 3, 10, 2, decorate_chance(8), decorate_square(), decorate_climate(9, 22, 0, 170, fuzzy=True))
    #place_species(rm, 'pipe_cactus', 'dynamictreesplus:pipe_cactus', 3, 10, 2, decorate_chance(8), decorate_square(), decorate_climate(0, 18, 0, 120, fuzzy=True))
    #place_species(rm, 'mega_cactus', 'dynamictreesplus:mega_cactus', 3, 10, 2, decorate_chance(8), decorate_square(), decorate_climate(6, 24, 0, 100, fuzzy=True))

    #rm.placed_feature_tag('tfc:feature/land_plants', 'dttfc:saguaro_cactus', 'dttfc:pillar_cactus', 'dttfc:pipe_cactus', 'dttfc:mega_cactus')

    rm.configured_feature('noop', 'minecraft:no_op', {})


def forest_config(rm: ResourceManager, min_rain: float, max_rain: float, min_temp: float, max_temp: float, tree: str, old_growth: bool = False, old_growth_chance: int = None, spoiler_chance: int = None, not_a_tree: bool = False):
    is_tree = not not_a_tree
    cfg = {
        'min_rain': min_rain,
        'max_rain': max_rain,
        'min_temp': min_temp,
        'max_temp': max_temp,
        'groundcover': [{'block': 'tfc:wood/twig/%s' % tree}] if is_tree else None,
        'normal_tree': 'tfc:tree/%s' % tree if is_tree else 'dttfc:noop',
        'dead_tree': 'tfc:tree/%s_dead' % tree if is_tree else 'dttfc:noop',
        'old_growth_chance': old_growth_chance,
        'spoiler_old_growth_chance': spoiler_chance,
    }
    if tree != 'palm' and is_tree:
        cfg['groundcover'] += [{'block': 'tfc:wood/fallen_leaves/%s' % tree}]
    if tree not in ('acacia', 'willow') and is_tree:
        cfg['fallen_log'] = 'tfc:wood/log/%s' % tree
    else:
        cfg['fallen_tree_chance'] = 0
    if tree not in NO_BUSHES and is_tree:
        cfg['bush_log'] = utils.block_state('tfc:wood/wood/%s[natural=true,axis=y]' % tree)
        cfg['bush_leaves'] = 'tfc:wood/leaves/%s' % tree
    if old_growth and is_tree:
        cfg['old_growth_tree'] = 'tfc:tree/%s_large' % tree

    fixed_cfg = {
        'entry': cfg,
        'species': 'dttfc:%s' % tree,
        'undergrowth_species': 'dttfc:%s_undergrowth' % tree if tree not in NO_BUSHES else None
    }

    rm.configured_feature(tree, 'dttfc:forest_entry', fixed_cfg)


def place_species(rm: ResourceManager, name: str, species_name: str, tries: int, xz: int, y: int, *patch_decorators):
    name = ident(name)
    rm.configured_feature(name, 'dttfc:species', {'species': species_name})
    rm.placed_feature(name, name, decorate_heightmap('world_surface_wg'), decorate_replaceable())
    rm.configured_feature(name + '_patch', 'minecraft:random_patch', {
        'tries': tries,
        'xz_spread': xz,
        'y_spread': y,
        'feature': name
    })
    rm.placed_feature(name + '_patch', name + '_patch', *patch_decorators)

def uniform_int(min_inclusive: int, max_inclusive: int) -> Dict[str, Any]:
    return {
        'type': 'uniform',
        'value': {
            'min_inclusive': min_inclusive,
            'max_inclusive': max_inclusive
        }
    }

Heightmap = Literal['motion_blocking', 'motion_blocking_no_leaves', 'ocean_floor', 'ocean_floor_wg', 'world_surface', 'world_surface_wg']

def decorate_replaceable() -> Json:
    return decorate_block_predicate({'type': 'tfc:replaceable'})

def decorate_block_predicate(predicate: Json) -> Json:
    return {
        'type': 'block_predicate_filter',
        'predicate': predicate
    }

def decorate_climate(min_temp: Optional[float] = None, max_temp: Optional[float] = None, min_rain: Optional[float] = None, max_rain: Optional[float] = None, needs_forest: Optional[bool] = False, fuzzy: Optional[bool] = None, min_forest: Optional[str] = None, max_forest: Optional[str] = None) -> Json:
    return {
        'type': 'tfc:climate',
        'min_temperature': min_temp,
        'max_temperature': max_temp,
        'min_rainfall': min_rain,
        'max_rainfall': max_rain,
        'min_forest': 'normal' if needs_forest else min_forest,
        'max_forest': max_forest,
        'fuzzy': fuzzy
    }


def decorate_chance(rarity_or_probability: Union[int, float]) -> Json:
    return {'type': 'minecraft:rarity_filter', 'chance': round(1 / rarity_or_probability) if isinstance(rarity_or_probability, float) else rarity_or_probability}


def decorate_count(count: int) -> Json:
    return {'type': 'minecraft:count', 'count': count}


def decorate_square() -> Json:
    return 'minecraft:in_square'

def decorate_heightmap(heightmap: Heightmap) -> Json:
    assert heightmap in get_args(Heightmap)
    return 'minecraft:heightmap', {'heightmap': heightmap.upper()}
