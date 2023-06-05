package org.labellum.mc.dttfc.tree;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.rooty.RootyBlock;
import com.ferreusveritas.dynamictrees.block.rooty.SoilProperties;
import org.labellum.mc.dttfc.content.RootyGrassBlock;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GrassSoilProperties extends SoilProperties
{
    public static final TypedRegistry.EntryType<SoilProperties> TYPE = TypedRegistry.newType(GrassSoilProperties::new);

    public GrassSoilProperties(final ResourceLocation registryName)
    {
        this(null, registryName);
    }

    public GrassSoilProperties(@Nullable final Block primitiveBlock, final ResourceLocation registryName)
    {
        super(registryName);
        this.primitiveSoilBlock = primitiveBlock != null ? primitiveBlock : Blocks.AIR;
    }

    @Override
    protected RootyBlock createBlock(BlockBehaviour.Properties blockProperties)
    {
        return new RootyGrassBlock(this, blockProperties);
    }
}
