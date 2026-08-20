package org.labellum.mc.dttfc.mixin;

import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import org.labellum.mc.dttfc.ConfigDTTFC;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Species.class)
public class SpeciesMixin {

  @Inject(method = "doesRequireTileEntity", at=@At("HEAD"), cancellable = true)
  public void doesRequireTileEntity(LevelAccessor level, BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
    ci.setReturnValue(true);
  }
    @Inject(method = "doesRequireTileEntity", at=@At("HEAD"), cancellable = true)
    public void doesRequireTileEntity(LevelAccessor level, BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
        if (ConfigDTTFC.USE_TFC_CALENDAR_GROWTH.isTrue()) {
            ci.setReturnValue(true);
        }
    }
}
