package org.labellum.mc.dttfc.client;

import java.util.Locale;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class DTTFCSeasonalLeavesModelLoader implements IGeometryLoader<DTTFCSeasonalLeavesModelGeometry>
{
    @Override
    public DTTFCSeasonalLeavesModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException
    {
        final Wood wood = getWood(jsonObject);
        return new DTTFCSeasonalLeavesModelGeometry(
            getBlockModel(jsonObject, deserializationContext, "dense_leaves"),
            getBlockModel(jsonObject, deserializationContext, "sparse_leaves"),
            getBlockModel(jsonObject, deserializationContext, "bare"),
            getBlockModel(jsonObject, deserializationContext, "blooming"),
            wood.isConifer(),
            wood.getFlowerOffset()
        );
    }

    private static Wood getWood(JsonObject jsonObject)
    {
        final JsonElement element = jsonObject.get("wood");
        if (element == null || !element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString())
        {
            throw new JsonParseException("DTTFC seasonal leaves model requires a string \"wood\" element.");
        }

        try
        {
            return Wood.valueOf(element.getAsString().toUpperCase(Locale.ROOT));
        }
        catch (IllegalArgumentException e)
        {
            throw new JsonParseException("Unknown TFC wood type for DTTFC seasonal leaves: " + element.getAsString(), e);
        }
    }

    private static BlockModel getBlockModel(JsonObject jsonObject, JsonDeserializationContext context, String key)
    {
        final JsonElement element = jsonObject.get(key);
        if (element == null || !element.isJsonObject())
        {
            throw new JsonParseException("DTTFC seasonal leaves model requires a block model object \"" + key + "\".");
        }

        return context.deserialize(element, BlockModel.class);
    }
}
