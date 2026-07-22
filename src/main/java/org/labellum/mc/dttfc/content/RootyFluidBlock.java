package org.labellum.mc.dttfc.content;

import java.util.Objects;
import com.dtteam.dynamictrees.tree.TreeHelper;
import com.dtteam.dynamictrees.block.branch.BranchBlock;
import com.dtteam.dynamictrees.block.soil.SoilBlock;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.fluids.FluidProperty;
import net.dries007.tfc.common.fluids.IFluidLoggable;
import net.dries007.tfc.common.fluids.TFCFluids;

public class RootyFluidBlock extends SoilBlock implements IFluidLoggable
{
    protected static final AABB WATER_ROOTS_AABB = new AABB(0.1, 0.0, 0.1, 0.9, 1.0, 0.9);
    public static final FluidProperty FLUID = TFCBlockStateProperties.WATER;

    public RootyFluidBlock(SoilProperties properties, Properties blockProperties)
    {
        super(properties, blockProperties);
        registerDefaultState(defaultBlockState().setValue(getFluidProperty(), getFluidProperty().keyFor(TFCFluids.SALT_WATER.getSource())));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(getFluidProperty()));
    }

    @Override
    public int getRadiusForConnection(BlockState state, BlockGetter level, BlockPos pos, BranchBlock from, Direction side, int fromRadius)
    {
        return 1;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state)
    {
        BlockState upState = level.getBlockState(pos.above());
        return TreeHelper.isBranch(upState) ? Objects.requireNonNull(TreeHelper.getBranch(upState)).getFamily().getBranchItem().map(ItemStack::new).orElse(ItemStack.EMPTY) : ItemStack.EMPTY;
    }

    @Override
    public float getHardness(BlockState state, BlockGetter level, BlockPos pos)
    {
        return 0.5f;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return Shapes.create(WATER_ROOTS_AABB);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos)
    {
        return Shapes.empty();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext)
    {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos)
    {
        FluidHelpers.tickFluid(level, pos, state);
        return super.updateShape(state, facing, facingState, level, pos, facingPos);
    }

    @Override
    public BlockState getDecayBlockState(BlockState state, BlockGetter level, BlockPos pos)
    {
        return state.getFluidState().isEmpty() ? Blocks.AIR.defaultBlockState() : super.getDecayBlockState(state, level, pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state)
    {
        return IFluidLoggable.super.getFluidState(state);
    }

    @Override
    public boolean getColorFromBark()
    {
        return true;
    }

    @Override
    public boolean fallWithTree(BlockState state, Level level, BlockPos pos, boolean breakLeaves)
    {
        level.setBlockAndUpdate(pos, getDecayBlockState(state, level, pos));
        return true;
    }

    @Override
    public FluidProperty getFluidProperty()
    {
        return FLUID;
    }
}
