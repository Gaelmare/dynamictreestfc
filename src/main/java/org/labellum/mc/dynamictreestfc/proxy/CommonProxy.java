package org.labellum.mc.dynamictreestfc.proxy;

import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.objects.blocks.BlocksTFC;
import net.dries007.tfc.objects.blocks.stone.BlockRockVariant;
import net.dries007.tfc.types.DefaultTrees;
import org.labellum.mc.dynamictreestfc.DTFCGenerator;

import static com.ferreusveritas.dynamictrees.ModTrees.*;

public class CommonProxy
{
    public static ImmutableList<BlockRockVariant> allGrowableVariants;
    public static ImmutableList<Species> allDTSpecies;

    public static Map<Tree, Species> treeMap;

    public void preInit() {
    }

    public void init() {
        buildMaps();
        replaceGenerators();
    }

    public void buildMaps()
    {
        ImmutableMap.Builder<Tree, Species> mapBuild = ImmutableMap.builder();
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.ACACIA), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, ACACIA)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.ASH), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, BIRCH)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.ASPEN), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, BIRCH)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.BIRCH), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, BIRCH)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.BLACKWOOD), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, DARKOAK)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.CHESTNUT), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, DARKOAK)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.DOUGLAS_FIR), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, CONIFER)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.HICKORY), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, DARKOAK)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.KAPOK), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, JUNGLE)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.MAPLE), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, OAK)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.OAK), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, OAK)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.PALM), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, JUNGLE)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.PINE), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, CONIFER)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.ROSEWOOD), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, OAK)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.SEQUOIA), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, CONIFER)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.SPRUCE), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, SPRUCE)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.SYCAMORE), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, SPRUCE)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.WHITE_CEDAR), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, CONIFER)));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.WILLOW), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, BIRCH)));

        treeMap = mapBuild.build();

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

    }

    private void replaceGenerators() {

        for (Tree tree: TFCRegistries.TREES.getValues()) {
            tree.setTreeGenerator(new DTFCGenerator());
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
