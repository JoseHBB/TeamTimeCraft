package me.jose.teamTimeCraft;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import static org.bukkit.scoreboard.Criteria.DUMMY;

public class Missions {
    JSONArray missions;
    public void getMissionsAsync(JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URI uri = URI.create("API_TOKEN");
                URL url = uri.toURL();
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                InputStreamReader reader = new InputStreamReader(con.getInputStream());
                JSONArray fetchedMissions = new JSONArray(new JSONTokener(reader));

                // Após baixar, atualize referencia das missões na thread principal
                Bukkit.getScheduler().runTask(plugin, () -> {
                    this.missions = fetchedMissions;
                    showMissions();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public void showMissions() {
        if (missions == null) return;
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective(
                "missions",
                DUMMY,
                Component.text("[ Missões ]").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD)
        );

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        int max = Math.min(missions.length(), 15); // Máximo permitido no sidebar

        int i = 0;

        for (Object obj : missions) {

            JSONObject card = (JSONObject) obj;
            String titulo = (String) card.get("name");
            boolean complete = (boolean) card.get("dueComplete");

            String linha = "§f" + titulo.substring(0, Math.min(titulo.length(), 35))
                    + (complete ? " §a✔" : " §c✖") + "§r "; // Adiciona espaço ou cor extra

            objective.getScore(linha).setScore(max - i); // ainda precisa setScore
            i++;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(scoreboard);
        }
    }
}
