package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.DTO.GameDTO;
import com.codeoftheweb.salvo.DTO.GamePlayerDTO;
import com.codeoftheweb.salvo.DTO.PlayerDTO;
import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.util.Util;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.Instant.now;
import static org.apache.logging.log4j.ThreadContext.isEmpty;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private GamePlayerRepository repoGamePlayers;

    @Autowired
    private GameRepository repoGames;

    @Autowired
    private PlayerRepository repoPlayers;

    @RequestMapping("/gamePlayers/{id}")
    public Map<String,Object> getGamePlayer(@PathVariable long id){
        GamePlayerDTO gpDTO = new GamePlayerDTO(repoGamePlayers.getOne(id));
        return gpDTO.makeGamePlayerDTO();
    }

    @RequestMapping(path = "/game_view/{id}", method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> getGameView(@PathVariable long id, Authentication authentication){
        Player player = repoPlayers.findByEmail(authentication.getName());
        GamePlayer gamePlayer = repoGamePlayers.getOne(id);

        if(Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error","Not logged in"), HttpStatus.UNAUTHORIZED);
        }

        if(player.getid() != gamePlayer.getPlayer().getid()){
            return new ResponseEntity<>(Util.makeMap("error","No vale pantallear salame"), HttpStatus.UNAUTHORIZED);
        }

        GamePlayerDTO gpDTO = new GamePlayerDTO(gamePlayer);
        return new ResponseEntity<>(gpDTO.makeGameViewDTO(), HttpStatus.ACCEPTED);
    }

    @RequestMapping("/leaderboard")
    public List<Map<String,Object>> getLeaderboard(){
        return repoPlayers.findAll()
                .stream()
                .map(player -> {
                    PlayerDTO pDTO = new PlayerDTO(player);
                    return pDTO.makePlayerScoreDTO();})
                .collect(Collectors.toList());
    }

    @RequestMapping(path = "/game/{game_id}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> joinGame(@PathVariable long idGame, Authentication authentication){

        if(Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Is guest."), HttpStatus.UNAUTHORIZED);
        }

        Player player = repoPlayers.findByEmail(authentication.getName());

        Game gameToJoin = repoGames.getOne(idGame);

        if(gameToJoin == null){
            return new ResponseEntity<>(Util.makeMap("error", "No such game."), HttpStatus.FORBIDDEN);
        }

        long gamePlayerCount = gameToJoin.getGamePlayers().size();

        if(gamePlayerCount == 1){
            GamePlayer gamePlayer = new GamePlayer(player,gameToJoin);
            repoGamePlayers.save(gamePlayer);
            return new ResponseEntity<>(Util.makeMap("gpid",gamePlayer.getid()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(Util.makeMap("error","Game is full!"), HttpStatus.FORBIDDEN);
        }
    }
}
