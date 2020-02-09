package org.labellum.mc.dynamictreestfc;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Tree;
import org.labellum.mc.dynamictreestfc.blocks.BlockLogDTTFC;
import org.labellum.mc.dynamictreestfc.proxy.ModelHelperTFC;

import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_ID;

public class ModModels
{
    @SideOnly(Side.CLIENT)
    public static void register(ModelRegistryEvent event) {

        //Register Meshers for Branches
        for(TreeFamily tree: ModTrees.tfcTrees) {
            ModelHelperTFC.regModel(tree.getDynamicBranch());//Register Branch itemBlock
            ModelHelperTFC.regModel(tree);//Register custom state mapper for branch
        }

        ModelLoader.setCustomStateMapper(ModBlocks.blockRootyDirt, new StateMap.Builder().ignore(BlockRooty.LIFE).build());

        TFCRegistries.TREES.getValuesCollection().forEach(t -> {
            ModelHelperTFC.regModel(BlockLogDTTFC.get(t));
        });

        ModTrees.tfcSpecies.values().stream().filter(s -> s.getSeed() != Seed.NULLSEED).forEach(s -> ModelHelperTFC.regModel(s.getSeed()));//Register Seed Item Models
    }

}
