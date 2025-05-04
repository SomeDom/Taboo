package net.somedom.taboo.manifestation.stigma;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;

public class StigmaMultiplierManager {
    public static float StigmaMultiplier(ServerPlayer player) {

        int timeSinceRest = player.getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));

        if (timeSinceRest >= 72000) {
            return 1.5f;
        }
        else {
            return 1.0f;
        }
    }
}
