package me.jose.teamTimeCraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaytimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            int daily = PlayTimeStorage.getInstance().getDailyTime(player.getUniqueId());
            int total = PlayTimeStorage.getInstance().getTotalTime(player.getUniqueId());
            int remaining = PlayTimeStorage.getInstance().getRemainingTime(player.getUniqueId());

            player.sendMessage("§aTempo jogado hoje: §f" + Utils.formatTime(daily));
            player.sendMessage("§aTempo jogado no total: §f" + Utils.formatTime(total));
            player.sendMessage("§aTempo restante hoje: §e" + Utils.formatTime(remaining));

            return true;
        }

        return false;
    }
}
