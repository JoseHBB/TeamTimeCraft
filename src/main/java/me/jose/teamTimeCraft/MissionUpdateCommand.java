package me.jose.teamTimeCraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MissionUpdateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Missions mission = TeamTimeCraft.getInstance().getMissions();
        mission.getMissionsAsync(TeamTimeCraft.getInstance());

        return true;
    }
}
