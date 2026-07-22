package org.labellum.mc.dttfc.tree;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.soil.AerialRootsSoilProperties;
import com.dtteam.dynamictrees.block.soil.SoilBlock;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class AerialRootsFluidSoilProperties extends AerialRootsSoilProperties
{
    public static final TypedRegistry.EntryType<SoilProperties> TFC_TYPE = TypedRegistry.newType(AerialRootsFluidSoilProperties::new);

    public AerialRootsFluidSoilProperties(ResourceLocation registryName)
    {
        super(registryName);
    }

    @Override
    protected SoilBlock createBlock(BlockBehaviour.Properties blockProperties)
    {
        return new RootyRootFluidBlock(this, blockProperties);
    }
}
