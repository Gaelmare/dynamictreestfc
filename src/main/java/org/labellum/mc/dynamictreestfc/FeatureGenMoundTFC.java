package org.labellum.mc.dynamictreestfc;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import com.ferreusveritas.dynamictrees.worldgen.JoCode;
import net.dries007.tfc.api.types.Rock;
import net.dries007.tfc.objects.blocks.stone.BlockRockVariant;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataProvider;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataTFC;

// almost worth doing an access transformer of the private fields instead of re-implementation

public class FeatureGenMoundTFC extends FeatureGenMound
{

    private static final SimpleVoxmap moundMap = new SimpleVoxmap(5, 4, 5, new byte[] {
        0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 2, 2, 2, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0,
        0, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 0,
        0, 1, 1, 1, 0, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 0, 1, 1, 1, 0,
        0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0
    }).setCenter(new BlockPos(2, 3, 2));

    private final int moundCutoffRadius;

    public FeatureGenMoundTFC(int moundCutoffRadius)
    {
        super(moundCutoffRadius);
        this.moundCutoffRadius = moundCutoffRadius;
    }


    /**
     * Used to create a 5x4x5 rounded mound that is one block higher than the ground surface.
     * This is meant to replicate the appearance of a root hill and gives generated surface
     * roots a better appearance.
     *
     * @param world      The world
     * @param rootPos    The position of the rooty dirt
     * @param safeBounds A safebounds structure for preventing runaway cascading generation
     * @return The modified position of the rooty dirt that is one block higher
     */
    @Override
    public BlockPos preGeneration(World world, BlockPos rootPos, Species species, int radius, EnumFacing facing, SafeChunkBounds safeBounds, JoCode joCode)
    {
        if (radius >= moundCutoffRadius && safeBounds != SafeChunkBounds.ANY)
        {//worldgen test
            IBlockState initialDirtState = world.getBlockState(rootPos);
            IBlockState initialUnderState = world.getBlockState(rootPos.down());

            if (initialUnderState.getMaterial() == Material.AIR || (initialUnderState.getMaterial() != Material.GROUND && initialUnderState.getMaterial() != Material.ROCK))
            {
                ChunkDataTFC chunkData = world.getChunk(rootPos).getCapability(ChunkDataProvider.CHUNK_DATA_CAPABILITY, null);
                initialUnderState = BlockRockVariant.get(chunkData.getRockHeight(rootPos), Rock.Type.DIRT).getDefaultState();
            }

            rootPos = rootPos.up();

            for (SimpleVoxmap.Cell cell : moundMap.getAllNonZeroCells())
            {
                IBlockState placeState = cell.getValue() == 1 ? initialDirtState : initialUnderState;
                world.setBlockState(rootPos.add(cell.getPos()), placeState);
            }
        }

        return rootPos;
    }
}
