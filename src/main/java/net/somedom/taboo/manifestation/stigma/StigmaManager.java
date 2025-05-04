package net.somedom.taboo.manifestation.stigma;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class StigmaManager {

    private static final String STIGMA_TAG = "taboo_stigma";

    // Use persistent player data that survives death
    private static CompoundTag getPersistentData(ServerPlayer player) {
        CompoundTag persistentData = player.getPersistentData();
        if (!persistentData.contains(Player.PERSISTED_NBT_TAG)) {
            persistentData.put(Player.PERSISTED_NBT_TAG, new CompoundTag());
        }
        return persistentData.getCompound(Player.PERSISTED_NBT_TAG);
    }

    public static int getStigma(ServerPlayer player) {
        CompoundTag data = getPersistentData(player);
        return data.getInt(STIGMA_TAG);
    }

    public static void addStigma(ServerPlayer player, int amount, boolean applyMultiplier) {
        int current = getStigma(player);
        float multiplier = applyMultiplier ? StigmaMultiplierManager.StigmaMultiplier(player) : 1.0f;
        int total = Math.round(amount * multiplier);
        setStigma(player, current + total);
    }

    public static void setStigma(ServerPlayer player, int value) {
        CompoundTag data = getPersistentData(player);
        data.putInt(STIGMA_TAG, value);
    }

    public static void clearStigma(ServerPlayer player) {
        setStigma(player, 0);
    }

    public static void removeStigma(ServerPlayer player, int amount) {
        int current = getStigma(player);
        int newVal = Math.max(current - amount, 0);
        setStigma(player, newVal);
    }
}
