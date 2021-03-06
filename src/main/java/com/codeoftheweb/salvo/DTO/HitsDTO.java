package com.codeoftheweb.salvo.DTO;

import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Salvo;
import com.codeoftheweb.salvo.util.Util;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class HitsDTO {

    public List<Map<String, Object>> makeHitsDTO(GamePlayer gamePlayer){
        List<Map<String, Object>>hits = new ArrayList<>();

        List<String>carrierLocations = Util.getLocationByType("carrier",gamePlayer);
        List<String>battleshipLocations = Util.getLocationByType("battleship",gamePlayer);
        List<String>submarineLocations = Util.getLocationByType("submarine",gamePlayer);
        List<String>destroyerLocations = Util.getLocationByType("destroyer",gamePlayer);
        List<String>patrolBoatLocations = Util.getLocationByType("patrolboat",gamePlayer);

        long carrierDamage = 0;
        long battleshipDamage = 0;
        long submarineDamage = 0;
        long destroyerDamage = 0;
        long patrolBoatDamage = 0;

        for(Salvo salvoShot: Util.getOpponent(gamePlayer).get().getSalvoes()){

            Map<String, Object> damagesPerTurn = new LinkedHashMap<>();
            List<String>hitCellsList = new ArrayList<>();
            Map<String, Object> hitsMapPerTurn = new LinkedHashMap<>();


            long missedShots = salvoShot.getLocations().size();
            long carrierHitsInTurn = 0;
            long battleshipHitsInTurn = 0;
            long submarineHitsInTurn = 0;
            long destroyerHitsInTurn = 0;
            long patrolBoatHitsInTurn = 0;

            for(String location: salvoShot.getLocations()){
                if (carrierLocations.contains(location)){
                    carrierDamage++;
                    carrierHitsInTurn++;
                    hitCellsList.add(location);
                    missedShots--;
                }

                if (battleshipLocations.contains(location)){
                    battleshipHitsInTurn++;
                    battleshipDamage++;
                    hitCellsList.add(location);
                    missedShots--;
                }


                if (submarineLocations.contains(location)){
                    submarineDamage++;
                    submarineHitsInTurn++;
                    hitCellsList.add(location);
                    missedShots--;
                }

                if (destroyerLocations.contains(location)){
                    destroyerDamage++;
                    destroyerHitsInTurn++;
                    hitCellsList.add(location);
                    missedShots--;
                }

                if (patrolBoatLocations.contains(location)){
                    patrolBoatDamage++;
                    patrolBoatHitsInTurn++;
                    hitCellsList.add(location);
                    missedShots--;
                }
            }

            damagesPerTurn.put("carrierHits", carrierHitsInTurn);
            damagesPerTurn.put("battleshipHits", battleshipHitsInTurn);
            damagesPerTurn.put("submarineHits", submarineHitsInTurn);
            damagesPerTurn.put("destroyerHits", destroyerHitsInTurn);
            damagesPerTurn.put("patrolboatHits", patrolBoatHitsInTurn);
            hitsMapPerTurn.put("turn", salvoShot.getTurn());
            hitsMapPerTurn.put("hitLocations", hitCellsList);
            hitsMapPerTurn.put("damages", damagesPerTurn);
            hitsMapPerTurn.put("missed", missedShots);
            hits.add(hitsMapPerTurn);
            damagesPerTurn.put("carrier", carrierDamage);
            damagesPerTurn.put("battleship", battleshipDamage);
            damagesPerTurn.put("submarine", submarineDamage);
            damagesPerTurn.put("destroyer", destroyerDamage);
            damagesPerTurn.put("patrolboat", patrolBoatDamage);
        }

        return hits;
    }

    public int makeDagame(GamePlayer gamePlayer){

        List<String> carrierLocations = Util.getLocationByType("carrier", gamePlayer);
        List<String> battleshipLocations = Util.getLocationByType("battleship", gamePlayer);
        List<String> submarineLocations = Util.getLocationByType("submarine", gamePlayer);
        List<String> destroyerLocations = Util.getLocationByType("destroyer",gamePlayer);
        List<String> patrolBoatLocations = Util.getLocationByType("patrolboat", gamePlayer);

        int countImpact = 0;

        for(Salvo salvoShot: Util.getOpponent(gamePlayer).get().getSalvoes()){

            for(String location: salvoShot.getLocations()){
                if (carrierLocations.contains(location)){
                    countImpact++;
                }
                if (battleshipLocations.contains(location)){
                    countImpact++;
                }
                if (submarineLocations.contains(location)){
                    countImpact++;
                }
                if (destroyerLocations.contains(location)){
                    countImpact++;
                }
                if (patrolBoatLocations.contains(location)){
                    countImpact++;
                }
            }

        }
        return countImpact++;

    }
}
