package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.DTO.GameDTO;
import com.codeoftheweb.salvo.DTO.GamePlayerDTO;
import com.codeoftheweb.salvo.DTO.PlayerDTO;
import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Score;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ScoreRepository;
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

import java.time.Instant;
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

    @Autowired
    private ScoreRepository repoScores;

    @RequestMapping("/gamePlayers/{id}")
    public Map<String,Object> getGamePlayer(@PathVariable long id){
        GamePlayerDTO gpDTO = new GamePlayerDTO(repoGamePlayers.getOne(id));
        return gpDTO.makeGamePlayerDTO();
    }

    /*@RequestMapping(path = "/game_view/{id}", method = RequestMethod.GET)
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
    }*/

    @RequestMapping(path = "/game_view/{ID}", method = RequestMethod.GET )
    public ResponseEntity<Map<String, Object>> getGamePlayerView(@PathVariable long ID, Authentication authentication) {
        if (Util.isGuest(authentication)) {
            return new ResponseEntity<>(Util.makeMap("error", "Not Logged in"), HttpStatus.UNAUTHORIZED);
        }
        Long playerLogged = repoPlayers.findByEmail(authentication.getName()).getid();
        Long playerCheck = repoGamePlayers.getOne(ID).getPlayer().getid();

        if (playerLogged != playerCheck){
            return new ResponseEntity<>(Util.makeMap("error", "This is not your game"), HttpStatus.FORBIDDEN);
        }

        GamePlayer gamePlayer = repoGamePlayers.getOne(ID);

        GamePlayerDTO dtoGame_View = new GamePlayerDTO(gamePlayer);

        if(Util.gameState(gamePlayer) == "WON"){
            if(gamePlayer.getGame().getScores().size()<2) {
                Set<Score> scores = new HashSet<>();
                Score score1 = new Score();
                score1.setPlayer(gamePlayer.getPlayer());
                score1.setGame(gamePlayer.getGame());
                score1.setFinishDate(LocalDateTime.now());
                score1.setScore(1D);
                repoScores.save(score1);
                Score score2 = new Score();
                score2.setPlayer(Util.getOpponent(gamePlayer).get().getPlayer());
                score2.setGame(gamePlayer.getGame());
                score2.setFinishDate(LocalDateTime.now());
                score2.setScore(0D);
                repoScores.save(score2);
                scores.add(score1);
                scores.add(score2);

                Util.getOpponent(gamePlayer).get().getGame().setScores(scores);
            }
        }
        if(Util.gameState(gamePlayer) == "TIE"){
            if(gamePlayer.getGame().getScores().size()<2) {
                Set<Score> scores = new HashSet<Score>();
                Score score1 = new Score();
                score1.setPlayer(gamePlayer.getPlayer());
                score1.setGame(gamePlayer.getGame());
                score1.setFinishDate(LocalDateTime.now());
                score1.setScore(0.5D);
                repoScores.save(score1);
                Score score2 = new Score();
                score2.setPlayer(Util.getOpponent(gamePlayer).get().getPlayer());
                score2.setGame(gamePlayer.getGame());
                score2.setFinishDate(LocalDateTime.now());
                score2.setScore(0.5D);
                repoScores.save(score2);
                scores.add(score1);
                scores.add(score2);

                Util.getOpponent(gamePlayer).get().getGame().setScores(scores);
            }
        }
        return new ResponseEntity<>(dtoGame_View.makeGameViewDTO(), HttpStatus.ACCEPTED);
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
    public ResponseEntity<Map<String,Object>> joinGame(@PathVariable long game_id, Authentication authentication){

        if(Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Is guest."), HttpStatus.UNAUTHORIZED);
        }

        Player player = repoPlayers.findByEmail(authentication.getName());

        Game gameToJoin = repoGames.getOne(game_id);

        if(gameToJoin == null){
            return new ResponseEntity<>(Util.makeMap("error", "No such game."), HttpStatus.FORBIDDEN);
        }

        long gamePlayerCount = gameToJoin.getGamePlayers().size();

        if(gamePlayerCount == 1){
            GamePlayer gamePlayer = new GamePlayer(player,gameToJoin);
            repoGamePlayers.save(gamePlayer);
            return new ResponseEntity<>(Util.makeMap("id",gamePlayer.getid()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(Util.makeMap("error","Game is full!"), HttpStatus.FORBIDDEN);
        }
    }
}
