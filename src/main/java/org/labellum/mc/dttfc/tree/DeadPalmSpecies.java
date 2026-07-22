package org.labellum.mc.dttfc.tree;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.PalmSpecies;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.resources.ResourceLocation;

public class DeadPalmSpecies extends PalmSpecies
{
    public static final TypedRegistry.EntryType<Species> TYPE = Species.createDefaultType(DeadPalmSpecies::new);

    public DeadPalmSpecies(ResourceLocation registryName, Family family, LeavesProperties leavesProperties)
    {
        super(registryName, family, DeadSpecies.NO_LEAVES);
    }

    @Override
    public Species setLeavesProperties(LeavesProperties leavesProperties)
    {
        return super.setLeavesProperties(DeadSpecies.NO_LEAVES);
    }

    @Override
    public LeavesProperties getLeavesProperties()
    {
        return DeadSpecies.NO_LEAVES;
    }
}
