package me.jose.teamTimeCraft;

import org.bukkit.plugin.java.JavaPlugin;

public final class TeamTimeCraft extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("TeamTimeCraft plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("TeamTimeCraft plugin disabled!");
    }
}
