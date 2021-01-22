package org.labellum.mc.dynamictreestfc.client;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.property.IExtendedBlockState;

import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import com.ferreusveritas.dynamictrees.models.bakedmodels.BakedModelBlockRooty;

import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_ID;

public class BakedModelBlockRootyTFC extends BakedModelBlockRooty
{
    private TextureAtlasSprite particleSprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("minecraft:dirt");
    private static final IModel rootyIModel = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation(MOD_ID, "block/roots"));
    private static final IBakedModel rootyIBakedModel = rootyIModel.bake(rootyIModel.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

    public BakedModelBlockRootyTFC()
    {
        super(null);
    }

    @Override
    @Nonnull
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
    {
        List<BakedQuad> quads = new ArrayList<>(16);
        if (state != null && state.getBlock() instanceof MimicProperty.IMimic && state instanceof IExtendedBlockState)
        {
            IExtendedBlockState extendedState = (IExtendedBlockState) state;
            IBlockState mimicState = extendedState.getValue(MimicProperty.MIMIC);

            BlockModelShapes blockModelShapes = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
            IBakedModel mimicModel = blockModelShapes.getModelForState(mimicState);
            quads.addAll(mimicModel.getQuads(mimicState, side, rand));
            quads.addAll(rootyIBakedModel.getQuads(state, side, rand));
            if (!quads.isEmpty())
                particleSprite = quads.get(0).getSprite();
        }
        return quads;
    }

    @Override
    @Nonnull
    public TextureAtlasSprite getParticleTexture()
    {
        return particleSprite;
    }
}
