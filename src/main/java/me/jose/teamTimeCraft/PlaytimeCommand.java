package me.jose.teamTimeCraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaytimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player)
        {
            int daily = PlayTimeStorage.getInstance().getDailyTime(player.getUniqueId());
            int total = PlayTimeStorage.getInstance().getTotalTime(player.getUniqueId());

            player.sendMessage("§aTempo jogado hoje: §f" + formatTime(daily));
            player.sendMessage("§aTempo jogado no total: §f" + formatTime(total));

            return true;
        }

        return false;
    }
    private String formatTime(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        return String.format("%02dh %02dm %02ds", h, m, s);
    }
}
