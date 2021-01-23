package org.labellum.mc.dynamictreestfc.blocks;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.dries007.tfc.world.classic.chunkdata.ChunkDataProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


import com.ferreusveritas.dynamictrees.blocks.BlockRootyDirt;
import net.dries007.tfc.api.types.Rock;
import net.dries007.tfc.objects.blocks.stone.BlockRockVariant;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataTFC;

@ParametersAreNonnullByDefault
public class BlockRootyDirtTFC extends BlockRootyDirt
{
    private static final EnumFacing[] NOT_UP = new EnumFacing[] {EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.SOUTH};

    public BlockRootyDirtTFC()
    {
        super(false);
    }

    @Override
    public IBlockState getMimic(IBlockAccess access, BlockPos pos) // this IBlockAccess is actually a ChunkCache which has no World access (therefore no chunk data)
    {
        IBlockState mimicState = super.getMimic(access, pos);
        if (mimicState.getBlock() == Blocks.DIRT)
        {
            for (int i = 1; i < 4; i++) // so we will search manually
            {
                for (EnumFacing d : NOT_UP)
                {
                    IBlockState state = access.getBlockState(pos.offset(d, i));
                    if (state.getBlock() instanceof BlockRockVariant)
                    {
                        Rock rock = ((BlockRockVariant) state.getBlock()).getRock();
                        return BlockRockVariant.get(rock, Rock.Type.DIRT).getDefaultState();
                    }
                }
            }
            // this doesn't *really* matter because the decay BlockState has World access and will always be correct
            // so in the 0.00001% of cases where the rooty block is somehow floating with nothing around, this will do.
            return BlockRockVariant.get(Rock.LIMESTONE, Rock.Type.DIRT).getDefaultState();
        }
        return mimicState;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        drops.clear();
        drops.add(new ItemStack(getDecayBlockState(world, pos).getBlock()));
    }

    @Override
    public IBlockState getDecayBlockState(IBlockAccess world, BlockPos pos)
    {
        if (world instanceof World) //lol
        {
            ChunkDataTFC chunkData = ((World) world).getChunk(pos).getCapability(ChunkDataProvider.CHUNK_DATA_CAPABILITY, null);
            if (chunkData != null) {
                Rock rock = chunkData.getRockHeight(pos);
                return BlockRockVariant.get(rock, Rock.Type.DIRT).getDefaultState();
            }
        }
        return super.getDecayBlockState(world, pos);
    }
}
