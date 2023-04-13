package io.github.dttfc.util;

import java.util.Random;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.LevelContext;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.worldgen.GenerationContext;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SpeciesFeature extends Feature<SpeciesConfig>
{
    public SpeciesFeature(Codec<SpeciesConfig> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SpeciesConfig> context)
    {
        final WorldGenLevel level = context.level();
        final BlockPos pos = context.origin();
        final Random rand = context.random();
        final BlockPos groundPos = pos.below();
        final BlockState dirtState = level.getBlockState(groundPos);
        final Species species = context.config().species();
        if (species.isValid())
        {
            if (species.isAcceptableSoilForWorldgen(level, groundPos, dirtState))
            {
                final Biome biome = level.getBiome(groundPos).value();
                final LevelContext levelContext = LevelContext.create(context.level());

                // noinspection removal
                return species.generate(new GenerationContext(levelContext, species, pos, groundPos.mutable(), biome, CoordUtils.getRandomDir(rand), 5, SafeChunkBounds.ANY_WG));
            }
        }

        return false;
    }
}
