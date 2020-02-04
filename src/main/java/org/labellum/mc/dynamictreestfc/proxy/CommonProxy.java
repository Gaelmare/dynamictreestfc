package org.labellum.mc.dynamictreestfc.proxy;

import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Tree;

import org.labellum.mc.dynamictreestfc.DTFCGenerator;

public class CommonProxy
{

    public void init() {
        replaceGenerators();
    }

    private void replaceGenerators() {

        for (Tree tree: TFCRegistries.TREES.getValues()) {
            tree.setTreeGenerator(new DTFCGenerator());
        }
    }

}
