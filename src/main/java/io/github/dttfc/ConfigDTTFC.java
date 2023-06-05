package io.github.dttfc;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ConfigDTTFC {
    public static ForgeConfigSpec.BooleanValue DT_TWEAKS;

    public static void register() {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("Settings for Dynamic Trees TerraFirmaCraft");
        DT_TWEAKS = COMMON_BUILDER
                .comment("Set Dynamic Trees configurations to match TFC expectations: Passable Leaves, Trees yield 1.5x wood, Leaf seed drops 2%, Axe damaged from tree volume")
                .define("tweakDTConfigs", true);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_BUILDER.build());
    }

}
