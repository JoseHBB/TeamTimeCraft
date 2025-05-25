package me.jose.teamTimeCraft;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
                int played = PlayTimeStorage.getInstance().getDailyTime(uuid);
                int remaining = Math.max(0, maxSeconds - played);
                float progress = Math.min(1.0f, (float) remaining / maxSeconds);

                BossBar.Color color = BossBar.Color.GREEN;
                if (progress <= 0.25) {
                    color = BossBar.Color.RED;
                } else if (progress <= 0.5) {
                    color = BossBar.Color.YELLOW;
                }

                BossBar bar = bossBars.get(uuid);

                Component barName = Component.empty()
                        .append(countComponent)
                        .append(Component.text(" - Tempo restante: " + formatTime(remaining), NamedTextColor.WHITE));


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
    private static String formatTime(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        return String.format("%02dh %02dm %02ds", h, m, s);

    }
}
