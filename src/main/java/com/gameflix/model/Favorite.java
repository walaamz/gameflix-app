package com.gameflix.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorites")
public class Favorite {

    @EmbeddedId
    private FavoriteId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("gameId")
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    public Favorite() {
    }

    public Favorite(User user, Game game) {
        this.user = user;
        this.game = game;
        this.id = new FavoriteId(
                user.getId(),
                game.getGameId()
        );
    }

    public FavoriteId getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Game getGame() {
        return game;
    }
}