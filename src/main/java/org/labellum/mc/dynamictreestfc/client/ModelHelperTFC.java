package org.labellum.mc.dynamictreestfc.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

/**
 * Copied from DT, uses TFC asset naming standard for manual branch models.
 * Would have only overridden 1 method, but it's private
 */
@SideOnly(Side.CLIENT)
public class ModelHelperTFC extends ModelHelper
{
    /**
     * Registers models associated with the tree.
     * At the moment this only deals with {@link BlockBranch} blocks
     *
     * @param tree
     */
    public static void regModel(TreeFamily tree)
    {

        BlockBranch blockBranch = tree.getDynamicBranch();

        ModelResourceLocation modelLocation = getBranchModelResourceLocation(blockBranch);

        setGenericStateMapper(blockBranch, modelLocation);
        if (blockBranch instanceof BlockBranchThick)
        {
            setGenericStateMapper(((BlockBranchThick) blockBranch).otherBlock, modelLocation);
        }

        BlockSurfaceRoot surfaceRoot = tree.getSurfaceRoots();
        if (surfaceRoot != null)
        {
            ModelLoader.setCustomStateMapper(surfaceRoot, new StateMap.Builder().ignore(surfaceRoot.getIgnorableProperties()).build());
        }
    }

    private static ModelResourceLocation getBranchModelResourceLocation(BlockBranch blockBranch)
    {
        ResourceLocation family = blockBranch.getFamily().getName();
        ResourceLocation resloc = new ResourceLocation(family.getNamespace(), "branch/" + family.getPath());
        return new ModelResourceLocation(resloc, null);
    }
}
