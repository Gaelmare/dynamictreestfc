package org.labellum.mc.dttfc.util;

import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record SpeciesConfig(Species species) implements FeatureConfiguration
{
    public static final Codec<SpeciesConfig> CODEC = ResourceLocation.CODEC.fieldOf("species").codec().comapFlatMap(
        res -> {
            final Species species = Species.REGISTRY.get(res);
            return species == null ? DataResult.error("not a species: " + res) : DataResult.success(new SpeciesConfig(species));
        }, s -> s.species.getRegistryName()
    );
}
