package com.gameflix.service;

import com.gameflix.model.Game;
import com.gameflix.repository.GameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.gameflix.dto.GameRequest;
import org.springframework.transaction.annotation.Transactional;
import com.gameflix.repository.FavoriteRepository;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final FavoriteRepository favoriteRepository;

    // Inject repositories for game management and favorite cleanup.
    public GameService(GameRepository gameRepository,
                       FavoriteRepository favoriteRepository) {
        this.gameRepository = gameRepository;
        this.favoriteRepository = favoriteRepository;
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

    // Delete a game and its associated favorites in a single transaction.
    @Transactional
    public void deleteGame(Long id) {

        // Validate the game ID and confirm the game exists.
        Game game = getGameById(id);

        // Remove dependent favorite records to prevent
        // foreign key constraint violations.
        favoriteRepository.deleteById_GameId(id);
        favoriteRepository.flush();

        // Delete the game after its associated favorites are removed.
        gameRepository.delete(game);
    }
}
