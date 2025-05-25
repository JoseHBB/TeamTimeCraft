package me.jose.teamTimeCraft;

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
    private final Map<UUID, Integer> totalTime = new HashMap<>();
    private final Map<UUID, LocalDate> lastSeenDate = new HashMap<>();

    public static void load() {
        File file = new File("plugins/TeamTimeCraft/data.ser");
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
    public void save() {
        File file = new File("plugins/TeamTimeCraft/data.ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
            TeamTimeCraft.getInstance().getLogger().info("PlaytimeStorage saved");
        } catch (Exception e) {
            e.printStackTrace();
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
    public void addTime(UUID uuid, int seconds){

        LocalDate today = getTodayInBrazil();
        checkDailyReset(uuid, today);
        dailyTime.put(uuid, getDailyTime(uuid) + seconds);
        totalTime.put(uuid, getTotalTime(uuid) + seconds);

    }
    public void restoreTime(UUID uuid, int seconds){
        dailyTime.put(uuid, getDailyTime(uuid) - seconds);
    }
    public void checkDailyReset(UUID uuid, LocalDate today) {

        LocalDate lastDate = lastSeenDate.get(uuid);
        if (lastDate == null ||
            !lastDate.equals(today)) {

            dailyTime.put(uuid, 0);
            lastSeenDate.put(uuid, today);
        }
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