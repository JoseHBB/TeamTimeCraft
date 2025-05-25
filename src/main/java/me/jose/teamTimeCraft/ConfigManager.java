package me.jose.teamTimeCraft;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private static final FileConfiguration config = TeamTimeCraft.getInstance().getConfig();

    public static int getMaxPlaytimeSeconds(){
        int baseHours = config.getInt("playtime.daily_limit.weekdays.minutes");
        int weekendHours = config.getInt("playtime.daily_limit.weekends.minutes");
        String day = PlayTimeStorage.getTodayInBrazil().getDayOfWeek().name();

        if (day.equals("SATURDAY") || day.equals("SUNDAY")) {
            return weekendHours * 60;
        }
        return baseHours * 60;
    }
    public static int getMaxPlayerCount(){
        return config.getInt("playtime.max_player_count");
    }
}
