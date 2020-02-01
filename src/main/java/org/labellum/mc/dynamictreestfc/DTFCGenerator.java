package org.labellum.mc.dynamictreestfc;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;


import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.api.util.ITreeGenerator;
import org.labellum.mc.dynamictreestfc.proxy.CommonProxy;


public class DTFCGenerator implements ITreeGenerator
{

    @Override
    public void generateTree(TemplateManager templateManager, World world, BlockPos blockPos, Tree tree, Random random, boolean b)
    {
        Species dtSpecies = CommonProxy.treeMap.get(tree);
        dtSpecies.generate(world, blockPos.down(), world.getBiome(blockPos), random, 8, SafeChunkBounds.ANY);
        //getJoCode("JP").setCareful(true).generate(world, dtSpecies, blockPos, world.getBiome(blockPos), EnumFacing.SOUTH, 8, SafeChunkBounds.ANY);
    }

    @Override
    public boolean canGenerateTree(World world, BlockPos pos, Tree treeType)
    {
        return ITreeGenerator.super.canGenerateTree(world, pos, treeType);
    }
}
