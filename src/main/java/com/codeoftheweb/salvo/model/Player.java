package com.codeoftheweb.salvo.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String email;
    private String name;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    public Player() { }

    public Player(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String nombre) { this.name = name; }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public long getid() {
        return id;
    }
    public void setid(long id) { this.id = id; }

    @JsonIgnore
    public List<Game> getGames() {
        return gamePlayers.stream().map(game -> game.getGame()).collect(toList());
    }
}