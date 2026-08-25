package org.labellum.mc.dttfc;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;

public class ConfigDTTFC {
    public static ModConfigSpec.BooleanValue DT_TWEAKS;
    public static ModConfigSpec.BooleanValue USE_TFC_CALENDAR_GROWTH;
    public static ModConfigSpec.IntValue GROW_EVERY_N_TICKS;
    public static ModConfigSpec.BooleanValue GROW_ON_CHUNK_LOAD;
    public static ModConfigSpec.BooleanValue ASYNC_RANDOM_TICK_TREE_GROWTH;

    public static void register(ModContainer container) {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
        COMMON_BUILDER.comment("Settings for Dynamic Trees TerraFirmaCraft");
        DT_TWEAKS = COMMON_BUILDER
                .comment("Set Dynamic Trees configurations to match TFC expectations: Passable Leaves, Trees yield 1.5x wood, Leaf seed drops 2%, Axe damaged from tree volume")
                .define("tweakDTConfigs", true);
        USE_TFC_CALENDAR_GROWTH = COMMON_BUILDER
                .comment("Control tree growth using the global TerraFirmaCraft calendar instead of relying on random ticks.")
                .define("useTfcCalendarGrowth", true);
        GROW_EVERY_N_TICKS = COMMON_BUILDER
                .comment("Set a Dynamic Trees growth event to run for every Nth tick. This is tied to the global TerraFirmaCraft calendar, so trees will grow while you're away. A lower value will cause trees to grow faster. At 2000 it will take approximately  two year to grow from sapling to full size. This will be affected by the species growth rate and season.")
                .defineInRange("growEveryNTicks", 2000, 20, Integer.MAX_VALUE);
        GROW_ON_CHUNK_LOAD = COMMON_BUILDER
                .comment("Whether to calculate tree growth when a chunk is loaded. This is to prevent trees growing a lot on random ticks later. The processing itself is done when there is enough time in each tick and should not cause lag.")
                .define("growOnChunkLoad", true);
        ASYNC_RANDOM_TICK_TREE_GROWTH = COMMON_BUILDER
                .comment("If enabled, all tree growth calculations will happen at the end of the world tick instead inside randomTick. This run if there is enough time left to not cause any lag.")
                .define("asyncRandomTickTreeGrowth", true);

        container.registerConfig(ModConfig.Type.COMMON, COMMON_BUILDER.build());
    }

}
