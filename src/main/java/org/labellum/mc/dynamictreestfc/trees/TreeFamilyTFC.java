package org.labellum.mc.dynamictreestfc.trees;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.ferreusveritas.dynamictrees.blocks.*;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorLogs;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.objects.blocks.wood.BlockLogTFC;
import org.labellum.mc.dynamictreestfc.ModBlocks;

import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_ID;

public class TreeFamilyTFC extends TreeFamily
{
    private boolean thick = false;

    @Override
    public boolean isThick()
    {
        return thick;
    }

    public void setThick(boolean thick)
    {
        this.thick = thick;
    }

    public TreeFamilyTFC(ResourceLocation name, Tree tree)
    {
        super(name);

        setPrimitiveLog(BlockLogTFC.get(tree).getDefaultState(),
                new ItemStack(Item.getItemFromBlock(BlockLogTFC.get(tree)),1));
    }

    //Species need not be created as a nested class.  They can be created after the tree has already been constructed.
    public class TreeTFCSpecies extends Species
    {
        public TreeTFCSpecies(TreeFamilyTFC treeFamily, LeavesProperties prop)
        {
            super(treeFamily.getName(), treeFamily, prop);
            setupStandardSeedDropping();

        }

        @Override
        public BlockRooty getRootyBlock() {
            return ModBlocks.blockRootyDirt;
        }

        @Override
        public Species generateSeed() {
            Seed seed = new Seed(MOD_ID + ":seed/"+getRegistryName().getPath());
            setSeedStack(new ItemStack(seed));
            return this;
        }
    }

    @Override
    public void createSpecies()
    {
        setCommonSpecies(new TreeTFCSpecies(this, ModBlocks.leafMap.get(getName().toString())));
        getCommonSpecies().generateSeed();
    }

    //This mod registers all of the seeds externally so we'll only provide the dynamic branch block here
    @Override
    public List<Item> getRegisterableItems(List<Item> itemList) {
        //Register an itemBlock for the branch block
        itemList.add(new ItemBlock(getDynamicBranch()).setRegistryName(getDynamicBranch().getRegistryName()));
        return itemList;
    }

    @Override
    public BlockBranch createBranch() {
        String branchName = getName().toString();
        return isThick() ? new BlockBranchThick(branchName) : new BlockBranchBasic(branchName);
    }

    @Override
    public boolean autoCreateBranch() {
        return true;
    }

}