package me.jose.teamTimeCraft;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TablistUtil {
    public static void atualizarTab(Player player, String tempoJogado) {
        // Obtém o Scoreboard atual do jogador
        Scoreboard scoreboard = player.getScoreboard();

        // Configura o Header (cabeçalho) e Footer (rodapé) da tablist
        String horaAtual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String footer = "§7Horário: §f" + horaAtual + " §7| Jogadores Online: §f" + Bukkit.getOnlinePlayers().size()
                + "\n§7Horas Jogadas: §b" + tempoJogado;

        player.sendPlayerListHeaderAndFooter(
                Component.text("§6§l[ComunaBarrinha]"), // Cabeçalho personalizado
                Component.text(footer)                 // Rodapé com o timer
        );

        // Evita interferir no nome do jogador
        // Apenas cria ou verifica a equipe sem adicionar prefixos/sufixos
        String teamName = "tab_" + player.getName();
        if (teamName.length() > 16) teamName = teamName.substring(0, 16);

        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            // Registra uma nova equipe, mas sem alterar o nome do jogador no mundo/chat
            team = scoreboard.registerNewTeam(teamName);
        }

        // Limpa prefixo e sufixo para evitar interferências nos nomes
        team.prefix(Component.text(""));
        team.suffix(Component.text(""));

        // Garante que o jogador está na equipe (até para controle futuro)
        team.addEntry(player.getName());

        // Configura novamente o Scoreboard do jogador (boas práticas)
        player.setScoreboard(scoreboard);
    }
}

//package me.jose.teamTimeCraft;
//
//import net.kyori.adventure.text.Component;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//import org.bukkit.scoreboard.Scoreboard;
//import org.bukkit.scoreboard.Team;
//
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//
//public class TablistUtil {
//    public static void atualizarTab(Player player, String tempoJogado) {
//        Scoreboard scoreboard = player.getScoreboard();
//
//        // Cria uma equipe única para cada jogador
//        String teamName = "tab_" + player.getName();
//        if (teamName.length() > 16) teamName = teamName.substring(0, 16);
//
//        Team team = scoreboard.getTeam(teamName);
//        if (team == null) team = scoreboard.registerNewTeam(teamName);
//
//        // Header e Footer da tablist
//        String horaAtual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
//        String footer = "§7Horário: §f" + horaAtual + "§7 Jogadores Online: §f" + Bukkit.getOnlinePlayers().size();
//
//        player.sendPlayerListHeaderAndFooter(
//                Component.text("§6§l[ComunaBarrinha]"),
//                Component.text(footer)
//        );
//
//        // Formata o tempo fixamente (sempre mesmo tamanho: 00h 00m 00s)
//        String tempoFormatado = String.format("%02dh %02dm %02ds",
//                Integer.parseInt(tempoJogado.substring(0, 2)),
//                Integer.parseInt(tempoJogado.substring(4, 6)),
//                Integer.parseInt(tempoJogado.substring(8, 10))
//        );
//
//        // Prefixo com tempo, nome limpo, nada no sufixo
//        team.prefix(Component.text("§fHoras jogadas: "+ "§b" + tempoFormatado + " §f"));
//        team.suffix(Component.text(""));
//
//        team.addEntry(player.getName());
//        player.setScoreboard(scoreboard);
//    }
//}
