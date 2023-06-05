package org.labellum.mc.dttfc;

import com.ferreusveritas.dynamictrees.api.cell.CellKit;
import com.ferreusveritas.dynamictrees.api.registry.RegistryEvent;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.block.rooty.SoilProperties;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import org.labellum.mc.dttfc.tree.DiagonalPalmFamily;
import org.labellum.mc.dttfc.tree.DiagonalPalmLogic;
import org.labellum.mc.dttfc.tree.GrassSoilProperties;
import org.labellum.mc.dttfc.tree.PalmCellKit;
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
        bus.addGenericListener(SoilProperties.class, ModEvents::registerSoils);
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

    public static void registerSoils(TypeRegistryEvent<SoilProperties> event)
    {
        event.registerType(DTTFC.identifier("grass"), GrassSoilProperties.TYPE);
    }


}
