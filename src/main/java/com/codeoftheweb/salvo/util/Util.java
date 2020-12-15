package com.codeoftheweb.salvo.util;

import com.codeoftheweb.salvo.DTO.HitsDTO;
import com.codeoftheweb.salvo.model.GamePlayer;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class Util {

    public static Map<String,Object> makeMap(String key, Object value){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }

    public static boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public static Optional<GamePlayer> getOpponent(GamePlayer gamePlayer){
        return gamePlayer.getGame().getGamePlayers()
                .stream()
                .filter(gp -> gp.getid() != gamePlayer.getid())
                .findFirst();
    }

    public static String gameState(GamePlayer gamePlayer){

        if(gamePlayer.getGame().getGamePlayers().size()==2) {
            HitsDTO dtoHit= new HitsDTO();
            int mySelfImpact= dtoHit.makeDagame(gamePlayer);
            int opponentImpact= dtoHit.makeDagame(getOpponent(gamePlayer).get());
            if(mySelfImpact==17 && opponentImpact==17){
                return  "TIE";
            }else if(mySelfImpact==17){
                return "LOSE";
            }else if(opponentImpact==17){
                return "WON";
            }
        }
        if (gamePlayer.getShips().isEmpty()) {
            return "PLACESHIPS";
        }else if( (gamePlayer.getGame().getGamePlayers().size()==1) || getOpponent(gamePlayer).get().getShips().size()==0 ){
            return "WAITINGFOROPP";
        }else if(gamePlayer.getGame().getGamePlayers().size()==2  && gamePlayer.getSalvoes().size()>getOpponent(gamePlayer).get().getSalvoes().size()) {
            return "WAIT";
        }else{
            return "PLAY";
        }
    }

    public static Map<String, Integer> shipTypes = Stream.of(
            new Object[][]{
                    {"carrier", 5},
                    {"battleship", 4},
                    {"submarine", 3},
                    {"destroyer", 3},
                    {"patrolboat", 2}
            }).collect(toMap(data -> (String)data[0], data -> (Integer)data[1]));

    public static List<String> getLocationByType(String type, GamePlayer gamePlayer) {
        return gamePlayer.getShips().size() == 0 ? new ArrayList<>() : gamePlayer.getShips().stream().filter(ship -> ship.getType().equals(type)).findFirst().get().getLocations();
    }
}
