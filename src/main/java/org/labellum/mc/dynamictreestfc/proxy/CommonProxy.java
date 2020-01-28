package org.labellum.mc.dynamictreestfc.proxy;

import net.minecraft.util.ResourceLocation;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Tree;
import org.labellum.mc.dynamictreestfc.DTFCGenerator;

import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_ID;

public class CommonProxy
{
    public void preInit() {
    }

    public void init() {
        replaceGenerators();
    }

    private void replaceGenerators() {
        Species oak = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak"));

        for (Tree tree: TFCRegistries.TREES.getValues()) {
            tree.setTreeGenerator(new DTFCGenerator());
        }
    }

    public void postInit() {
    }
}
