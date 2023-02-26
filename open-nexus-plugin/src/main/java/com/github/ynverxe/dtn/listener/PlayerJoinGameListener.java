package com.github.ynverxe.dtn.listener;

import org.bukkit.event.EventHandler;
import com.github.ynverxe.dtn.game.GameState;
import com.github.ynverxe.dtn.game.GameInstance;
import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.dtn.DestroyTheNexus;
import com.github.ynverxe.dtn.board.ComplexBoard;
import com.github.ynverxe.dtn.translation.DefaultTranslationContainer;
import com.github.ynverxe.dtn.board.BoardModel;
import com.github.ynverxe.dtn.event.room.PlayerJoinGameEvent;
import org.bukkit.event.Listener;

public class PlayerJoinGameListener implements Listener {

    @EventHandler
    public void onRoomJoin(PlayerJoinGameEvent event) {
        APlayer aPlayer = event.player();
        GameInstance gameInstance = event.game();
        GameState gameState = gameInstance.state();
        ComplexBoard board = aPlayer.board();

        BoardModel boardModel;

        switch (gameState) {
            case IN_GAME:
            case ENDING: {
                boardModel = aPlayer.findTranslationResource(DefaultTranslationContainer.IN_GAME_BOARD, null);
                break;
            }
            default: {
                boardModel = aPlayer.findTranslationResource(DefaultTranslationContainer.WAITING_BOARD, null);
                break;
            }
        }

        boardModel.applyToBoard(board);

        switch (gameState) {
            case WAITING:
            case STARTING: {
                DestroyTheNexus.instance().messenger().dispatchResource(gameInstance, DefaultTranslationContainer.ROOM_JOIN.replacing("<player>", aPlayer.name()));
                break;
            }
        }
    }
}
