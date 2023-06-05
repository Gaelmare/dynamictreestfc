package org.labellum.mc.dttfc.util;

import com.ferreusveritas.dynamictrees.compat.season.NormalSeasonManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.climate.Climate;

public class TFCSeasonManager extends NormalSeasonManager
{
    private static final float[] SEASON_FACTORS = {0.2f, 0.3f, 0.35f, 0.8f, 0.9f, 0.9f, 0.8f, 0.7f, 0.5f, 0.4f, 0.3f, 0.2f};

    @Override
    public float getGrowthFactor(Level level, BlockPos blockPos, float v)
    {
        return Math.min(getWeightedSeasonFactor(level) + 0.25f, 1);
    }

    @Override
    public float getSeedDropFactor(Level level, BlockPos blockPos, float v)
    {
        return Math.min(getWeightedSeasonFactor(level) + 0.1f, 1);
    }

    @Override
    public float getFruitProductionFactor(Level level, BlockPos blockPos, float v, boolean b)
    {
        return getWeightedSeasonFactor(level);
    }

    @Override
    public Float getSeasonValue(Level level, BlockPos blockPos)
    {
        return getPercentPassedSinceSpring(level);
    }

    @Override
    public Float getPeakFruitProductionSeasonValue(Level level, BlockPos blockPos, float offset)
    {
        return getWeightedSeasonFactor(level);
    }

    @Override
    public boolean isTropical(Level level, BlockPos blockPos)
    {
        return Climate.getTemperature(level, blockPos) > 19f && Climate.getRainfall(level, blockPos) > 330f;
    }

    @Override
    public boolean shouldSnowMelt(Level level, BlockPos blockPos)
    {
        return Climate.getTemperature(level, blockPos) > 0f;
    }

    private float getWeightedSeasonFactor(Level level)
    {
        return SEASON_FACTORS[Calendars.get(level).getCalendarMonthOfYear().ordinal()];
    }

    private float getPercentPassedSinceSpring(Level level)
    {
        float pct = getYearPercentPassed(level) + 0.25f;
        if (pct > 1)
        {
            pct -= 1;
        }
        return pct;
    }

    /**
     * [0, 1], 0 == january
     */
    private float getYearPercentPassed(Level level)
    {
        final ICalendar calendar = Calendars.get(level);
        return (float) (calendar.getCalendarTicks() % calendar.getCalendarTicksInYear()) / calendar.getCalendarTicksInYear();
    }
}
