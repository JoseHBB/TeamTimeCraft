package me.jose.teamTimeCraft;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.LocalDate;
import java.util.UUID;

public class JoinLeaveListener implements Listener {
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {

        Player player = event.getPlayer();

        int remaining = PlayTimeStorage.getInstance().getRemainingTime(player.getUniqueId());

        if (remaining == 0) {

            if (Bukkit.getOnlinePlayers().size() + 1 >= ConfigManager.getMaxPlayerCount())
            {
                int restoreTime = 5 * 60;
                PlayTimeStorage.getInstance().restoreTime(player.getUniqueId(), restoreTime);

                Bukkit.getScheduler().runTaskLater(
                        TeamTimeCraft.getInstance(),
                        () -> player.sendMessage(Component.text("Você recebeu " + restoreTime / 60 + " minutos, a jogadores o suficiente!").color(NamedTextColor.AQUA)),
                        60L
                );

                return;
            }

            event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
                    Component.text("Não não, você não pode entrar mais hoje.\nE a quantidade de jogadores online é menor que o necessário para sua entrada."));
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        LocalDate today = PlayTimeStorage.getTodayInBrazil();
        PlayTimeStorage.getInstance().checkDailyReset(player.getUniqueId(), today);

        PlayTimeStorage.getInstance().setPlayerName(player.getUniqueId(), player.getName());

        if (player.getName().equalsIgnoreCase("teteufra")){

            event.joinMessage(Component.text()
                    .content("")
                    .append(Component.text(event.getPlayer().getName(), NamedTextColor.DARK_RED, TextDecoration.BOLD))
                    .append(Component.text(" conhecido como ", NamedTextColor.RED, TextDecoration.BOLD))
                    .append(Component.text(event.getPlayer().getName(), NamedTextColor.DARK_PURPLE, TextDecoration.OBFUSCATED))
                    .append(Component.text(" entrou no mundo CUIDADO ⚠",NamedTextColor.RED, TextDecoration.BOLD))
                    .build());

            return;

        }

        event.joinMessage(Component.text(event.getPlayer().getName() + " entrou no servidor comunitário barrinhense ✔",  NamedTextColor.GREEN));

        Missions missions = TeamTimeCraft.getInstance().getMissions();
        missions.showMissions();

        if (!player.hasPlayedBefore()){
            player.sendTitle(
                    "§6§lComunaBarrinha", // Título grande e colorido
                    "§eTodos precisam de um início!!", // Subtítulo menor
                    10, // fadeIn (ticks)
                    100, // stay (ticks)
                    20 // fadeOut (ticks)
            );
        }
        //this works but is deprecated
        //event.setJoinMessage(ChatColor.GREEN + "Welcome to the server, " + player.getName() + "!");
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        if (player.getName().equalsIgnoreCase("teteufra")){

            event.quitMessage(Component.text()
                    .content("")
                    .append(Component.text(event.getPlayer().getName(), NamedTextColor.DARK_GREEN, TextDecoration.BOLD))
                    .append(Component.text(" conhecido como ", NamedTextColor.GREEN, TextDecoration.BOLD))
                    .append(Component.text(event.getPlayer().getName(), NamedTextColor.DARK_PURPLE, TextDecoration.OBFUSCATED))
                    .append(Component.text(" saiu do mundo ESTAMOS A SALVO",NamedTextColor.GREEN, TextDecoration.BOLD))
                    .build());

            return;
        }

        event.quitMessage(Component.text(event.getPlayer().getName() + " saiu do servidor comunitário barrinhense ☹",  NamedTextColor.RED));

        UUID uuid = event.getPlayer().getUniqueId();
        BossBar bar = BossBarTimer.bossBars.remove(uuid);
        if (bar != null) {
            event.getPlayer().hideBossBar(bar);
        }
    }
}
