package com.gameflix.controller;

import com.gameflix.model.Game;
import com.gameflix.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<Game>> getGames(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre) {

        return ResponseEntity.ok(
                gameService.getGames(title, genre)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                gameService.getGameById(id)
        );
    }
}
