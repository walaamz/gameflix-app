package com.gameflix.repository;

import com.gameflix.model.Favorite;
import com.gameflix.model.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository
        extends JpaRepository<Favorite, FavoriteId> {

    // Retrieve all favorites belonging to a specific user.
    List<Favorite> findById_UserId(Long userId);

    // Remove associated favorites before deleting a game.
    void deleteById_GameId(Long gameId);
}