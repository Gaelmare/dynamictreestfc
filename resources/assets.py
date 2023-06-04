from typing import Dict, Any, NamedTuple, List

from mcresources import ResourceManager, utils, loot_tables


def generate(rm: ResourceManager):
    for name in ALL_SPECIES:
        branch = rm.blockstate('%s_branch' % name).with_lang(lang('%s branch', name))
        strip = rm.blockstate('stripped_%s_branch' % name).with_lang(lang('stripped %s branch', name))
        sap = rm.blockstate('%s_sapling' % name).with_lang(lang('%s sapling', name))
        rm.lang(['species.dttfc.%s' % name, name.title()])

        leaves(rm, name, name)
        if name not in NO_BUSHES:
            leaves(rm, name + '_undergrowth', name)

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
        sap.with_tag('dynamictrees:saplings')

        branch.with_block_loot()
        sap.with_block_loot()

        rm.loot(name, {
            'name': ident('%s_seed' % name),
            'conditions': [{
                'condition': 'dynamictrees:voluntary_seed_drop_chance',
                'rarity': 1.0
            }]
        }, path='trees/voluntary', loot_type='dynamictrees:voluntary')

        rm.loot(name, {
            'name': 'tfc:wood/log/%s' % name,
            'functions': [func('dynamictrees:multiply_logs_count')]
        }, {
            'name': 'minecraft:stick',
            'functions': [func('dynamictrees:multiply_sticks_count')]
        }, path='trees/branches', loot_type='dynamictrees:branches')

        rm.loot('stripped_%s' % name, {
            'name': 'tfc:wood/stripped_log/%s' % name,
            'functions': [func('dynamictrees:multiply_logs_count')]
        }, {
            'name': 'minecraft:stick',
            'functions': [{'function': 'dynamictrees:multiply_sticks_count'}]
        }, path='trees/branches', loot_type='dynamictrees:branches')

        rm.block_tag('dynamictrees:foliage', '#tfc:plants')

        if name == 'kapok':
            rm.blockstate('%s_root' % name).with_lang(lang('%s root', name))
            rm.custom_block_model('%s_root' % name, 'dynamictrees:root', {'textures': {'bark': 'tfc:block/wood/log/%s' % name}})


def leaves(rm: ResourceManager, name: str, base_name: str):
    if name == 'palm':
        leaf = rm.blockstate_multipart('%s_leaves' % name,
           ({'distance': '1|2'}, {'model': 'dttfc:block/palm_frond'}),
           ({'distance': '3'}, {'model': 'dttfc:block/palm_core_top'}),
           ({'distance': '4'}, {'model': 'dttfc:block/palm_core_bottom'}),
           ({'OR': [{'direction': '0', 'distance': '1|2'}, {'distance': '5|6|7'}]}, {'model': 'tfc:block/wood/leaves/palm'}),
           )
    else:
        leaf = rm.blockstate('%s_leaves' % name, model='tfc:block/wood/leaves/%s' % base_name)

    leaf.with_block_loot(loot_tables.alternatives(({
        'name': 'tfc:wood/leaves/%s' % base_name,
        'conditions': [loot_tables.or_condition(loot_tables.match_tag('forge:shears'), loot_tables.silk_touch())]
    }, {
        'type': 'dynamictrees:seed_item',
        'conditions': [cond('dynamictrees:seasonal_seed_drop_chance')]
    }, {
        'name': 'minecraft:stick',
        'conditions': [loot_tables.random_chance(0.02)]
    })))
    leaf.with_tag('dynamictrees:leaves')

    leaf.with_lang(lang('%s leaves', name))

    rm.loot(name, {
        'type': 'dynamictrees:seed_item',
        'conditions': [cond('dynamictrees:seasonal_seed_drop_chance')]
    }, {
        'name': 'minecraft:stick',
        'functions': [loot_tables.set_count(1, 2)],
        'conditions': [loot_tables.random_chance(0.02)]
    }, path='trees/leaves', loot_type='dynamictrees:leaves')

def func(name: str):
    return {'function': name}

def cond(name: str):
    return {'condition': name}

def ident(path: str) -> str:
    return utils.resource_location('dttfc', path).join()


def on_error(error: str, e: Exception):
    print(error)
    raise e


def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()


ALL_SPECIES = ['acacia', 'ash', 'aspen', 'birch', 'blackwood', 'chestnut', 'douglas_fir', 'hickory', 'kapok', 'maple', 'oak', 'palm', 'pine', 'rosewood', 'sequoia', 'spruce', 'sycamore', 'white_cedar', 'willow']
LAND_BIOMES = ['plains', 'hills', 'lowlands', 'low_canyons', 'rolling_hills', 'badlands', 'inverted_badlands', 'plateau', 'canyons', 'mountains', 'old_mountains', 'oceanic_mountains', 'volcanic_mountains', 'volcanic_oceanic_mountains']
DIRT_TYPES = ['sandy_loam', 'loam', 'silty_loam', 'silt']
SAND_COLORS = ['black', 'brown', 'green', 'pink', 'red', 'white', 'yellow']

NO_BUSHES = ['palm', 'rosewood', 'sycamore']
