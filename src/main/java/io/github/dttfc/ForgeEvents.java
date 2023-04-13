package io.github.dttfc;

import com.ferreusveritas.dynamictrees.init.DTConfigs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ForgeEvents
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(ForgeEvents::onLoggedIn);
    }

    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        DTConfigs.IS_LEAVES_PASSABLE.set(true);
        DTConfigs.TREE_HARVEST_MULTIPLIER.set(2d);
        DTConfigs.SEED_DROP_RATE.set(0.02);
    }
}
