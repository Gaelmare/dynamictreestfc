package org.labellum.mc.dynamictreestfc.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import org.labellum.mc.dynamictreestfc.ModBlocks;
import org.labellum.mc.dynamictreestfc.ModTrees;
import org.labellum.mc.dynamictreestfc.blocks.BlockRootyTFC;

import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_ID;

public class ClientProxy extends CommonProxy
{

    @Override
    public void init() {
        super.init();
        registerColorHandlers();
    }

    public void registerColorHandlers() {

 /*     //not sure what this code actually accomplishes
        try {
            ResourceLocation loc = new ResourceLocation(MOD_ID, "models/item/seed/colors.json");
            InputStream in;
            in = Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            Gson gson = new Gson();
            JsonElement je = gson.fromJson(reader, JsonElement.class);
            JsonObject json = je.getAsJsonObject();
            for(Map.Entry<String, JsonElement> entry : json.entrySet()) {
                String speciesName = entry.getKey();
                Species species = ModTrees.tfcSpecies.get(speciesName);
                if(species != null) {
                    Seed seed = species.getSeed();
                    JsonArray colors = entry.getValue().getAsJsonArray();
                    List<Integer> colorArray = new ArrayList<>(4);
                    colors.forEach(i -> colorArray.add(Integer.parseInt(i.getAsString(), 16)));
                    if(colors != null) {
                        ModelHelper.regColorHandler(seed, new IItemColor() {
                            @Override
                            public int colorMultiplier(ItemStack stack, int tintIndex) {
                                return tintIndex >= 0 && tintIndex < colorArray.size() ? colorArray.get(tintIndex) : 0xFFFFFF;
                            }
                        });
                    }

                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
*/
        final int magenta = 0x00FF00FF;//for errors.. because magenta sucks.

        //Register GrowingLeavesBlocks Colorizers
        for(BlockDynamicLeaves leaves: LeavesPaging.getLeavesMapForModId(MOD_ID).values()) {
            ModelHelper.regColorHandler(leaves, new IBlockColor() {
                @Override
                public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
                    Block block = state.getBlock();

                    if(TreeHelper.isLeaves(block)) {
                        return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
                    }
                    return magenta;
                }
            });
        }

        final int white = 0xFFFFFFFF;

        final BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();

        //Register Rooty Colorizers
        blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> {
            switch(tintIndex) {
                case 0: return blockColors.colorMultiplier(Blocks.GRASS.getDefaultState(), world, pos, tintIndex);
                case 1: return state.getBlock() instanceof BlockRootyTFC ? ((BlockRootyTFC) state.getBlock()).rootColor(state, world, pos) : white;
                default: return white;
            }
        },
        new Block[] { ModBlocks.blockRootyDirt});

    }
}
