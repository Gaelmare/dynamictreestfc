package org.labellum.mc.dynamictreestfc.proxy;

import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Tree;
import org.labellum.mc.dynamictreestfc.DTTFCGenerator;
import org.labellum.mc.dynamictreestfc.ModBlocks;
import org.labellum.mc.dynamictreestfc.ModTrees;

public class CommonProxy
{
    public void preInit()
    {
        ModBlocks.preInit();
        ModTrees.preInit();
    }

    public void init()
    {
        replaceGenerators();
    }

    private void replaceGenerators()
    {
        for (Tree tree : TFCRegistries.TREES.getValuesCollection())
        {
            tree.setTreeGenerator(new DTTFCGenerator());
        }
    }
}
