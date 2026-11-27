package com.gameflix.controller;

import com.gameflix.dto.FavoriteResponse;
import com.gameflix.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // Return favorites belonging to the authenticated user.
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
            Principal principal) {

        return ResponseEntity.ok(
                favoriteService.getFavorites(principal.getName()));
    }

    @PostMapping("/{gameId}")
    public ResponseEntity<Void> addFavorite(
            Principal principal,
            @PathVariable Long gameId) {

        favoriteService.addFavorite(
                principal.getName(), gameId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> removeFavorite(
            Principal principal,
            @PathVariable Long gameId) {

        favoriteService.removeFavorite(
                principal.getName(), gameId);

        return ResponseEntity.noContent().build();
    }
}