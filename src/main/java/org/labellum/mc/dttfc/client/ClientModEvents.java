package org.labellum.mc.dttfc.client;

import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class ClientModEvents
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ClientModEvents::onModelRegister);
    }

    public static void onModelRegister(ModelEvent.RegisterGeometryLoaders event)
    {
        event.register("palm_fronds", new PalmLeavesModelLoader());
    }
}
