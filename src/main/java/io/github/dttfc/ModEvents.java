package io.github.dttfc;

import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.leaves.SolidLeavesProperties;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.dries007.tfc.common.blocks.wood.Wood;

public final class ModEvents
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addGenericListener(Family.class, ModEvents::registerFamilies);
        bus.addGenericListener(LeavesProperties.class, ModEvents::registerLeafProperties);
    }

    public static void registerFamilies(TypeRegistryEvent<Family> event)
    {
        for (Wood wood : Wood.VALUES)
        {
            event.registerType(DTTFC.identifier(wood.getSerializedName()), Family.TYPE);
        }
    }

    public static void registerLeafProperties(TypeRegistryEvent<LeavesProperties> event)
    {
        for (Wood wood : Wood.VALUES)
        {
            event.registerType(DTTFC.identifier(wood.getSerializedName()), SolidLeavesProperties.TYPE);
        }
    }

}
