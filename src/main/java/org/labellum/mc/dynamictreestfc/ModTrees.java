package org.labellum.mc.dynamictreestfc;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKits;
import com.ferreusveritas.dynamictrees.growthlogic.IGrowthLogicKit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.objects.blocks.BlocksTFC;
import net.dries007.tfc.objects.blocks.wood.BlockLeavesTFC;
import net.dries007.tfc.objects.blocks.wood.BlockSaplingTFC;

import org.labellum.mc.dynamictreestfc.trees.TreeFamilyTFC;


import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_ID;


public class ModTrees
{
    public static ArrayList<TreeFamily> tfcTrees = new ArrayList<>();
    public static Map<String, Species> tfcSpecies = new HashMap<>();

    public static void preInit() {

        //Set up a map of species and their sapling types
        Map<String, BlockSaplingTFC> saplingMap = new HashMap<>();
        BlocksTFC.getAllSaplingBlocks().forEach(s -> saplingMap.put(s.wood.toString(),s));

        //Map of strings to leaves
        Map<String, BlockLeavesTFC> leafMap = new HashMap<>();
        BlocksTFC.getAllLeafBlocks().forEach(s -> leafMap.put(s.wood.toString(),s));

        Map<String, float[]> paramMap = new HashMap<>();
        Map<String, IGrowthLogicKit> logicMap = new HashMap<>();
        fillMaps(paramMap,logicMap);

        TFCRegistries.TREES.getValuesCollection().forEach(t -> {
            String treeName = t.toString();
            ResourceLocation resLoc = new ResourceLocation(MOD_ID, treeName);
            IBlockState leaf = leafMap.get(treeName).getDefaultState();

            TreeFamily family = new TreeFamilyTFC(resLoc,t);
            if(t.toString() == "sequoia" ||
               t.toString() == "kapok" ) {
                ((TreeFamilyTFC) family).setThick(true);
            }
            tfcTrees.add(family);

            ModBlocks.leafMap.get(treeName).setTree(family);

            Species species = family.getCommonSpecies().setGrowthLogicKit(logicMap.get(treeName)).
                    setBasicGrowingParameters(paramMap.get(treeName)[0],paramMap.get(treeName)[0],(int)paramMap.get(treeName)[0],(int)paramMap.get(treeName)[0],paramMap.get(treeName)[0]);

            species.clearAcceptableSoils();
            Block[] blocks = new Block[]{};
            species.addAcceptableSoil(ModBlocks.allGrowableVariants.toArray(blocks));

            tfcSpecies.put(treeName, species);
            Species.REGISTRY.register(species);
        });

        for(Map.Entry<String, Species> entry : tfcSpecies.entrySet()) {
            TreeRegistry.registerSaplingReplacer(saplingMap.get(entry.getKey()).getDefaultState(), entry.getValue());
        }

    }

    private static void fillMaps(Map<String, float[]> paramMap, Map<String, IGrowthLogicKit> logicMap)
    {
        paramMap.put("acacia",new float[]{0.10f,14f,6,6,0.90f});
        paramMap.put("ash",new float[]{0.25f,12f,4,3,1.00f});
        paramMap.put("aspen",new float[]{0.30f,16f,8,3,1.00f});
        paramMap.put("birch",new float[]{0.25f,12f,5,4,1.15f});
        paramMap.put("blackwood",new float[]{0.20f,13f,3,4,0.90f});
        paramMap.put("chestnut",new float[]{0.20f,10f,3,3,1.00f});
        paramMap.put("douglas_fir",new float[]{0.15f,15f,5,3,1.15f});
        paramMap.put("hickory",new float[]{0.20f,14f,5,3,0.80f});
        paramMap.put("kapok",new float[]{0.10f,24f,7,4,0.85f});
        paramMap.put("maple",new float[]{0.15f,15f,6,3,0.95f});
        paramMap.put("oak",new float[]{0.30f,16f,3,3,0.85f});
        paramMap.put("palm",new float[]{0.05f,16f,5,4,1.10f});
        paramMap.put("pine",new float[]{0.20f,18f,6,2,1.20f});
        paramMap.put("rosewood",new float[]{0.35f,15f,7,3,1.00f});
        paramMap.put("sequoia",new float[]{0.20f,28f,9,4,0.70f});
        paramMap.put("spruce",new float[]{0.15f,12f,6,3,1.10f});
        paramMap.put("sycamore",new float[]{0.20f,10f,4,3,0.90f});
        paramMap.put("white_cedar",new float[]{0.15f,20f,6,2,1.10f});
        paramMap.put("willow",new float[]{0.55f,8f,4,5,1.40f});

        logicMap.put("acacia",GrowthLogicKits.nullLogic);
        logicMap.put("ash",GrowthLogicKits.nullLogic);
        logicMap.put("aspen",TreeRegistry.findGrowthLogicKit("Conifer"));
        logicMap.put("birch",GrowthLogicKits.nullLogic);
        logicMap.put("blackwood",TreeRegistry.findGrowthLogicKit("DarkOak"));
        logicMap.put("chestnut",TreeRegistry.findGrowthLogicKit("DarkOak"));
        logicMap.put("douglas_fir",TreeRegistry.findGrowthLogicKit("Conifer"));
        logicMap.put("hickory",TreeRegistry.findGrowthLogicKit("DarkOak"));
        logicMap.put("kapok",TreeRegistry.findGrowthLogicKit("Jungle"));
        logicMap.put("maple",GrowthLogicKits.nullLogic);
        logicMap.put("oak",TreeRegistry.findGrowthLogicKit("DarkOak"));
        logicMap.put("palm",TreeRegistry.findGrowthLogicKit("Jungle"));
        logicMap.put("pine",TreeRegistry.findGrowthLogicKit("Conifer"));
        logicMap.put("rosewood",GrowthLogicKits.nullLogic);
        logicMap.put("sequoia",TreeRegistry.findGrowthLogicKit("Conifer"));
        logicMap.put("spruce",TreeRegistry.findGrowthLogicKit("Conifer"));
        logicMap.put("sycamore",GrowthLogicKits.nullLogic);
        logicMap.put("white_cedar",TreeRegistry.findGrowthLogicKit("Conifer"));
        logicMap.put("willow",TreeRegistry.findGrowthLogicKit("DarkOak"));
    }

}
