package org.labellum.mc.dynamictreestfc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKits;
import com.ferreusveritas.dynamictrees.growthlogic.IGrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.objects.blocks.BlocksTFC;
import net.dries007.tfc.objects.blocks.stone.BlockRockVariant;
import net.dries007.tfc.objects.blocks.wood.BlockSaplingTFC;
import org.labellum.mc.dynamictreestfc.blocks.BlockLogDTTFC;
import org.labellum.mc.dynamictreestfc.trees.TreeFamilyTFC;

import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_ID;


public class ModTrees
{
    public static ArrayList<TreeFamilyTFC> tfcTrees = new ArrayList<>();
    public static Map<String, Species> tfcSpecies = new HashMap<>();

    public static void preInit()
    {
    }

    public static void registerBlocks(IForgeRegistry<Block> registry)
    {

        ArrayList<Block> treeBlocks = new ArrayList<>();

        Map<String, float[]> paramMap = new HashMap<>();
        Map<String, IGrowthLogicKit> logicMap = new HashMap<>();
        fillMaps(paramMap, logicMap);

        for (Tree t1 : TFCRegistries.TREES.getValuesCollection())
        {
            String treeName = t1.toString();

            ResourceLocation resLoc = new ResourceLocation(MOD_ID, treeName);

            TreeFamilyTFC family = new TreeFamilyTFC(resLoc, t1);

            tfcTrees.add(family);

            float[] map = paramMap.get(treeName) == null ? new float[] {0.20f, 10f, 3, 3, 1.00f} : paramMap.get(treeName);

            Species species = family.getCommonSpecies().setGrowthLogicKit(logicMap.get(treeName) == null ? GrowthLogicKits.nullLogic : logicMap.get(treeName)).
                setBasicGrowingParameters(map[0], map[1], (int) map[2], (int) map[3], map[4]);

            tfcSpecies.put(treeName, species);
            Species.REGISTRY.register(species);
            treeBlocks.add(new BlockLogDTTFC(t1).setRegistryName(MOD_ID, "block/log/" + treeName));
        }

        //Set up a map of species and their sapling types
        Map<String, BlockSaplingTFC> saplingMap = new HashMap<>();
        BlocksTFC.getAllSaplingBlocks().forEach(s -> saplingMap.put(s.getWood().toString(), s));

        for (Map.Entry<String, Species> entry : tfcSpecies.entrySet())
        {
            TreeRegistry.registerSaplingReplacer(saplingMap.get(entry.getKey()).getDefaultState(), entry.getValue());
        }

        tfcTrees.forEach(t -> {
            String treeName = t.getName().getPath();
            ModBlocks.leafMap.get(treeName).setTree(t);
            Species species = tfcSpecies.get(treeName);
            species.setLeavesProperties(ModBlocks.leafMap.get(treeName));

            switch (treeName)
            {
                case "acacia":
                    species.addAcceptableSoils(DirtHelper.HARDCLAYLIKE); //match base DT
                    break;
                case "douglas_fir":
                case "spruce":
                case "pine":
                case "sequoia":
                case "white_cedar":
                    species.addGenFeature(new FeatureGenConiferTopper(ModBlocks.leafMap.get(treeName)));
                    t.hasConiferVariants = true;
            }
        });

        tfcTrees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));

        treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(MOD_ID).values());
        registry.registerAll(treeBlocks.toArray(new Block[0]));
    }


    public static void registerItems(IForgeRegistry<Item> registry) //has to wait until TFC Items have been registered
    {
        TFCRegistries.TREES.getValuesCollection().forEach(t -> {
            String treeName = t.toString();
            ((TreeFamilyTFC) tfcSpecies.get(treeName).getFamily()).setPrimitiveLog(BlockLogDTTFC.get(t).getDefaultState());
        });
    }

    public static void postInit()
    {
        for (BlockRockVariant rock : BlocksTFC.getAllBlockRockVariants())
        {
            IBlockState def = rock.getDefaultState();
            if (BlocksTFC.isGrowableSoil(def))
            {
                DirtHelper.registerSoil(def.getBlock(),DirtHelper.DIRTLIKE);
            } else if (BlocksTFC.isSand(def))
            {
                DirtHelper.registerSoil(def.getBlock(),DirtHelper.SANDLIKE);
            } else if (BlocksTFC.isSoilOrGravel(def)) // soil caught above
            {
                DirtHelper.registerSoil(def.getBlock(),DirtHelper.GRAVELLIKE);
            }
        }
        DirtHelper.registerSoil(FluidsTFC.FRESH_WATER.get().getBlock(),DirtHelper.WATERLIKE);
        DirtHelper.registerSoil(FluidsTFC.SALT_WATER.get().getBlock(),DirtHelper.WATERLIKE); // maybe?
        // "hot spring water" won't grow trees, I expect
    }

    private static void fillMaps(Map<String, float[]> paramMap, Map<String, IGrowthLogicKit> logicMap)
    {
        paramMap.put("acacia", new float[] {0.10f, 14f, 6, 6, 0.90f});
        paramMap.put("ash", new float[] {0.25f, 12f, 4, 3, 1.00f});
        paramMap.put("aspen", new float[] {0.30f, 16f, 8, 2, 1.50f});
        paramMap.put("birch", new float[] {0.25f, 12f, 5, 5, 1.15f});
        paramMap.put("blackwood", new float[] {0.20f, 13f, 3, 4, 0.90f});
        paramMap.put("chestnut", new float[] {0.20f, 10f, 3, 3, 1.00f});
        paramMap.put("douglas_fir", new float[] {0.15f, 20f, 5, 3, 1.15f});
        paramMap.put("hickory", new float[] {0.20f, 14f, 5, 3, 0.80f});
        paramMap.put("kapok", new float[] {0.10f, 30f, 7, 4, 0.85f});
        paramMap.put("maple", new float[] {0.15f, 15f, 6, 3, 0.95f});
        paramMap.put("oak", new float[] {0.30f, 16f, 3, 3, 0.85f});
        paramMap.put("palm", new float[] {0.05f, 16f, 5, 4, 1.10f});
        paramMap.put("pine", new float[] {0.20f, 18f, 6, 2, 1.20f});
        paramMap.put("rosewood", new float[] {0.35f, 15f, 7, 3, 1.00f});
        paramMap.put("sequoia", new float[] {0.20f, 36f, 9, 4, 0.70f});
        paramMap.put("spruce", new float[] {0.15f, 12f, 6, 3, 1.10f});
        paramMap.put("sycamore", new float[] {0.20f, 10f, 4, 3, 0.90f});
        paramMap.put("white_cedar", new float[] {0.15f, 20f, 6, 2, 1.10f});
        paramMap.put("willow", new float[] {0.55f, 15f, 2, 2, 1.40f});
        //TFCTech
        paramMap.put("hevea", new float[] {0.20f, 13f, 3, 7, 1.25f});

        logicMap.put("acacia", GrowthLogicKits.nullLogic);
        logicMap.put("ash", GrowthLogicKits.nullLogic);
        logicMap.put("aspen", TreeRegistry.findGrowthLogicKit("Conifer"));
        logicMap.put("birch", GrowthLogicKits.nullLogic);
        logicMap.put("blackwood", TreeRegistry.findGrowthLogicKit("DarkOak"));
        logicMap.put("chestnut", TreeRegistry.findGrowthLogicKit("DarkOak"));
        logicMap.put("douglas_fir", TreeRegistry.findGrowthLogicKit("Conifer"));
        logicMap.put("hickory", TreeRegistry.findGrowthLogicKit("DarkOak"));
        logicMap.put("kapok", TreeRegistry.findGrowthLogicKit("Jungle"));
        logicMap.put("maple", GrowthLogicKits.nullLogic);
        logicMap.put("oak", TreeRegistry.findGrowthLogicKit("DarkOak"));
        logicMap.put("palm", TreeRegistry.findGrowthLogicKit("Jungle"));
        logicMap.put("pine", TreeRegistry.findGrowthLogicKit("Conifer"));
        logicMap.put("rosewood", GrowthLogicKits.nullLogic);
        logicMap.put("sequoia", new ConiferLogic(5.0f));
        logicMap.put("spruce", TreeRegistry.findGrowthLogicKit("Conifer"));
        logicMap.put("sycamore", GrowthLogicKits.nullLogic);
        logicMap.put("white_cedar", TreeRegistry.findGrowthLogicKit("Conifer"));
        logicMap.put("willow", TreeRegistry.findGrowthLogicKit("DarkOak"));
        //TFCTech
        logicMap.put("hevea", TreeRegistry.findGrowthLogicKit("DarkOak"));
    }

}
