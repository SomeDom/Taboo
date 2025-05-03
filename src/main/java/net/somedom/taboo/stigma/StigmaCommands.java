package net.somedom.taboo.stigma;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

import java.util.Collection;

public class StigmaCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("stigma")
                .requires(cs -> cs.hasPermission(2))

                // Get Stigma
                .then(Commands.literal("get")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(ctx -> {
                                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                    targets.forEach(player -> {
                                        int stigma = StigmaManager.getStigma(player);
                                        ctx.getSource().sendSystemMessage(Component.literal(
                                                player.getName().getString() + "'s Stigma: " + stigma));
                                    });
                                    return 1;
                                })
                        )
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            int stigma = StigmaManager.getStigma(player);
                            ctx.getSource().sendSystemMessage(Component.literal("Your Stigma: " + stigma));
                            return 1;
                        })
                )

                // Set Stigma
                .then(Commands.literal("set")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                            targets.forEach(player -> {
                                                StigmaManager.setStigma(player, amount);
                                                ctx.getSource().sendSystemMessage(Component.literal(
                                                        "Set " + player.getName().getString() + "'s Stigma to " + amount));
                                            });
                                            return 1;
                                        })
                                )
                        )
                )

                // Add Stigma
                .then(Commands.literal("add")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(ctx -> {
                                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                            targets.forEach(player -> {
                                                StigmaManager.addStigma(player, amount);
                                                int newStigma = StigmaManager.getStigma(player);
                                                ctx.getSource().sendSystemMessage(Component.literal(
                                                        "Added " + amount + " Stigma to " + player.getName().getString()));
                                            });
                                            return 1;
                                        })
                                )
                        )
                )

                // Clear stigma
                .then(Commands.literal("clear")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(ctx -> {
                                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                    targets.forEach(player -> {
                                        StigmaManager.clearStigma(player);
                                        ctx.getSource().sendSystemMessage(Component.literal(
                                                "Cleared Stigma for " + player.getName().getString()));
                                    });
                                    return 1;
                                })
                        )
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            StigmaManager.clearStigma(player);
                            ctx.getSource().sendSystemMessage(Component.literal("Cleared your Stigma"));
                            return 1;
                        })
                )
        );
    }
}
