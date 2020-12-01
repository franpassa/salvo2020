package com.codeoftheweb.salvo.DTO;

import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GameDTO {

    Game game;
    GamePlayerDTO gamePlayerDTO;

    public GameDTO(Game game) { this.game = game; }

    public Game getGame() { return game; }

    public void setGame(Game game) { this.game = game; }

    public Map<String,Object> makeGameDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();

        dto.put("id",game.getid());
        dto.put("created",game.getCreationDate());

        Set<GamePlayer> gamePlayers = game.getGamePlayers();

        dto.put("gamePlayers",gamePlayers
                .stream()
                .map(gamePlayer -> {
                    GamePlayerDTO gamePlayerDTO = new GamePlayerDTO(gamePlayer);
                    return gamePlayerDTO.makeGamePlayerDTO();})
                .collect(Collectors.toList()));

        dto.put("scores",game.getScores()
                .stream()
                .map(score -> {
                    ScoreDTO pDTO = new ScoreDTO(score);
                    return pDTO.makeScoreDTO();})
                .collect(Collectors.toList()));

        return dto;
    }
}
