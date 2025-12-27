package net.somedom.taboo.mixin.activity;

import net.minecraft.world.entity.schedule.Activity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Activity.class)
public class ActivityMixin implements IActivityExtension {

}
