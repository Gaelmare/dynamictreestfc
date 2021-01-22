package org.labellum.mc.dynamictreestfc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import net.dries007.tfc.ConfigTFC;
import net.dries007.tfc.util.OreDictionaryHelper;

import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ForgeEventHandler
{
    @SubscribeEvent
    public static void onHarvestDrops(BlockEvent.HarvestDropsEvent event)
    {
        EntityPlayer player = event.getHarvester();
        if (player != null && event.getState().getBlock() instanceof BlockBranch)
        {
            ItemStack held = player.getHeldItemMainhand();
            if (OreDictionaryHelper.doesStackMatchOre(held, "axeStone"))
            {
                for (ItemStack s: event.getDrops())
                {
                    if (OreDictionaryHelper.doesStackMatchOre(s,"logWood"))
                    {
                        s.setCount((int) (s.getCount() * ConfigTFC.General.TREE.stoneAxeReturnRate));
                        //not consolidating partial item stacks on ground
                    }
                }
            }
        }
    }
}
