package org.labellum.mc.dttfc.util;

import com.dtteam.dynamictrees.block.soil.SoilBlock;
import com.dtteam.dynamictrees.block.soil.SpeciesBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.function.BooleanSupplier;

import static org.labellum.mc.dttfc.util.TFCChunkTreeHelper.TICKET_LEVEL_BORDER;

public class AsyncTreeGrower {
    private static final Logger log = LoggerFactory.getLogger(AsyncTreeGrower.class);

    private static final Queue<BlockPos> processingQueue = new SynchronizedArrayQueue<>();
    private static final Collection<BlockPos> processAgain = Collections.synchronizedList(new ArrayList<>());

    public static void addQueue(BlockPos pos) {
        processingQueue.add(pos);
    }

    public static void processQueue(ServerLevel level, BooleanSupplier continueProcessing) {
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
                TreeGrower.attemptGrowth(
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

    public static void clearQueue() {
        processingQueue.clear();
        processAgain.clear();
    }
}
