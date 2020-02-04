package org.labellum.mc.dynamictreestfc.proxy;

import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.objects.blocks.BlocksTFC;
import net.dries007.tfc.objects.blocks.stone.BlockRockVariant;
import net.dries007.tfc.types.DefaultTrees;
import org.labellum.mc.dynamictreestfc.DTFCGenerator;
import org.labellum.mc.dynamictreestfc.ModBlocks;
import org.labellum.mc.dynamictreestfc.ModTrees;

import static com.ferreusveritas.dynamictrees.ModTrees.*;

public class CommonProxy
{

    public void preInit() {
        buildMaps();
        ModBlocks.preInit();
        ModTrees.preInit();
    }

    public void init() {
        replaceGenerators();
    }

    public void buildMaps()
    {

    }

    private void replaceGenerators() {

        for (Tree tree: TFCRegistries.TREES.getValues()) {
            tree.setTreeGenerator(new DTFCGenerator());
        }
    }

    public void postInit() {
    }
}
