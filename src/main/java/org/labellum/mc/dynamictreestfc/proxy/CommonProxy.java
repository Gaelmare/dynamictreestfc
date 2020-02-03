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
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.ACACIA),
                TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, ACACIA)).
                        setBasicGrowingParameters(0.10f, 14f, 6, 6, 0.90f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.ASH),
                TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, BIRCH)).
                        setBasicGrowingParameters(0.25f, 12f, 4, 3, 1.00f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.ASPEN), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, BIRCH)).setBasicGrowingParameters(0.30f, 16f, 8, 3, 1.00f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.BIRCH), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, BIRCH)).setBasicGrowingParameters(0.25f, 12f, 5, 4, 1.15f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.BLACKWOOD), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, DARKOAK)).setBasicGrowingParameters(0.20f, 13f, 3, 4, 0.90f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.CHESTNUT), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, DARKOAK)).setBasicGrowingParameters(0.20f, 10f, 3, 3, 1.00f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.DOUGLAS_FIR), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, CONIFER)).setBasicGrowingParameters(0.15f, 15f, 5, 3, 1.15f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.HICKORY), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, DARKOAK)).setBasicGrowingParameters(0.20f, 14f, 5, 3, 0.80f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.KAPOK), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, JUNGLE)).setBasicGrowingParameters(0.10f, 24f, 7, 4, 0.85f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.MAPLE), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, OAK)).setBasicGrowingParameters(0.15f, 15f, 6, 3, 0.95f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.OAK), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, OAK)).setBasicGrowingParameters(0.30f, 16f, 3, 3, 0.90f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.PALM), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, JUNGLE)).setBasicGrowingParameters(0.05f, 16f, 5, 4, 1.10f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.PINE), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, CONIFER)).setBasicGrowingParameters(0.20f, 18f, 6, 2, 1.20f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.ROSEWOOD), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, OAK)).setBasicGrowingParameters(0.35f, 15f, 7, 3, 1.00f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.SEQUOIA), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, CONIFER)).setBasicGrowingParameters(0.20f, 28f, 9, 4, 0.70f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.SPRUCE), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, SPRUCE)).setBasicGrowingParameters(0.15f, 12f, 6, 3, 1.10f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.SYCAMORE), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, SPRUCE)).setBasicGrowingParameters(0.20f, 10f, 4, 3, 0.90f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.WHITE_CEDAR), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, CONIFER)).setBasicGrowingParameters(0.15f, 20f, 6, 2, 1.10f));
        mapBuild.put(TFCRegistries.TREES.getValue(DefaultTrees.WILLOW), TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, BIRCH)).setBasicGrowingParameters(0.55f, 8f, 4, 5, 1.40f));

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
