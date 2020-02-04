package org.labellum.mc.dynamictreestfc.trees;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.objects.blocks.wood.BlockLogTFC;
import org.labellum.mc.dynamictreestfc.ModBlocks;
import org.labellum.mc.dynamictreestfc.ModTrees;

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
        setPrimitiveLog(BlockLogTFC.get(tree).getDefaultState());
    }

    //Species need not be created as a nested class.  They can be created after the tree has already been constructed.
    public class TreeTFCSpecies extends Species
    {
        public TreeTFCSpecies(TreeFamilyTFC treeFamily, LeavesProperties prop)
        {
            super(treeFamily.getName(), treeFamily, prop);
        }

        @Override
        public Species generateSeed() {
            Seed seed = new Seed("seed." + getRegistryName());
            setSeedStack(new ItemStack(seed));
            return this;
        }
    }

    public BlockRooty getRootyBlock() {
        return ModBlocks.blockRootyDirt;
    }


    @Override
    public void createSpecies()
    {
        setCommonSpecies(new TreeTFCSpecies(this, ModBlocks.leafMap.get(getName().toString())));
        getCommonSpecies().generateSeed();
    }
}