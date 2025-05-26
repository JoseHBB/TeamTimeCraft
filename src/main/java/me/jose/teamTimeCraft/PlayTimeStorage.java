package me.jose.teamTimeCraft;

import org.bukkit.Bukkit;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayTimeStorage implements Serializable  {
    @Serial
    private static final long serialVersionUID = 1L;

    private static PlayTimeStorage instance;
    private final Map<UUID, Integer> dailyTime = new HashMap<>();
    private final Map<UUID, Integer> remainingTime = new HashMap<>();

    private final Map<UUID, Integer> totalTime = new HashMap<>();
    private final Map<UUID, LocalDate> lastSeenDate = new HashMap<>();
    private final Map<UUID, String> playerNames = new HashMap<>();

    public static void load() {
        File file = new File(TeamTimeCraft.getInstance().getDataFolder(), "data.ser");
        file.getParentFile().mkdirs();

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {

                instance = (PlayTimeStorage) ois.readObject();
                TeamTimeCraft.getInstance().getLogger().info("PlaytimeStorage loaded");

            } catch (Exception e) {

                e.printStackTrace();
                instance = new PlayTimeStorage();

            }
        } else {
            instance = new PlayTimeStorage();
        }
    }
    public void saveAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(TeamTimeCraft.getInstance(), this::save);
    }
    public void save() {

        File file = new File(TeamTimeCraft.getInstance().getDataFolder(), "data.ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
            TeamTimeCraft.getInstance().getLogger().info("PlaytimeStorage saved");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void resetAllPlayTimes() {
        LocalDate today = getTodayInBrazil();
        for (UUID uuid : dailyTime.keySet()) {
            setRemainingTime(uuid, ConfigManager.getMaxPlaytimeSeconds());
            lastSeenDate.put(uuid, today);
        }
    }
    public static PlayTimeStorage getInstance() {
        return instance;
    }
    public int getDailyTime(UUID uuid) {
        return dailyTime.getOrDefault(uuid, 0);
    }
    public int getTotalTime(UUID uuid) {
        return totalTime.getOrDefault(uuid, 0);
    }
    public int getRemainingTime(UUID uuid) {
        return remainingTime.getOrDefault(uuid, ConfigManager.getMaxPlaytimeSeconds());
    }
    public void setRemainingTime(UUID uuid, int seconds) {
        remainingTime.put(uuid, seconds);
    }
    public void addTime(UUID uuid, int seconds){

        LocalDate today = getTodayInBrazil();
        checkDailyReset(uuid, today);
        dailyTime.put(uuid, getDailyTime(uuid) + seconds);
        totalTime.put(uuid, getTotalTime(uuid) + seconds);

        int remaining = getRemainingTime(uuid) - seconds;
        setRemainingTime(uuid, Math.max(0, remaining)); // nunca negativo
    }
    public void addTimeWithoutRemoving(UUID uuid, int seconds){

        dailyTime.put(uuid, getDailyTime(uuid) + seconds);
        totalTime.put(uuid, getTotalTime(uuid) + seconds);
    }
    public void restoreTime(UUID uuid, int seconds){
        setRemainingTime(uuid, Math.max(0, getRemainingTime(uuid) + seconds));
    }
    public void checkDailyReset(UUID uuid, LocalDate today) {

        LocalDate lastDate = lastSeenDate.get(uuid);
        if (lastDate == null ||
            !lastDate.equals(today)) {

            dailyTime.put(uuid, 0);
            remainingTime.put(uuid, ConfigManager.getMaxPlaytimeSeconds());
            lastSeenDate.put(uuid, today);
        }
    }
    public void setPlayerName(UUID uuid, String name) {
        playerNames.put(uuid, name);
    }
    public String getPlayerName(UUID uuid) {
        return playerNames.get(uuid);
    }
    public Map<UUID, String> getPlayerNamesMap() {
        return playerNames;
    }
    public Map<UUID, Integer> getDailyTimeMap() {
        return dailyTime;
    }
    public Map<UUID, Integer> getTotalTimeMap() {
        return this.totalTime;
    }
    public static LocalDate getTodayInBrazil() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toLocalDate();
    }
}