package com.github.ynverxe.dtn.dimension.properties;

import com.github.ynverxe.dtn.model.data.BossModel;
import com.github.ynverxe.dtn.model.data.MatchMapBlockData;
import com.github.ynverxe.dtn.model.instance.ImplicitlyDeserializable;
import com.github.ynverxe.dtn.team.TeamColor;
import com.github.ynverxe.dtn.world.NamedPosition;
import com.github.ynverxe.structured.data.ModelDataList;
import com.github.ynverxe.structured.data.ModelDataTree;
import com.github.ynverxe.structured.data.ModelDataTreeImpl;
import com.github.ynverxe.structured.data.ModelDataValue;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.ynverxe.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MatchMapPropertiesContainer extends ModelDataTreeImpl implements PropertiesContainer {

    private static final ModelDataTree DEFAULT_NEXUS = new ModelDataTreeImpl();

    static {
        DEFAULT_NEXUS.setValue("position", NamedPosition.ZERO);
        DEFAULT_NEXUS.setValue("health", 10);
    }

    public void addSpawn(@NotNull TeamColor color, @NotNull NamedPosition position) {
        getTeamList(color, "spawns").add(new ModelDataValue(position));
    }

    @Nullable
    public NamedPosition removeSpawn(@NotNull TeamColor color, int index) {
        ModelDataList list = getTeamList(color, "spawns");

        ModelDataValue removed = list.remove(index);

        if (removed == null) return null;

        return NamedPosition.PARSER.parseDataValue(removed);
    }

    @NotNull
    public List<NamedPosition> getTeamSpawns(@NotNull TeamColor color) {
        return getTeamList(color, "spawns")
                .stream()
                .map(NamedPosition.PARSER::parseDataValue)
                .collect(Collectors.toList());
    }

    public void setTeamNexus(@NotNull TeamColor color, @NotNull NamedPosition position, int health) {
        ModelDataTree tree = new ModelDataTreeImpl();

        tree.setValue("position", position);
        tree.setValue("health", health);

        getTeamSection(color).setValue("nexus", tree);
    }

    public @Nullable Pair<NamedPosition, Integer> getTeamNexus(@NotNull TeamColor color) {
        ModelDataTree teamSection;
        if (!(teamSection = getTeamSection(color)).contains("nexus")) return null;

        ModelDataTree nexus = teamSection.getValue("nexus").asTree();

        NamedPosition position = NamedPosition.PARSER.parseDataValue(nexus.getValue("position"));
        int health = nexus.mapValue("health", ModelDataValue::asInt);

        return new Pair<>(position, health);
    }

    public void addBossModel(@NotNull BossModel bossModel) {
        if (!bossModel.isAllBasicFieldsCompleted())
            throw new IllegalArgumentException("Incomplete BossModel");

        getBossesSection().setValue(bossModel.name(), bossModel);
    }

    @Nullable
    public BossModel removeBossModel(@NotNull String bossModelName) {
        ModelDataTree bosses = getBossesSection();

        ModelDataValue removed = bosses.removeValue(bossModelName);

        if (removed == null) return null;

        return new BossModel(removed.asTree());
    }

    @NotNull
    public Map<String, BossModel> bossModels() {
        return getBossesSection().entries()
                .stream()
                .map(Map.Entry::getValue)
                .map(BossModel::new)
                .collect(Collectors.toMap(BossModel::name, bossModel -> bossModel));
    }

    @Override
    public @NotNull String typeName() {
        return "match-map";
    }

    @NotNull
    public MatchMapPropertiesContainer copy() {
        MatchMapPropertiesContainer container = new MatchMapPropertiesContainer();
        container.consume(this, true);
        return container;
    }

    public void addBlockData(@NotNull NamedPosition namedPosition, @NotNull MatchMapBlockData blockData) {
        ModelDataTree blockDataTree = setValueIfAbsent("blockData", ModelDataTreeImpl::new).asTree();

        blockDataTree.setValue(namedPosition.normalizeAsBlock().formatWithWorld(), blockData);
    }

    public MatchMapBlockData removeBlockData(@NotNull NamedPosition namedPosition) {
        ModelDataValue removed = removeValue("blockData." + namedPosition.normalizeAsBlock().formatWithWorld());

        if (removed == null) return null;

        return ImplicitlyDeserializable.expectModel(removed, MatchMapBlockData::new);
    }

    @NotNull
    public Map<NamedPosition, MatchMapBlockData> blockDataMap() {
        return getBlockDataSection().entries()
                .stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(
                        NamedPosition.fromLine(entry.getKey()),
                        ImplicitlyDeserializable.expectModel(entry.getValue(), MatchMapBlockData::new)
                ))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void addDiamondPosition(@NotNull NamedPosition namedPosition) {
        getDiamondList().add(namedPosition.normalizeAsBlock().toModelData());
    }

    public boolean removeDiamondPosition(@NotNull NamedPosition namedPosition) {
        return getDiamondList().remove(namedPosition.normalizeAsBlock().toModelData());
    }

    @Nullable
    public NamedPosition removeDiamondPosition(int index) {
        ModelDataList diamonds = getDiamondList();

        if (index >= diamonds.size() || index < 0)
            return null;

        return NamedPosition.PARSER.parseDataValue(diamonds.remove(index));
    }

    @NotNull
    public List<NamedPosition> specialDiamondsPositions() {
        return getDiamondList().stream()
                .map(NamedPosition.PARSER::parseDataValue)
                .collect(Collectors.toList());
    }

    public int diamondRegenerationTime() {
        return setValueIfAbsent("diamondRegenerationTime", () -> 10).asInt();
    }

    public void setDiamondRegenerationTime(int diamondRegenerationTime) {
        if (diamondRegenerationTime <= 0)
            throw new IllegalArgumentException("diamondRegenerationTime <= 0");

        setValue("diamondRegenerationTime", diamondRegenerationTime);
    }

    private ModelDataList getDiamondList() {
        return setListIfAbsent("diamonds");
    }

    private ModelDataTree getTeamSection(TeamColor color) {
        return setTreeIfAbsent("teams." + color.lowerCaseName());
    }

    private ModelDataTree getTeamSection(TeamColor color, String sectionName) {
        return setTreeIfAbsent("teams." + color.lowerCaseName()).setTreeIfAbsent(sectionName);
    }

    private ModelDataList getTeamList(TeamColor color, String listName) {
        return setTreeIfAbsent("teams." + color.lowerCaseName()).setListIfAbsent(listName);
    }

    private ModelDataTree getBossesSection() {
        return setTreeIfAbsent("bosses");
    }

    private ModelDataTree getBlockDataSection() {
        return setTreeIfAbsent("blockData");
    }
}