package org.labellum.mc.dttfc.util;

import com.dtteam.dynamictrees.utility.CoordUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.*;
import net.minecraft.world.level.ChunkPos;

public class TFCChunkTreeHelper {
    public static final int TICKET_LEVEL_BORDER = ChunkLevel.byStatus(FullChunkStatus.FULL);
    public static final int TICKET_LEVEL_INACCESSIBLE = ChunkLevel.byStatus(FullChunkStatus.INACCESSIBLE);
    public static final int TICKET_LEVEL_BLOCK_TICKING = ChunkLevel.byStatus(FullChunkStatus.BLOCK_TICKING);

    public static boolean isSurroundedByLoadedChunks(ServerLevel level, BlockPos pos) {
        for (CoordUtils.Surround surr : CoordUtils.Surround.values()) {
            Vec3i dir = surr.getOffset();
            if (getTicketLevelAtPos(level, pos.offset(dir)) > TICKET_LEVEL_BORDER) {
                return false;
            }
        }

        return true;
    }

    public static int getTicketLevelAtPos(ServerLevel level, BlockPos pos) {
        ServerChunkCache chunkCache = level.getChunkSource();

        ChunkHolder holder = chunkCache.chunkMap.getVisibleChunkIfPresent(ChunkPos.asLong(pos));

        if (holder == null) {
            return TICKET_LEVEL_INACCESSIBLE;
        }

        return holder.getTicketLevel();
    }
}
