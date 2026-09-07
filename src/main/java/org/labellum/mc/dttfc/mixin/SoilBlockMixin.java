package org.labellum.mc.dttfc.mixin;

import com.dtteam.dynamictrees.block.soil.SoilBlock;
import com.dtteam.dynamictrees.block.soil.SpeciesBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.labellum.mc.dttfc.ConfigDTTFC;
import org.labellum.mc.dttfc.util.AsyncTreeGrower;
import org.labellum.mc.dttfc.util.TreeGrower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoilBlock.class)
public class SoilBlockMixin {
    private static final Logger log = LoggerFactory.getLogger(SoilBlockMixin.class);

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (ConfigDTTFC.USE_TFC_CALENDAR_GROWTH.isFalse()) {
            return;
        }

        SoilBlock soilBlock = (SoilBlock) (Object) this;

        if (level.getBlockEntity(pos) instanceof SpeciesBlockEntity blockEntity) {
            if (ConfigDTTFC.ASYNC_RANDOM_TICK_TREE_GROWTH.isTrue()) {
                AsyncTreeGrower.addQueue(pos);
            } else {
                TreeGrower.attemptGrowth(state, level, pos, random, soilBlock, blockEntity);
            }
            ci.cancel();
        } else {
            log.warn(
                    "TFC Dynamic Trees: {} is missing \"SpeciesBlockEntity\" for its BlockEntity \"{}\" at pos {}. Fallback to normal randomTick growth.",
                    soilBlock.getClass(),
                    level.getBlockEntity(pos),
                    pos);
        }
    }
}
