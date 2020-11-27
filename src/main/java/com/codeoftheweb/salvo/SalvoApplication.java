package com.codeoftheweb.salvo;
import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;
import java.util.Arrays;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repositoryPlayer,
									  GameRepository repositoryGame,
									  GamePlayerRepository repositoryGamePlayer,
									  ShipRepository repositoryShip,
									  SalvoRepository repositorySalvo) {
		return (args) -> {
			// save a couple of customers
			Player franco = new Player("franco@gmail.com", "Franco Passarelli");
			Player giuli = new Player("giuli@gmail.com", "Giuli Vetere");

			repositoryPlayer.save(franco);
			repositoryPlayer.save(giuli);

			Ship dest1 = new Ship("Destructor", Arrays.asList("A1", "A2", "A3"));
			Ship subm1 = new Ship("Submarine", Arrays.asList("B1","B2","B3"));
			Ship patr1 = new Ship("Patrol_Boat", Arrays.asList("C1","C2"));
			Ship dest2 = new Ship("Destructor", Arrays.asList("D1", "D2", "D3"));
			Ship subm2 = new Ship("Submarine", Arrays.asList("E1","E2","E3"));
			Ship patr2 = new Ship("Patrol_Boat", Arrays.asList("F1","F2"));


			repositoryShip.save(dest1);
			repositoryShip.save(subm1);
			repositoryShip.save(patr1);
			repositoryShip.save(dest2);
			repositoryShip.save(subm2);
			repositoryShip.save(patr2);

			Game game = new Game(LocalDateTime.now());
			repositoryGame.save(game);

			GamePlayer franco_game = new GamePlayer(franco,game);
			GamePlayer giuli_game = new GamePlayer(giuli,game);
			repositoryGame.save(game);

			Salvo salvo_franco_1 = new Salvo(1);
			Salvo salvo_giuli_1 = new Salvo(1);

			salvo_franco_1.addLocation("H1");
			salvo_franco_1.addLocation("H2");

			salvo_giuli_1.addLocation("A1");
			salvo_giuli_1.addLocation("A2");

			franco_game.addShip(dest1);
			giuli_game.addShip(subm1);
			franco_game.addSalvo(salvo_franco_1);
			giuli_game.addSalvo(salvo_giuli_1);

			repositoryGamePlayer.save(franco_game);
			repositoryGamePlayer.save(giuli_game);
			repositoryGame.save(game);

			repositoryShip.save(subm1);
			repositoryShip.save(dest1);

			repositorySalvo.save(salvo_franco_1);
			repositorySalvo.save(salvo_giuli_1);
	};
	}
}
