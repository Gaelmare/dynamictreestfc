package io.github.dttfc.tree;

import com.ferreusveritas.dynamictrees.api.cell.Cell;
import com.ferreusveritas.dynamictrees.api.cell.CellKit;
import com.ferreusveritas.dynamictrees.api.cell.CellNull;
import com.ferreusveritas.dynamictrees.api.cell.CellSolver;
import com.ferreusveritas.dynamictrees.cell.CellKits;
import com.ferreusveritas.dynamictrees.cell.LeafClusters;
import com.ferreusveritas.dynamictrees.cell.PalmFrondCell;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class PalmCellKit extends CellKit
{
    private final Cell palmBranch = new Cell()
    {
        @Override
        public int getValue()
        {
            return 5;
        }

        @Override
        public int getValueFromSide(Direction side)
        {
            return side == Direction.UP ? getValue() : 0;
        }

    };

    private final Cell[] palmFrondCells = {
        CellNull.NULL_CELL,
        new PalmFrondCell(1),
        new PalmFrondCell(2),
        new PalmFrondCell(3),
        new PalmFrondCell(4),
        new PalmFrondCell(5),
        new PalmFrondCell(6),
        new PalmFrondCell(7)
    };

    private final CellKits.BasicSolver palmSolver = new CellKits.BasicSolver(new short[] {0x0514, 0x0413, 0x0312, 0x0221});

    public PalmCellKit(ResourceLocation registryName)
    {
        super(registryName);
    }

    @Override
    public Cell getCellForLeaves(int hydro)
    {
        return palmFrondCells[hydro];
    }

    @Override
    public Cell getCellForBranch(int radius, int meta)
    {
        return radius == 3 ? palmBranch : CellNull.NULL_CELL;
    }

    @Override
    public SimpleVoxmap getLeafCluster()
    {
        return LeafClusters.PALM;
    }

    @Override
    public CellSolver getCellSolver()
    {
        return palmSolver;
    }

    @Override
    public int getDefaultHydration()
    {
        return 4;
    }
}
