package org.labellum.mc.dttfc.util;

import java.util.function.Function;
import com.dtteam.dynamictrees.api.worldgen.LevelContext;
import com.dtteam.dynamictrees.systems.poissondisc.PoissonDisc;
import com.dtteam.dynamictrees.worldgen.feature.DynamicTreeFeature;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ForestType;
import net.dries007.tfc.world.feature.tree.ForestConfig;

public class DeadForestFeature extends DynamicForestFeature
{
    public DeadForestFeature(Codec<ForestConfig> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ForestConfig> context)
    {
        final LevelContext levelContext = LevelContext.create(context.level());
        final WorldGenLevel level = context.level();
        final BlockPos origin = context.origin();
        final RandomSource random = context.random();
        final ForestConfig config = context.config();
        final ChunkData data = ChunkData.get(level, origin);
        final ForestType forestType = data.getForestType();

        if (random.nextFloat() > forestType.getPerChunkChance())
        {
            return false;
        }

        int remainingTrees = forestType.sampleTrees(random);
        if (remainingTrees <= 0)
        {
            return false;
        }

        boolean generated = false;
        final int tries = remainingTrees * 8;

        for (int i = 0; i < tries && remainingTrees > 0; i++)
        {
            final int x = origin.getX() + random.nextInt(16);
            final int z = origin.getZ() + random.nextInt(16);
            final BlockPos groundPos = new BlockPos(x, level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1, z);
            final PoissonDisc disc = new PoissonDisc(x, z, 4);

            if (generateTree(levelContext, level, disc, origin, groundPos, data, random, config, speciesFinder()) == DynamicTreeFeature.GeneratorResult.GENERATED)
            {
                remainingTrees--;
                generated = true;
            }
        }

        return generated;
    }

    @Override
    protected Function<DFEFeature.Entry, ResourceLocation> speciesFinder()
    {
        return entry -> entry.deadSpecies().orElseGet(entry::species);
    }
}
