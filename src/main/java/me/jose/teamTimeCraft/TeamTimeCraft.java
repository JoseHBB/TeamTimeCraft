package me.jose.teamTimeCraft;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import java.util.Objects;

public final class TeamTimeCraft extends JavaPlugin {
    private static TeamTimeCraft instance;
    private Missions missions;
    public static boolean toggle = true;
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        saveDefaultConfig();

        PlayTimeStorage.load();
        BossBarTimer.start(this);

        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new EEListeners(getConfig()), this);
        getServer().getPluginManager().registerEvents(new AdvancementListener(), this);
        getServer().getPluginManager().registerEvents(new RandomChorus(), this);


        Objects.requireNonNull(getCommand("playtime")).setExecutor(new PlaytimeCommand());
        Objects.requireNonNull(getCommand("allplaytime")).setExecutor(new AllPlaytimeCommand());
        Objects.requireNonNull(getCommand("toggleplaytime")).setExecutor(new TogglePlaytimeCommand());
        Objects.requireNonNull(getCommand("restoreplaytime")).setExecutor(new RestorePlaytimeCommand());
        Objects.requireNonNull(getCommand("resetremainingtime")).setExecutor(new ResetRemainingTimeCommand());
        Objects.requireNonNull(getCommand("missionsupdate")).setExecutor(new MissionUpdateCommand());

        // Instanciando as missões
        missions = new Missions();
        // Já busca as missões ao iniciar
        missions.getMissionsAsync(this);

        getServer().getScheduler().runTaskTimer(this,
            () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    String time = Utils.formatTime(PlayTimeStorage.getInstance().getTotalTime(p.getUniqueId()));
                    TablistUtil.atualizarTab(p, time);
                }
            },
            0L, 20L
        );

        getServer().getScheduler().runTaskTimer(
                this,
                () -> missions.getMissionsAsync(this),
                600L, // Delay inicial (5 minutos)
                6000L  // Intervalo entre execuções (5 minutos)
        );

        getServer().getScheduler().runTaskTimer(
                this,
                () ->
                {
                    PlayTimeStorage.getInstance().save();
                },
                1200L, // Delay inicial (1 minuto)
                1200L  // Intervalo entre execuções (1 minuto)
        );

        new DailyResetTask(this).start();

        getLogger().info("Plugin carregado com sucesso!");
    }

    @Override
    public void onDisable() {
        // Chama o metodo que salva os tempos
        PlayTimeStorage.getInstance().save();
    }

    public static TeamTimeCraft getInstance() {
        return instance;
    }
    public Missions getMissions() {
        return missions;
    }
    public static boolean getToggle() {
        return toggle;
    }
}
