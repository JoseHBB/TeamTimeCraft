package me.jose.teamTimeCraft;

public class Utils {
    public static String formatTime(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        return String.format("%02dh %02dm %02ds", h, m, s);
    }
}
