package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gameplayer_id")
    private GamePlayer gameplayer;

    @ElementCollection
    private List<String> locations;

    public Ship(){}

    public Ship(String type, List<String> locations, GamePlayer gamePlayer) {
        this.type = type;
        this.locations = locations;
        this.gameplayer = gamePlayer;
    }

    public long getid() { return id; }
    public void setid(long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public GamePlayer getGameplayer() { return gameplayer; }
    public void setGameplayer(GamePlayer gameplayer) { this.gameplayer = gameplayer; }

    public List<String> getLocations() { return locations; }
    public void setLocations(List<String> locations) { this.locations = locations; }
}
