package com.gameflix.controller;

import com.gameflix.dto.GameRequest;
import com.gameflix.model.Game;
import com.gameflix.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/games")
public class AdminGameController {

    private final GameService gameService;

    public AdminGameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<Game> createGame(
            @Valid @RequestBody GameRequest request) {

        Game game = gameService.createGame(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(
            @PathVariable Long id,
            @Valid @RequestBody GameRequest request) {

        Game game = gameService.updateGame(id, request);

        return ResponseEntity.ok(game);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {

        gameService.deleteGame(id);

        return ResponseEntity.noContent().build();
    }
}