package io.github.dynamictreestfc;


import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(DynamicTreesTFC.MOD_ID)
public class DynamicTreesTFC
{
    public static final String MOD_ID = "dynamictreestfc";

    public DynamicTreesTFC()
    {

        ModEvents.init();

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
        }
    }

    public static ResourceLocation identifier(String path)
    {
        return new ResourceLocation(DynamicTreesTFC.MOD_ID, path);
    }

}
