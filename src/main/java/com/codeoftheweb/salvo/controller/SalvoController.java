package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.DTO.GameDTO;
import com.codeoftheweb.salvo.DTO.GamePlayerDTO;
import com.codeoftheweb.salvo.DTO.PlayerDTO;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<Map<String,Object>> getGameAll(){
        return repoGames.findAll()
                .stream()
                .map(game -> {
                    GameDTO gDTO = new GameDTO(game);
                    return gDTO.makeGameDTO();})
                .collect(Collectors.toList());
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
}
