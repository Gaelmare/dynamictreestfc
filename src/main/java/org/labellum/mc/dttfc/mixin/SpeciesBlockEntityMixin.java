package org.labellum.mc.dttfc.mixin;

import com.dtteam.dynamictrees.block.soil.SpeciesBlockEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.labellum.mc.dttfc.content.LastProcessedTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SpeciesBlockEntity.class)
public abstract class SpeciesBlockEntityMixin implements LastProcessedTick {
  private static final long MISSING_DATA = Long.MIN_VALUE;
  private long lastProcessedTick = MISSING_DATA;

  @Override
  public long getLastProcessedTick() {
    return lastProcessedTick;
  }

  @Override
  public void setLastProcessedTick(long lastProcessedTick) {
    this.lastProcessedTick = lastProcessedTick;
  }

  @Override
  public boolean haveLastProcessedTick() {
    return lastProcessedTick != MISSING_DATA;
  }

  @Inject(method = "loadAdditional", at = @At("HEAD"))
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
    if (tag.contains("tfc_last_processed_tick")) {
      setLastProcessedTick(tag.getLong("tfc_last_processed_tick"));
    }
  }

  @Inject(method = "saveAdditional", at = @At("HEAD"))
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
    tag.putLong("tfc_last_processed_tick", getLastProcessedTick());
  }
}
