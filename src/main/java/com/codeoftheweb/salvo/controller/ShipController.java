package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Ship;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ShipRepository;
import com.codeoftheweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ShipController {

    @Autowired
    private GamePlayerRepository repoGamePlayers;

    @Autowired
    private PlayerRepository repoPlayers;

    @Autowired
    private ShipRepository repoShips;

    @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method= RequestMethod.POST)
    ResponseEntity<Map<String,Object>> addShips(@PathVariable long gamePlayerId, @RequestBody Set<Ship> ships, Authentication authentication){
        GamePlayer gamePlayer = repoGamePlayers.findById(gamePlayerId).orElse(null);
        Player player = repoPlayers.findByEmail(authentication.getName());

        if(Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error","Not logged in"), HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer == null){
            return new ResponseEntity<>(Util.makeMap("error","There is no player with given ID"), HttpStatus.UNAUTHORIZED);
        }

        if(!(gamePlayer.getPlayer().getid() == player.getid())){
            return new ResponseEntity<>(Util.makeMap("error","This is not your board"), HttpStatus.FORBIDDEN);
        }

        if(!gamePlayer.getShips().isEmpty()){
            return new ResponseEntity<>(Util.makeMap("error","User already have ships"), HttpStatus.FORBIDDEN);
        }

        if(ships.size() != 5){
            return new ResponseEntity<>(Util.makeMap("error","You need five ships"), HttpStatus.FORBIDDEN);
        }

        List<Ship> newShips = ships
                .stream()
                .map(ship -> {
                    ship.setGameplayer(gamePlayer);
                    return ship;})
                .collect(Collectors.toList());

        repoShips.saveAll(newShips);

        return new ResponseEntity<>(Util.makeMap("OK","Added ships."),HttpStatus.CREATED);
    }
}
