package com.codeoftheweb.salvo.util;

import com.codeoftheweb.salvo.model.GamePlayer;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

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
}
