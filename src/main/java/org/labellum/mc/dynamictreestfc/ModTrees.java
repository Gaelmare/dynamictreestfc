package org.labellum.mc.dynamictreestfc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import org.labellum.mc.dynamictreestfc.trees.TreeDouglasFir;

public class ModTrees
{
    protected interface ISpeciesCreator {
        Species createSpecies(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties, String fruitName, SaplingType saplingType);
    }

    public static ArrayList<TreeFamily> tfcTrees = new ArrayList<TreeFamily>();
    public static Map<ResourceLocation, Species> tfcSpecies = new HashMap<>();

    public static void init() {
    }

    public static void preInit() {

        //Register all of the trees
        Collections.addAll(tfcTrees, new TreeDouglasFir());
        tfcTrees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
        tfcTrees.forEach(tree -> tfcSpecies.put(tree.getName().getResourcePath(), tree.getCommonSpecies()));

        //Basic creators
        ISpeciesCreator fruitTreeCreator = (name, treeFamily, leavesProperties, fruitName, saplingType) -> new SpeciesFruit(name, treeFamily, leavesProperties, fruitName, saplingType);

        //Set up a map of species and their sapling types
        Map<String, SaplingType> saplingMap = new HashMap<>();
        saplingMap.putAll(FruitRegistry.registeringFruits);

        //Set up a map of sapling types to tree family common species
        Map<SaplingType, TreeFamily> familyMap = new EnumMap<>(SaplingType.class);
        familyMap.put(SaplingType.TEMPERATE, TreeRegistry.findSpeciesSloppy("oak").getFamily());
        familyMap.put(SaplingType.WARM, TreeRegistry.findSpeciesSloppy("jungle").getFamily());
        familyMap.put(SaplingType.COLD, TreeRegistry.findSpeciesSloppy("spruce").getFamily());

        //Set up a map of species names and their creator lambdas
        Map<String, ISpeciesCreator> creatorMap = new HashMap<>();
        FruitRegistry.registeringFruits.forEach((k, v) -> creatorMap.put(k, fruitTreeCreator));

        //Tailor creators to fit Dynamic Trees
        alterCreatorMap(creatorMap);

        //Create all of the species
        for(Entry<String, ISpeciesCreator> creatorEntry : creatorMap.entrySet()) {
            String fruitName = creatorEntry.getKey();
            ISpeciesCreator creator = creatorEntry.getValue();
            SaplingType saplingType = saplingMap.get(fruitName);
            TreeFamily family = familyMap.get(saplingType);
            ResourceLocation resLoc = new ResourceLocation(ModConstants.MODID, fruitName);
            ILeavesProperties leavesProperties = family.getCommonSpecies().getLeavesProperties();
            Species species = creator.createSpecies(resLoc, family, leavesProperties, fruitName, saplingType);
            tfcSpecies.put(fruitName, species);
            Species.REGISTRY.register(species);
        }

        for(Entry<String, Species> entry : tfcSpecies.entrySet()) {
            TreeRegistry.registerSaplingReplacer(FruitRegistry.getSapling(entry.getKey()).getDefaultState(), entry.getValue());
        }

    }

    private static void alterCreatorMap(Map<String, ISpeciesCreator> creatorMap) {

        //Dynamic Trees already has an apple tree
        creatorMap.remove(FruitRegistry.APPLE);

        //Temperate nut trees are typically hardwoods that grow slowly and usually very large.
        creatorMap.put(FruitRegistry.WALNUT, (name, treeFamily, leavesProperties, fruitName, saplingType) -> new SpeciesFruit(name, treeFamily, leavesProperties, fruitName, saplingType) {
            @Override protected void fruitTreeDefaults() { setBasicGrowingParameters(0.4f, 12.0f, 1, 4, 0.7f, 8); }
        });
        creatorMap.put(FruitRegistry.CHESTNUT, (name, treeFamily, leavesProperties, fruitName, saplingType) -> new SpeciesFruit(name, treeFamily, leavesProperties, fruitName, saplingType) {
            @Override protected void fruitTreeDefaults() { setBasicGrowingParameters(0.45f, 11.0f, 1, 4, 0.6f, 8); }
        });
        creatorMap.put(FruitRegistry.PECAN, (name, treeFamily, leavesProperties, fruitName, saplingType) -> new SpeciesFruit(name, treeFamily, leavesProperties, fruitName, saplingType) {
            @Override protected void fruitTreeDefaults() { setBasicGrowingParameters(0.45f, 11.0f, 1, 4, 0.6f, 8); }
        });

    }
}
