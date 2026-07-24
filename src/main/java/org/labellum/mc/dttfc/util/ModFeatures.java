package org.labellum.mc.dttfc.util;

import java.util.function.Function;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;
import org.labellum.mc.dttfc.DTTFC;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.world.feature.tree.ForestConfig;

@SuppressWarnings("unused")
public final class ModFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, DTTFC.MOD_ID);

    public static final Supplier<DynamicForestFeature> FOREST = register("forest", DynamicForestFeature::new, ForestConfig.CODEC);
    public static final Supplier<DFEFeature> FOREST_ENTRY = register("forest_entry", DFEFeature::new, DFEFeature.Entry.CODEC);
    public static final Supplier<SpeciesFeature> SPECIES = register("species", SpeciesFeature::new, SpeciesConfig.CODEC);

    private static <C extends FeatureConfiguration, F extends Feature<C>> Supplier<F> register(String name, Function<Codec<C>, F> factory, Codec<C> codec)
    {
        return FEATURES.register(name, () -> factory.apply(codec));
    }
}
