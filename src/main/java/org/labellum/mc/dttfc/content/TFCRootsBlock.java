package org.labellum.mc.dttfc.content;

import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.block.branch.BasicRootsBlock;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.common.fluids.FluidProperty;
import net.dries007.tfc.common.fluids.IFluidLoggable;
import net.dries007.tfc.util.Helpers;

public class TFCRootsBlock extends BasicRootsBlock implements IFluidLoggable
{
    public static final FluidProperty FLUID = TFCBlockStateProperties.WATER;

    public TFCRootsBlock(ResourceLocation name, Properties properties)
    {
        super(name, properties);
        registerDefaultState(defaultBlockState().setValue(getFluidProperty(), getFluidProperty().keyFor(Fluids.EMPTY)));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (!this.isFullBlock(state))
        {
            Block coverBlock = this.getFamily().getPrimitiveCoveredRoots().orElse(null);
            if (coverBlock != null && handStack.getItem() == coverBlock.asItem())
            {
                final BlockState newState = FluidHelpers.fillWithFluid(state.setValue(LAYER, Layer.COVERED), level.getFluidState(pos).getType());
                if (newState != null && this.canPlace(player, level, pos, newState))
                {
                    level.setBlock(pos, newState, 3);
                    if (!player.isCreative())
                    {
                        handStack.shrink(1);
                    }

                    level.playSound(null, pos, coverBlock.getSoundType(state, level, pos, player).getPlaceSound(), SoundSource.BLOCKS, 1.0F, 0.8F);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        return super.useItemOn(handStack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public int setRadius(LevelAccessor level, BlockPos pos, int radius, @Nullable Direction originDir, int flags)
    {
        destroyMode = DynamicTrees.DestroyMode.SET_RADIUS;
        BlockState currentState = level.getBlockState(pos);

        boolean replacingWater = Helpers.isFluid(currentState.getFluidState(), TFCTags.Fluids.WATER_LIKE);
        boolean replacingGround = this.getFamily().isAcceptableSoilForRootSystem(currentState);
        boolean setWaterlogged = replacingWater && !replacingGround;
        Layer layer = currentState.getBlock() == this ? currentState.getValue(LAYER) : (replacingGround ? BasicRootsBlock.Layer.COVERED : BasicRootsBlock.Layer.EXPOSED);
        BlockState placeState = this.getStateForRadius(radius).setValue(LAYER, layer);
        boolean placedWaterlogged = false;
        if (setWaterlogged)
        {
            BlockState filled = FluidHelpers.fillWithFluid(placeState, currentState.getFluidState().getType());
            if (filled != null)
            {
                level.setBlock(pos, filled, flags);
                placedWaterlogged = true;
            }
        }
        if (!placedWaterlogged)
        {
            level.setBlock(pos, placeState, flags);
        }
        destroyMode = DynamicTrees.DestroyMode.SLOPPY;
        return radius;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState)
    {
        return IFluidLoggable.super.placeLiquid(level, pos, state, fluidState);
    }

    @Override
    public boolean canPlaceLiquid(Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid)
    {
        return IFluidLoggable.super.canPlaceLiquid(player, level, pos, state, fluid);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos)
    {
        FluidHelpers.tickFluid(level, pos, state);
        return state.setValue(WATERLOGGED, !state.getFluidState().isEmpty());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(getFluidProperty()));
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return IFluidLoggable.super.getFluidState(state);
    }

    @Override
    public FluidProperty getFluidProperty()
    {
        return FLUID;
    }
}
