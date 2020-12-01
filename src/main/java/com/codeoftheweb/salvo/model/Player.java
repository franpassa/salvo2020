package com.codeoftheweb.salvo.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    @OrderBy
    private Set<Score> scores = new HashSet<>();

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

    public Set<Score> getScores() { return scores; }
    public void setScores(Set<Score> scores) { this.scores = scores; }

    public Score getScore(Game game){
        return scores.stream().filter(p -> p.getGame().getid() == game.getid()).findFirst().orElse(null);
    }

    public void addScore(Score score){
        score.setPlayer(this);
        scores.add(score);
    }

    public long getWinScore(){
        return scores.stream().filter(score -> score.getScore() == 1.0D).count();
    }

    public long getLostScore(){
        return scores.stream().filter(score -> score.getScore() == 0D).count();
    }

    public long getTiedScore(){
        return scores.stream().filter(score -> score.getScore() == 0.5D).count();
    }

    public double getTotalScore(){
        return scores.stream().mapToDouble(score -> score.getScore()).sum();
    }

    @JsonIgnore
    public List<Game> getGames() {
        return gamePlayers.stream().map(game -> game.getGame()).collect(toList());
    }
}