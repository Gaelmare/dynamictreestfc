package org.labellum.mc.dttfc.client;

import java.util.List;

import net.dries007.tfc.client.ClientHelpers;
import net.dries007.tfc.client.ClimateRenderCache;
import net.dries007.tfc.client.overworld.SolarCalculator;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.climate.Climate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DTTFCSeasonalLeavesBakedModel implements IDynamicBakedModel
{
    private static final ModelProperty<BakedModel> SELECTED_MODEL = new ModelProperty<>();

    private final BakedModel denseLeaves;
    private final BakedModel sparseLeaves;
    private final BakedModel bare;
    private final BakedModel blooming;
    private final boolean conifer;
    private final float flowerOffset;

    public DTTFCSeasonalLeavesBakedModel(BakedModel denseLeaves, BakedModel sparseLeaves, BakedModel bare, BakedModel blooming, boolean conifer, float flowerOffset)
    {
        this.denseLeaves = denseLeaves;
        this.sparseLeaves = sparseLeaves;
        this.bare = bare;
        this.blooming = blooming;
        this.conifer = conifer;
        this.flowerOffset = flowerOffset;
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData)
    {
        return modelData.derive()
            .with(SELECTED_MODEL, getModelForPosition(pos))
            .build();
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData modelData, @Nullable RenderType renderType)
    {
        return getModel(modelData).getQuads(state, side, rand, modelData, renderType);
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data)
    {
        return getModel(data).getRenderTypes(state, rand, data);
    }

    @Override
    public boolean useAmbientOcclusion()
    {
        return true;
    }

    @Override
    public boolean isGui3d()
    {
        return false;
    }

    @Override
    public boolean usesBlockLight()
    {
        return true;
    }

    @Override
    public boolean isCustomRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon()
    {
        return denseLeaves.getParticleIcon();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull ModelData data)
    {
        return getModel(data).getParticleIcon(data);
    }

    @Override
    public ItemOverrides getOverrides()
    {
        return ItemOverrides.EMPTY;
    }

    private BakedModel getModel(ModelData modelData)
    {
        final BakedModel selected = modelData.get(SELECTED_MODEL);
        return selected == null ? denseLeaves : selected;
    }

    private BakedModel getModelForPosition(@Nullable BlockPos pos)
    {
        if (!ClientHelpers.useFancyGraphics())
        {
            return denseLeaves;
        }

        final Level level = ClientHelpers.getLevel();
        if (level == null)
        {
            return denseLeaves;
        }

        if (pos == null)
        {
            pos = BlockPos.ZERO;
        }

        final BlockPos climatePos = new BlockPos(pos.getX(), 63, pos.getZ());
        float averageTemperature = Climate.getAverageTemperature(level, climatePos);
        float rainfallVariance = Climate.getRainfallVariance(level, pos);

        if ((averageTemperature > 11.7f && averageTemperature < 12.8f) || (rainfallVariance > 0.38f && rainfallVariance < 0.42f))
        {
            final int hash = Helpers.hash(912381187503828153L, pos) & 127;
            averageTemperature += (hash - 63) / 4000f;
            rainfallVariance += (hash - 63) / 60000f;
        }

        final float rainfallVarianceAbs = Math.abs(rainfallVariance);
        float year = Calendars.CLIENT.getCalendarFractionOfYear();
        final boolean northernHemisphere = SolarCalculator.getInNorthernHemisphere(pos.getZ(), ClimateRenderCache.INSTANCE.getHemisphereScale());
        float yearOffset = 0f;
        final float seasonScale;
        final boolean evergreen;

        if (averageTemperature <= 12f)
        {
            seasonScale = 1.25f * Math.max(averageTemperature, -20f) + 7.6f;
            evergreen = false;
            if (!northernHemisphere)
            {
                yearOffset = 0.5f;
            }
        }
        else
        {
            final float averageRainfall = Climate.getAverageRainfall(level, climatePos);
            final float drySeasonRainfall = averageRainfall * (1f - rainfallVarianceAbs);
            if (rainfallVarianceAbs > 0.4f && averageTemperature > 12.5f && drySeasonRainfall <= 120f)
            {
                if (rainfallVariance < 0f)
                {
                    yearOffset = 0.5f;
                }
                seasonScale = -0.2604f * (0.4f - rainfallVarianceAbs) * (120f - drySeasonRainfall) + 18.75f + 5.3f;
                evergreen = false;
            }
            else
            {
                if (!northernHemisphere)
                {
                    yearOffset = 0.5f;
                }
                seasonScale = 24.05f;
                evergreen = true;
            }
        }

        final float seasonScaleCubed = seasonScale * seasonScale * seasonScale / 4096f;
        final float seasonScaleSquared = seasonScale * seasonScale / 256f;
        final int hash = Helpers.hash(836494187578334123L, pos) & 127;
        year = (1f + year + yearOffset + (hash - 63) / 4096f) % 1f;

        final float bareStart = (seasonScaleCubed - seasonScaleSquared + 10.5f) / 12f;
        final float bareEnd = 1f - bareStart;
        final float bareSpan = bareStart - bareEnd;
        final float bloomStart = bareEnd + flowerOffset * bareSpan;

        if (year > bloomStart)
        {
            final float bloomEnd = bloomStart + Math.min(0.167f * bareSpan, 0.125f);
            if (year < bloomEnd)
            {
                return blooming;
            }
        }

        if (evergreen || conifer)
        {
            return denseLeaves;
        }

        if (year > bareStart)
        {
            return bare;
        }

        final float sparseStart = (seasonScaleCubed - seasonScaleSquared + 8.5f) / 12f;
        final float sparseWindow = 0.5f * (bareStart + sparseStart);
        if (year > sparseWindow)
        {
            return sparseLeaves;
        }

        final float secondDenseStart = 1f - sparseWindow;
        if (year > secondDenseStart)
        {
            return denseLeaves;
        }

        return year > bareEnd ? sparseLeaves : bare;
    }
}
