package org.labellum.mc.dynamictreestfc.trees;

import net.minecraft.util.ResourceLocation;

import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

public class TreeFamilyTFC extends TreeFamily
{
    public TreeFamilyTFC(ResourceLocation name, LeavesProperties prop) {
        super(name);
        createSpecies(prop);
    }

    //Species need not be created as a nested class.  They can be created after the tree has already been constructed.
    public class TreeTFCSpecies extends Species
    {
        public TreeTFCSpecies(TreeFamilyTFC treeFamily, LeavesProperties prop) {

            super(treeFamily.getName(), treeFamily, prop);

        }
    }

    public void createSpecies(LeavesProperties prop) {
        setCommonSpecies(new TreeTFCSpecies(this,prop));
        getCommonSpecies().generateSeed();
    }
}