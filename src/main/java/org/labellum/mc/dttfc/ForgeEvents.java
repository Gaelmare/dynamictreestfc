package org.labellum.mc.dttfc;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.block.FutureBreakable;
import com.dtteam.dynamictrees.config.DTConfigs;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import net.dries007.tfc.common.TFCCreativeTabs;
import net.dries007.tfc.util.events.LoggingEvent;

import static org.labellum.mc.dttfc.ConfigDTTFC.DT_TWEAKS;

public final class ForgeEvents
{
    public static void init(IEventBus modEventBus)
    {
        NeoForge.EVENT_BUS.addListener(ForgeEvents::onLoggedIn);
        NeoForge.EVENT_BUS.addListener(ForgeEvents::onLogging);
        modEventBus.addListener(ForgeEvents::onCreativeTabs);
        //modEventBus.addListener(ForgeEvents::onBreakSpeed);
    }

    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (DT_TWEAKS.get()) {
            DTConfigs.SERVER.isLeavesPassable.set(true);
            DTConfigs.SERVER.treeHarvestMultiplier.set(1.5d);
            DTConfigs.SERVER.leavesSeedDropRate.set(0.02);
            DTConfigs.SERVER.axeDamageMode.set(DynamicTrees.AxeDamage.VOLUME);
        }
    }

    public static void onLogging(LoggingEvent event)
    {
        if (event.getState().getBlock() instanceof FutureBreakable)
        {
            event.setCanceled(true);
        }
    }

    public static void onCreativeTabs(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTab() == TFCCreativeTabs.WOOD.tab().get())
        {
            BuiltInRegistries.ITEM.forEach((item) -> {
                if (BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(DTTFC.MOD_ID))
                {
                    event.accept(item.getDefaultInstance());
                }
            });
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
