package com.gameflix.service;

import com.gameflix.model.Game;
import com.gameflix.repository.GameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.gameflix.dto.GameRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getGames(String title, String genre) {

        if (title != null && title.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid search title"
            );
        }

        if (genre != null && genre.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid genre"
            );
        }

        if (title != null && genre != null) {
            return gameRepository
                    .findByTitleContainingIgnoreCaseAndGenreIgnoreCase(
                            title.trim(), genre.trim());
        }

        if (title != null) {
            return gameRepository
                    .findByTitleContainingIgnoreCase(title.trim());
        }

        if (genre != null) {
            return gameRepository
                    .findByGenreIgnoreCase(genre.trim());
        }

        return gameRepository.findAll();
    }

    public Game getGameById(Long id) {

        if (id == null || id <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid game ID"
            );
        }

        return gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Game not found"
                ));
    }

    @Transactional
    public Game createGame(GameRequest request) {
        Game game = new Game(
                request.getTitle().trim(),
                request.getDescription().trim(),
                request.getGenre().trim(),
                request.getReleaseDate()
        );

        return gameRepository.save(game);
    }

    @Transactional
    public Game updateGame(Long id, GameRequest request) {
        Game game = getGameById(id);

        game.setTitle(request.getTitle().trim());
        game.setDescription(request.getDescription().trim());
        game.setGenre(request.getGenre().trim());
        game.setReleaseDate(request.getReleaseDate());

        return gameRepository.save(game);
    }

    @Transactional
    public void deleteGame(Long id) {
        Game game = getGameById(id);

        gameRepository.delete(game);
    }
}
