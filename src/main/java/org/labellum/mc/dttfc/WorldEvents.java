package org.labellum.mc.dttfc;


import com.dtteam.dynamictrees.block.soil.SpeciesBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.ChunkTicketLevelUpdatedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.labellum.mc.dttfc.util.AsyncTreeGrower;

import java.util.*;

import static org.labellum.mc.dttfc.util.TFCChunkTreeHelper.*;

public class WorldEvents {

    public static void init() {
        final IEventBus bus = NeoForge.EVENT_BUS;

        bus.addListener(EventPriority.LOWEST, WorldEvents::onWorldTick);
        bus.addListener(EventPriority.LOWEST, WorldEvents::onChunkLoad);
        bus.addListener(WorldEvents::onServerStopped);
        bus.addListener(EventPriority.LOWEST, WorldEvents::onTicketLevelUpdate);
    }

    public static void onServerStopped(ServerStoppedEvent event) {
        AsyncTreeGrower.clearQueue();
    }

    public static void onWorldTick(ServerTickEvent.Post event) {
        AsyncTreeGrower.processQueue(event.getServer().overworld(), event::hasTime);
    }

    public static void onTicketLevelUpdate(ChunkTicketLevelUpdatedEvent event) {
        int oldLevel = event.getOldTicketLevel();
        int newLevel = event.getNewTicketLevel();

        if (event.getLevel().dimension() != Level.OVERWORLD
                || event.getLevel().isClientSide()
                || ConfigDTTFC.GROW_ON_CHUNK_LOAD.isFalse()) {
            return;
        }

        // Handle bug in NeoForge ChunkTicketLevelUpdatedEvent where oldLevel is not being passed correctly.
        if (newLevel == TICKET_LEVEL_BLOCK_TICKING && oldLevel == TICKET_LEVEL_INACCESSIBLE) {
            return;
        }

        ChunkAccess chunk = event.getChunkHolder().getLatestChunk();

        if (newLevel <= TICKET_LEVEL_BLOCK_TICKING && oldLevel > TICKET_LEVEL_BLOCK_TICKING && chunk != null) {
            addTreesInChunkToQueue(chunk);
        }
    }

    public static void onChunkLoad(ChunkEvent.Load event) {
        if ((event.getChunk() instanceof LevelChunk levelChunk
                && levelChunk.getLevel().dimension() != Level.OVERWORLD)
                || event.getLevel().isClientSide()
                || ConfigDTTFC.GROW_ON_CHUNK_LOAD.isFalse()) {
            return;
        }

        addTreesInChunkToQueue(event.getChunk());
    }

    private static void addTreesInChunkToQueue(ChunkAccess chunk) {
        Set<BlockPos> blockEntitiesPos = chunk.getBlockEntitiesPos();

        blockEntitiesPos.forEach(pos -> {
            if (chunk.getBlockEntity(pos) instanceof SpeciesBlockEntity) {
                AsyncTreeGrower.addQueue(pos);
            }
        });
    }
}
