package com.codeoftheweb.salvo.DTO;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Salvo;
import java.util.LinkedHashMap;
import java.util.Map;

public class SalvoDTO {

    Salvo salvo;

    public SalvoDTO(Salvo salvo) { this.salvo = salvo; }

    public Salvo getSalvo() { return salvo; }
    public void setSalvo(Salvo salvo) { this.salvo = salvo; }

    public Map<String,Object> makeSalvoDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();

        GamePlayer gamePlayer = salvo.getGamePlayer();

        dto.put("turn",salvo.getTurn());
        dto.put("player", gamePlayer.getPlayer().getid());
        dto.put("locations",salvo.getLocations());

        return dto;
    }
}
