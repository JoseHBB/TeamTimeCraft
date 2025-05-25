package me.jose.teamTimeCraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.UUID;

public class AllPlaytimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PlayTimeStorage storage = PlayTimeStorage.getInstance();
        sender.sendMessage("Tempo total de jogadores:");

        for (Map.Entry<UUID, Integer> entry : storage.getTotalTimeMap().entrySet()) {
            UUID uuid = entry.getKey();
            int seconds = entry.getValue();
            String name = storage.getPlayerName(uuid);
            if (name == null) name = uuid.toString().substring(0, 8); // Fallback simples
            sender.sendMessage("§f" + name + ": §b" + formatTime(seconds));
        }

        return true;
    }
    private String formatTime(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        return String.format("%02dh %02dm %02ds", h, m, s);
    }
}