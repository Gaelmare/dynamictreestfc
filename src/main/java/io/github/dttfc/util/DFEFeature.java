package io.github.dttfc.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
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

    public record Entry(ResourceLocation species, ForestConfig.Entry entry) implements FeatureConfiguration
    {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("species").forGetter(c -> c.species),
            ForestConfig.Entry.CODEC.fieldOf("entry").forGetter(c -> c.entry)
        ).apply(instance, Entry::new));

        public boolean isValid(float temperature, float rainfall)
        {
            return rainfall >= entry.minRainfall() && rainfall <= entry.maxRainfall() && temperature >= entry.minAverageTemp() && temperature <= entry.maxAverageTemp();
        }

        public float distanceFromMean(float temperature, float rainfall)
        {
            return (rainfall + temperature - getAverageTemp() - getAverageRain()) / 2;
        }

        public float getAverageTemp()
        {
            return (entry.maxAverageTemp() - entry.minAverageTemp()) / 2;
        }

        public float getAverageRain()
        {
            return (entry.maxRainfall() - entry.minRainfall()) / 2;
        }

        public ConfiguredFeature<?, ?> getFeature()
        {
            return entry.treeFeature().value();
        }

        public ConfiguredFeature<?, ?> getDeadFeature()
        {
            return entry.deadFeature().value();
        }

        public ConfiguredFeature<?, ?> getOldGrowthFeature()
        {
            return entry.oldGrowthFeature().orElse(entry.treeFeature()).value();
        }
    }
}
