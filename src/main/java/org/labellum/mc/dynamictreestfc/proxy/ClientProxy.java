package org.labellum.mc.dynamictreestfc.proxy;

import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_NAME;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        super.preInit();
        System.out.println(MOD_NAME + " is loading");
    }
}
