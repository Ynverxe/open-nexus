package com.github.ynverxe.dtn.command;

import com.github.ynverxe.dtn.game.GameInstance;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.team.Team;
import com.github.ynverxe.dtn.translation.DefaultTranslationContainer;
import com.github.ynverxe.dtn.team.TeamColor;
import com.github.ynverxe.dtn.annotation.CSender;
import com.github.ynverxe.dtn.player.APlayer;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.CommandClass;

@Command(names = { "team" })
public class TeamCommand implements CommandClass {
    @Command(names = { "join" })
    public boolean joinTeam(@CSender final APlayer sender, TeamColor color) {
        final GameInstance game = sender.playingGame();
        if (game == null) {
            sender.renderResource(DefaultTranslationContainer.NOT_IN_A_GAME);
            return true;
        }

        Match match = game.runningMatch();

        if (match != null) {
            Team team = match.teams().get(color);

            if (team.disqualified()) {
                sender.renderResource(DefaultTranslationContainer.DISQUALIFIED_TEAM);
                return true;
            }
        }

        game.teamSelector().bindTeamToPlayer(sender, color);

        sender.renderResource(DefaultTranslationContainer.TEAM_JOIN.replacing("<team>", color.coloredName()));
        return true;
    }
    
    @Command(names = { "leave" })
    public boolean leaveTeam(@CSender final APlayer sender) {
        GameInstance game = sender.playingGame();
        if (game == null) {
            sender.renderResource(DefaultTranslationContainer.NOT_IN_A_GAME);
            return true;
        }

        TeamColor teamColor = game.teamSelector().discardPlayerSelection(sender);
        if (teamColor == null) {
            sender.renderResource(DefaultTranslationContainer.NOT_IN_A_TEAM);
            return true;
        }

        sender.renderResource(DefaultTranslationContainer.TEAM_LEAVE.replacing("<team>", teamColor.coloredName()));
        return true;
    }
}
