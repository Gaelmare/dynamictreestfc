package io.github.dttfc.content;

import java.util.Map;
import java.util.Random;
import com.ferreusveritas.dynamictrees.block.rooty.RootyBlock;
import com.ferreusveritas.dynamictrees.block.rooty.SoilProperties;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import net.dries007.tfc.common.blocks.DirectionPropertyBlock;
import net.dries007.tfc.common.blocks.soil.IGrassBlock;

public class RootyGrassBlock extends RootyBlock implements IGrassBlock
{
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    private static final Map<Direction, BooleanProperty> PROPERTIES = ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.WEST, WEST, Direction.SOUTH, SOUTH);

    public RootyGrassBlock(SoilProperties properties, Properties blockProperties)
    {
        super(properties, blockProperties);

        registerDefaultState(stateDefinition.any().setValue(SOUTH, false).setValue(EAST, false).setValue(NORTH, false).setValue(WEST, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)
    {
        if (facing.getAxis().isHorizontal())
        {
            return updateStateFromDirection(level, currentPos, stateIn, facing);
        }
        return stateIn;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        level.scheduleTick(pos, this, 0);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            level.scheduleTick(pos.relative(direction).above(), this, 0);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            level.scheduleTick(pos.relative(direction).above(), this, 0);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random)
    {
        super.randomTick(state, level, pos, random);
        // don't spread, the light level checks don't work here
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rand)
    {
        level.setBlock(pos, updateStateFromNeighbors(level, pos, state), 2);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return updateStateFromNeighbors(context.getLevel(), context.getClickedPos(), defaultBlockState());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(NORTH, EAST, SOUTH, WEST));
    }

    protected BlockState updateStateFromNeighbors(BlockGetter worldIn, BlockPos pos, BlockState state)
    {
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            state = updateStateFromDirection(worldIn, pos, state, direction);
        }
        return state;
    }

    protected BlockState updateStateFromDirection(BlockGetter worldIn, BlockPos pos, BlockState stateIn, Direction direction)
    {
        return stateIn.setValue(PROPERTIES.get(direction), worldIn.getBlockState(pos.relative(direction).below()).getBlock() instanceof IGrassBlock);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rot)
    {
        return DirectionPropertyBlock.rotate(state, rot);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return DirectionPropertyBlock.mirror(state, mirror);
    }

    @Override
    public BlockState getDirt()
    {
        return defaultBlockState(); // we don't want to allow tfc items to delete the rooty block (such as hoeing)
    }
}
