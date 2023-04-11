package io.github.dttfc.mixin;

import com.ferreusveritas.dynamictrees.api.FutureBreakable;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.dries007.tfc.util.AxeLoggingHelper;

@Mixin(AxeLoggingHelper.class)
public abstract class AxeLoggingHelperMixin
{

    @Inject(method = "isLoggingBlock", at= @At("HEAD"), cancellable = true, remap = false)
    private static void inject$isLoggingBlock(BlockState state, CallbackInfoReturnable<Boolean> cir)
    {
        if (state.getBlock() instanceof FutureBreakable)
        {
            cir.setReturnValue(false);
        }
    }
}
