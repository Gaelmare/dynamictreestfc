package org.labellum.mc.dttfc.util;

import com.dtteam.dynamictrees.api.season.SeasonProvider;
import com.dtteam.dynamictrees.systems.season.ActiveSeasonGrowthCalculator;
import com.dtteam.dynamictrees.systems.season.NormalSeasonManager;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.calendar.Month;
import net.dries007.tfc.util.climate.Climate;

public class TFCSeasonManager extends NormalSeasonManager
{
    private static final int MONTHS_IN_YEAR = 12;
    private static final int MONTHS_IN_SEASON = 3;
    private static final int SPRING_START_MONTH = Month.MARCH.ordinal();

    public TFCSeasonManager()
    {
        super(level -> new Tuple<>(new TFCSeasonProvider(), new ActiveSeasonGrowthCalculator()));
        this.setTropicalPredicate((level, pos) -> TFCSeasonManager.isTropicalClimate(level, pos));
    }

    private static boolean isTropicalClimate(LevelAccessor level, BlockPos pos)
    {
        if (!(level instanceof Level actualLevel) || !canQueryClimate(actualLevel, pos))
        {
            return false;
        }
        return Climate.getInstantTemperature(actualLevel, pos) > 19f && Climate.getAverageRainfall(actualLevel, pos) > 330f;
    }

    private static boolean canQueryClimate(Level level, BlockPos pos)
    {
        final var server = level.getServer();
        return !level.isClientSide()
            && level.dimension().equals(Level.OVERWORLD)
            && server != null
            && Thread.currentThread() == server.getRunningThread()
            && level.isLoaded(pos);
    }

    private static class TFCSeasonProvider implements SeasonProvider
    {
        @Override
        public Float getSeasonValue(Level level, BlockPos pos)
        {
            return getSeasonValueFromTFCMonth(level, pos);
        }

        @Override
        public void updateTick(Level level, long dayTime)
        {
        }

        @Override
        public boolean shouldSnowMelt(Level level, BlockPos pos)
        {
            if (!canQueryClimate(level, pos))
            {
                return false;
            }
            return Climate.getInstantTemperature(level, pos) > 0f;
        }

        private float getSeasonValueFromTFCMonth(Level level, BlockPos pos)
        {
            final ICalendar calendar = Calendars.get(level);
            final Month month = calendar.getHemispheralCalendarMonthOfYear(isInNorthernHemisphere(level, pos));
            final float monthProgress = month.ordinal() + calendar.getCalendarFractionOfMonth();
            float monthsSinceSpring = monthProgress - SPRING_START_MONTH;

            if (monthsSinceSpring < 0)
            {
                monthsSinceSpring += MONTHS_IN_YEAR;
            }

            return monthsSinceSpring / MONTHS_IN_SEASON;
        }

        private boolean isInNorthernHemisphere(Level level, BlockPos pos)
        {
            if (!level.dimension().equals(Level.OVERWORLD))
            {
                return true;
            }

            final float hemisphereScale = Climate.get(level).hemisphereScale();
            if (hemisphereScale == 0f)
            {
                return true;
            }

            final int shiftedZ = pos.getZ() - (int) (hemisphereScale / 2f);
            final int hemisphereSize = (int) (hemisphereScale * 2f);
            return Mth.positiveModulo(shiftedZ, hemisphereSize * 2) > hemisphereSize;
        }
    }
}
