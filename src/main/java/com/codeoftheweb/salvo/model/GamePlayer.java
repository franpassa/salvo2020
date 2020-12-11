package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy="gameplayer", fetch=FetchType.EAGER)
    @OrderBy
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    Set<Salvo> salvoes = new HashSet<>();

    public GamePlayer() { }

    public GamePlayer(Player player, Game game) {
        this.creationDate = LocalDateTime.now();
        this.player = player;
        this.game = game;
    }

    public long getid() {
        return id;
    }
    public void setid(long id) { this.id = id; }

    public LocalDateTime  getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime  creationDate) { this.creationDate = creationDate; }

    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    public Set<Ship> getShips() { return ships; }
    public void setShips(Set<Ship> ships) { this.ships = ships; }

    public Set<Salvo> getSalvoes() { return salvoes; }
    public void setSalvoes(Set<Salvo> salvoes) { this.salvoes = salvoes; }

    public void addShip(Ship ship){
        ship.setGameplayer(this);
        ships.add(ship);
    }
    public void addSalvo(Salvo salvo){
        salvo.setGamePlayer(this);
        salvoes.add(salvo);
    }

    public Score getScore(){
        return player.getScore(game);
    }
}
