package com.github.ynverxe.dtn.command;

import java.util.stream.Collectors;

import com.github.ynverxe.dtn.dimension.properties.RoomLobbyPropertiesContainer;
import com.github.ynverxe.dtn.exception.ExceptionCatcher;
import com.github.ynverxe.dtn.game.*;
import com.github.ynverxe.util.Pair;
import java.util.List;

import com.github.ynverxe.dtn.gson.GlobalGson;
import java.util.Map;
import java.util.Date;
import com.github.ynverxe.dtn.translation.ResourceReceiver;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.translation.DefaultTranslationContainer;
import com.github.ynverxe.dtn.annotation.CSender;
import com.github.ynverxe.dtn.player.APlayer;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;

import java.text.SimpleDateFormat;

@Command(names = { "game" })
public class GameManagementCommand implements CommandClass {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");

    public static final String ID_IS_ALREADY_IN_USE = "&cThe id '%s' is already in use by another game.";
    public static final String GAME_CREATED = "&dGame[&b%s&d, &e%s&d] &acreated!";
    public static final String GAME_STATE_SET = "&bGame &e'%s' %s.";
    public static final String GAME_NO_APT_TO_EDIT = "&cGame is not apt to edit, disable it first.";
    public static final String GAME_REBUILD_SUCCESSFULLY = "&aGame '%s' rebuild successfully.";
    public static final String PLAYER_NOT_IN_LOBBY = "&cYou're not in the game lobby.";
    public static final String GAME_IS_NOT_ENABLED = "&cGame room is not enabled.";
    public static final String MATCH_IS_RUNNING = "&cGame is running in this room.";
    private final GameManager gameManager;
    
    public GameManagementCommand() {
        this.gameManager = GameManager.instance();
    }
    
    @Command(names = "", permission = "dtn.game.help")
    public boolean commandList(@CSender APlayer aPlayer) {
        aPlayer.renderResource(DefaultTranslationContainer.GAME_COMMAND_LIST);
        return true;
    }
    
    @Command(names = "start", permission = "dtn.game.start")
    public boolean startGame(@CSender APlayer aPlayer, GameRoom gameRoom, Dimension dimension) {
        if (!gameRoom.enabled()) {
            aPlayer.renderResource("&cGame room is not enabled.");
            return true;
        }

        GameInstance gameInstance = gameRoom.instance();
        if (gameInstance.runningMatch() != null) {
            aPlayer.renderResource("&cGame is running in this room.");
            return true;
        }

        ExceptionCatcher.instance().catchException(() -> gameInstance.startMatch(dimension));
        return true;
    }
    
    @Command(names = "create", permission = "dtn.game.create")
    public boolean createGame(@CSender ResourceReceiver sender, String gameId, String typeName, String dimensionName, boolean enabled) {
        if (this.gameManager.get(gameId) != null) {
            sender.renderResource(String.format("&cThe id '%s' is already in use by another game.", gameId));
            return true;
        }

        Dimension lobbyDimension = null;
        if ("absent".equals(dimensionName)) {
            lobbyDimension = Dimension.manager().createEmptyDimension(gameId, true, "room-lobby", new RoomLobbyPropertiesContainer());
        }

        if (lobbyDimension == null) {
            lobbyDimension = Dimension.manager().get(dimensionName);
        }

        if (lobbyDimension == null) {
            String message = String.format("Dimension '%s' doesn't exists.", dimensionName);
            sender.renderResource(message);
            return true;
        }

        GameRoomModel gameRoomModel = GameRoomModel.create(gameId, new Date(), typeName, lobbyDimension.name(), new Rules(), enabled);
        this.gameManager.createRoom(gameRoomModel);
        sender.renderResource(String.format("&dGame[&b%s&d, &e%s&d] &acreated!", gameId, typeName));
        return true;
    }
    
    @Command(names = "setenabled", permission = "dtn.game.setenabled")
    public boolean setEnabledGame(@CSender ResourceReceiver sender, GameRoom gameRoom, boolean enabled) {
        gameRoom.changeEnablement(enabled);
        sender.renderResource(String.format("&bGame &e'%s' %s.", gameRoom.name(), enabled ? "&aenabled" : "&cdisabled"));
        return true;
    }
    
    @Command(names = "remove", permission = "dtn.game.remove")
    public boolean removeGame(@CSender ResourceReceiver sender, GameRoom gameRoom) {
        gameRoom.terminate();
        sender.renderResource(DefaultTranslationContainer.GAME_REMOVE.replacing("<gameId>", gameRoom.name()));
        return true;
    }
    
    @Command(names = "join", permission = "dtn.game.join")
    public boolean joinGame(@CSender APlayer sender, GameInstance game) {
        game.joinPlayer(sender);
        sender.renderResource(DefaultTranslationContainer.GAME_JOIN.replacing("<roomId>", game.name()));
        return true;
    }
    
    @Command(names = { "leave" }, permission = "dtn.game.leave")
    public boolean leaveGame(@CSender APlayer sender) {
        GameInstance game = sender.playingGame();
        if (game == null) {
            sender.renderResource(DefaultTranslationContainer.NO_PLAYING_ROOM);
            return true;
        }

        game.leavePlayer(sender);
        sender.renderResource(DefaultTranslationContainer.GAME_LEAVE.replacing("<gameId>", game.name()));
        return true;
    }
    
    @Command(names = { "settype" }, permission = "dtn.game.settype")
    public boolean setGameType(@CSender ResourceReceiver sender, GameRoom gameRoom, String gameTypeName) {
        if (gameRoom.enabled()) {
            sender.renderResource("&cGame is not apt to edit, disable it first.");
            return true;
        }

        gameRoom.applyType(gameTypeName);
        sender.renderResource(String.format("&aGame '%s' rebuild successfully.", gameRoom.name()));
        return true;
    }
    
    @Command(names = "setrules", permission = "dtn.game.setrules")
    public boolean setGameRules(@CSender APlayer aPlayer, GameRoom gameRoom, String gameTypeName, Map<String, String> rules) {
        if (gameRoom.enabled()) {
            aPlayer.renderResource("&cGame is not apt to edit, disable it first.");
            return true;
        }

        Rules mappedRules = GlobalGson.fromMap(rules, Rules.class);
        gameRoom.applyRules(mappedRules);
        aPlayer.renderResource(String.format("&aGame '%s' rebuild successfully.", gameRoom.name()));
        return true;
    }
    
    @Command(names = { "setlobby" }, permission = "dtn.game.setlobby")
    public boolean setGameLobby(@CSender APlayer aPlayer, GameRoom gameRoom, Dimension lobbyDimension) {
        gameRoom.rebuildLobby(lobbyDimension);
        aPlayer.renderResource(String.format("&aGame '%s' rebuild successfully.", gameRoom.name()));
        return true;
    }
    
    @Command(names = { "list" }, permission = "dtn.game.list")
    public boolean listGames(@CSender APlayer aPlayer) {
        for (GameRoom value : this.gameManager.values()) {
            showGameInfo(aPlayer, value);
        }

        return true;
    }
    
    @Command(names = { "info" }, permission = "dtn.game.info")
    public boolean showGameInfo(@CSender APlayer aPlayer, GameRoom gameRoom) {
        List<String> gameInfo = buildGameInfo(
                "Name", "&e" + gameRoom.name(),
                "Creation date", "&e" + GameManagementCommand.DATE_FORMAT.format(gameRoom.creationDate()),
                "Enabled", gameRoom.enabled() ? "&atrue" : "&cfalse",
                "Status", "&7" + (gameRoom.enabled() ? gameRoom.instance().state() : GameState.DISABLED),
                "Lobby dimension", "&a" + gameRoom.lobby().world().getName()
        );

        aPlayer.renderResource(gameInfo);
        return true;
    }
    
    private static List<String> buildGameInfo(Object... objects) {
        return Pair.fromObjects(objects).stream().map(pair -> String.format("&8» &b%s: %s", pair.left(), pair.right())).collect(Collectors.toList());
    }
}
