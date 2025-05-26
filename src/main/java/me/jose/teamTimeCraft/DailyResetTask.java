package me.jose.teamTimeCraft;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.LocalDateTime;

public class DailyResetTask {
    private final JavaPlugin plugin;

    public DailyResetTask(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        long ticksUntilMidnight = getTicksUntilMidnight();
        new BukkitRunnable() {
            @Override
            public void run() {
                // Resetar todos os tempos dos jogadores aqui
                PlayTimeStorage.getInstance().resetAllPlayTimes();
                PlayTimeStorage.getInstance().saveAsync();

                plugin.getLogger().info("Reset diário executado. Próxima execução agendada.");
                // Reagendar para o próximo dia
            }
        }.runTaskTimer(plugin, ticksUntilMidnight , 24 * 60 * 60 * 20);
    }

    private long getTicksUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay();
        Duration duration = Duration.between(now, nextMidnight);
        long seconds = duration.getSeconds();
        return seconds * 20; // 20 ticks = 1 segundo
    }
}
