package org.labellum.mc.dynamictreestfc.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.ferreusveritas.dynamictrees.blocks.BlockRootyDirt;
import net.dries007.tfc.api.types.Rock;
import net.dries007.tfc.objects.blocks.stone.BlockRockVariant;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataProvider;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataTFC;

public class BlockRootyTFC extends BlockRootyDirt
{

    static String name = "rootydirttfc";

    public BlockRootyTFC()
    {
        super(false);
    }

    public IBlockState getDecayBlockState(IBlockAccess access, BlockPos pos) {
        Chunk chunk = ((World)access).getChunk(pos); //puerile cast of interface
        ChunkDataTFC chunkData = chunk.getCapability(ChunkDataProvider.CHUNK_DATA_CAPABILITY, null);
        if (chunkData == null) { //we're not in Kansas anymore
            return Blocks.DIRT.getDefaultState();
        }
        return BlockRockVariant.get(chunkData.getRockHeight(pos), Rock.Type.DIRT).getDefaultState();
    }
}
