package me.jose.teamTimeCraft;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (player.getName().equalsIgnoreCase("josehb")){

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

        //this works but is deprecated
        //event.setJoinMessage(ChatColor.GREEN + "Welcome to the server, " + player.getName() + "!");
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        if (player.getName().equalsIgnoreCase("josehb")){

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
    }
}
