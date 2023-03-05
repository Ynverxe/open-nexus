package com.github.ynverxe.dtn.translation;

import java.util.Arrays;
import com.github.ynverxe.dtn.title.Title;
import com.github.ynverxe.dtn.board.BoardModel;
import com.github.ynverxe.translation.resource.ResourceReference;

public final class DefaultTranslationContainer {

    public static final ResourceReference<String> INSUFFICIENT_PLAYERS;
    public static final ResourceReference<String> MATCH_START;
    public static final ResourceReference<String> CONFIGURE_DIMENSION;
    public static final ResourceReference<String> DIMENSION_COMMAND_LIST;
    public static final ResourceReference<String> GAME_COMMAND_LIST;
    public static final ResourceReference<String> GAME_JOIN;
    public static final ResourceReference<String> NO_PLAYING_ROOM;
    public static final ResourceReference<String> GAME_LEAVE;
    public static final ResourceReference<String> GAME_REMOVE;
    public static final ResourceReference<String> TEAM_JOIN;
    public static final ResourceReference<String> TEAM_LEAVE;
    public static final ResourceReference<String> NOT_IN_A_GAME;
    public static final ResourceReference<String> NOT_IN_A_TEAM;
    public static final ResourceReference<BoardModel> LOBBY_BOARD;
    public static final ResourceReference<BoardModel> WAITING_BOARD;
    public static final ResourceReference<BoardModel> IN_GAME_BOARD;
    public static final ResourceReference<String> COUNTDOWN_TO_START;
    public static final ResourceReference<String> ROOM_JOIN;
    public static final ResourceReference<String> NO_MAP_FOUND;
    public static final ResourceReference<String> PLAYER_HIT_NEXUS;
    public static final ResourceReference<?> YOUR_NEXUS_HAS_BEEN_DESTROYED;
    public static final ResourceReference<?> GLOBAL_NEXUS_DEATH;
    public static final ResourceReference<String> PLAYER_HIT_HIS_OWN_NEXUS;
    public static final ResourceReference<String> NEXUS_IS_DEAD;
    public static final ResourceReference<String> MATCH_WILL_END;
    public static final ResourceReference<String> TEAM_HAS_WON;
    public static final ResourceReference<Title> YOUR_TEAM_HAS_WON;
    public static final ResourceReference<?> FIRST_PHASE_MESSAGE;
    public static final ResourceReference<?> SECOND_PHASE_MESSAGE;
    public static final ResourceReference<?> THIRD_PHASE_MESSAGE;
    public static final ResourceReference<?> FOURTH_PHASE_MESSAGE;
    public static final ResourceReference<?> FIFTH_PHASE_MESSAGE;
    public static final ResourceReference<Title> MATCH_TIE;
    
    static {
        INSUFFICIENT_PLAYERS = ResourceReference.create(String.class, "match-cannot-start.insufficient-players")
                .withDefaultValue("&7» &cNot enough players in room to start");
        MATCH_START = ResourceReference.create(String.class, "match-start")
                .withDefaultValue("&7» &bThe match has started. &aGood Luck!");
        CONFIGURE_DIMENSION = ResourceReference.create(String.class, "configure-dimension")
                .withDefaultValue("&7» &aYou're now configuring &6<dimension>&a.");
        DIMENSION_COMMAND_LIST = ResourceReference.create(String.class, "dimension-commands")
                .withDefaultValue(Arrays.asList(
                        "&a&n===[&bDimension Commands&a&n]===",
                        "&7[&b*&7] &6/dimension &3create &a<id> <typeName> <worlds-names> &b- &eCreate a dimension.",
                        "&7[&b*&7] &6/dimension &3remove &a<id> <deleteData> &b- &eRemove a dimension.",
                        "&7[&b*&7] &6/dimension &3rebuild &a<id> <newId> <newTypeName> &b- &eRebuild a dimension.",
                        "&7[&b*&7] &6/dimension &3info &ai<id> &b- &eShow the dimension info.",
                        "&7[&b*&7] &6/dimension &3list &b- &eShow all dimensions info.",
                        "&7[&b*&7] &6/dimension &3list-type &a<type> &b- &eShow and filter by type all dimensions info.",
                        "&7[&b*&7] &6/dimension &3configure &a<id> &b- &eConfigure a dimension.",
                        "&7[&b*&7] &6/dimension &3save &a<id> &b- &eSave the dimension configuration changes.",
                        "&7[&b*&7] &6/dimension &3types &b- &eShow the available dimension types."
                ));
        GAME_COMMAND_LIST = ResourceReference.create(String.class, "game-commands")
                .withDefaultValue(Arrays.asList(
                        "&a&n===[&bGame Commands&a&n]===",
                        "&7[&b*&7] &6/game &3create &a<id> <expansion-type-name> <lobby-dimension> &b- &eCreate a new game.",
                        "&7[&b*&7] &6/game &3setenabled &a<id> <true/false> &b- &eEnable or disable a game.",
                        "&7[&b*&7] &6/game &3remove &a<id> &b- &eRemove a existent game.",
                        "&7[&b*&7] &6/game &3join &a<id> &b- &eJoin a game room.",
                        "&7[&b*&7] &6/game &3leave &b- &eLeave from the current game."
                ));
        GAME_JOIN = ResourceReference.create(String.class, "game-join").withDefaultValue("&aJoining to <roomId>.");
        NO_PLAYING_ROOM = ResourceReference.create(String.class, "no-playing-room").withDefaultValue("&cYou're not in a game room.");
        GAME_LEAVE = ResourceReference.create(String.class, "game-leave").withDefaultValue("&cYou leaved from <gameId>.");
        GAME_REMOVE = ResourceReference.create(String.class, "game-remove").withDefaultValue("&a<gameId> removed successfully.");
        TEAM_JOIN = ResourceReference.create(String.class, "team-join").withDefaultValue("&7» &bYou &6joined &bthe <team>&b.");
        TEAM_LEAVE = ResourceReference.create(String.class, "team-join").withDefaultValue("&7» &bYou &cleaved &bthe <team>&b.");
        NOT_IN_A_GAME = ResourceReference.create(String.class, "not-in-a-game").withDefaultValue("&cYou're not in a game.");
        NOT_IN_A_TEAM = ResourceReference.create(String.class, "not-in-a-team").withDefaultValue("&cYou're not in a team.");
        LOBBY_BOARD = ResourceReference.create(BoardModel.class, "lobby-board");
        WAITING_BOARD = ResourceReference.create(BoardModel.class, "waiting-game-board");
        IN_GAME_BOARD = ResourceReference.create(BoardModel.class, "in-game-board");
        COUNTDOWN_TO_START = ResourceReference.create(String.class, "countdown-to-start").withDefaultValue("&7» &aStarting in &b<seconds>%a.");
        ROOM_JOIN = ResourceReference.create(String.class, "player-join-room").withDefaultValue("&7» &6<player> &bjoined to the game.");
        NO_MAP_FOUND = ResourceReference.create(String.class, "no-map-found").withDefaultValue("No map found to start the game.");
        PLAYER_HIT_NEXUS = ResourceReference.create(String.class, "player-hit-nexus")
                .withDefaultValue("<player> &7has damaged the <nexus>&7. &e<remainingHealth>");
        YOUR_NEXUS_HAS_BEEN_DESTROYED = ResourceReference.create("title", "nexus-has-been-destroyed")
                .withDefaultValue(new Title("&cYour nexus", "&chas been destroyed"));
        GLOBAL_NEXUS_DEATH = ResourceReference.create(String.class, "nexus-death-message")
                .withDefaultValue("<nexus> &7has been destroyed by <player>&7.");
        PLAYER_HIT_HIS_OWN_NEXUS = ResourceReference.create(String.class, "player-hit-his-own-nexus")
                .withDefaultValue("&cWhy you hit your own nexus?");
        NEXUS_IS_DEAD = ResourceReference.create(String.class, "dead-nexus").withDefaultValue("&cNexus is dead.");
        MATCH_WILL_END = ResourceReference.create(String.class, "match-will-end").withDefaultValue("&7» &aMatch will end in &6<time>&a.");
        TEAM_HAS_WON = ResourceReference.create(String.class, "team-has-won").withDefaultValue("&7» &bThe <team> &bhas &6won&b!");
        YOUR_TEAM_HAS_WON = ResourceReference.create(Title.class, "your-team-has-won").withDefaultValue(new Title("&6&lYour team has won!"));
        FIRST_PHASE_MESSAGE = ResourceReference.create("string", "first-phase-start")
                .withDefaultValue(Arrays.asList(Arrays.asList(
                        "&8&m]----------[ &bPhase &a1 &8§&m]----------[",
                        "&7The &bgame &7starts! Get resources and equipment",
                        "&7befour your enemies. If you are efficient, this",
                        "&7phase can give you the advantage. &bAll nexus are",
                        "&6invincible."),
                        new Title("&b&lPhase &a&l1", "&estarts!"))
                );
        SECOND_PHASE_MESSAGE = ResourceReference.create("string", "second-phase-start")
                .withDefaultValue(Arrays.asList(Arrays.asList(
                        "&8&m]----------[ &bPhase &e2 &8§&m]----------[",
                        "&7All nexus are vulnerable, protect your nexus from enemies",
                        "&7or show your courage hitting the enemies nexus."),
                        new Title("&b&lPhase &e&l1", "&estarts!"))
                );
        THIRD_PHASE_MESSAGE = ResourceReference.create("string", "third-phase-start")
                .withDefaultValue(Arrays.asList(Arrays.asList(
                        "&8&m]----------[ &bPhase &63 &8§&m]----------[",
                        "&7The most anticipated phase starts!",
                        "&bdiamonds &7have been spawned at the center",
                        "&7of the map."),
                        new Title("&b&lPhase &6&l3", "&estarts!"))
                );
        FOURTH_PHASE_MESSAGE = ResourceReference.create("string", "first-phase-start")
                .withDefaultValue(Arrays.asList(Arrays.asList(
                        "&8&m]----------[ &bPhase &c4 &8§&m]----------[",
                        "&7The bravest who kills the &5boss &7will getValue",
                        "&7a very &6powerful &7item."),
                        new Title("&b&lPhase &a&l1", "&estarts!"))
                );
        FIFTH_PHASE_MESSAGE = ResourceReference.create("string", "fifth-phase-start")
                .withDefaultValue(Arrays.asList(Arrays.asList(
                        "&8&m]----------[ &bPhase &c4 &8§&m]----------[",
                        "&7The definitive phase starts! Be careful",
                        "&7now the &dnexus &7damage are &cdouble&7."),
                        new Title("&b&lPhase &a&l1", "&estarts!"))
                );
        MATCH_TIE = ResourceReference.create(Title.class, "match-tie")
                .withDefaultValue(new Title("&d&lTie!", "&eIt was a good game!"));
    }
}
