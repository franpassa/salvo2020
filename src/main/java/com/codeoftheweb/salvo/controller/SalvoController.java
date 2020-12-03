package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.DTO.GameDTO;
import com.codeoftheweb.salvo.DTO.GamePlayerDTO;
import com.codeoftheweb.salvo.DTO.PlayerDTO;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GamePlayerRepository repoGamePlayers;

    @Autowired
    private GameRepository repoGames;

    @Autowired
    private PlayerRepository repoPlayers;

    @RequestMapping("/players")
    public  List<Map<String,Object>> getPlayerAll(){
        return repoPlayers.findAll()
                .stream()
                .map(player -> {
                    PlayerDTO pDTO = new PlayerDTO(player);
                    return pDTO.makePlayerDTO();})
                .collect(Collectors.toList());
    }

    @RequestMapping("/games")
    public Map<String,Object> getGameAll(Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<>();

        if(isGuest(authentication)){
            dto.put("player","Guest");
        } else {
            Player player  = repoPlayers.findByEmail(authentication.getName());
            PlayerDTO   playerDTO   =   new PlayerDTO(player);
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

    @RequestMapping("/gamePlayers/{id}")
    public Map<String,Object> getGamePlayer(@PathVariable long id){
        GamePlayerDTO gpDTO = new GamePlayerDTO(repoGamePlayers.getOne(id));
        return gpDTO.makeGamePlayerDTO();
    }

    @RequestMapping("/game_view/{id}")
    public Map<String,Object> getGameView(@PathVariable long id){
        GamePlayerDTO gpDTO = new GamePlayerDTO(repoGamePlayers.getOne(id));
        return gpDTO.makeGameViewDTO();
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

    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object>register(
            @RequestParam String email,
            @RequestParam String password){
        if(email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (repoPlayers.findByEmail(email) != null) {
            return  new ResponseEntity<>("Name already in use",HttpStatus.FORBIDDEN);
        }
        repoPlayers.save(new Player(email,passwordEncoder().encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
}
