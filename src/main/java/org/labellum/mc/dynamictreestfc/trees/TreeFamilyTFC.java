package org.labellum.mc.dynamictreestfc.trees;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.ferreusveritas.dynamictrees.blocks.*;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.objects.blocks.wood.BlockLogTFC;
import org.labellum.mc.dynamictreestfc.ModBlocks;
import org.labellum.mc.dynamictreestfc.blocks.BlockLogDTTFC;
import org.labellum.mc.dynamictreestfc.dropcreators.DropCreatorTFCLog;

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
    }

    // need to have ItemStack be BlockLogTFC, but have the tree log be
    // BlockLogDTTFC
    public TreeFamily setPrimitiveLog(IBlockState primLog) {
        BlockLogDTTFC primLogBlock = (BlockLogDTTFC) primLog.getBlock();
        BlockLogTFC log = BlockLogTFC.get(primLogBlock.wood);
        ItemStack stack = new ItemStack(Item.getItemFromBlock(log));
        return super.setPrimitiveLog(primLog, stack);
    }

    //Species need not be created as a nested class.  They can be created after the tree has already been constructed.
    public class TreeTFCSpecies extends Species
    {
        public TreeTFCSpecies(TreeFamilyTFC treeFamily, LeavesProperties prop)
        {
            super(treeFamily.getName(), treeFamily, prop);
            setupStandardSeedDropping();

            //addDropCreator(new DropCreatorTFCLog(treeFamily));
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

/* //comment out for now
    @Override
    public List<Item> getRegisterableItems(List<Item> itemList) {
        return itemList;
    }*/

    @Override
    public BlockBranch createBranch() {
        String branchName = "branch/" + getName().getPath();
        return isThick() ? new BlockBranchThick(branchName) : new BlockBranchBasic(branchName);
    }

    @Override
    public boolean autoCreateBranch() {
        return true;
    }

}