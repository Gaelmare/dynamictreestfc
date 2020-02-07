package org.labellum.mc.dynamictreestfc.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.models.ModelResourceLocationWrapped;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_ID;

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
    public static void regModel(TreeFamily tree) {

        BlockBranch blockBranch = tree.getDynamicBranch();
        ModelResourceLocation modelLocation = getCreateBranchModel(blockBranch, tree.autoCreateBranch());

        setGenericStateMapper(blockBranch, modelLocation);
        if(blockBranch instanceof BlockBranchThick) {
            setGenericStateMapper(((BlockBranchThick) blockBranch).otherBlock, modelLocation);
        }

        BlockSurfaceRoot surfaceRoot = tree.getSurfaceRoots();
        if(surfaceRoot != null) {
            ModelLoader.setCustomStateMapper(surfaceRoot, new StateMap.Builder().ignore(surfaceRoot.getIgnorableProperties()).build());
        }
    }

    private static ModelResourceLocation getCreateBranchModel(BlockBranch blockBranch, boolean automatic) {
        return automatic ? getCreateBranchModelAuto(blockBranch) : getCreateBranchModelManual(blockBranch);
    }

    private static ModelResourceLocation getCreateBranchModelAuto(BlockBranch blockBranch) {
        ResourceLocation family = blockBranch.getFamily().getName();
        return new ModelResourceLocationWrapped(new ResourceLocation(ModConstants.MODID, "branch"), blockBranch.getDefaultState());
    }

    private static ModelResourceLocation getCreateBranchModelManual(BlockBranch blockBranch) {
        ResourceLocation family = blockBranch.getFamily().getName();
        ResourceLocation resloc = new ResourceLocation(family.getNamespace(), "block/branch/" + family.getPath() );
        return new ModelResourceLocation(resloc , null);
    }
}
