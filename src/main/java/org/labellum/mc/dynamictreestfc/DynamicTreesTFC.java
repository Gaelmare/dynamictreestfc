package org.labellum.mc.dynamictreestfc;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.ferreusveritas.dynamictrees.event.BiomeSuitabilityEvent;
import net.dries007.tfc.TerraFirmaCraft;
import org.labellum.mc.dynamictreestfc.proxy.CommonProxy;
import org.labellum.mc.dynamictreestfc.proxy.ClientProxy;

import static com.ferreusveritas.dynamictrees.ModConstants.*;

@Mod(
        modid = DynamicTreesTFC.MOD_ID,
        name = DynamicTreesTFC.MOD_NAME,
        version = DynamicTreesTFC.VERSION,
        dependencies = DynamicTreesTFC.DEPENDENCIES
)
public class DynamicTreesTFC
{

    public static final String MOD_ID = "dynamictreestfc";
    public static final String MOD_NAME = "DynamicTreesTFC";
    public static final String VERSION = "0.1";
    public static final String DEPENDENCIES
            = REQAFTER + TerraFirmaCraft.MOD_ID +
            //AT + "0.28.2.115" + ORGREATER +
            NEXT +
            REQAFTER + DYNAMICTREES_LATEST
;

    //TFC version in dev is the string "${version}" so not sure how this works.

    /*
    https://github.com/ferreusveritas/DynamicTrees/blob/f7edfc2d423b87bf6b7ebf2ad1b628a694114171/src/main/java/com/ferreusveritas/dynamictrees/trees/Species.java#L963
    need to catch BiomeSuitabilityEvent

3:16 PM]AlcatrazEscapee:I've told you before, I think the only thing you should need to make trees grow using dynamic trees is to overwrite the ITreeGenerator on init
[3:16 PM]Deyna:Yep
[3:16 PM]AlcatrazEscapee:I can make it accessable so it's not required to do private final modifications
[3:17 PM]Deyna:It might be easier to just run a check if Dynamic Trees exists and hand over the actual placement to it
[3:18 PM]Deyna:I'm no expert but I think the tree generator also handles where the trees are placed + forest generation, no?
[3:18 PM]AlcatrazEscapee:yes, and that uses TFC's temperature / rainfall restrictions to achieve natural forest distributions
[3:18 PM]AlcatrazEscapee:I don't see why you'd need to change that
    3:19 PM]AlcatrazEscapee:The ITreeGenerator is called at the point when TFC says "give me a grown tree"
[3:19 PM]Deyna:that- makes things easy
[3:19 PM]AlcatrazEscapee:You might need to special case saplings though, idk what you'd expect from that
[3:19 PM]Deyna:that makes things really easy
[3:20 PM]AlcatrazEscapee:How does dynamic trees handle saplings? Do they grow into full size trees and then grow from there?
[3:20 PM]Deyna:it places a rooty dirt block
[3:20 PM]Deyna:and then the tree grows from a sapling via growth instructions
[3:21 PM]AlcatrazEscapee:In which case, probably just registry replace TFC saplings with a new one that overrides the placement (because TFC ones will call the ITreeGenerator)
[3:21 PM]AlcatrazEscapee:Or actually, it'd probably be nicer if ITreeGenerator had a flag for if it was called by world gen or by a sapling growth
[3:22 PM]Deyna:& the rest of it is just modifying tree drop tables, registering the new items + some spritework

8:03 AM]Dirius:Is there a way to intercept the tree generation from TFC? That would be useful for making Dynamic Tree's integration
[8:31 AM]AlcatrazEscapee:Not in the sense I think you are wanting to.
[8:31 AM]AlcatrazEscapee:However, there's probably a neat solution
[8:32 AM]AlcatrazEscapee:You'll want to replace the ITreeGenerator instance of each Tree object sometime after init
[8:32 AM]AlcatrazEscapee:Right now it's a private final field on Tree, but if that's a potential candidate for addon compatibility, it can be easily changed.
[8:33 AM]AlcatrazEscapee:If you want more control, you can registry replace the Tree objects themselves with modified ones
[8:33 AM]AlcatrazEscapee:There's no real way to inject at a higher level (i.e. stop TFC from generating any trees and only call your tree gen), but you shouldn't need to do that.

    [7:28 PM]Dirius:If someone was going to go about adding dynamic trees compatibility. What would be the best way to intercept all of TFC's normal tree spawn events?
[7:29 PM]AlcatrazEscapee:Iterate through the Tree registry and call setTreeGenerator on each of the elements
[7:31 PM]AlcatrazEscapee:You will need to create a tree generator, which is basically a wrapper around an object used to create a tree, both for world gen and when a sapling grows
[7:32 PM]AlcatrazEscapee:Each different wood type has a specific one
     */


    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static DynamicTreesTFC INSTANCE;


    @SidedProxy(clientSide = "org.labellum.mc.dynamictreestfc.proxy.ClientProxy",
                serverSide = "org.labellum.mc.dynamictreestfc.proxy.CommonProxy")
    public static CommonProxy proxy;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
        System.out.println(MOD_NAME + " is loading");
        proxy.preInit();
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {

    }

    /**
     * Forge will automatically look up and bind blocks to the fields in this class
     * based on their registry name.
     */
    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Blocks
    {
      /*
          public static final MySpecialBlock mySpecialBlock = null; // placeholder for special block below
      */
    }

    /**
     * Forge will automatically look up and bind items to the fields in this class
     * based on their registry name.
     */
    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Items
    {
      /*
          public static final ItemBlock mySpecialBlock = null; // itemblock for the block above
          public static final MySpecialItem mySpecialItem = null; // placeholder for special item below
      */
    }

    /**
     * This is a special class that listens to registry events, to allow creation of mod blocks and items at the proper time.
     */
    @Mod.EventBusSubscriber
    public static class ObjectRegistryHandler
    {
        /**
         * Listen for the register event for creating custom items
         */
        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event)
        {
            ModItems.register(event.getRegistry());
            ModTrees.registerItems(event.getRegistry());
        }

        /**
         * Listen for DynamicTrees event for biome suitability
         * Ignore suitability computations for TFC Trees for now
         */
        @SubscribeEvent
        public static void biomeHandler(BiomeSuitabilityEvent event)
        {
            event.setSuitability(1.0f); //doesn't change value, sets isHandled
        }

        /**
         * Listen for the register event for creating custom blocks
         */
        @SubscribeEvent
        public static void addBlocks(RegistryEvent.Register<Block> event)
        {
   			ModBlocks.register(event.getRegistry());
            ModTrees.registerBlocks(event.getRegistry());
        }

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void registerModels(ModelRegistryEvent event) {
            ModModels.register(event);
        }
    }
    /* EXAMPLE ITEM AND BLOCK - you probably want these in separate files
    public static class MySpecialItem extends Item {

    }

    public static class MySpecialBlock extends Block {

    }
    */
}
