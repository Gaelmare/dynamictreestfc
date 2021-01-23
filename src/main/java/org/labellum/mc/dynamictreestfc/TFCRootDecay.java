package org.labellum.mc.dynamictreestfc;

import net.dries007.tfc.TerraFirmaCraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.ferreusveritas.dynamictrees.api.ICustomRootDecay;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.dries007.tfc.api.types.Rock;
import net.dries007.tfc.objects.blocks.stone.BlockRockVariant;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataProvider;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataTFC;

public class TFCRootDecay implements ICustomRootDecay
{
    public static TFCRootDecay INSTANCE = new TFCRootDecay();

    @Override
    public boolean doDecay(World world, BlockPos pos, IBlockState state, Species species)
    {
        ChunkDataTFC chunkData = world.getChunk(pos).getCapability(ChunkDataProvider.CHUNK_DATA_CAPABILITY, null);
        if (chunkData != null)
        {
            world.setBlockState(pos, BlockRockVariant.get(chunkData.getRockHeight(pos), Rock.Type.DIRT).getDefaultState(), 3);
            return true;
        } else if (world.getWorldType() == TerraFirmaCraft.getWorldType() )
        { //failed to get chunkdata, but tfc worldtype still, apply a default rocktype
            world.setBlockState(pos, BlockRockVariant.get(Rock.LIMESTONE, Rock.Type.DIRT).getDefaultState(), 3);
            return true;
        }
        return false;
    }
}
