package org.labellum.mc.dttfc;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.block.FutureBreakable;
import com.dtteam.dynamictrees.config.DTConfigs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

import net.dries007.tfc.common.TFCCreativeTabs;
import net.dries007.tfc.util.events.LoggingEvent;
import org.labellum.mc.dttfc.util.DynamicForestFeature;

import static org.labellum.mc.dttfc.ConfigDTTFC.DT_TWEAKS;

public final class ForgeEvents
{
    public static void init(IEventBus modBus)
    {
        final IEventBus bus = NeoForge.EVENT_BUS;

        bus.addListener(ForgeEvents::onLoggedIn);
        bus.addListener(ForgeEvents::onLogging);
        bus.addListener(ForgeEvents::onLevelUnload);
        modBus.addListener(ForgeEvents::onCreativeTabs);
        //bus.addListener(ForgeEvents::onBreakSpeed);
    }

    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (DT_TWEAKS.get()) {
            DTConfigs.SERVER.isLeavesPassable.set(true);
            DTConfigs.SERVER.treeHarvestMultiplier.set(1.5d);
            DTConfigs.SERVER.leavesSeedDropRate.set(0.02d);
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

    public static void onLevelUnload(LevelEvent.Unload event)
    {
        if (event.getLevel() instanceof ServerLevel level)
        {
            DynamicForestFeature.DISC_PROVIDER.unloadWorld(level);
        }
    }

    public static void onCreativeTabs(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTab() == TFCCreativeTabs.WOOD.tab().get())
        {
            BuiltInRegistries.ITEM.entrySet().forEach((entry) -> {
                if (entry.getKey().location().getNamespace().equals(DTTFC.MOD_ID))
                {
                    event.accept(entry.getValue());
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
