package org.labellum.mc.dynamictreestfc;

import net.minecraftforge.common.config.Config;

@Config(modid = DynamicTreesTFC.MOD_ID)
@Config.LangKey("config." + DynamicTreesTFC.MOD_ID)
public class DTTFCConfig
{
    @Config.RequiresWorldRestart
    @Config.Comment("General settings")
    @Config.LangKey("config." + DynamicTreesTFC.MOD_ID + ".general")
    public static final GeneralCfg General = new GeneralCfg();

    public static class GeneralCfg
    {
        @Config.Comment("Multiplier for tree radius.")
        @Config.LangKey("config." + DynamicTreesTFC.MOD_ID + ".general.radiusMultiplier")
        @Config.RangeDouble(min = 0.1, max = 1.0)
        public double radiusMultiplier = 0.75;
    }
}