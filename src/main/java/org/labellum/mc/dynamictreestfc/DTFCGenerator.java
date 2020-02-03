package org.labellum.mc.dynamictreestfc;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;


import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.api.util.ITreeGenerator;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataProvider;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataTFC;
import org.labellum.mc.dynamictreestfc.proxy.CommonProxy;


public class DTFCGenerator implements ITreeGenerator
{

    @Override
    public void generateTree(TemplateManager templateManager, World world, BlockPos blockPos, Tree tree, Random random, boolean b)
    {
        Species dtSpecies = CommonProxy.treeMap.get(tree);
        SafeChunkBounds bounds = new SafeChunkBounds(world, world.getChunk(blockPos).getPos());
        dtSpecies.generate(world, blockPos.down(), world.getBiome(blockPos), random, 8, bounds);
        //getJoCode("JP").setCareful(true).generate(world, dtSpecies, blockPos, world.getBiome(blockPos), EnumFacing.SOUTH, 8, SafeChunkBounds.ANY);
    }

    @Override
    public boolean canGenerateTree(World world, BlockPos pos, Tree treeType)
    {
        //experimental plains?
        ChunkDataTFC chunkData = world.getChunk(pos).getCapability(ChunkDataProvider.CHUNK_DATA_CAPABILITY, null);
        if (chunkData != null)
        {
            if ((chunkData.getFloraDensity() * 100) % 10 == 0) {
                return false;
            }
        }

        return ITreeGenerator.super.canGenerateTree(world, pos, treeType);
    }
}
