package com.gameflix.repository;

import com.gameflix.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByTitleContainingIgnoreCase(String title);

    List<Game> findByGenreIgnoreCase(String genre);

    List<Game> findByTitleContainingIgnoreCaseAndGenreIgnoreCase(
            String title, String genre);
}
