package org.labellum.mc.dttfc.tree;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.soil.SoilBlock;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.labellum.mc.dttfc.content.RootyFluidBlock;

public class FluidSoilProperties extends SoilProperties
{
    public static final TypedRegistry.EntryType<SoilProperties> TYPE = TypedRegistry.newType(FluidSoilProperties::new);

    public FluidSoilProperties(ResourceLocation registryName)
    {
        super(null, registryName);
    }

    @Override
    protected SoilBlock createBlock(BlockBehaviour.Properties blockProperties)
    {
        return new RootyFluidBlock(this, blockProperties);
    }

    public MapColor getDefaultMapColor() {
        return MapColor.WATER;
    }

    @Override
    public BlockBehaviour.Properties getDefaultBlockProperties(MapColor mapColor)
    {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.WATER);
    }
}
