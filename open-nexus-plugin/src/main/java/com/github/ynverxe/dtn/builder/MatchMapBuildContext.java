package com.github.ynverxe.dtn.builder;

import com.github.ynverxe.dtn.boss.PreparedBoss;
import com.github.ynverxe.dtn.context.DimensionParsingContext;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.factory.BossFactory;
import com.github.ynverxe.dtn.map.MatchMap;
import com.github.ynverxe.dtn.map.MatchMapImpl;
import com.github.ynverxe.dtn.map.Nexus;
import com.github.ynverxe.dtn.map.NexusImpl;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.model.data.BossModel;
import com.github.ynverxe.dtn.model.data.MatchMapBlockData;
import com.github.ynverxe.dtn.model.instance.ImplicitlyDeserializable;
import com.github.ynverxe.dtn.team.Team;
import com.github.ynverxe.dtn.team.TeamColor;
import com.github.ynverxe.dtn.world.NamedPosition;
import com.github.ynverxe.structured.data.ModelDataList;
import com.github.ynverxe.structured.data.ModelDataTree;
import com.github.ynverxe.structured.data.ModelDataValue;
import com.github.ynverxe.structured.exception.UnexpectedValueStateException;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchMapBuildContext extends DimensionParsingContext {

    private final Map<TeamColor, List<Location>> teamSpawns = new HashMap<>();
    private final Map<TeamColor, Nexus> nexusMap = new HashMap<>();
    private final Map<NamedPosition, MatchMapBlockData> blockDataMap = new HashMap<>();
    private final List<PreparedBoss> bosses = new ArrayList<>();
    private final List<Location> diamonds = new ArrayList<>();
    private int diamondRegenTime = 5;
    private final Match match;

    public MatchMapBuildContext(Dimension dimension, Match match) {
        super(dimension, match.room().name());
        this.match = match;
    }

    public void setTeamData(ModelDataValue value) {
        ModelDataTree tree = value.asTree();

        for (TeamColor color : TeamColor.values()) {
            List<Location> spawns = new ArrayList<>();

            String teamTreeDataKey = color.lowerCaseName();

            tree.consumeValue(teamTreeDataKey + ".spawns", spawnsDataList -> {
                ModelDataList list = spawnsDataList.asList();

                if (list.isEmpty())
                    throw new UnexpectedValueStateException(color.formalName() + " spawns list is empty");

                for (ModelDataValue spawnDataValue : list) {
                    ModelDataTree spawnData = spawnDataValue.asTree();

                    Location location = createLocationBySchemeName(spawnData);
                    spawns.add(location);
                }
            });

            String nexusPath = teamTreeDataKey + ".nexus";

            Location location = tree.mapValue(nexusPath + ".position",
                    positionDataValue -> createLocationBySchemeName(positionDataValue.asTree()));

            int health = tree.mapValue(nexusPath + ".health", ModelDataValue::asInt);

            Team team = match.teams().get(color);

            Nexus nexus = new NexusImpl(team, location, health);

            nexusMap.put(color, nexus);
            teamSpawns.put(color, spawns);
        }
    }

    public void addBlockData(ModelDataValue blockDataSection) {
        ModelDataTree tree = blockDataSection.asTree();

        tree.forEach((k, v) -> {
            NamedPosition position = NamedPosition.fromLine(k);

            MatchMapBlockData blockData = ImplicitlyDeserializable.expectModel(v.copy(), MatchMapBlockData::new);
            blockDataMap.put(position, blockData);
        });
    }

    public void addBosses(ModelDataValue bossModelsSection) {
        ModelDataTree tree = bossModelsSection.asTree();

        tree.forEach((k, v) -> {
            BossModel bossModel = new BossModel(v.copy());

            bosses.add(BossFactory.createBoss(match, bossModel));
        });
    }

    public void setDiamondRegenTime(int diamondRegenTime) {
        this.diamondRegenTime = diamondRegenTime;
    }

    public void setDiamonds(ModelDataValue value) {
        ModelDataList list = value.asList();

        for (ModelDataValue modelDataValue : list) {
            this.diamonds.add(createLocationBySchemeName(modelDataValue.asTree()));
        }
    }

    public MatchMap build() {
        if (teamSpawns.isEmpty() || nexusMap.isEmpty())
            throw new IllegalStateException("Team properties was not initialized");

        MatchMap matchMap = new MatchMapImpl(
                match,
                worldContainer,
                teamSpawns,
                nexusMap,
                bosses,
                diamonds,
                diamondRegenTime
        );

        blockDataMap.forEach((k, v) -> {
            Block block = createLocationBySchemeName(k).getBlock();

            v.apply(block);
        });

        return matchMap;
    }
}