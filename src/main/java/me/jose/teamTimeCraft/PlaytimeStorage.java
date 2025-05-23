package me.jose.teamTimeCraft;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.*;
import java.util.HashMap;
import java.util.UUID;

public class PlaytimeStorage {

    private final JavaPlugin plugin;
    private final File file;
    private final YamlConfiguration config;
    private final Hashmap<UUID, Integer> todayPlaytime = new Hashmap<>();
    private final Hashmap<UUID, Integer> totalPlaytime = new Hashmap<>();
    private final Hashmap<UUID, Integer> lastPlaytime = new Hashmap<>();

    private final int weekdayLimit;
    private final int weekendLimit;
    private final int maxOnline;

    public Playtimestorage(JavaPlugin plugin){
        this.plugin = plugin;
        
        this.weekdayLimit = plugin.getConfig().getInt("daily_limit.weekdays");
        this.weekendLimit = plugin.getConfig().getInt("daily_limit.weekends");
        this.maxOnline = plugin.getConfig().getInt("max_player_count");

        this.file = new File(plugin.getDataFolder(), "data/playtime.yml");
        if (!file.exists()){
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        load();
    }

    public void checkDailyReset(UUID uuid, LocalDate today){}
}
