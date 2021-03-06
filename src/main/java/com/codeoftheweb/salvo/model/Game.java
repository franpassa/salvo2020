package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private ZonedDateTime creationDate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    @OrderBy
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    @OrderBy
    private Set<Score> scores = new HashSet<>();

    public Game() {
        this.creationDate = ZonedDateTime.now();
    }

    public long getid() {
        return id;
    }
    public void setid(long id) { this.id = id; }

    public ZonedDateTime  getCreationDate() { return creationDate; }
    public void setCreationDate(ZonedDateTime  creationDate) { this.creationDate = creationDate; }

    public Set<GamePlayer> getGamePlayers() { return gamePlayers; }

    public Set<Score> getScores() { return scores; }
    public void setScores(Set<Score> scores) { this.scores = scores; }

    public List<Player> getPlayers() {
        return gamePlayers.stream().map(player -> player.getPlayer()).collect(toList());
    }
}
