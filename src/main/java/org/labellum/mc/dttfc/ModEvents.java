package org.labellum.mc.dttfc;

import com.dtteam.dynamictrees.api.cell.CellKit;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import com.dtteam.dynamictrees.event.RegistryEvent;
import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import com.dtteam.dynamictrees.systems.growthlogic.GrowthLogicKit;
import com.dtteam.dynamictrees.tree.family.Family;
import org.labellum.mc.dttfc.tree.AerialRootsFluidSoilProperties;
import org.labellum.mc.dttfc.tree.DiagonalPalmFamily;
import org.labellum.mc.dttfc.tree.DiagonalPalmLogic;
import org.labellum.mc.dttfc.tree.FluidSoilProperties;
import org.labellum.mc.dttfc.tree.GrassSoilProperties;
import org.labellum.mc.dttfc.tree.PalmCellKit;
import net.neoforged.bus.api.IEventBus;
import org.labellum.mc.dttfc.tree.TFCMangroveFamily;

public final class ModEvents
{
    public static void init(IEventBus modEventBus)
    {
        modEventBus.addListener(ModEvents::registerFamilies);
        modEventBus.addListener(ModEvents::registerGrowth);
        modEventBus.addListener(ModEvents::registerCells);
        modEventBus.addListener(ModEvents::registerSoils);
    }

    public static void registerFamilies(TypeRegistryEvent<Family> event)
    {
        event.registerType(DTTFC.identifier("diagonal_palm"), DiagonalPalmFamily.TYPE);
        event.registerType(DTTFC.identifier("mangrove"), TFCMangroveFamily.TFC_TYPE);
    }

    public static void registerGrowth(RegistryEvent<GrowthLogicKit> event)
    {
        if (event.isEntryOfType(GrowthLogicKit.class)) {
            event.getRegistry().register(new DiagonalPalmLogic(DTTFC.identifier("diagonal_palm")));
        }
    }

    public static void registerCells(RegistryEvent<CellKit> event)
    {
        if (event.isEntryOfType(CellKit.class)) {
            event.getRegistry().register(new PalmCellKit(DTTFC.identifier("palm")));
        }
    }

    public static void registerSoils(TypeRegistryEvent<SoilProperties> event)
    {
        event.registerType(DTTFC.identifier("grass"), GrassSoilProperties.TYPE);
        event.registerType(DTTFC.identifier("fluid"), FluidSoilProperties.TYPE);
        event.registerType(DTTFC.identifier("aerial_roots"), AerialRootsFluidSoilProperties.TFC_TYPE);
    }


}
