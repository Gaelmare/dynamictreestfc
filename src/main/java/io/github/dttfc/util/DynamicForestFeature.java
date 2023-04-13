package io.github.dttfc.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.block.rooty.RootyBlock;
import com.ferreusveritas.dynamictrees.systems.poissondisc.PoissonDisc;
import com.ferreusveritas.dynamictrees.systems.poissondisc.UniversalPoissonDiscProvider;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.LevelContext;
import com.ferreusveritas.dynamictrees.util.RandomXOR;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.worldgen.DynamicTreeFeature;
import com.ferreusveritas.dynamictrees.worldgen.GenerationContext;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.util.EnvironmentHelpers;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ChunkDataProvider;
import net.dries007.tfc.world.chunkdata.ForestType;
import net.dries007.tfc.world.feature.tree.ForestConfig;

public class DynamicForestFeature extends Feature<ForestConfig>
{
    public static final UniversalPoissonDiscProvider DISC_PROVIDER = new UniversalPoissonDiscProvider();
    protected static final RandomXOR RANDOM = new RandomXOR();

    public DynamicForestFeature(Codec<ForestConfig> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ForestConfig> context)
    {
        final LevelContext levelContext = LevelContext.create(context.level());
        final WorldGenLevel level = context.level();
        final BlockPos pos = context.origin();
        final Random rand = context.random();
        final ForestConfig config = context.config();
        final ChunkDataProvider provider = ChunkDataProvider.get(context.chunkGenerator());
        final ChunkData data = provider.get(level, pos);
        final ForestType forestType = data.getForestType();
        final float density = data.getForestDensity();
        final ChunkPos chunkPos = new ChunkPos(pos);
        final ForestConfig.Type forestTypeConfig = config.typeMap().get(forestType);

        if (rand.nextFloat() > forestTypeConfig.perChunkChance())
        {
            return false;
        }

        final AtomicInteger trees = new AtomicInteger(forestTypeConfig.treeCount().sample(rand));

        final AtomicBoolean gen = new AtomicBoolean(false);
        DISC_PROVIDER.getPoissonDiscs(levelContext, chunkPos).forEach(disc -> {
            if (trees.get() >= 0)
            {
                gen.set(gen.get() | generateTrees(levelContext, level, disc, pos, trees, data, rand, config, DFEFeature.Entry::species));
            }
        });

        if (gen.get())
        {
            placeUndergrowth(level, rand, pos, config, data, new BlockPos.MutableBlockPos(), forestTypeConfig.sampleBushCount(rand, forestTypeConfig.bushCount(), trees.get(), density));
            placeGroundcover(level, rand, pos, config, data, new BlockPos.MutableBlockPos(), forestTypeConfig.groundcoverCount().sample(rand));
        }

        return gen.get();
    }

    protected boolean generateTrees(LevelContext levelContext, WorldGenLevel level, PoissonDisc disc, BlockPos originPos, AtomicInteger counter, ChunkData data, Random random, ForestConfig config, Function<DFEFeature.Entry, ResourceLocation> speciesFinder)
    {
        boolean gen = false;
        final BlockPos pos = new BlockPos(disc.x, level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, disc.x, disc.z) - 1, disc.z);
        if ((level instanceof WorldGenRegion region && !this.ensureCanWrite(region, pos)) || counter.decrementAndGet() <= 0)
        {
            return false;
        }
        if (generateTree(levelContext, disc, originPos, pos, data, random, config, speciesFinder) == DynamicTreeFeature.GeneratorResult.GENERATED)
        {
            gen = true;
        }
        return gen;
    }

    protected DynamicTreeFeature.GeneratorResult generateTree(LevelContext levelContext, PoissonDisc circle, BlockPos originPos, BlockPos groundPos, ChunkData data, Random random, ForestConfig config, Function<DFEFeature.Entry, ResourceLocation> speciesFinder)
    {
        if (groundPos == BlockPos.ZERO)
        {
            return DynamicTreeFeature.GeneratorResult.NO_GROUND;
        }
        else if (levelContext.accessor().getBlockState(groundPos).getBlock() instanceof RootyBlock)
        {
            return DynamicTreeFeature.GeneratorResult.ALREADY_GENERATED;
        }
        else
        {
            RANDOM.setXOR(groundPos);
            final BlockState dirtState = levelContext.accessor().getBlockState(groundPos);
            var result = DynamicTreeFeature.GeneratorResult.GENERATED;

            final var entry = getTree(data, random, config, groundPos);

            if (entry != null)
            {
                final ResourceLocation res = speciesFinder.apply(entry);
                if (res == null) return DynamicTreeFeature.GeneratorResult.NO_TREE;
                final var species = Species.REGISTRY.get(res);
                if (species != null && species.isValid())
                {
                    if (species.isAcceptableSoilForWorldgen(levelContext.accessor(), groundPos, dirtState))
                    {
                        final Biome biome = levelContext.level().getBiome(groundPos).value();

                        // noinspection removal
                        if (!species.generate(new GenerationContext(levelContext, species, originPos, groundPos.mutable(), biome, CoordUtils.getRandomDir(RANDOM), circle.radius, SafeChunkBounds.ANY_WG)))
                        {
                            result = DynamicTreeFeature.GeneratorResult.FAIL_GENERATION;
                        }
                    }
                    else
                    {
                        result = DynamicTreeFeature.GeneratorResult.FAIL_SOIL;
                    }
                }
                else
                {
                    result = DynamicTreeFeature.GeneratorResult.NO_TREE;
                }
            }
            else
            {
                result = DynamicTreeFeature.GeneratorResult.UNHANDLED_BIOME;
            }

            return result;
        }
    }

    private void placeUndergrowth(WorldGenLevel level, Random random, BlockPos chunkBlockPos, ForestConfig config, ChunkData data, BlockPos.MutableBlockPos mutablePos, int tries)
    {
        final int chunkX = chunkBlockPos.getX();
        final int chunkZ = chunkBlockPos.getZ();

        mutablePos.set(chunkX + random.nextInt(16), 0, chunkZ + random.nextInt(16));
        mutablePos.setY(level.getHeight(Heightmap.Types.OCEAN_FLOOR, mutablePos.getX(), mutablePos.getZ()));

        final DFEFeature.Entry entry = getTree(data, random, config, mutablePos);
        if (entry != null)
        {
            for (int j = 0; j < tries; ++j)
            {
                mutablePos.set(chunkX + random.nextInt(16), 0, chunkZ + random.nextInt(16));
                mutablePos.setY(level.getHeight(Heightmap.Types.OCEAN_FLOOR, mutablePos.getX(), mutablePos.getZ()));

                if (isAreaClear(level, mutablePos))
                {
                    final LevelContext levelContext = LevelContext.create(level);
                    final ResourceLocation res = entry.undergrowthSpecies().orElse(null);
                    if (res == null) return;
                    final var species = Species.REGISTRY.get(res);
                    if (species != null && species.isValid())
                    {
                        BlockPos groundPos = mutablePos.immutable().below();
                        BlockState dirtState = level.getBlockState(groundPos);
                        if (species.isAcceptableSoilForWorldgen(levelContext.accessor(), groundPos, dirtState))
                        {
                            final Biome biome = levelContext.level().getBiome(groundPos).value();

                            // noinspection removal
                            species.generate(new GenerationContext(levelContext, species, mutablePos, groundPos.mutable(), biome, CoordUtils.getRandomDir(RANDOM), 3, SafeChunkBounds.ANY_WG));
                        }
                    }
                }


            }

        }
    }

    private boolean isAreaClear(WorldGenLevel level, BlockPos.MutableBlockPos pos)
    {
        final BlockPos original = pos.immutable();

        for (int dx = -2; dx <= 2; dx++)
        {
            for (int dy = -1; dy <= 2; dy++)
            {
                for (int dz = -2; dz <= 2; dz++)
                {
                    pos.setWithOffset(original, dx, dy, dz);
                    if (TreeHelper.isBranch(level.getBlockState(pos)))
                    {
                        return false;
                    }
                }
            }
        }
        pos.set(original);
        return true;
    }

    private void placeGroundcover(WorldGenLevel level, Random random, BlockPos chunkBlockPos, ForestConfig config, ChunkData data, BlockPos.MutableBlockPos mutablePos, int tries)
    {
        final int chunkX = chunkBlockPos.getX();
        final int chunkZ = chunkBlockPos.getZ();

        mutablePos.set(chunkX + random.nextInt(16), 0, chunkZ + random.nextInt(16));
        mutablePos.setY(level.getHeight(Heightmap.Types.OCEAN_FLOOR, mutablePos.getX(), mutablePos.getZ()));

        final DFEFeature.Entry entry = getTree(data, random, config, mutablePos);
        if (entry != null)
        {
            entry.entry().groundcover().ifPresent(groundcover -> {
                for (int j = 0; j < tries; ++j)
                {
                    BlockState placementState = groundcover.get(random);

                    mutablePos.set(chunkX + random.nextInt(16), 0, chunkZ + random.nextInt(16));
                    mutablePos.setY(level.getHeight(Heightmap.Types.OCEAN_FLOOR, mutablePos.getX(), mutablePos.getZ()));

                    placementState = FluidHelpers.fillWithFluid(placementState, level.getFluidState(mutablePos).getType());
                    if (placementState != null && EnvironmentHelpers.isWorldgenReplaceable(level.getBlockState(mutablePos)) && EnvironmentHelpers.isOnSturdyFace(level, mutablePos))
                    {
                        setBlock(level, mutablePos, placementState);
                    }
                }
            });
        }
    }

    @Nullable
    private DFEFeature.Entry getTree(ChunkData chunkData, Random random, ForestConfig config, BlockPos pos)
    {
        List<DFEFeature.Entry> entries = new ArrayList<>(4);
        float rainfall = chunkData.getRainfall(pos);
        float averageTemperature = chunkData.getAverageTemp(pos);
        config.entries().stream().map(configuredFeature -> configuredFeature.value().config()).map(cfg -> (DFEFeature.Entry) cfg).forEach(entry -> {
            float lastRain = entry.getAverageRain();
            float lastTemp = entry.getAverageTemp();
            if (entry.isValid(averageTemperature, rainfall))
            {
                if (entry.distanceFromMean(lastTemp, lastRain) < entry.distanceFromMean(averageTemperature, rainfall))
                {
                    entries.add(entry);
                }
                else
                {
                    entries.add(0, entry);
                }
            }
        });

        if (entries.isEmpty()) return null;
        if (config.useWeirdness())
        {
            float weirdness = chunkData.getForestWeirdness();
            Collections.rotate(entries, -(int) (weirdness * (entries.size() - 1f)));
            for (int i = 1; i >= -1; i--)
            {
                if (entries.size() <= 1)
                    break;
                if (random.nextFloat() > weirdness - (0.15f * i) + 0.1f)
                {
                    entries.remove(entries.size() - 1);
                }
            }
        }

        int index = 0;
        while (index < entries.size() - 1 && random.nextFloat() < 0.6f)
        {
            index++;
        }
        return entries.get(index);
    }

    private boolean ensureCanWrite(WorldGenRegion level, BlockPos pos)
    {
        final int xSection = SectionPos.blockToSectionCoord(pos.getX());
        final int zSection = SectionPos.blockToSectionCoord(pos.getZ());
        final ChunkPos chunkpos = level.getCenter();
        return chunkpos.x == xSection && chunkpos.z == zSection;
    }

}
