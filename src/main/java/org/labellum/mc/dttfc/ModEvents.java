package org.labellum.mc.dttfc;

import com.dtteam.dynamictrees.api.cell.CellKit;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import com.dtteam.dynamictrees.systems.growthlogic.GrowthLogicKit;
import com.dtteam.dynamictrees.tree.family.Family;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import org.labellum.mc.dttfc.tree.AerialRootsFluidSoilProperties;
import org.labellum.mc.dttfc.tree.DiagonalPalmFamily;
import org.labellum.mc.dttfc.tree.DiagonalPalmLogic;
import org.labellum.mc.dttfc.tree.FluidSoilProperties;
import org.labellum.mc.dttfc.tree.GrassSoilProperties;
import org.labellum.mc.dttfc.tree.PalmCellKit;
import org.labellum.mc.dttfc.tree.TFCMangroveFamily;

public final class ModEvents
{
    private static boolean initialized;

    public static void init(IEventBus modEventBus)
    {
        if (initialized) {
            return;
        }
        initialized = true;

        registerFamilyTypes();
        registerGrowthLogicAndCellKits();
        registerSoilTypesAndEntries();
    }

    private static void registerFamilyTypes()
    {
        final ResourceLocation diagonalPalmId = DTTFC.identifier("diagonal_palm");
        if (!Family.REGISTRY.hasType(diagonalPalmId)) {
            Family.REGISTRY.registerType(diagonalPalmId, DiagonalPalmFamily.TYPE);
        }

        final ResourceLocation mangroveId = DTTFC.identifier("mangrove");
        if (!Family.REGISTRY.hasType(mangroveId)) {
            Family.REGISTRY.registerType(mangroveId, TFCMangroveFamily.TFC_TYPE);
        }
    }

    private static void registerGrowthLogicAndCellKits()
    {
        final ResourceLocation diagonalPalmLogicId = DTTFC.identifier("diagonal_palm");
        if (!GrowthLogicKit.REGISTRY.has(diagonalPalmLogicId)) {
            GrowthLogicKit.REGISTRY.register(new DiagonalPalmLogic(diagonalPalmLogicId));
        }

        final ResourceLocation palmCellKitId = DTTFC.identifier("palm");
        if (!CellKit.REGISTRY.has(palmCellKitId)) {
            CellKit.REGISTRY.register(new PalmCellKit(palmCellKitId));
        }
    }

    private static void registerSoilTypesAndEntries()
    {
        final ResourceLocation grassId = DTTFC.identifier("grass");
        if (!SoilProperties.REGISTRY.hasType(grassId)) {
            SoilProperties.REGISTRY.registerType(grassId, GrassSoilProperties.TYPE);
        }

        final ResourceLocation fluidId = DTTFC.identifier("fluid");
        if (!SoilProperties.REGISTRY.hasType(fluidId)) {
            SoilProperties.REGISTRY.registerType(fluidId, FluidSoilProperties.TYPE);
        }

        final ResourceLocation aerialRootsId = DTTFC.identifier("aerial_roots");
        if (!SoilProperties.REGISTRY.hasType(aerialRootsId)) {
            SoilProperties.REGISTRY.registerType(aerialRootsId, AerialRootsFluidSoilProperties.TFC_TYPE);
        }

        final ResourceLocation saltWaterId = DTTFC.identifier("salt_water");
        if (!SoilProperties.REGISTRY.has(saltWaterId)) {
            SoilProperties.REGISTRY.register(new FluidSoilProperties(saltWaterId));
        }

        final ResourceLocation mangroveAerialRootsId = DTTFC.identifier("mangrove_aerial_roots");
        if (!SoilProperties.REGISTRY.has(mangroveAerialRootsId)) {
            SoilProperties.REGISTRY.register(new AerialRootsFluidSoilProperties(mangroveAerialRootsId));
        }
    }

}
