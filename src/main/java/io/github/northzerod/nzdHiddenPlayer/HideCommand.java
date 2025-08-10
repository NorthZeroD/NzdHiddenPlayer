package io.github.northzerod.nzdHiddenPlayer;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class HideCommand {
    public static LiteralCommandNode<CommandSourceStack> getNode(JavaPlugin plugin) {
        LiteralArgumentBuilder<CommandSourceStack> hide = Commands.literal("hide")
                .executes(
                        ctx -> {
                            if (ctx.getSource().getExecutor() instanceof Player player) {
                                PersistentDataContainer container = player.getPersistentDataContainer();
                                NamespacedKey key = new NamespacedKey(plugin, "is_hidden");
                                Boolean is_hidden = container.get(key, PersistentDataType.BOOLEAN);
                                HideManager hideManager = new HideManager(plugin);

                                if (is_hidden != null) {
                                    hideManager.setHide(player, !is_hidden);
                                    if (!is_hidden) {
                                        player.sendRichMessage("<green>你已对他人隐藏自己");
                                        return Command.SINGLE_SUCCESS;
                                    } else {
                                        player.sendRichMessage("<red>你已对他人显示自己");
                                        return Command.SINGLE_SUCCESS;
                                    }
                                } else {
                                    hideManager.setHide(player, true);
                                    player.sendRichMessage("<green>你已对他人隐藏自己");
                                    return Command.SINGLE_SUCCESS;
                                }
                            }
                            ctx.getSource().getSender().sendPlainMessage("执行失败: 此命令需要一个玩家");
                            return Command.SINGLE_SUCCESS;
                        }
                )
                .requires(sender ->
                        sender.getSender().isOp() || sender.getSender().hasPermission("nzd.command.hide")
                );
        return hide.build();
    }
}
