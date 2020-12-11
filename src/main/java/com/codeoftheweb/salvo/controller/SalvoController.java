package com.codeoftheweb.salvo.controller;


import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.SalvoRepository;
import com.codeoftheweb.salvo.repository.ShipRepository;
import com.codeoftheweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GamePlayerRepository repoGamePlayers;

    @Autowired
    private PlayerRepository repoPlayers;

    @Autowired
    private ShipRepository repoShips;

    @Autowired
    private SalvoRepository repoSalvoes;

    @RequestMapping(value = "/games/players/{gamePlayerId}/salvoes", method= RequestMethod.POST)
    ResponseEntity<Map<String,Object>> addSalvo(@PathVariable long gamePlayerId, @RequestBody Salvo salvo, Authentication authentication){
        GamePlayer gamePlayer = repoGamePlayers.findById(gamePlayerId).orElse(null);
        Player player = repoPlayers.findByEmail(authentication.getName());
        GamePlayer opponent;

        if(Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error","Not logged in"), HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer == null){
            return new ResponseEntity<>(Util.makeMap("error","There is no player with given ID"), HttpStatus.UNAUTHORIZED);
        }

        if(Util.getOpponent(gamePlayer).isPresent()){
            opponent = Util.getOpponent(gamePlayer).get();
        } else {
            return new ResponseEntity<>(Util.makeMap("error","There's no opponent yet."), HttpStatus.FORBIDDEN);
        }

        long myTurn = gamePlayer.getSalvoes().size();
        long enemyTurn = opponent.getSalvoes().size();

        if(myTurn > enemyTurn){
            return new ResponseEntity<>(Util.makeMap("error","Its your opponent's turn."), HttpStatus.FORBIDDEN);
        }

        salvo.setTurn(myTurn+1);
        gamePlayer.addSalvo(salvo);
        repoGamePlayers.save(gamePlayer);
        repoSalvoes.save(salvo);

        return new ResponseEntity<>(Util.makeMap("OK","Added salvo."),HttpStatus.CREATED);
    }
}
