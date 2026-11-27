package com.gameflix.dto;

import com.gameflix.model.Game;

import java.time.LocalDate;

/**
 * Represents the game information returned by the Favorites API.
 * Prevents internal Hibernate metadata and user information
 * from being exposed in the API response.
 */
public class FavoriteResponse {

    private final Long gameId;
    private final String title;
    private final String description;
    private final String genre;
    private final LocalDate releaseDate;

    // Extract only the game fields required by the client.
    public FavoriteResponse(Game game) {
        this.gameId = game.getGameId();
        this.title = game.getTitle();
        this.description = game.getDescription();
        this.genre = game.getGenre();
        this.releaseDate = game.getReleaseDate();
    }

    public Long getGameId() {
        return gameId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }
}