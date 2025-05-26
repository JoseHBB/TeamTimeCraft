package me.jose.teamTimeCraft;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarTimer {
    public static final Map<UUID, BossBar> bossBars = new HashMap<>();
    public static void start(Plugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            int maxSeconds = ConfigManager.getMaxPlaytimeSeconds();
            int onlineLimit = ConfigManager.getMaxPlayerCount();
            boolean shouldCount = Bukkit.getOnlinePlayers().size() < onlineLimit;
            boolean toggle = TeamTimeCraft.getToggle();

            String countText = (shouldCount && toggle) ? "CONTANDO" : "PARADO";
            Component countComponent = Component.text(countText)
                    .color((shouldCount && toggle) ? NamedTextColor.RED : NamedTextColor.GREEN);

            for (Player p : Bukkit.getOnlinePlayers()) {
                UUID uuid = p.getUniqueId();
                int remaining = PlayTimeStorage.getInstance().getRemainingTime(uuid);
                float progress = Math.min(1.0f, (float) remaining / maxSeconds);

                BossBar.Color color = BossBar.Color.GREEN;
                if (progress <= 0.25) {
                    color = BossBar.Color.RED;
                } else if (progress <= 0.5) {
                    color = BossBar.Color.YELLOW;
                }

                if (remaining <= 10) {showCenterCountdown(p, remaining, null);}

                BossBar bar = bossBars.get(uuid);

                Component barName = Component.empty()
                        .append(countComponent)
                        .append(Component.text(" - Tempo restante: " + Utils.formatTime(remaining), NamedTextColor.WHITE));


                if (bar == null) {

                    bar = BossBar.bossBar(
                            barName,
                            progress,
                            color,
                            BossBar.Overlay.NOTCHED_12
                    );
                    p.showBossBar(bar);
                    bossBars.put(uuid, bar);

                } else {

                    bar.progress(progress);
                    bar.name(barName);
                    bar.color(color);
                    p.showBossBar(bar);

                }
                if (shouldCount && toggle) {
                    PlayTimeStorage.getInstance().addTime(p.getUniqueId(), 1);

                    if (remaining == 0) {
                        p.kick(Component.text("Parece que você jogou um pouco de mais hoje!!!"));
                    }
                }
                else {
                    PlayTimeStorage.getInstance().addTimeWithoutRemoving(p.getUniqueId(), 1);
                }
            }

            bossBars.entrySet().removeIf(entry -> {
                Player p = Bukkit.getPlayer(entry.getKey());
                if (p == null || !p.isOnline()) {
                    // Não precisa chamar hideBossBar pois o jogador já saiu
                    return true;
                }
                return false;
            });
        }, 0, 20L);
    }
    /**
     * Mostra uma contagem regressiva central na tela do jogador, tipo Build Battle.
     * @param player Jogador que vai receber o timer.
     * @param seconds Quantidade de segundos da contagem.
     * @param onFinish Runnable opcional para rodar ao terminar a contagem.
     */
    public static void showCenterCountdown(Player player, int seconds, Runnable onFinish) {
        new BukkitRunnable() {
            int timer = seconds;
            @Override
            public void run() {
                if (timer > 0) {
                    // Exibe o tempo restante no centro da tela
                    player.sendTitle("", "§c" + timer, 0, 20, 0);
                    timer--;
                } else {
                    player.resetTitle();
                    if (onFinish != null) onFinish.run();
                    cancel();
                }
            }
        }.runTaskTimer(TeamTimeCraft.getInstance(), 0, 20L);
    }
}
