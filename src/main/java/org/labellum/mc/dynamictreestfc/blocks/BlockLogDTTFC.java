package org.labellum.mc.dynamictreestfc.blocks;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.init.Blocks;

import net.dries007.tfc.api.types.Tree;

import static org.labellum.mc.dynamictreestfc.DynamicTreesTFC.MOD_ID;
// The only purpose of this block is to avoid calling getBlockHardness on a BlockLogTFC with a
// DynamicTrees branch blockstate, which doesn't have the needed properties to compute it.
// This block just holds the textures, really.
public class BlockLogDTTFC extends BlockLog
{
        private static final Map<Tree, BlockLogDTTFC> MAP = new HashMap();
        public final Tree wood;

        public static BlockLogDTTFC get(Tree wood) {
            return MAP.get(wood);
        }

        public BlockLogDTTFC(Tree wood) {
            super();
            if (MAP.put(wood, this) != null) {
                throw new IllegalStateException("There can only be one.");
            } else {
                this.wood = wood;
                setSoundType(SoundType.WOOD);
                setHardness(2.0F).setResistance(5.0F);
                setHarvestLevel("axe", 0);
                setCreativeTab(null);
                Blocks.FIRE.setFireInfo(this, 5, 5);
            }
        }
}
