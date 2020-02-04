package org.labellum.mc.dynamictreestfc.trees;

import net.minecraft.util.ResourceLocation;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import org.labellum.mc.dynamictreestfc.ModBlocks;

public class SpeciesTFC extends Species
{
    public SpeciesTFC(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties) {
        super(name, treeFamily, leavesProperties);

    }
    @Override
    public BlockRooty getRootyBlock() {
        return ModBlocks.blockRootyDirt;
    }
}
