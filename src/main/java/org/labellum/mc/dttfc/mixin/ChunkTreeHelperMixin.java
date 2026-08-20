package org.labellum.mc.dttfc.mixin;

import com.dtteam.dynamictrees.tree.ChunkTreeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.labellum.mc.dttfc.ConfigDTTFC;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkTreeHelper.class)
public class ChunkTreeHelperMixin {

    @Inject(method = "isSurroundedByLoadedChunks", at = @At("HEAD"), cancellable = true)
    private static void isSurroundedByLoadedChunks(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
        if (ConfigDTTFC.USE_TFC_CALENDAR_GROWTH.isTrue()) {
            ci.setReturnValue(true);
        }
    }
}
