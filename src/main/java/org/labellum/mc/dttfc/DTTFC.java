package org.labellum.mc.dttfc;

import com.dtteam.dynamictrees.registry.NeoForgeRegistryHandler;
import com.dtteam.dynamictrees.systems.season.SeasonCompatibilityHandler;
import org.labellum.mc.dttfc.client.ClientModEvents;
import org.labellum.mc.dttfc.content.ModBlocks;
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

    public DTTFC(IEventBus modBus, ModContainer modContainer)
    {
        NeoForgeRegistryHandler.setup(MOD_ID, modBus);
        ConfigDTTFC.register(modContainer);
        ModEvents.init(modBus);
        ForgeEvents.init(modBus);
        SeasonCompatibilityHandler.registerSeasonManager(MOD_ID, TFCSeasonManager::new);

        ModBlocks.register(modBus);
        ModFeatures.FEATURES.register(modBus);

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientModEvents.init(modBus);
        }
    }

    public static ResourceLocation identifier(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(DTTFC.MOD_ID, path);
    }

}
