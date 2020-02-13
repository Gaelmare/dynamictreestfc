package org.labellum.mc.dynamictreestfc.dropcreators;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.objects.blocks.BlocksTFC;
import net.dries007.tfc.objects.blocks.wood.BlockLogTFC;
import org.labellum.mc.dynamictreestfc.blocks.BlockLogDTTFC;
import org.labellum.mc.dynamictreestfc.trees.TreeFamilyTFC;

import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_ID;

public class DropCreatorTFCLog extends DropCreator
{
    public DropCreatorTFCLog(String name) {
        super(new ResourceLocation(MOD_ID, name+"logs"));
    }

    @Override
    public List<ItemStack> getLogsDrop(World world, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, float volume) {
        Species.LogsAndSticks las = species.getLogsAndSticks(volume);
        int numLogs = las.logs;
        ItemStack logs = species.getFamily().getPrimitiveLogItemStack(1);
        int stackSize = BlockLogTFC.get(((BlockLogDTTFC)species.getFamily().getPrimitiveLog().getBlock()).wood).getStackSize(logs);
        while(numLogs > 0) {
            dropList.add(species.getFamily().getPrimitiveLogItemStack(Math.min(numLogs, stackSize))); // yay magic numbers
            numLogs -= stackSize;
        }
        int numSticks = las.sticks;
        if(numSticks > 0) {
            dropList.add(species.getFamily().getStick(numSticks));
        }
        return dropList;

/*        BlockLogDTTFC primLogBlock = (BlockLogDTTFC) treeFamily.getPrimitiveLog().getBlock();
        BlockLogTFC log = BlockLogTFC.get(primLogBlock.wood);
        ItemStack stack = new ItemStack(Item.getItemFromBlock(log),(int)(volume*yieldPerLog));

        return dropList;*/
    }

}