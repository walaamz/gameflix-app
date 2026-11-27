package com.gameflix.service;

import com.gameflix.model.Favorite;
import com.gameflix.model.FavoriteId;
import com.gameflix.model.Game;
import com.gameflix.model.User;
import com.gameflix.repository.FavoriteRepository;
import com.gameflix.repository.GameRepository;
import com.gameflix.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.gameflix.dto.FavoriteResponse;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            GameRepository gameRepository,
            UserRepository userRepository) {

        this.favoriteRepository = favoriteRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    // Retrieve only the authenticated user's favorites and
// convert game entities into clean API response objects.
    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavorites(String email) {

        User user = getUser(email);

        return favoriteRepository.findById_UserId(user.getId())
                .stream()
                .map(favorite -> new FavoriteResponse(favorite.getGame()))
                .toList();
    }

    @Transactional
    public void addFavorite(String email, Long gameId) {

        validateGameId(gameId);

        User user = getUser(email);

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Game not found"));

        FavoriteId favoriteId = new FavoriteId(
                user.getId(), gameId);

        if (favoriteRepository.existsById(favoriteId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Game is already in favorites");
        }

        favoriteRepository.save(new Favorite(user, game));
    }

    @Transactional
    public void removeFavorite(String email, Long gameId) {

        validateGameId(gameId);

        User user = getUser(email);

        FavoriteId favoriteId = new FavoriteId(
                user.getId(), gameId);

        if (!favoriteRepository.existsById(favoriteId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Favorite not found");
        }

        favoriteRepository.deleteById(favoriteId);
    }

    private User getUser(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User not found"));
    }

    private void validateGameId(Long gameId) {

        if (gameId == null || gameId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid game ID");
        }
    }
}