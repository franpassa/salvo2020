package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDateTime creationDate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    public Game() { }

    public Game(LocalDateTime  creationDate) {
        this.creationDate = creationDate;
    }

    public long getid() {
        return id;
    }
    public void setid(long id) { this.id = id; }

    public LocalDateTime  getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime  creationDate) { this.creationDate = creationDate; }

    public Set<GamePlayer> getGamePlayers() { return gamePlayers; }

    public List<Player> getPlayers() {
        return gamePlayers.stream().map(player -> player.getPlayer()).collect(toList());
    }
}
