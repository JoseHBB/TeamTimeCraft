package me.jose.teamTimeCraft;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class RestorePlaytimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 2){
            sender.sendMessage("§cUso correto: /restoreplaytime <jogador> <segundos>");
            return false;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        if (!player.hasPlayedBefore() && !player.isOnline()) {
            sender.sendMessage("§cJogador não encontrado!");
            return false;
        }

        UUID uuid = player.getUniqueId();
        int seconds;
        try {
            seconds = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cValor de segundos inválido!");
            return false;
        }

        PlayTimeStorage.getInstance().restoreTime(uuid, seconds);
        PlayTimeStorage.getInstance().saveAsync();

        sender.sendMessage("§aTempo diário restaurado para " + args[0] + " em " + seconds + " segundos!");
        return true;
    }
}
