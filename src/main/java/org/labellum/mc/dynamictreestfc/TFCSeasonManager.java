package org.labellum.mc.dynamictreestfc;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.ferreusveritas.dynamictrees.api.seasons.ISeasonManager;
import net.dries007.tfc.util.calendar.CalendarTFC;
import net.dries007.tfc.util.calendar.Month;
import net.dries007.tfc.util.climate.ClimateTFC;

public class TFCSeasonManager implements ISeasonManager
{
    public static final TFCSeasonManager INSTANCE = new TFCSeasonManager();

    @Override
    public void updateTick(World world, long worldTicks)
    {

    }

    @Override
    public void flushMappings()
    {

    }

    @Override
    public float getGrowthFactor(World world, BlockPos rootPos, float offset)
    {
        return getMonthModifier(CalendarTFC.CALENDAR_TIME.getMonthOfYear());
    }

    @Override
    public float getSeedDropFactor(World world, BlockPos rootPos, float offset)
    {
        return getMonthModifier(CalendarTFC.CALENDAR_TIME.getMonthOfYear()) + 0.05F;
    }

    @Override
    public float getFruitProductionFactor(World world, BlockPos rootPos, float offset)
    {
        float mod = getMonthModifier(CalendarTFC.CALENDAR_TIME.getMonthOfYear());
        return mod < 0.25F ? 0 : mod; // we don't want to fruit at all if it's the coldest part of the years
    }

    /**
     * Yeah because DT thinks 3 = winter and 0 = spring we actually get the modifier for the month 3 months ahead and multiply it by 4
     * This value is not allowed to be 4 exactly so to be 100% sure we're capping it at 3.999
     * Ideally this should be based off of ticks in the year and divide wholesale but I don't feel like writing handling for that
     */
    @Override
    public Float getSeasonValue(World world, BlockPos blockPos)
    {
        return (float) Math.min(getMonthModifier(CalendarTFC.CALENDAR_TIME.getMonthOfYear().next().next().next()) * 4, 3.999);
    }

    @Override
    public boolean isTropical(World world, BlockPos blockPos)
    {
        return ClimateTFC.getRainfall(world, blockPos) > 300 && ClimateTFC.getAvgTemp(world, blockPos) > 20;
    }

    @Override
    public boolean shouldSnowMelt(World world, BlockPos blockPos)
    {
        return ClimateTFC.getDailyTemp(world, blockPos) > 0;
    }

    /**
     * If you notice how the months in TFC have a max temperature modifier 66.5 and lowest 27,
     * and that this modifier reaches the high point in the coldest months,
     * scaling this to be out of 1 and then inverting it means we can ensure the warmest months see the most growth.
     */
    private float getMonthModifier(Month month)
    {
        return 1 - ((month.getTemperatureModifier() - 27.0F) / 66.5F);
    }
}
