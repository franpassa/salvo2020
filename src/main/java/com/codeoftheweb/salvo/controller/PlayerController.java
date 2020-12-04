package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.DTO.PlayerDTO;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PlayerController {

    @Autowired
    private GamePlayerRepository repoGamePlayers;

    @Autowired
    private GameRepository repoGames;

    @Autowired
    private PlayerRepository repoPlayers;

    @RequestMapping(value = "/players", method = RequestMethod.GET)
    public List<Map<String,Object>> getPlayerAll(){
        return repoPlayers.findAll()
                .stream()
                .map(player -> {
                    PlayerDTO pDTO = new PlayerDTO(player);
                    return pDTO.makePlayerDTO();})
                .collect(Collectors.toList());
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password){

        if(email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (repoPlayers.findByEmail(email) != null) {
            return  new ResponseEntity<>("Name already in use",HttpStatus.FORBIDDEN);
        }
        repoPlayers.save(new Player(email, Util.passwordEncoder().encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
