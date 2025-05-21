package org.labellum.mc.dttfc.client;

import java.util.function.Function;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.Nullable;

public class PalmLeavesModelGeometry implements IUnbakedGeometry<PalmLeavesModelGeometry>
{
    @Nullable
    protected final ResourceLocation frondsResLoc;

    public PalmLeavesModelGeometry(@Nullable final ResourceLocation frondsResLoc)
    {
        this.frondsResLoc = frondsResLoc;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation)
    {
        return new PalmLeavesBakedModel(modelLocation, frondsResLoc, spriteGetter);
    }
}