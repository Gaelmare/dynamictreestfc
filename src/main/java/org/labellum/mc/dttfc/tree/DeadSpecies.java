package org.labellum.mc.dttfc.tree;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.resources.ResourceLocation;
import org.labellum.mc.dttfc.DTTFC;

public class DeadSpecies extends Species
{
    public static final TypedRegistry.EntryType<Species> TYPE = Species.createDefaultType(DeadSpecies::new);
    public static final LeavesProperties NO_LEAVES = createNoLeavesProperties();

    public DeadSpecies(ResourceLocation registryName, Family family, LeavesProperties leavesProperties)
    {
        super(registryName, family, NO_LEAVES);
    }

    @Override
    public Species setLeavesProperties(LeavesProperties leavesProperties)
    {
        return super.setLeavesProperties(NO_LEAVES);
    }

    @Override
    public LeavesProperties getLeavesProperties()
    {
        return NO_LEAVES;
    }

    private static LeavesProperties createNoLeavesProperties()
    {
        final LeavesProperties properties = new LeavesProperties(DTTFC.identifier("no_leaves"));
        properties.setCellKit(new NoLeavesCellKit(DTTFC.identifier("no_leaves")));
        properties.setSmotherLeavesMax(0);
        return properties;
    }
}
