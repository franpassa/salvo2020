package com.codeoftheweb.salvo.DTO;

import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Salvo;
import com.codeoftheweb.salvo.model.Ship;

import java.util.*;
import java.util.stream.Collectors;

public class GamePlayerDTO {

    GamePlayer gamePlayer;

    public GamePlayer getGamePlayer() { return gamePlayer; }

    public void setGamePlayer(GamePlayer gamePlayer) { this.gamePlayer = gamePlayer; }

    public GamePlayerDTO(GamePlayer gamePlayer) { this.gamePlayer = gamePlayer; }

    public Map<String, Object> makeGamePlayerDTO() {
        Map<String,Object> dto = new LinkedHashMap<>();

        PlayerDTO pDTO = new PlayerDTO(gamePlayer.getPlayer());

        dto.put("id",gamePlayer.getid());
        dto.put("player", pDTO.makePlayerDTO());

        return dto;
    }

    public Map<String,Object> makeGameViewDTO(){

        GameDTO gDTO = new GameDTO(gamePlayer.getGame());

        Map<String,Object> dto = gDTO.makeGameDTO();

        Map<String, Object> hits = new LinkedHashMap<>();

        hits.put("self", new ArrayList<>());

        hits.put("opponent", new ArrayList<>());

        List<Ship> ships = gamePlayer.getShips().stream().collect(Collectors.toList());

        Set<GamePlayer> gamePlayers = gamePlayer.getGame().getGamePlayers();

        List<Salvo> salvoes = gamePlayers
                .stream()
                .flatMap(gp -> gp.getSalvoes().stream())
                .collect(Collectors.toList());

        dto.put("ships",ships
                .stream()
                .map(ship -> {
                    ShipDTO shipDTO = new ShipDTO(ship);
                    return shipDTO.makeShipDTO();})
                .collect(Collectors.toList()));

        dto.put("salvoes",salvoes
                .stream()
                .map(salvo -> {
                    SalvoDTO salvoDTO = new SalvoDTO(salvo);
                    return salvoDTO.makeSalvoDTO();
                }).collect(Collectors.toList()));

        dto.put("hits", hits);
        dto.put("gameState", "PLACESHIPS");

        return dto;
    }
}
