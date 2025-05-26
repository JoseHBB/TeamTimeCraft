package me.jose.teamTimeCraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.scoreboard.Team;

public class AdvancementListener implements Listener {
    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();

        // Obtém a equipe única do jogador no Scoreboard
        String teamName = "tab_" + player.getName();
        if (teamName.length() > 16) teamName = teamName.substring(0, 16);

        Team team = player.getScoreboard().getTeam(teamName);
        if (team != null) {
            // Remove prefixo e sufixo para evitar interferências nas conquistas
            team.prefix(null);
            team.suffix(null);
        }
    }
}
