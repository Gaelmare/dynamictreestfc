package io.github.dttfc;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.FutureBreakable;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import net.dries007.tfc.util.events.LoggingEvent;

import static io.github.dttfc.ConfigDTTFC.DT_TWEAKS;

public final class ForgeEvents
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(ForgeEvents::onLoggedIn);
        bus.addListener(ForgeEvents::onLogging);
        //bus.addListener(ForgeEvents::onBreakSpeed);
    }

    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (DT_TWEAKS.get()) {
            DTConfigs.IS_LEAVES_PASSABLE.set(true);
            DTConfigs.TREE_HARVEST_MULTIPLIER.set(1.5d);
            DTConfigs.SEED_DROP_RATE.set(0.02);
            DTConfigs.AXE_DAMAGE_MODE.set(DynamicTrees.AxeDamage.VOLUME);
        }
    }

    public static void onLogging(LoggingEvent event)
    {
        if (event.getState().getBlock() instanceof FutureBreakable)
        {
            event.setCanceled(true);
        }
    }

    /* Stone axe break speeds: vanilla TFC 1.7: 12 seconds, 1.12: 9 seconds, 1.18: 6 seconds
    * DTTFC 1.18: 7 seconds, no need to slow further
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        if (event.getState().getBlock() instanceof BranchBlock)
        {
            event.setNewSpeed(event.getNewSpeed() * 0.5f);
        }
    }*/
}
