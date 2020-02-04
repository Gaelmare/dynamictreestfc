package org.labellum.mc.dynamictreestfc.trees;

import net.minecraft.util.ResourceLocation;

import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Species;

import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_ID;

public class TreeDouglasFir extends TreeFamilyTFC
{
    public static final String speciesName = "douglas_fir";

    public TreeDouglasFir(ResourceLocation name, LeavesProperties prop)
    {
        super(name, prop);
    }

    //Species need not be created as a nested class.  They can be created after the tree has already been constructed.
    public class TreeDouglasFirSpecies extends Species
    {
        public TreeDouglasFirSpecies(TreeFamilyTFC treeFamily, LeavesProperties prop) {

            super(treeFamily.getName(), treeFamily, prop);

        }
    }

}
