package com.codeoftheweb.salvo.DTO;

import com.codeoftheweb.salvo.model.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerDTO {

    Player player;

    public PlayerDTO(Player player) {
        this.player = player;
    }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }

    public Map<String, Object> makePlayerDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id",player.getid());
        dto.put("email",player.getEmail());
        dto.put("name",player.getName());

        return dto;
    }

    public Map<String, Object> makePlayerScoreDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        Map<String,Object> score = new LinkedHashMap<>();
        dto.put("id",player.getid());
        dto.put("email",player.getEmail());
        dto.put("name",player.getName());
        dto.put("score",score);

        score.put("total", player.getTotalScore());
        score.put("win", player.getWinScore());
        score.put("lost", player.getLostScore());
        score.put("tied", player.getTiedScore());

        return dto;
    }
}
