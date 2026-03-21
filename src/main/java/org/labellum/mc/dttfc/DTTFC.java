package org.labellum.mc.dttfc;

import com.dtteam.dynamictrees.registry.NeoForgeRegistryHandler;
import com.dtteam.dynamictrees.systems.season.SeasonCompatibilityHandler;

import org.labellum.mc.dttfc.client.ClientModEvents;
import org.labellum.mc.dttfc.util.ModFeatures;
import org.labellum.mc.dttfc.util.TFCSeasonManager;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(DTTFC.MOD_ID)
public class DTTFC
{
    public static final String MOD_ID = "dttfc";

    public DTTFC(IEventBus modEventBus, ModContainer container)
    {
        NeoForgeRegistryHandler.setup(MOD_ID, modEventBus);
        ConfigDTTFC.register(container);
        ModEvents.init(modEventBus);
        ForgeEvents.init(modEventBus);
        SeasonCompatibilityHandler.registerSeasonManager(MOD_ID, TFCSeasonManager::new);

        ModFeatures.FEATURES.register(modEventBus);

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientModEvents.init(modEventBus);
        }
    }

    public static ResourceLocation identifier(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(DTTFC.MOD_ID, path);
    }

}
