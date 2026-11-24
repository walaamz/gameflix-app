package com.gameflix.config;

import com.gameflix.model.Game;
import com.gameflix.repository.GameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.List;

@Configuration
@Profile("demo")
public class GameDataSeeder {

    @Bean
    CommandLineRunner seedGames(GameRepository gameRepository) {
        return args -> {

            if (gameRepository.count() == 0) {

                gameRepository.saveAll(List.of(

                        new Game(
                                "Minecraft",
                                "Explore, build and survive in an open world.",
                                "Sandbox",
                                LocalDate.of(2011, 11, 18)
                        ),

                        new Game(
                                "Elden Ring",
                                "Explore a vast fantasy world filled with challenging enemies.",
                                "RPG",
                                LocalDate.of(2022, 2, 25)
                        ),

                        new Game(
                                "Stardew Valley",
                                "Build a farm and become part of a rural community.",
                                "Simulation",
                                LocalDate.of(2016, 2, 26)
                        ),

                        new Game(
                                "Red Dead Redemption 2",
                                "Experience an open-world adventure in the American frontier.",
                                "Adventure",
                                LocalDate.of(2018, 10, 26)
                        ),

                        new Game(
                                "Hades",
                                "Battle through the underworld in a roguelike adventure.",
                                "Action",
                                LocalDate.of(2020, 9, 17)
                        ),

                        new Game(
                                "Cyberpunk 2077",
                                "Explore the futuristic open world of Night City.",
                                "RPG",
                                LocalDate.of(2020, 12, 10)
                        )

                ));

                System.out.println("GameFlix sample games added successfully.");
            }
        };
    }
}
