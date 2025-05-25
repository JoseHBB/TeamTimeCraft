package me.jose.teamTimeCraft;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TeamTimeCraft extends JavaPlugin {
    private static TeamTimeCraft instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        saveDefaultConfig();

        PlayTimeStorage.load();
        BossBarTimer.Start(this);

        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);
        Objects.requireNonNull(getCommand("Playtime")).setExecutor(new PlaytimeCommand());
        Objects.requireNonNull(getCommand("AllPlaytime")).setExecutor(new AllPlaytimeCommand());

        getServer().getScheduler().runTaskTimer(
                this,
                () -> PlayTimeStorage.getInstance().save(),
                1200L, // Delay inicial (1 minuto)
                1200L  // Intervalo entre execuções (1 minuto)
        );

        getLogger().info("Plugin carregado com sucesso!");
    }

    @Override
    public void onDisable() {
        // Chama o método que salva os tempos
        PlayTimeStorage.getInstance().save();
    }

    public static TeamTimeCraft getInstance() {
        return instance;
    }
}
