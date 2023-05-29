package io.github.dttfc;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.FutureBreakable;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import net.dries007.tfc.util.events.LoggingEvent;

public final class ForgeEvents
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(ForgeEvents::onLoggedIn);
        bus.addListener(ForgeEvents::onLogging);
        bus.addListener(ForgeEvents::onBreakSpeed);
    }

    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        DTConfigs.IS_LEAVES_PASSABLE.set(true);
        DTConfigs.TREE_HARVEST_MULTIPLIER.set(2d);
        DTConfigs.SEED_DROP_RATE.set(0.02);
        DTConfigs.AXE_DAMAGE_MODE.set(DynamicTrees.AxeDamage.VOLUME);
    }

    public static void onLogging(LoggingEvent event)
    {
        if (event.getState().getBlock() instanceof FutureBreakable)
        {
            event.setCanceled(true);
        }
    }

    public static void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        if (event.getState().getBlock() instanceof BranchBlock)
        {
            event.setNewSpeed(event.getNewSpeed() * 0.5f);
        }
    }
}
