package org.labellum.mc.dttfc.client;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.labellum.mc.dttfc.DTTFC;

public final class ClientModEvents
{
    public static void init(IEventBus modEventBus)
    {
        modEventBus.addListener(ClientModEvents::onModelRegister);
    }

    public static void onModelRegister(ModelEvent.RegisterGeometryLoaders event)
    {
        event.register(ResourceLocation.fromNamespaceAndPath(DTTFC.MOD_ID, "palm_fronds"), new PalmLeavesModelLoader());
    }
}
