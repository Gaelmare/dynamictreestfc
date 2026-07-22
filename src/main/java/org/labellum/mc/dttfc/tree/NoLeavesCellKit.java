package org.labellum.mc.dttfc.tree;

import com.dtteam.dynamictrees.api.cell.Cell;
import com.dtteam.dynamictrees.api.cell.CellKit;
import com.dtteam.dynamictrees.api.cell.CellNull;
import com.dtteam.dynamictrees.api.cell.CellSolver;
import com.dtteam.dynamictrees.api.voxmap.SimpleVoxmap;
import com.dtteam.dynamictrees.systems.cell.LeafClusters;
import net.minecraft.resources.ResourceLocation;

public class NoLeavesCellKit extends CellKit
{
    public NoLeavesCellKit(ResourceLocation registryName)
    {
        super(registryName);
    }

    @Override
    public Cell getCellForLeaves(int hydro)
    {
        return CellNull.NULL_CELL;
    }

    @Override
    public Cell getCellForBranch(int radius, int meta)
    {
        return CellNull.NULL_CELL;
    }

    @Override
    public CellSolver getCellSolver()
    {
        return NULL_CELL_SOLVER;
    }

    @Override
    public SimpleVoxmap getLeafCluster()
    {
        return LeafClusters.NULL_MAP;
    }

    @Override
    public int getDefaultHydration()
    {
        return 0;
    }
}
