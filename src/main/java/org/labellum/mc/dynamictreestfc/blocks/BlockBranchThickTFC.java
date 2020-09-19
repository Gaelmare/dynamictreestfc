package org.labellum.mc.dynamictreestfc.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import net.dries007.tfc.ConfigTFC;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class BlockBranchThickTFC extends BlockBranchThick {
    public BlockBranchThickTFC(String name) {
        super(name);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos cutPos, EntityPlayer player, boolean canHarvest) {
        ItemStack tool = player.getHeldItemMainhand();
    // after BlockLogTFC#harvestBlock
        final Set<String> toolClasses = tool.getItem().getToolClasses(tool);
        if (toolClasses.contains("axe") || toolClasses.contains("saw")) {
            // success!
        } else if (toolClasses.contains("hammer") && ConfigTFC.General.TREE.enableHammerSticks) {
            // can't force only stick drop from here
            // so hammers only drop a few sticks from dynamic trees
            return false; //No wood for you!
        } else if (ConfigTFC.General.TREE.requiresAxe) {
            // Here, there was no valid tool used. Deny spawning any drops since logs require axes
            return false; //Also no wood for you!
        } else {
            // or no tool, but handle normally
        }

        return super.removedByPlayer(state, world, cutPos, player, canHarvest);
    }
}
