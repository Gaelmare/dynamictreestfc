package org.labellum.mc.dttfc.client;

import com.dtteam.dynamictrees.client.SoundInstanceHandler;
import com.dtteam.dynamictrees.entity.FallingTreeEntity;
import com.dtteam.dynamictrees.entity.animation.AnimationHandlers;
import com.dtteam.dynamictrees.tree.species.Species;
import org.labellum.mc.dttfc.DTTFC;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public final class ClientModEvents
{
    private static final Set<FallingTreeEntity> FALLING_TREE_SOUNDS = Collections.newSetFromMap(new WeakHashMap<>());

    public static void init(IEventBus bus)
    {
        bus.addListener(ClientModEvents::onModelRegister);
        NeoForge.EVENT_BUS.addListener(ClientModEvents::onEntityTick);
    }

    public static void onModelRegister(ModelEvent.RegisterGeometryLoaders event)
    {
        event.register(DTTFC.identifier("palm_fronds"), new PalmLeavesModelLoader());
        event.register(DTTFC.identifier("seasonal_leaves"), new DTTFCSeasonalLeavesModelLoader());
    }

    public static void onEntityTick(EntityTickEvent.Post event)
    {
        final Entity entity = event.getEntity();
        if (!(entity instanceof FallingTreeEntity fallingTree) || !fallingTree.level().isClientSide())
        {
            return;
        }

        if (!fallingTree.isAlive())
        {
            FALLING_TREE_SOUNDS.remove(fallingTree);
            return;
        }

        if (fallingTree.currentAnimationHandler == AnimationHandlers.falloverAnimationHandler || !FALLING_TREE_SOUNDS.add(fallingTree))
        {
            return;
        }

        final Species species = fallingTree.getSpecies();
        final float volume = fallingTree.getVolume();
        final SoundEvent sound = species.getFallingTreeStartSound(volume, fallingTree.hasLeaves());

        SoundInstanceHandler.playSoundInstance(sound, species.getFallingTreePitch(volume), fallingTree.position(), fallingTree);
    }
}
