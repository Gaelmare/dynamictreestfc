package org.labellum.mc.dynamictreestfc.proxy;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.objects.blocks.BlocksTFC;
import net.dries007.tfc.objects.blocks.stone.BlockRockVariant;
import org.labellum.mc.dynamictreestfc.DTFCGenerator;

public class CommonProxy
{
    public ImmutableList<BlockRockVariant> allGrowableVariants;
    public ImmutableList<Species> allDTSpecies;
    public void preInit() {
    }

    public void init() {
        replaceGenerators();
    }

    private void replaceGenerators() {

        for (Tree tree: TFCRegistries.TREES.getValues()) {
            tree.setTreeGenerator(new DTFCGenerator());
        }

        ImmutableList.Builder<BlockRockVariant> rockBuild = ImmutableList.builder();
        for (BlockRockVariant rock : BlocksTFC.getAllBlockRockVariants())
        {
            if (BlocksTFC.isGrowableSoil(rock.getDefaultState())) {
                rockBuild.add(rock);
            }
            allGrowableVariants = rockBuild.build();
        }

        ImmutableList.Builder<Species> specBuild = ImmutableList.builder();
        for ( ResourceLocation loc : TreeRegistry.getSpeciesDirectory())
        {
            specBuild.add(TreeRegistry.findSpecies(loc));
            allDTSpecies = specBuild.build();
        }

        for (Species species : allDTSpecies) {
            species.clearAcceptableSoils();
            Block[] blocks = new Block[]{};
            species.addAcceptableSoil( allGrowableVariants.toArray(blocks));
        }
    }

    public void postInit() {
    }
}
