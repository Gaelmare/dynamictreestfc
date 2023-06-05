package org.labellum.mc.dttfc;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.compat.CompatHandler;
import org.labellum.mc.dttfc.client.ClientModEvents;
import org.labellum.mc.dttfc.util.ModFeatures;
import org.labellum.mc.dttfc.util.TFCSeasonManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(DTTFC.MOD_ID)
public class DTTFC
{
    public static final String MOD_ID = "dttfc";

    public DTTFC()
    {
        RegistryHandler.setup(MOD_ID);
        ConfigDTTFC.register();
        ModEvents.init();
        ForgeEvents.init();
        CompatHandler.registerSeasonManager(MOD_ID, TFCSeasonManager::new);

        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModFeatures.FEATURES.register(bus);

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientModEvents.init();
        }
    }

    public static ResourceLocation identifier(String path)
    {
        return new ResourceLocation(DTTFC.MOD_ID, path);
    }

}
