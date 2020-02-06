package org.labellum.mc.dynamictreestfc.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.BlockRootyDirt;
import net.dries007.tfc.api.types.Rock;
import net.dries007.tfc.objects.blocks.stone.BlockRockVariant;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataProvider;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataTFC;

public class BlockRootyTFC extends BlockRooty
{
    public BlockRootyTFC()
    {
        this("rootydirt",Material.GROUND , false);
    }


    public BlockRootyTFC(String name, Material material, boolean isTileEntity)
    {
        super(name, material, isTileEntity);

    }

    public IBlockState getDecayBlockState(IBlockAccess access, BlockPos pos) {
        Chunk chunk = ((World)access).getChunk(pos); //puerile cast of interface
        ChunkDataTFC chunkData = chunk.getCapability(ChunkDataProvider.CHUNK_DATA_CAPABILITY, null);
        if (chunkData == null) throw new IllegalStateException("ChunkData capability is missing.");
        return BlockRockVariant.get(chunkData.getRock1(pos), Rock.Type.DIRT).getDefaultState();
    }
}
