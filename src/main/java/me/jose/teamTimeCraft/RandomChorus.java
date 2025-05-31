package me.jose.teamTimeCraft;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.Collections;
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
                        if (random.nextInt(1_000_000) == 0){
                            player.getWorld().createExplosion(player.getLocation(), 0F, false, false);
                            ItemStack[] contents = player.getInventory().getContents();
                            Collections.shuffle(Arrays.asList(contents)); // Embaralha o inventário
                            player.getInventory().setContents(contents);
                            player.getWorld().spawnParticle(Particle.HEART, player.getLocation(), 10); // Partículas fofas
                            player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 10);
                            player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
                            player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, player.getLocation(), 30);
                            player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1, 1);
                            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1, 1);
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
