package org.labellum.mc.dttfc.client;

import org.labellum.mc.dttfc.DTTFC;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class ClientModEvents
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ClientModEvents::onModelBake);
        bus.addListener(ClientModEvents::onModelRegister);
    }

    public static void onModelRegister(ModelRegistryEvent event)
    {
        ModelLoaderRegistry.registerLoader(DTTFC.identifier("palm_fronds"), new PalmLeavesModelLoader());
    }

    public static void onModelBake(ModelBakeEvent event)
    {
        // Setup fronds models
        PalmLeavesBakedModel.INSTANCES.forEach(PalmLeavesBakedModel::setupModels);
    }
}
