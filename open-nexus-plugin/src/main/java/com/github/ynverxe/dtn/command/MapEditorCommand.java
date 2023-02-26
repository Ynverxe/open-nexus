package com.github.ynverxe.dtn.command;

import com.github.ynverxe.dtn.dimension.properties.MatchMapPropertiesContainer;
import com.github.ynverxe.dtn.model.data.BossModel;
import com.github.ynverxe.util.Pair;
import com.github.ynverxe.util.TextColorizer;
import com.github.ynverxe.dtn.dimension.edition.EditionInstance;
import me.fixeddev.commandflow.exception.CommandUsage;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import org.bukkit.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import com.github.ynverxe.dtn.boss.BossType;
import org.bukkit.block.Block;
import com.github.ynverxe.dtn.world.Position;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import com.github.ynverxe.dtn.world.NamedPosition;
import com.github.ynverxe.dtn.edition.defaults.MatchMapEditionHandler;
import com.github.ynverxe.dtn.team.TeamColor;
import com.github.ynverxe.dtn.annotation.CSender;
import com.github.ynverxe.dtn.player.APlayer;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.CommandClass;

import java.util.List;

@Command(names = { "mapeditor" })
public class MapEditorCommand implements CommandClass {

    private static final String SPAWN_SET = "%s &6spawn set at: %s.";
    private static final String SPAWN_REMOVE = "%s &6Spawn &a(#%s, %s) &6removed.";
    private static final String NEXUS_SET = "%s nexus &6 placed at: &b%s&c&6, with health: &b%s";
    private static final String LISTING_NEXUS_FORMAT = "%s nexus &6- &blocation:&6%s, &chealth:&a%s";
    private static final String NOT_FACING_BLOCK = "&cYou're not facing a block.";
    private static final String INVALID_INDEX = "&cInvalid index (%s).";
    private static final String NOT_EDITING_A_MAP = TextColorizer.colorize("&cYou're not editing a map.");
    private static final String LISTING_POSITION_FORMAT = "&b#%s - %s";
    private static final String BOSS_INFO_FORMAT = "BossModel[%s, %s, %s]";
    private static final String BOSS_ADDED_AT = "&5%s &aadded at location &e%s&a.";
    private static final String BOSS_DOES_NOT_EXISTS = "&cBoss '%s' doesn't exists.";
    private static final String BOSS_REMOVED = "&5%s &eremoved.";
    private static final String PLACED_BOSS_INFO_FORMAT = "&5BossModel[%s, %s, %s] &aat &b%s&a.";
    private static final String DIAMOND_PLACED = "&bDiamond &aplaced at &e%s&a.";
    private static final String DIAMOND_REMOVED = "&bDiamond &cremoved from &e%s&c.";
    private static final String NO_DIAMOND_ORE_AT = "&cThere's any diamond ore at this block.";
    private static final String NO_DIAMOND_ORE_AT_INDEX = "&cThere's any diamond ore at index '%s'.";
    private static final String DIAMOND_REGENERATION_SETTED = "&bDiamond Ores &aregeneration time setted in &6%s &aseconds.";
    private static final String NEGATIVE_NUMBER = "&cThe input number is negative.";
    private static final String SHOW_REGEN_TIME = "&bDiamond &aregeneration time are &e%s &aseconds.";

    @Command(names = { "setspawn" })
    public boolean setSpawn(@CSender APlayer player, TeamColor color) {
        MatchMapEditionHandler handler = fromPlayer(player);
        NamedPosition namedPosition = player.position();
        handler.container().addSpawn(color, namedPosition);
        String message = String.format(SPAWN_SET, color.coloredName(), namedPosition.format());
        player.renderResource(message);

        return true;
    }
    
    @Command(names = { "removespawn" })
    public boolean removeSpawn(@CSender APlayer player, TeamColor color, int index) {
        MatchMapEditionHandler handler = fromPlayer(player);
        NamedPosition namedPosition = player.position();
        if (handler.container().removeSpawn(color, index) == null) {
            player.renderResource(String.format(INVALID_INDEX, index));
            return false;
        }

        String message = String.format(SPAWN_REMOVE, color.coloredName(), index, namedPosition.format());
        player.renderResource(message);
        return true;
    }
    
    @Command(names = "spawns")
    public boolean listSpawns(@CSender APlayer player) {
        MatchMapEditionHandler handler = fromPlayer(player);

        for (TeamColor team : TeamColor.values()) {
            List<NamedPosition> spawns = handler.container().getTeamSpawns(team);

            String header = team.coloredName() + " &bspawn list: ";

            if (spawns.isEmpty()) {
                header += "&cempty";
            }

            player.renderResource(header);

            int index = 0;

            for (NamedPosition spawn : spawns) {
                String message = String.format(LISTING_POSITION_FORMAT, index, spawn.formatWithWorld());

                TextComponent component = new TextComponent(message);

                ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mapeditor removespawn " + team.name() + " " + index);

                component.setClickEvent(clickEvent);

                player.renderResource(component);
                ++index;
            }
        }

        return true;
    }
    
    @Command(names ="setnexus")
    public boolean setNexus(@CSender final APlayer player, TeamColor teamColor, int health) {
        MatchMapEditionHandler handler = fromPlayer(player);
        Block block = player.bukkitPlayer().getTargetBlock(null, 5);

        if (!block.getType().isSolid()) {
            player.renderResource(NOT_FACING_BLOCK);
            return true;
        }

        if (health <= 0) {
            player.renderResource("&cHealth cannot be zero or less.");
            return true;
        }

        NamedPosition namedPosition = Position.ofLocation(block.getLocation());
        handler.container().setTeamNexus(teamColor, namedPosition, health);
        player.renderResource(String.format(NEXUS_SET, teamColor.coloredName(), namedPosition.formatWithWorld(), health));

        return true;
    }
    
    @Command(names = { "listnexus" })
    public boolean listNexus(@CSender APlayer player) {
        MatchMapEditionHandler handler = fromPlayer(player);

        for (TeamColor team : TeamColor.values()) {
            Pair<NamedPosition, Integer> nexus = handler.container().getTeamNexus(team);

            if (nexus == null) {
                player.renderResource("&cNo nexus data found for team '" + team.formalName() + "'");
                continue;
            }

            NamedPosition position = nexus.left();
            int health = nexus.right();

            String message = String.format(LISTING_NEXUS_FORMAT, team.coloredName(), position.formatWithWorld(), health);
            player.renderResource(message);
        }

        return true;
    }
    
    @Command(names = "addboss")
    public boolean addBoss(@CSender APlayer player, String bossName, BossType bossType, String bossDisplayName, int health) {
        MatchMapEditionHandler handler = fromPlayer(player);
        MatchMapPropertiesContainer propertiesContainer = handler.container();
        BossModel bossModel = new BossModel(bossName, bossDisplayName, bossType.typeName());

        NamedPosition position = player.position();
        bossModel.setHealth(health);
        bossModel.setSpawnLocation(position);
        propertiesContainer.addBossModel(bossModel);

        String formattedBossInfo = String.format(BOSS_INFO_FORMAT, bossName, bossType.typeName(), health);
        String message = String.format(BOSS_ADDED_AT, formattedBossInfo, position.formatWithWorld());
        player.renderResource(message);
        return true;
    }
    
    @Command(names = "removeboss")
    public boolean removeBoss(@CSender APlayer player, String bossName) {
        MatchMapEditionHandler editionHandler = fromPlayer(player);
        MatchMapPropertiesContainer propertiesContainer = editionHandler.container();
        BossModel bossModel;

        if ((bossModel = propertiesContainer.removeBossModel(bossName)) == null) {
            player.renderResource(String.format(BOSS_DOES_NOT_EXISTS, bossName));
            return true;
        }

        String formattedBossInfo = String.format(BOSS_INFO_FORMAT, bossName, bossModel.typeName(), bossModel.health());
        String message = String.format(BOSS_REMOVED, formattedBossInfo);
        player.renderResource(message);
        return true;
    }
    
    @Command(names = { "bosses" })
    public boolean listBosses(@CSender APlayer player) {
        MatchMapEditionHandler editionHandler = fromPlayer(player);
        MatchMapPropertiesContainer propertiesContainer = editionHandler.container();

        for (BossModel value : propertiesContainer.bossModels().values()) {
            String formatedBossInfo = String.format(PLACED_BOSS_INFO_FORMAT, value.name(), value.typeName(), value.health(), value.spawnLocation().formatWithWorld());
            BaseComponent baseComponent = new TextComponent(formatedBossInfo);

            ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mapeditor removeboss " + value.name());
            baseComponent.setClickEvent(clickEvent);

            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] {
                    new TextComponent(ChatColor.GREEN + "Click to remove the boss")
            });
            baseComponent.setHoverEvent(hoverEvent);

            player.renderResource(baseComponent);
        }
        return true;
    }
    
    @Command(names = { "setdiamondore" })
    public boolean setDiamondOre(@CSender APlayer player) {
        MatchMapEditionHandler editionHandler = fromPlayer(player);
        MatchMapPropertiesContainer propertiesContainer = editionHandler.container();
        Block facingAt = player.bukkitPlayer().getTargetBlock(null, 5);

        if (!facingAt.getType().isSolid()) {
            player.renderResource(NOT_FACING_BLOCK);
            return true;
        }

        NamedPosition position = Position.ofLocation(facingAt.getLocation());
        propertiesContainer.addDiamondPosition(position);
        player.renderResource(String.format(DIAMOND_PLACED, position.formatWithWorld()));
        return true;
    }
    
    @Command(names = { "removediamondore" })
    public boolean removeDiamondOre(@CSender APlayer player, @OptArg Integer index) {
        MatchMapEditionHandler editionHandler = fromPlayer(player);
        MatchMapPropertiesContainer propertiesContainer = editionHandler.container();
        NamedPosition position;

        if (index == null) {
            Block facingAt = player.bukkitPlayer().getTargetBlock(null, 5);
            if (!facingAt.getType().isSolid()) {
                player.renderResource(NOT_FACING_BLOCK);
                return true;
            }

            position = Position.ofLocation(facingAt.getLocation());
            if (!propertiesContainer.removeDiamondPosition(position)) {
                player.renderResource(NO_DIAMOND_ORE_AT);
                return true;
            }
        } else if ((position = propertiesContainer.removeDiamondPosition(index)) == null) {
            player.renderResource(String.format(NO_DIAMOND_ORE_AT_INDEX, index));
            return true;
        }

        player.renderResource(String.format(DIAMOND_REMOVED, position.formatWithWorld()));
        return true;
    }
    
    @Command(names = { "diamondores" })
    public boolean listDiamondOres(@CSender final APlayer player) {
        MatchMapEditionHandler handler = fromPlayer(player);
        MatchMapPropertiesContainer propertiesContainer = handler.container();

        int index = 0;
        for (NamedPosition position : propertiesContainer.specialDiamondsPositions()) {
            String message = String.format(LISTING_POSITION_FORMAT, index, position.formatWithWorld());
            BaseComponent baseComponent = new TextComponent(message);
            baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mapeditor removediamondore " + index));
            player.renderResource(baseComponent);
            ++index;
        }

        return true;
    }
    
    @Command(names = { "setdiamondregenerationtime" })
    public boolean setDiamondOreRegenerationTime(@CSender APlayer player, int timeInSeconds) {
        MatchMapEditionHandler handler = fromPlayer(player);
        MatchMapPropertiesContainer propertiesContainer = handler.container();
        if (timeInSeconds <= 0) {
            player.renderResource(NEGATIVE_NUMBER);
            return true;
        }

        propertiesContainer.setDiamondRegenerationTime(timeInSeconds);
        player.renderResource(String.format(DIAMOND_REGENERATION_SETTED, timeInSeconds));
        return true;
    }
    
    @Command(names = { "showdiamondregenerationtime" })
    public boolean showDiamondRegenerationTime(@CSender final APlayer player) {
        final MatchMapEditionHandler handler = fromPlayer(player);
        final MatchMapPropertiesContainer propertiesContainer = handler.container();
        player.renderResource(String.format("&bDiamond &aregeneration time are &e%s &aseconds.", propertiesContainer.diamondRegenerationTime()));
        return true;
    }
    
    private static MatchMapEditionHandler fromPlayer(APlayer aPlayer) {
        EditionInstance editionInstance = aPlayer.editionInstance();
        if (editionInstance == null) {
            throw new CommandUsage(MapEditorCommand.NOT_EDITING_A_MAP);
        }

        MatchMapEditionHandler handler = editionInstance.configurationHandler(MatchMapEditionHandler.class);
        if (handler == null) {
            throw new CommandUsage(MapEditorCommand.NOT_EDITING_A_MAP);
        }

        return handler;
    }
}
