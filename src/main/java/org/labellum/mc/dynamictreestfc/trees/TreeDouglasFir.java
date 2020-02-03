package org.labellum.mc.dynamictreestfc.trees;

import net.minecraft.util.ResourceLocation;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_ID;

public class TreeDouglasFir extends TreeFamilyTFC
{
    public static final String speciesName = "douglas_fir";

    //Species need not be created as a nested class.  They can be created after the tree has already been constructed.
    public class TreeDouglasFirSpecies extends Species
    {

        public TreeDouglasFirSpecies(TreeFamilyTFC treeFamily) {
            super(treeFamily.getName(), treeFamily, ModBlocks.mapleLeavesProperties);

            setBasicGrowingParameters(0.15f, 14.0f, 4, 4, 1.05f);

            setupStandardSeedDropping();

            addDropCreator(new DropCreatorFruitLogProduct((TreeFamilyTFC) treeFamily));
        }
    }

    public TreeDouglasFir() {
        super(new ResourceLocation(MOD_ID, speciesName));

        //Set up primitive log. This controls what is dropped on harvest.
        setPrimitiveLog(ModBlocks.primMapleLog.getDefaultState());

        ModBlocks.mapleLeavesProperties.setTree(this);
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new TreeDouglasFirSpecies(this));
        getCommonSpecies().generateSeed();
    }


    public TreeDouglasFir(ResourceLocation resourceLocation)
    {
        super(resourceLocation);
    }

}
