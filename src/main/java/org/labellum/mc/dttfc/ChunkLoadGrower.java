package org.labellum.mc.dttfc;


import com.dtteam.dynamictrees.block.soil.SoilBlock;
import com.dtteam.dynamictrees.block.soil.SpeciesBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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
import org.labellum.mc.dttfc.util.GrowthHandler;
import org.labellum.mc.dttfc.util.SynchronizedArrayQueue;
import org.labellum.mc.dttfc.util.TFCChunkTreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BooleanSupplier;

import static org.labellum.mc.dttfc.util.TFCChunkTreeHelper.*;

public class ChunkLoadGrower {
    private static final Logger log = LoggerFactory.getLogger(ChunkLoadGrower.class);

    private static final Queue<BlockPos> processingQueue = new SynchronizedArrayQueue<>();
    private static final Collection<BlockPos> processAgain = Collections.synchronizedList(new ArrayList<>());

    public static void init() {
        final IEventBus bus = NeoForge.EVENT_BUS;

        bus.addListener(EventPriority.LOWEST,ChunkLoadGrower::onWorldTick);
        bus.addListener(EventPriority.LOWEST, ChunkLoadGrower::onChunkLoad);
        bus.addListener(ChunkLoadGrower::onServerStopped);
        bus.addListener(EventPriority.LOWEST, ChunkLoadGrower::onTicketLevelUpdate);
    }

    public static void onServerStopped(ServerStoppedEvent event) {
        processingQueue.clear();
        processAgain.clear();
    }

    public static void onWorldTick(ServerTickEvent.Post event) {
        processQueue(event.getServer().overworld(), event::hasTime);
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
            addDynamicTreesInChunkToQueue(chunk);
        }
    }

    public static void onChunkLoad(ChunkEvent.Load event) {
        if (event.getChunk() instanceof LevelChunk levelChunk
                && levelChunk.getLevel().dimension() != Level.OVERWORLD
                || event.getLevel().isClientSide()
                || ConfigDTTFC.GROW_ON_CHUNK_LOAD.isFalse()) {
            return;
        }

        addDynamicTreesInChunkToQueue(event.getChunk());
    }

    private static void addDynamicTreesInChunkToQueue(ChunkAccess chunk) {
        Set<BlockPos> blockEntitiesPos = chunk.getBlockEntitiesPos();

        blockEntitiesPos.forEach(pos -> {
            if (chunk.getBlockEntity(pos) instanceof SpeciesBlockEntity) {
                processingQueue.add(pos);
            }
        });
    }

    private static void processQueue(ServerLevel level, BooleanSupplier continueProcessing) {
        while (continueProcessing.getAsBoolean() && !processingQueue.isEmpty()) {
            BlockPos pos = processingQueue.remove();

            if (!level.isLoaded(pos)) {
                continue;
            }

            if (TFCChunkTreeHelper.getTicketLevelAtPos(level, pos) > TICKET_LEVEL_BORDER) {
                continue;
            }

            if (!TFCChunkTreeHelper.isSurroundedByLoadedChunks(level, pos)) {
                processAgain.add(pos);
                continue;
            }

            if (level.getBlockEntity(pos) instanceof SpeciesBlockEntity blockEntity) {
                GrowthHandler.processRandomTick(
                        level.getBlockState(pos),
                        level,
                        pos,
                        level.getRandom(),
                        (SoilBlock) level.getBlockState(pos).getBlock(),
                        blockEntity);
            } else {
                log.error("No LastProcessedTick at pos {}", pos);
            }
        }

        if (!processAgain.isEmpty()) {
            processingQueue.addAll(processAgain);
            processAgain.clear();
        }
    }
}
