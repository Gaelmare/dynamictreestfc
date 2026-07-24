package org.labellum.mc.dttfc;

import com.dtteam.dynamictrees.api.cell.CellKit;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.block.leaves.PalmLeavesProperties;
import com.dtteam.dynamictrees.event.RegistryEvent;
import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import com.dtteam.dynamictrees.systems.growthlogic.GrowthLogicKit;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import org.labellum.mc.dttfc.tree.AerialRootsFluidSoilProperties;
import org.labellum.mc.dttfc.tree.DeadPalmSpecies;
import org.labellum.mc.dttfc.tree.DeadSpecies;
import org.labellum.mc.dttfc.tree.DeadUndergroundRootsSpecies;
import org.labellum.mc.dttfc.tree.DiagonalPalmFamily;
import org.labellum.mc.dttfc.tree.DiagonalPalmLogic;
import org.labellum.mc.dttfc.tree.FluidSoilProperties;
import org.labellum.mc.dttfc.tree.GrassSoilProperties;
import org.labellum.mc.dttfc.tree.PalmCellKit;
import net.neoforged.bus.api.IEventBus;
import org.labellum.mc.dttfc.tree.TFCMangroveFamily;

public final class ModEvents
{
    public static void init(IEventBus bus)
    {
        bus.addListener(ModEvents::registerFamilies);
        bus.addListener(ModEvents::registerSpecies);
        bus.addListener(ModEvents::registerGrowth);
        bus.addListener(ModEvents::registerCells);
        bus.addListener(ModEvents::registerSoils);
        bus.addListener(ModEvents::registerLeaves);
    }

    public static void registerFamilies(TypeRegistryEvent<?> event)
    {
        if (!event.isEntryOfType(Family.class))
        {
            return;
        }

        @SuppressWarnings("unchecked")
        final TypeRegistryEvent<Family> familyEvent = (TypeRegistryEvent<Family>) event;

        familyEvent.registerType(DTTFC.identifier("diagonal_palm"), DiagonalPalmFamily.TYPE);
        familyEvent.registerType(DTTFC.identifier("mangrove"), TFCMangroveFamily.TFC_TYPE);
    }

    public static void registerSpecies(TypeRegistryEvent<?> event)
    {
        if (!event.isEntryOfType(Species.class))
        {
            return;
        }

        @SuppressWarnings("unchecked")
        final TypeRegistryEvent<Species> speciesEvent = (TypeRegistryEvent<Species>) event;

        speciesEvent.registerType(DTTFC.identifier("dead"), DeadSpecies.TYPE);
        speciesEvent.registerType(DTTFC.identifier("dead_palm"), DeadPalmSpecies.TYPE);
        speciesEvent.registerType(DTTFC.identifier("dead_underground_roots"), DeadUndergroundRootsSpecies.TYPE);
    }

    public static void registerGrowth(RegistryEvent<?> event)
    {
        if (!event.isEntryOfType(GrowthLogicKit.class))
        {
            return;
        }

        @SuppressWarnings("unchecked")
        final RegistryEvent<GrowthLogicKit> growthEvent = (RegistryEvent<GrowthLogicKit>) event;

        growthEvent.getRegistry().register(new DiagonalPalmLogic(DTTFC.identifier("diagonal_palm")));
    }

    public static void registerCells(RegistryEvent<?> event)
    {
        if (!event.isEntryOfType(CellKit.class))
        {
            return;
        }

        @SuppressWarnings("unchecked")
        final RegistryEvent<CellKit> cellEvent = (RegistryEvent<CellKit>) event;

        cellEvent.getRegistry().register(new PalmCellKit(DTTFC.identifier("palm")));
    }

    public static void registerSoils(TypeRegistryEvent<?> event)
    {
        if (!event.isEntryOfType(SoilProperties.class))
        {
            return;
        }

        @SuppressWarnings("unchecked")
        final TypeRegistryEvent<SoilProperties> soilEvent = (TypeRegistryEvent<SoilProperties>) event;

        soilEvent.registerType(DTTFC.identifier("grass"), GrassSoilProperties.TYPE);
        soilEvent.registerType(DTTFC.identifier("fluid"), FluidSoilProperties.TYPE);
        soilEvent.registerType(DTTFC.identifier("aerial_roots"), AerialRootsFluidSoilProperties.TFC_TYPE);
    }

    public static void registerLeaves(TypeRegistryEvent<?> event)
    {
        if (!event.isEntryOfType(LeavesProperties.class))
        {
            return;
        }

        @SuppressWarnings("unchecked")
        final TypeRegistryEvent<LeavesProperties> leavesEvent = (TypeRegistryEvent<LeavesProperties>) event;

        leavesEvent.registerType(DTTFC.identifier("palm"), PalmLeavesProperties.TYPE);
    }

}
