package org.labellum.mc.dttfc.util;

import com.dtteam.dynamictrees.block.soil.SoilBlock;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.labellum.mc.dttfc.ConfigDTTFC;

public class GrowthHandler {
    public static void processRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, SoilBlock soilBlock, LastProcessedTick lastProcessedTick) {
        long calendarTicks = Calendars.SERVER.getCalendarTicks();

        if (!lastProcessedTick.haveLastProcessedTick()) {
            lastProcessedTick.setLastProcessedTick(calendarTicks);
            return;
        }

        if (calendarTicks - lastProcessedTick.getLastProcessedTick() > ConfigDTTFC.GROW_EVERY_N_TICKS.getAsInt()
            && TFCChunkTreeHelper.isSurroundedByLoadedChunks(level, pos)) {

            int growthAttempts = (int) Math.floor((double) (calendarTicks - lastProcessedTick.getLastProcessedTick()) / ConfigDTTFC.GROW_EVERY_N_TICKS.getAsInt());
            for (int i = 0; i < growthAttempts; i++) {
                soilBlock.updateTree(state, level, pos, random, true);
            }

            lastProcessedTick.setLastProcessedTick(calendarTicks);
        }
    }
}
