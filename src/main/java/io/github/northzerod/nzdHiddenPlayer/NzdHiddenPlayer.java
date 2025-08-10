package io.github.northzerod.nzdHiddenPlayer;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NzdHiddenPlayer extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new HideManager(this), this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(HideCommand.getNode(this));
        });

        this.getLogger().info("插件 NzdHiddenPlayer 已启用");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("插件 NzdHiddenPlayer 已禁用");
    }

    public static JavaPlugin getInstance() {
        return (JavaPlugin) Bukkit.getPluginManager().getPlugin("NzdHiddenPlayer");
    }
}
