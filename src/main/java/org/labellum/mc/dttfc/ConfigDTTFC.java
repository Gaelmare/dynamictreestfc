package org.labellum.mc.dttfc;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;

public class ConfigDTTFC {
    public static ModConfigSpec.BooleanValue DT_TWEAKS;

    public static void register(ModContainer modContainer) {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
        COMMON_BUILDER.comment("Settings for Dynamic Trees TerraFirmaCraft");
        DT_TWEAKS = COMMON_BUILDER
                .comment("Set Dynamic Trees configurations to match TFC expectations: Passable Leaves, Trees yield 1.5x wood, Leaf seed drops 2%, Axe damaged from tree volume")
                .define("tweakDTConfigs", true);

        modContainer.registerConfig(ModConfig.Type.COMMON, COMMON_BUILDER.build());
    }

}
