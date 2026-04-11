package net.somedom.taboo.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.somedom.taboo.Taboo;
import net.somedom.taboo.potion.ModPotions;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = Taboo.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    @SubscribeEvent
    public static void addCustomTraits(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.CLERIC) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(5).add((entity, randomSource) -> {
                DataComponentPredicate waterPredicate = DataComponentPredicate.builder()
                        .expect(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER))
                        .build();

                ItemCost waterBottle = new ItemCost(Items.POTION.builtInRegistryHolder(), 1, waterPredicate);

                ItemStack holyWater = new ItemStack(Items.POTION);
                holyWater.set(DataComponents.POTION_CONTENTS, new PotionContents(ModPotions.HOLY_WATER));

                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 16),
                        Optional.of(waterBottle),
                        holyWater,
                        4,
                        5,
                        0.05f
                );
            });
        }
    }

}
