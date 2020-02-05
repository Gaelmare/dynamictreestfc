package org.labellum.mc.dynamictreestfc.dropcreators;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.dries007.tfc.objects.blocks.wood.BlockLogTFC;
import org.labellum.mc.dynamictreestfc.trees.TreeFamilyTFC;

import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_ID;

public class DropCreatorTFCLog extends DropCreator
{

    private final TreeFamilyTFC treeFamily;
    private final float yieldPerLog;

    public DropCreatorTFCLog(TreeFamilyTFC treeFamily) {
        this(treeFamily, 1.0f);
    }

    public DropCreatorTFCLog(TreeFamilyTFC treeFamily, float yieldPerLog) {
        super(new ResourceLocation(MOD_ID, treeFamily.toString()));
        this.treeFamily = treeFamily;
        this.yieldPerLog = yieldPerLog;
    }

    @Override
    public List<ItemStack> getLogsDrop(World world, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, float volume) {

        Block primLogBlock = treeFamily.getPrimitiveLog().getBlock();

        if(primLogBlock instanceof BlockLogTFC) {
        }

        return dropList;
    }

}