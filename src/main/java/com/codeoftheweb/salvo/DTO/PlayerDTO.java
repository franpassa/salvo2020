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
}
