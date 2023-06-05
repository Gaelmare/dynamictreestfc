package org.labellum.mc.dttfc.client;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraftforge.client.model.data.IDynamicBakedModel;

public abstract class BaseBakedModel implements IDynamicBakedModel
{
    @Override
    public ItemOverrides getOverrides()
    {
        return ItemOverrides.EMPTY;
    }

    @Override
    public boolean doesHandlePerspectives()
    {
        return false;
    }

    @Override
    public boolean useAmbientOcclusion()
    {
        return false;
    }

    @Override
    public boolean isGui3d()
    {
        return false;
    }

    @Override
    public boolean usesBlockLight()
    {
        return false;
    }

    @Override
    public boolean isCustomRenderer()
    {
        return true;
    }

}
