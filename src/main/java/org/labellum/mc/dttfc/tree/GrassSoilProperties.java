package org.labellum.mc.dttfc.tree;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.soil.SoilBlock;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
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
    protected SoilBlock createBlock(BlockBehaviour.Properties blockProperties)
    {
        return new RootyGrassBlock(this, blockProperties);
    }
}
