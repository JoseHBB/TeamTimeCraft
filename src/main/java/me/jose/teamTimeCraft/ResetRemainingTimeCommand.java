package me.jose.teamTimeCraft;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ResetRemainingTimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Set<UUID> uniqueUUIDs = new HashSet<>();
        int seconds = ConfigManager.getMaxPlaytimeSeconds();

        // Adiciona UUIDs dos jogadores online
        Bukkit.getOnlinePlayers().forEach(p -> uniqueUUIDs.add(p.getUniqueId()));
        // Adiciona UUIDs dos jogadores offline
        for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            uniqueUUIDs.add(op.getUniqueId());
        }

        for (UUID uuid : uniqueUUIDs) {
            PlayTimeStorage.getInstance().setRemainingTime(uuid, seconds);
        }
        PlayTimeStorage.getInstance().saveAsync();

        sender.sendMessage("§aTimer de TODOS os jogadores (online/offline) foi resetado!");
        return true;
    }
}
