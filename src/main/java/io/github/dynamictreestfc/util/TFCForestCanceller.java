package io.github.dttfc.util;

import java.util.Set;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import net.dries007.tfc.world.feature.tree.ForestConfig;

public class TFCForestCanceller extends FeatureCanceller
{
    public TFCForestCanceller(ResourceLocation registryName)
    {
        super(registryName);
    }

    @Override
    public boolean shouldCancel(ConfiguredFeature<?, ?> cf, Set<String> set)
    {
        return cf.config() instanceof ForestConfig;
    }
}
