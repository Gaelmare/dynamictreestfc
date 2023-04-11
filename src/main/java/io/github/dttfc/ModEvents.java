package io.github.dttfc;

import com.ferreusveritas.dynamictrees.api.cell.CellKit;
import com.ferreusveritas.dynamictrees.api.registry.RegistryEvent;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import io.github.dttfc.tree.DiagonalPalmFamily;
import io.github.dttfc.tree.DiagonalPalmLogic;
import io.github.dttfc.tree.PalmCellKit;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class ModEvents
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addGenericListener(Family.class, ModEvents::registerFamilies);
        bus.addGenericListener(GrowthLogicKit.class, ModEvents::registerGrowth);
        bus.addGenericListener(CellKit.class, ModEvents::registerCells);
        bus.addGenericListener(LeavesProperties.class, ModEvents::registerLeafProperties);
    }

    public static void registerFamilies(TypeRegistryEvent<Family> event)
    {
        event.registerType(DTTFC.identifier("diagonal_palm"), DiagonalPalmFamily.TYPE);
    }

    public static void registerGrowth(RegistryEvent<GrowthLogicKit> event)
    {
        event.getRegistry().register(new DiagonalPalmLogic(DTTFC.identifier("diagonal_palm")));
    }

    public static void registerCells(RegistryEvent<CellKit> event)
    {
        event.getRegistry().register(new PalmCellKit(DTTFC.identifier("palm")));
    }

    public static void registerLeafProperties(TypeRegistryEvent<LeavesProperties> event)
    {
    }

}
