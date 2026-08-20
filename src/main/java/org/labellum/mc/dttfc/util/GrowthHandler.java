package org.labellum.mc.dttfc.util;

import com.dtteam.dynamictrees.block.soil.SoilBlock;
import com.dtteam.dynamictrees.block.soil.SpeciesBlockEntity;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.labellum.mc.dttfc.ConfigDTTFC;

public class GrowthHandler {
    public static void processRandomTick(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            RandomSource random,
            SoilBlock soilBlock,
            SpeciesBlockEntity blockEntity) {
        long calendarTicks = Calendars.SERVER.getCalendarTicks();

        LastProcessedTick lastProcessedTick = (LastProcessedTick) blockEntity;
        if (!lastProcessedTick.haveLastProcessedTick()) {
            lastProcessedTick.setLastProcessedTick(calendarTicks);
            return;
        }

        float growthRate = blockEntity.getSpecies().getGrowthRate(level, pos);
        float ticksPerGrowth = ConfigDTTFC.GROW_EVERY_N_TICKS.getAsInt() / growthRate;
        long ticksSinceLastCheck = calendarTicks - lastProcessedTick.getLastProcessedTick();

        if (ticksSinceLastCheck > ticksPerGrowth && TFCChunkTreeHelper.isSurroundedByLoadedChunks(level, pos)) {
            int growthAttempts = (int) Math.floor(ticksSinceLastCheck / ticksPerGrowth);
            for (int i = 0; i < growthAttempts; i++) {
                soilBlock.updateTree(state, level, pos, random, true);
            }

            lastProcessedTick.setLastProcessedTick(calendarTicks);
        }
    }
}
