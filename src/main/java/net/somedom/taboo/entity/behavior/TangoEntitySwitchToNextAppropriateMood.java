package net.somedom.taboo.entity.behavior;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.somedom.taboo.entity.custom.TangoEntity;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class TangoEntitySwitchToNextAppropriateMood extends ExtendedBehaviour<TangoEntity> {

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }


    @Override
    protected void start(TangoEntity entity) {

        if (entity.tickCount % 20 != 0) {
            return;
        }

        if (entity.getTarget() != null && !entity.getBrain().getActiveNonCoreActivity().equals(Activity.WORK)) {
            entity.getBrain().setActiveActivityIfPossible(Activity.WORK);
            System.out.println("SWITCHED TO MOOD: WORK");
            return;
        }

        entity.getBrain().setActiveActivityIfPossible(Activity.IDLE);
    }
}

