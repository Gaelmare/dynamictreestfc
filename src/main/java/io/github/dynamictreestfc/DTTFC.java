package io.github.dttfc;


import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(io.github.dttfc.DTTFC.MOD_ID)
public class DTTFC
{
    public static final String MOD_ID = "dttfc";

    public DTTFC()
    {
        RegistryHandler.setup(MOD_ID);
        io.github.dttfc.ModEvents.init();

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
        }
    }

    public static ResourceLocation identifier(String path)
    {
        return new ResourceLocation(io.github.dttfc.DTTFC.MOD_ID, path);
    }

}
