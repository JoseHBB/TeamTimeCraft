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
            sender.sendMessage("§f" + name + ": §b" + Utils.formatTime(seconds));
        }

        return true;
    }
}