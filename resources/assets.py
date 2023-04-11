from typing import Dict, Any, NamedTuple, List

from mcresources import ResourceManager, utils, loot_tables


def generate(rm: ResourceManager):
    do_worldgen(rm)

    for name in ALL_SPECIES:
        branch = rm.blockstate('%s_branch' % name).with_lang(lang('%s branch', name))
        strip = rm.blockstate('stripped_%s_branch' % name).with_lang(lang('stripped %s branch', name))
        sap = rm.blockstate('%s_sapling' % name).with_lang(lang('%s sapling', name))

        if name == 'palm':
            leaf = rm.blockstate_multipart('%s_leaves' % name,
               ({'distance': '1|2'}, {'model': 'dttfc:block/palm_frond'}),
               ({'distance': '3'}, {'model': 'dttfc:block/palm_core_top'}),
               ({'distance': '4'}, {'model': 'dttfc:block/palm_core_bottom'}),
               ({'OR': [{'direction': '0', 'distance': '1|2'}, {'distance': '5|6|7'}]}, {'model': 'tfc:block/wood/leaves/palm'}),
            )
        else:
            leaf = rm.blockstate('%s_leaves' % name, model='tfc:block/wood/leaves/%s' % name)
        leaf.with_lang(lang('%s leaves', name))

        sap.with_block_model(parent='dynamictrees:block/smartmodel/sapling', textures={
            'particle': 'tfc:block/wood/leaves/%s' % name,
            'log': 'tfc:block/wood/log/%s' % name,
            'leaves': 'tfc:block/wood/leaves/%s' % name
        })
        rm.custom_block_model('%s_branch' % name, 'dynamictrees:branch', {'textures': {
            'bark': 'tfc:block/wood/log/%s' % name,
            'rings': 'tfc:block/wood/log_top/%s' % name,
        }})
        rm.custom_block_model('stripped_%s_branch' % name, 'dynamictrees:branch', {'textures': {
            'bark': 'tfc:block/wood/stripped_log/%s' % name,
            'rings': 'tfc:block/wood/stripped_log_top/%s' % name,
        }})
        rm.item_model('%s_branch' % name, {
            'bark': 'tfc:block/wood/log/%s' % name,
            'rings': 'tfc:block/wood/log_top/%s' % name,
        }, parent='dynamictrees:item/branch')
        rm.item_model('%s_seed' % name, {'layer0': 'dttfc:item/seed/%s' % name}, parent='dynamictrees:item/standard_seed').with_lang(lang('%s seed', name))

        branch.with_tag('dynamictrees:branches_that_burn')
        strip.with_tag('dynamictrees:branches_that_burn')
        branch.with_tag('dynamictrees:branches')
        strip.with_tag('dynamictrees:branches')
        leaf.with_tag('dynamictrees:leaves')
        sap.with_tag('dynamictrees:saplings')

        branch.with_block_loot()
        sap.with_block_loot()
        leaf.with_block_loot(loot_tables.alternatives(({
               'name': 'tfc:wood/leaves/%s' % name,
               'conditions': [loot_tables.or_condition(loot_tables.match_tag('forge:shears'), loot_tables.silk_touch())]
        }, {
           'type': 'dynamictrees:seed_item',
           'conditions': [loot_tables.random_chance(0.8)]
        }, {
           'name': 'minecraft:stick',
           'conditions': [loot_tables.random_chance(0.02)]
        })))

        rm.loot(name, {
            'name': ident('%s_seed' % name),
            'conditions': [{
                'condition': 'dynamictrees:voluntary_seed_drop_chance',
                'rarity': 1.0
            }]
        }, path='trees/voluntary', loot_type='dynamictrees:voluntary')

        rm.loot(name, {
            'name': 'tfc:wood/log/%s' % name,
            'conditions': [loot_tables.random_chance(0.8)]
        }, {
            'name': 'minecraft:stick',
            'functions': [{'function': 'dynamictrees:multiply_sticks_count'}]
        }, path='trees/branches', loot_type='dynamictrees:branches')

        rm.loot('stripped_%s' % name, {
            'name': 'tfc:wood/stripped_log/%s' % name,
            'conditions': [loot_tables.random_chance(0.8)]
        }, {
            'name': 'minecraft:stick',
            'functions': [{'function': 'dynamictrees:multiply_sticks_count'}]
        }, path='trees/branches', loot_type='dynamictrees:branches')

        rm.loot(name, {
            'type': 'dynamictrees:seed_item',
            'conditions': [loot_tables.random_chance(0.02)]
        }, {
            'name': 'minecraft:stick',
            'conditions': [loot_tables.random_chance(0.02)]
        }, path='trees/leaves', loot_type='dynamictrees:leaves')

        rm.block_tag('dynamictrees:foliage', '#tfc:plants')


def do_worldgen(rm: ResourceManager):
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

    rm.configured_feature_tag('dynamic_forest_trees', *[ident(s) for s in ALL_SPECIES])

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


def forest_config(rm: ResourceManager, min_rain: float, max_rain: float, min_temp: float, max_temp: float, tree: str, old_growth: bool, old_growth_chance: int = None, spoiler_chance: int = None):
    cfg = {
        'min_rain': min_rain,
        'max_rain': max_rain,
        'min_temp': min_temp,
        'max_temp': max_temp,
        'groundcover': [{'block': 'tfc:wood/twig/%s' % tree}],
        'normal_tree': 'tfc:tree/%s' % tree,
        'dead_tree': 'tfc:tree/%s_dead' % tree,
        'old_growth_chance': old_growth_chance,
        'spoiler_old_growth_chance': spoiler_chance,
    }
    if tree != 'palm':
        cfg['groundcover'] += [{'block': 'tfc:wood/fallen_leaves/%s' % tree}]
    if tree not in ('acacia', 'willow'):
        cfg['fallen_log'] = 'tfc:wood/log/%s' % tree
    else:
        cfg['fallen_tree_chance'] = 0
    if tree not in ('palm', 'rosewood', 'sycamore'):
        cfg['bush_log'] = utils.block_state('tfc:wood/wood/%s[natural=true,axis=y]' % tree)
        cfg['bush_leaves'] = 'tfc:wood/leaves/%s' % tree
    if old_growth:
        cfg['old_growth_tree'] = 'tfc:tree/%s_large' % tree

    fixed_cfg = {
        'entry': cfg,
        'species': 'dttfc:%s' % tree
    }

    rm.configured_feature(tree, 'dttfc:forest_entry', fixed_cfg)


def ident(path: str) -> str:
    return utils.resource_location('dttfc', path).join()


def on_error(error: str, e: Exception):
    print(error)
    raise e


def uniform_int(min_inclusive: int, max_inclusive: int) -> Dict[str, Any]:
    return {
        'type': 'uniform',
        'value': {
            'min_inclusive': min_inclusive,
            'max_inclusive': max_inclusive
        }
    }


def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()


ALL_SPECIES = ['acacia', 'ash', 'aspen', 'birch', 'blackwood', 'chestnut', 'douglas_fir', 'hickory', 'kapok', 'maple', 'oak', 'palm', 'pine', 'rosewood', 'sequoia', 'spruce', 'sycamore', 'white_cedar', 'willow']
LAND_BIOMES = ['plains', 'hills', 'lowlands', 'low_canyons', 'rolling_hills', 'badlands', 'inverted_badlands', 'plateau', 'canyons', 'mountains', 'old_mountains', 'oceanic_mountains', 'volcanic_mountains', 'volcanic_oceanic_mountains']
DIRT_TYPES = ['sandy_loam', 'loam', 'silty_loam', 'silt']
SAND_COLORS = ['black', 'brown', 'green', 'pink', 'red', 'white', 'yellow']
