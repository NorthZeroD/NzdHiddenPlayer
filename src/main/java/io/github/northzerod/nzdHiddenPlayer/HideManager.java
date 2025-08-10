package io.github.northzerod.nzdHiddenPlayer;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public final class HideManager implements Listener {
    private final JavaPlugin plugin;
    private final Set<Player> hidePlayers = new HashSet<>();
    private final NamespacedKey key;

    public HideManager(JavaPlugin plugin) {
        this.plugin = plugin;
        key = new NamespacedKey(plugin, "is_hidden");
    }

    public void setHide(Player target, boolean is_hidden) {
        PersistentDataContainer container = target.getPersistentDataContainer();
        container.set(key, PersistentDataType.BOOLEAN, is_hidden);

        if (is_hidden) {
            hidePlayers.add(target);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.hidePlayer(plugin, target);
            }
        } else {
            hidePlayers.remove(target);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(plugin, target);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joined = event.getPlayer();
        PersistentDataContainer container = joined.getPersistentDataContainer();

        Boolean is_hidden = container.get(key, PersistentDataType.BOOLEAN);
        if (is_hidden != null && is_hidden) {
            setHide(joined, true);
            joined.sendRichMessage("<gold>你正在对他人隐藏自己");
        }

        for (Player hidePlayer : hidePlayers) {
            joined.hidePlayer(plugin, hidePlayer);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        hidePlayers.remove(event.getPlayer());
    }
}
