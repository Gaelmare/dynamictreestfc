package io.github.dttfc.util;

import java.util.function.Function;
import com.mojang.serialization.Codec;
import io.github.dttfc.DTTFC;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.world.feature.tree.ForestConfig;

@SuppressWarnings("unused")
public final class ModFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, DTTFC.MOD_ID);

    public static final RegistryObject<DynamicForestFeature> FOREST = register("forest", DynamicForestFeature::new, ForestConfig.CODEC);
    public static final RegistryObject<DFEFeature> FOREST_ENTRY = register("forest_entry", DFEFeature::new, DFEFeature.Entry.CODEC);

    private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> register(String name, Function<Codec<C>, F> factory, Codec<C> codec)
    {
        return FEATURES.register(name, () -> factory.apply(codec));
    }
}
