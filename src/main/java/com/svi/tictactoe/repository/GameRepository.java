package com.svi.tictactoe.repository;

import com.svi.tictactoe.model.entity.GameMove;

import java.util.List;
import java.util.UUID;

public interface GameRepository {
    GameMove saveMove(GameMove move);
    List<GameMove> getGameDetailsByGameId(UUID id);
}

