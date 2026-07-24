package org.labellum.mc.dttfc.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import com.dtteam.dynamictrees.tree.TreeHelper;
import com.dtteam.dynamictrees.block.soil.SoilBlock;
import com.dtteam.dynamictrees.systems.poissondisc.PoissonDisc;
import com.dtteam.dynamictrees.systems.poissondisc.UniversalPoissonDiscProvider;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dynamictrees.utility.CoordUtils;
import com.dtteam.dynamictrees.api.worldgen.LevelContext;
import com.dtteam.dynamictrees.api.worldgen.RandomXOR;
import com.dtteam.dynamictrees.worldgen.feature.DynamicTreeFeature;
import com.dtteam.dynamictrees.worldgen.DynamicTreeGenerationContext;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.client.overworld.SolarCalculator;
import net.dries007.tfc.common.blocks.wood.FallenLeavesBlock;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.util.EnvironmentHelpers;
import net.dries007.tfc.world.chunkdata.ChunkData;
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
        final var rand = context.random();
        final ForestConfig config = context.config();
        final ChunkData data = ChunkData.get(level, pos);
        final ForestType forestType = data.getForestType();
        final ChunkPos chunkPos = new ChunkPos(pos);

        if (rand.nextFloat() > forestType.getPerChunkChance())
        {
            return false;
        }

        final AtomicInteger trees = new AtomicInteger(forestType.sampleTrees(rand));

        final AtomicBoolean gen = new AtomicBoolean(false);
        DISC_PROVIDER.getPoissonDiscs(levelContext, chunkPos).forEach(disc -> {
            if (trees.get() > 0)
            {
                gen.set(gen.get() | generateTrees(levelContext, level, disc, pos, trees, data, rand, config, speciesFinder()));
            }
        });

        if (gen.get())
        {
            placeUndergrowth(level, rand, pos, config, data, new BlockPos.MutableBlockPos(), forestType, forestType.sampleBushes(rand));
            placeGroundcover(level, rand, pos, config, data, new BlockPos.MutableBlockPos(), forestType, forestType.sampleGroundcover(rand));
            placeLeafPile(level, rand, pos, config, data, new BlockPos.MutableBlockPos(), forestType, forestType.sampleLeafPiles(rand));
        }

        return gen.get();
    }

    protected boolean generateTrees(LevelContext levelContext, WorldGenLevel level, PoissonDisc disc, BlockPos originPos, AtomicInteger counter, ChunkData data, RandomSource random, ForestConfig config, Function<DFEFeature.Entry, ResourceLocation> speciesFinder)
    {
        boolean gen = false;
        final BlockPos pos = new BlockPos(disc.x, level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, disc.x, disc.z) - 1, disc.z);
        if (level instanceof WorldGenRegion region && !this.ensureCanWrite(region, pos))
        {
            return false;
        }
        if (counter.get() <= 0)
        {
            return false;
        }
        if (generateTree(levelContext, level, disc, originPos, pos, data, random, config, speciesFinder) == DynamicTreeFeature.GeneratorResult.GENERATED)
        {
            counter.decrementAndGet();
            gen = true;
        }
        return gen;
    }

    protected Function<DFEFeature.Entry, ResourceLocation> speciesFinder()
    {
        return DFEFeature.Entry::species;
    }

    protected DynamicTreeFeature.GeneratorResult generateTree(LevelContext levelContext, WorldGenLevel level, PoissonDisc circle, BlockPos originPos, BlockPos groundPos, ChunkData data, RandomSource random, ForestConfig config, Function<DFEFeature.Entry, ResourceLocation> speciesFinder)
    {
        if (groundPos == BlockPos.ZERO)
        {
            return DynamicTreeFeature.GeneratorResult.NO_GROUND;
        }
        else if (levelContext.accessor().getBlockState(groundPos).getBlock() instanceof SoilBlock)
        {
            return DynamicTreeFeature.GeneratorResult.ALREADY_GENERATED;
        }
        else
        {
            RANDOM.setXOR(groundPos);
            final BlockState dirtState = levelContext.accessor().getBlockState(groundPos);
            var result = DynamicTreeFeature.GeneratorResult.GENERATED;

            final var entry = getTree(level, data, random, config, groundPos, data.getForestType());

            if (entry != null)
            {
                final ResourceLocation res = getSpeciesResource(entry, data.getForestType(), random, speciesFinder);
                if (res == null) return DynamicTreeFeature.GeneratorResult.NO_TREE;
                final var species = Species.REGISTRY.get(res);
                if (species != null && species.isValid())
                {
                    if (species.isAcceptableSoilForWorldgen(levelContext.accessor(), groundPos, dirtState))
                    {
                        final var biome = levelContext.level().getBiome(groundPos);

                        // noinspection removal
                        if (!species.generate(new DynamicTreeGenerationContext(levelContext, species, originPos, groundPos.mutable(), biome, CoordUtils.getRandomDir(RANDOM), circle.radius, true)))
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

    private static ResourceLocation getSpeciesResource(DFEFeature.Entry entry, ForestType forestType, RandomSource random, Function<DFEFeature.Entry, ResourceLocation> speciesFinder)
    {
        if (shouldGenerateDeadTree(entry.entry(), forestType, random))
        {
            return entry.deadSpecies().orElseGet(() -> speciesFinder.apply(entry));
        }
        return speciesFinder.apply(entry);
    }

    private static boolean shouldGenerateDeadTree(ForestConfig.Entry entry, ForestType forestType, RandomSource random)
    {
        final int deadChance = entry.deadChance();
        return deadChance > 0 && (forestType.isDead() || random.nextInt(deadChance) == 0);
    }

    private void placeUndergrowth(WorldGenLevel level, RandomSource random, BlockPos chunkBlockPos, ForestConfig config, ChunkData data, BlockPos.MutableBlockPos mutablePos, ForestType forestType, int tries)
    {
        final int chunkX = chunkBlockPos.getX();
        final int chunkZ = chunkBlockPos.getZ();

        mutablePos.set(chunkX + random.nextInt(16), 0, chunkZ + random.nextInt(16));
        mutablePos.setY(level.getHeight(Heightmap.Types.OCEAN_FLOOR, mutablePos.getX(), mutablePos.getZ()));

        final DFEFeature.Entry entry = getTree(level, data, random, config, mutablePos, forestType);
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
                            final var biome = levelContext.level().getBiome(groundPos);

                            species.generate(new DynamicTreeGenerationContext(levelContext, species, mutablePos, groundPos.mutable(), biome, CoordUtils.getRandomDir(RANDOM), 3, true));
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

    private void placeGroundcover(WorldGenLevel level, RandomSource random, BlockPos chunkBlockPos, ForestConfig config, ChunkData data, BlockPos.MutableBlockPos mutablePos, ForestType forestType, int tries)
    {
        final int chunkX = chunkBlockPos.getX();
        final int chunkZ = chunkBlockPos.getZ();

        mutablePos.set(chunkX + random.nextInt(16), 0, chunkZ + random.nextInt(16));
        mutablePos.setY(level.getHeight(Heightmap.Types.OCEAN_FLOOR, mutablePos.getX(), mutablePos.getZ()));

        final DFEFeature.Entry entry = getTree(level, data, random, config, mutablePos, forestType);
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


    private void placeLeafPile(WorldGenLevel level, RandomSource random, BlockPos chunkBlockPos, ForestConfig config, ChunkData data, BlockPos.MutableBlockPos mutablePos, ForestType forestType, int tries)
    {
        final int chunkX = chunkBlockPos.getX();
        final int chunkZ = chunkBlockPos.getZ();

        mutablePos.set(chunkX + random.nextInt(16), 0, chunkZ + random.nextInt(16));
        mutablePos.setY(level.getHeight(Heightmap.Types.OCEAN_FLOOR, mutablePos.getX(), mutablePos.getZ()));

        final DFEFeature.Entry entry = getTree(level, data, random, config, mutablePos, forestType);
        if (entry != null)
        {
            entry.entry().fallenLeaves().ifPresent(placementState -> {
                for (int i = 0; i < tries; ++i)
                {
                    mutablePos.set(chunkX + random.nextInt(16), 0, chunkZ + random.nextInt(16));
                    mutablePos.setY(level.getHeight(Heightmap.Types.OCEAN_FLOOR, mutablePos.getX(), mutablePos.getZ()));
                    final BlockPos origin = mutablePos.immutable();

                    for (int j = 0; j < 8; j++)
                    {
                        mutablePos.setWithOffset(origin, Mth.nextInt(random, -2, 2), 0, Mth.nextInt(random, -2, 2));
                        if (level.getFluidState(mutablePos).isEmpty() && EnvironmentHelpers.isOnSturdyFace(level, mutablePos) && EnvironmentHelpers.isWorldgenReplaceable(level, mutablePos))
                        {
                            placementState = placementState.setValue(FallenLeavesBlock.LAYERS, Mth.nextInt(random, 1, FallenLeavesBlock.MAX_LAYERS - 3));
                            level.setBlock(mutablePos, placementState, 3);
                        }
                    }
                }
            });
        }
    }

    @Nullable
    private DFEFeature.Entry getTree(WorldGenLevel level, ChunkData chunkData, RandomSource random, ForestConfig config, BlockPos pos, ForestType forestType)
    {
        final List<DFEFeature.Entry> entries = new ArrayList<>(4);
        final float groundwater = chunkData.getAverageGroundwater(pos);
        final float rainVariance = chunkData.getRainVariance(pos) * (SolarCalculator.getInNorthernHemisphere(pos, level.getLevel()) ? 1f : -1f);
        final float averageTemperature = EnvironmentHelpers.adjustAvgTempForElev(pos.getY(), chunkData.getAverageSeaLevelTemp(pos));
        final int elevation = pos.getY();

        config.entries().stream()
            .map(configuredFeature -> configuredFeature.value().config())
            .filter(DFEFeature.Entry.class::isInstance)
            .map(DFEFeature.Entry.class::cast)
            .filter(entry -> entry.entry().isValid(averageTemperature, groundwater, rainVariance, elevation))
            .sorted(Comparator.comparingDouble(entry -> entry.entry().distanceFromMean(averageTemperature, groundwater, rainVariance, elevation)))
            .forEach(entries::add);

        if (entries.isEmpty()) return null;

        while (entries.size() > forestType.getMaxTreeTypes())
        {
            entries.remove(entries.size() - 1);
        }

        int alternateSize = forestType.getAlternateSize();
        while (entries.size() > 1 && alternateSize > 0)
        {
            entries.remove(0);
            alternateSize--;
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
