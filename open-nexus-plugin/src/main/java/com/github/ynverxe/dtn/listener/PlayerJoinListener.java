package com.github.ynverxe.dtn.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.Bukkit;
import com.github.ynverxe.dtn.event.player.PlayerJoinToLobby;
import com.github.ynverxe.dtn.board.ComplexBoard;
import com.github.ynverxe.dtn.translation.DefaultTranslationContainer;
import com.github.ynverxe.dtn.board.BoardModel;
import com.github.ynverxe.dtn.player.APlayer;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        APlayer player = APlayer.cache().findOrCache(event.getPlayer());

        ComplexBoard board = player.board();
        BoardModel boardModel = player.findTranslationResource(DefaultTranslationContainer.LOBBY_BOARD, null);

        boardModel.applyToBoard(board);
        board.display(player.bukkitPlayer());

        PlayerJoinToLobby playerJoinToLobby = new PlayerJoinToLobby(player);
        Bukkit.getPluginManager().callEvent(playerJoinToLobby);
    }
}
