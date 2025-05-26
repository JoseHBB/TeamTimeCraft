package me.jose.teamTimeCraft;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class EEListeners implements Listener {
    private boolean flint = false;
    private boolean chicken = false;
    private boolean chickenJockey = false;
    private final FileConfiguration config;

    public EEListeners(FileConfiguration config) {
        this.config = config;

        if (config.isSet("foundFlint")) {
            flint = config.getBoolean("foundFlint");
        }
        if (config.isSet("foundChicken")) {
            chicken = config.getBoolean("foundChicken");
        }
        if (config.isSet("foundChickenJockey")) {
            chickenJockey = config.getBoolean("foundChickenJockey");
        }
    }
    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event){
        if (!flint){
            Player player = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;

            if (player == null) return;

            if (event.getItem().getItemStack().getType() == Material.FLINT_AND_STEEL) {
            Bukkit.broadcast(Component.text("§e§lPEDERNEIRA"));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            flint = true;
            saveData();
            }
        }
    }
    @EventHandler
    public void onFurnaceCook(FurnaceSmeltEvent event) {
        // Verifica o resultado do cozimento
        if (!chicken){
            ItemStack result = event.getResult();
            if (result.getType() == Material.COOKED_CHICKEN) { // Se o item cozido é frango frito
                Block furnaceBlock = event.getBlock();

                // Verifica se o bloco é realmente uma fornalha
                if (furnaceBlock.getState() instanceof Furnace furnaceState) {
                    // Obtém o combustível atualmente na fornalha
                    ItemStack fuel = furnaceState.getInventory().getItem(1); // Slot 1 é o slot de combustível

                    if (fuel != null && fuel.getType() == Material.BUCKET) { // Combustível é lava?
                        // Reaja ao frango frito feito com lava
                        Bukkit.broadcast(Component.text("§e§lLAVA CHICKEN"));
                        chicken = true;
                        saveData();
                    }
                }
            }
        }
    }
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!chickenJockey) {
            LivingEntity entity = event.getEntity();
            // Verifica se a entidade tem um passageiro
            if (!entity.getPassengers().isEmpty()) {
                // Descobre quem é o passageiro
                if (entity instanceof org.bukkit.entity.Chicken) { // Certifica-se de que é um Jockey

                    // Agendamento para checagem de proximidade de jogadores
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Location jockeyLocation = entity.getLocation();

                            for (Player player : Bukkit.getOnlinePlayers()) {
                                double distance = player.getLocation().distance(jockeyLocation);
                                if (distance <= 10) {
                                    Bukkit.broadcast(Component.text("§c§lJOCKEY DE GALINHA"));
                                    chickenJockey = true;
                                    saveData();

                                    this.cancel(); // Cancela esta task
                                    break;
                                }
                            }
                        }
                    }.runTaskTimer(TeamTimeCraft.getInstance(), 0L, 20L); // Executa a cada 20 ticks (1 segundo)
                }
            }
        }
    }
    public void saveData() {
        config.set("foundFlint", flint);
        config.set("foundChicken", chicken);
        config.set("foundChickenJockey", chickenJockey);
        TeamTimeCraft.getInstance().saveConfig();
    }
}
