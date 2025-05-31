package me.jose.teamTimeCraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;
import java.util.UUID;

public class RandomChorus implements Listener {

    private final Random random = new Random();
    private final String playerName = "TeteuFRA";
    private BukkitTask chorusTask;
    private UUID jogadorUUID = null;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (player.getName().equalsIgnoreCase(playerName)){
            jogadorUUID = player.getUniqueId();
            chorusTask = new BukkitRunnable() {
                @Override
                public void run() {
                    Player target = Bukkit.getPlayer(jogadorUUID);

                    if (target != null && target.isOnline()){
                        if (random.nextInt(10) == 0){
                            target.teleport(target.getLocation().add(
                                    random.nextInt(16) - 8,
                                    0,
                                    random.nextInt(16) - 8
                            ));
                            target.sendMessage("Havia uma chande de 1 milhão disso acontecer com você TeteuFRA!");
                        }
                    }
                }
            }.runTaskTimer(TeamTimeCraft.getInstance(), 20, 20);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (player.getName().equalsIgnoreCase(playerName)){
            if (chorusTask != null){
                chorusTask.cancel();
                chorusTask = null;
            }
            jogadorUUID = null;
        }
    }
}
