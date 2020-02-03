package org.labellum.mc.dynamictreestfc.trees;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import net.dries007.tfc.api.types.Tree;

public class TreeFamilyTFC extends TreeFamily
{
    private Tree tree;

    public TreeFamilyTFC(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    //This mod registers all of the seeds externally so we'll only provide the dynamic branch block here
    @Override
    public List<Item> getRegisterableItems(List<Item> itemList) {
        //Register an itemBlock for the branch block
        itemList.add(new ItemBlock(getDynamicBranch()).setRegistryName(getDynamicBranch().getRegistryName()));
        return itemList;
    }
}