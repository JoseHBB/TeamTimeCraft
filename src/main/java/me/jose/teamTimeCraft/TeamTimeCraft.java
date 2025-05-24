package me.jose.teamTimeCraft;

import org.bukkit.plugin.java.JavaPlugin;

public final class TeamTimeCraft extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);

        getLogger().info("Plugin carregado com sucesso!");
    }

}
