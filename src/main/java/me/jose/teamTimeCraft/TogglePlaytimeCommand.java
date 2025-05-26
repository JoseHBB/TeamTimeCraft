package me.jose.teamTimeCraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TogglePlaytimeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        TeamTimeCraft.toggle = !TeamTimeCraft.toggle;

        if (TeamTimeCraft.toggle) {sender.sendMessage("A contagem de tempo está §cLIGADA.");}
        else {sender.sendMessage("A contagem de tempo está §aDESLIGADA.");}

        return true;
    }
}
