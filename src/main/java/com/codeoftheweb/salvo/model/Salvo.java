package com.codeoftheweb.salvo.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    private long turn;

    @ElementCollection
    @Column(name = "location")
    private List<String> locations = new ArrayList<>();

    public Salvo() { }
    public Salvo(long turn) { this.turn = turn; }

    public Salvo(long turn, List<String> locations){
        this.turn = turn;
        this.locations = locations;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public GamePlayer getGamePlayer() { return gamePlayer; }
    public void setGamePlayer(GamePlayer gamePlayer) { this.gamePlayer = gamePlayer; }

    public long getTurn(){ return turn; }
    public void setTurn(long turn) { this.turn = turn; }

    public List<String> getLocations() { return locations; }
    public void setLocations(List<String> locations) { this.locations = locations; }

    public void addLocation(String location){
        locations.add(location);
    }
}