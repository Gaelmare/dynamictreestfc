package org.labellum.mc.dttfc.client;

import java.util.function.Function;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

public class DTTFCSeasonalLeavesModelGeometry implements IUnbakedGeometry<DTTFCSeasonalLeavesModelGeometry>
{
    private final BlockModel denseLeaves;
    private final BlockModel sparseLeaves;
    private final BlockModel bare;
    private final BlockModel blooming;
    private final boolean conifer;
    private final float flowerOffset;

    public DTTFCSeasonalLeavesModelGeometry(BlockModel denseLeaves, BlockModel sparseLeaves, BlockModel bare, BlockModel blooming, boolean conifer, float flowerOffset)
    {
        this.denseLeaves = denseLeaves;
        this.sparseLeaves = sparseLeaves;
        this.bare = bare;
        this.blooming = blooming;
        this.conifer = conifer;
        this.flowerOffset = flowerOffset;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides)
    {
        return new DTTFCSeasonalLeavesBakedModel(
            denseLeaves.bake(baker, spriteGetter, modelState),
            sparseLeaves.bake(baker, spriteGetter, modelState),
            bare.bake(baker, spriteGetter, modelState),
            blooming.bake(baker, spriteGetter, modelState),
            conifer,
            flowerOffset
        );
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context)
    {
        denseLeaves.resolveParents(modelGetter);
        sparseLeaves.resolveParents(modelGetter);
        bare.resolveParents(modelGetter);
        blooming.resolveParents(modelGetter);
    }
}
