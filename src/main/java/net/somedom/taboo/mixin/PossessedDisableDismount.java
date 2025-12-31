package net.somedom.taboo.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class PossessedDisableDismount {

    @Shadow public abstract CompoundTag getPersistentData();

    @Inject(method = "dismountsUnderwater", at = @At("HEAD"), cancellable = true)
    public void dismount(CallbackInfoReturnable<Boolean> cir) {

        if (getPersistentData().getBoolean("tango_possessed")) {
            cir.setReturnValue(false);
        }
    }
}
