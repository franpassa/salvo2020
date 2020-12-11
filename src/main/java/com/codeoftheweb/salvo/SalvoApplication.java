package com.codeoftheweb.salvo;
import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repositoryPlayer,
									  GameRepository repositoryGame,
									  GamePlayerRepository repositoryGamePlayer,
									  ShipRepository repositoryShip,
									  SalvoRepository repositorySalvo,
									  ScoreRepository repositoryScore) {
		return (args) -> {
			// save a couple of customers
			Player franco = new Player("franco@gmail.com", "Franco", passwordEncoder().encode("123"));
			Player giuli = new Player("giuli@gmail.com", "Giuli",passwordEncoder().encode("123"));

			repositoryPlayer.save(franco);
			repositoryPlayer.save(giuli);

			Game game = new Game(LocalDateTime.now());
			repositoryGame.save(game);

			GamePlayer franco_game = new GamePlayer(franco,game);
			GamePlayer giuli_game = new GamePlayer(giuli,game);

			repositoryGamePlayer.save(franco_game);
			repositoryGamePlayer.save(giuli_game);

			Ship dest1 = new Ship("destroyer", Arrays.asList("A1", "A2", "A3"),franco_game);
			Ship subm1 = new Ship("submarine", Arrays.asList("B1","B2","B3"),franco_game);
			Ship patr1 = new Ship("patrolboat", Arrays.asList("C1","C2"),franco_game);
			Ship dest2 = new Ship("destroyer", Arrays.asList("D1", "D2", "D3"),giuli_game);
			Ship subm2 = new Ship("submarine", Arrays.asList("E1","E2","E3"),giuli_game);
			Ship patr2 = new Ship("patrolboat", Arrays.asList("F1","F2"),giuli_game);

			repositoryShip.save(dest1);
			repositoryShip.save(subm1);
			repositoryShip.save(patr1);
			repositoryShip.save(dest2);
			repositoryShip.save(subm2);
			repositoryShip.save(patr2);
	};
	}
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName -> {
			Player player = playerRepository.findByEmail(inputName);
			if (player != null) {
				return new User(player.getEmail(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}

/*	UserDetailsService loadByUsername(String username){
		UserDetailsService userDetails = new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return null;
			}
		};
		userDetails.loadUserByUsername(username);

		return userDetails;
	}*/

}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/web/**").permitAll()
				.antMatchers("/api/game_view/*").hasAuthority("USER")
				.antMatchers("/h2-console/**").permitAll()
				.antMatchers("/api/games").permitAll();

		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();
		http.headers().frameOptions().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}