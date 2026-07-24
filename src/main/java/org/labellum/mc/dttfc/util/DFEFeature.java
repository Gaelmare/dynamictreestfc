package org.labellum.mc.dttfc.util;

import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import net.dries007.tfc.world.feature.tree.ForestConfig;

public class DFEFeature extends Feature<DFEFeature.Entry>
{
    public DFEFeature(Codec<Entry> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Entry> pContext)
    {
        throw new IllegalArgumentException("This is not a real feature and should never be placed!");
    }

    public record Entry(ResourceLocation species, Optional<ResourceLocation> undergrowthSpecies, ForestConfig.Entry entry) implements FeatureConfiguration
    {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("species").forGetter(c -> c.species),
            ResourceLocation.CODEC.optionalFieldOf("undergrowth_species").forGetter(c -> c.undergrowthSpecies),
            ForestConfig.Entry.CODEC.fieldOf("entry").forGetter(c -> c.entry)
        ).apply(instance, Entry::new));

        public boolean isValid(float temperature, float rainfall)
        {
            final var c = entry.climate();
            return rainfall >= c.getMinRainVariance() && rainfall <= c.getMaxRainVariance() && temperature >= c.getMinTemp() && temperature <= c.getMaxTemp();
        }

        public float distanceFromMean(float temperature, float rainfall)
        {
            return (rainfall + temperature - getAverageTemp() - getAverageRain()) / 2;
        }

        public float getAverageTemp()
        {
            return (entry.climate().getMaxTemp() - entry.climate().getMinTemp()) / 2;
        }

        public float getAverageRain()
        {
            return entry.getAverageRainVar();
        }
    }
}
