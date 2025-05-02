package net.somedom.taboo.stigma;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class StigmaManager {

    private static final String STIGMA_TAG = "taboo_stigma";

    public static int getStigma(ServerPlayer player) {
        CompoundTag tag = player.getPersistentData();
        return tag.getInt(STIGMA_TAG);
    }

    public static void addStigma(ServerPlayer player, int amount) {
        int current = getStigma(player);
        setStigma(player, current + amount);
    }

    public static void setStigma(ServerPlayer player, int value) {
        CompoundTag tag = player.getPersistentData();
        tag.putInt(STIGMA_TAG, value);
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
