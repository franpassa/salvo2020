package com.codeoftheweb.salvo.DTO;

import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Salvo;
import com.codeoftheweb.salvo.model.Score;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScoreDTO {

    Score score;

    public ScoreDTO(Score score) { this.score = score; }

    public Score getScore() { return score; }
    public void setScore(Score score) { this.score = score; }

    public Map<String,Object> makeScoreDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();

        dto.put("player",score.getPlayer().getid());
        dto.put("score", score.getScore());
        dto.put("finishDate",score.getFinishDate());

        return dto;
    }
}
