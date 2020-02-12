package org.labellum.mc.dynamictreestfc;

import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;


import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.api.util.ITreeGenerator;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataProvider;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataTFC;


public class DTFCGenerator implements ITreeGenerator
{

    @Override
    public void generateTree(TemplateManager templateManager, World world, BlockPos blockPos, Tree tree, Random random, boolean b)
    {
        Species dtSpecies = ModTrees.tfcSpecies.get(tree.toString());
        SafeChunkBounds bounds = new SafeChunkBounds(world, world.getChunk(blockPos).getPos()); //figure out how to cache these?
        dtSpecies.generate(world, blockPos.down(), world.getBiome(blockPos), random, 8, bounds);
        //dtSpecies.getJoCode("JP").setCareful(true).generate(world, dtSpecies, blockPos, world.getBiome(blockPos), EnumFacing.SOUTH, 8, SafeChunkBounds.ANY);
    }

    @Override
    public boolean canGenerateTree(World world, BlockPos pos, Tree treeType)
    {
        //experimental plains?
        ChunkDataTFC chunkData = world.getChunk(pos).getCapability(ChunkDataProvider.CHUNK_DATA_CAPABILITY, null);
        if (chunkData != null)
        {
            if (((int)(chunkData.getFloraDensity() * 100)) % 10 == 0) {
                return false;
            }
        }

        return ITreeGenerator.super.canGenerateTree(world, pos, treeType);
    }
}
    /*

Comments giving some direction

[3:16 PM]AlcatrazEscapee:I've told you before, I think the only thing you should need to make trees grow using dynamic trees is to overwrite the ITreeGenerator on init
[3:16 PM]Deyna:Yep
[3:16 PM]AlcatrazEscapee:I can make it accessable so it's not required to do private final modifications
[3:17 PM]Deyna:It might be easier to just run a check if Dynamic Trees exists and hand over the actual placement to it
[3:18 PM]Deyna:I'm no expert but I think the tree generator also handles where the trees are placed + forest generation, no?
[3:18 PM]AlcatrazEscapee:yes, and that uses TFC's temperature / rainfall restrictions to achieve natural forest distributions
[3:18 PM]AlcatrazEscapee:I don't see why you'd need to change that
[3:19 PM]AlcatrazEscapee:The ITreeGenerator is called at the point when TFC says "give me a grown tree"
[3:19 PM]Deyna:that- makes things easy
[3:19 PM]AlcatrazEscapee:You might need to special case saplings though, idk what you'd expect from that
[3:19 PM]Deyna:that makes things really easy
[3:20 PM]AlcatrazEscapee:How does dynamic trees handle saplings? Do they grow into full size trees and then grow from there?
[3:20 PM]Deyna:it places a rooty dirt block
[3:20 PM]Deyna:and then the tree grows from a sapling via growth instructions
[3:21 PM]AlcatrazEscapee:In which case, probably just registry replace TFC saplings with a new one that overrides the placement (because TFC ones will call the ITreeGenerator)
[3:21 PM]AlcatrazEscapee:Or actually, it'd probably be nicer if ITreeGenerator had a flag for if it was called by world gen or by a sapling growth
[3:22 PM]Deyna:& the rest of it is just modifying tree drop tables, registering the new items + some spritework

[8:03 AM]Dirius:Is there a way to intercept the tree generation from TFC? That would be useful for making Dynamic Tree's integration
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

