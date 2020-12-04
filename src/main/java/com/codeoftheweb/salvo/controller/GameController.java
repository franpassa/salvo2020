package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.DTO.GameDTO;
import com.codeoftheweb.salvo.DTO.PlayerDTO;
import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private GameRepository repoGames;

    @Autowired
    private GamePlayerRepository repoGamePlayers;

    @Autowired
    private PlayerRepository repoPlayers;

    @RequestMapping(path = "/games", method = RequestMethod.GET)
    public Map<String,Object> getGameAll(Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>();

        if(Util.isGuest(authentication)){
            dto.put("player","Guest");
        } else {
            Player player  = repoPlayers.findByEmail(authentication.getName());
            PlayerDTO playerDTO   =   new PlayerDTO(player);
            dto.put("player", playerDTO.makePlayerDTO());
        }

        dto.put("games", repoGames.findAll()
                .stream()
                .map(game -> {
                    GameDTO gDTO = new GameDTO(game);
                    return gDTO.makeGameDTO();})
                .collect(Collectors.toList()));

        return dto;
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> createNewGame(Authentication authentication){
        Map<String,Object> dto = new LinkedHashMap<>();

        if(Util.isGuest(authentication)){
            return new ResponseEntity<>("No está autorizado", HttpStatus.UNAUTHORIZED);
        }

        Player player = repoPlayers.findByEmail(authentication.getName());

        if(player == null){
            return new ResponseEntity<>("No está autorizado", HttpStatus.UNAUTHORIZED);
        }

        Game game = repoGames.save(new Game());

        GamePlayer gamePlayer = repoGamePlayers.save(new GamePlayer(player,game));

        return new ResponseEntity<>(Util.makeMap("id",gamePlayer.getid()), HttpStatus.CREATED);
    }
}
