package org.labellum.mc.dynamictreestfc.blocks;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.util.OreDictionaryHelper;

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
                this.setSoundType(SoundType.WOOD);
                this.setHardness(15.0F).setResistance(5.0F);
                this.setHarvestLevel("axe", 0);
                this.setCreativeTab(null);
                Blocks.FIRE.setFireInfo(this, 5, 5);
            }
        }
}
