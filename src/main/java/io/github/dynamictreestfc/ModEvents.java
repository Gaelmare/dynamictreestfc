package io.github.dynamictreestfc;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.api.registry.RegistryEvent;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.leaves.SolidLeavesProperties;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import io.github.dynamictreestfc.util.TFCForestCanceller;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.dries007.tfc.common.blocks.wood.Wood;

public final class ModEvents
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addGenericListener(Family.class, ModEvents::registerFamilies);
        bus.addGenericListener(FeatureCanceller.class, ModEvents::registerFeatureCancellers);
        bus.addGenericListener(LeavesProperties.class, ModEvents::registerLeafProperties);
    }

    public static final FeatureCanceller FOREST_CANCELLER = new TFCForestCanceller(DynamicTreesTFC.identifier("forest"));

    public static void registerFamilies(TypeRegistryEvent<Family> event)
    {
        for (Wood wood : Wood.VALUES)
        {
            event.registerType(DynamicTreesTFC.identifier(wood.getSerializedName()), Family.TYPE);
        }
    }

    public static void registerLeafProperties(TypeRegistryEvent<LeavesProperties> event)
    {
        for (Wood wood : Wood.VALUES)
        {
            event.registerType(DynamicTreesTFC.identifier(wood.getSerializedName()), SolidLeavesProperties.TYPE);
        }
    }

    public static void registerFeatureCancellers(RegistryEvent<FeatureCanceller> event)
    {
        final Registry<FeatureCanceller> r = event.getRegistry();
        r.register(FOREST_CANCELLER);
    }

}
