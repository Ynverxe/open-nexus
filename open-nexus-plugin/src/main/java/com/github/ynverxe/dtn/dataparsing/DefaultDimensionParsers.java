package com.github.ynverxe.dtn.dataparsing;

import com.github.ynverxe.dtn.builder.MatchMapBuildContext;
import com.github.ynverxe.dtn.data.ModelDataConsumer;
import com.github.ynverxe.structured.data.ModelDataValue;

public final class DefaultDimensionParsers {

    private DefaultDimensionParsers() {}

    public static final ModelDataConsumer<MatchMapBuildContext> MATCH_MAP_DATA_CONSUMER = ModelDataConsumer.create(MatchMapBuildContext.class)
            .consumeData("teams", MatchMapBuildContext::setTeamData, false)
            .consumeData("blockData", MatchMapBuildContext::addBlockData, true)
            .consumeData("bosses", MatchMapBuildContext::addBosses, true)
            .consumeData("diamonds", MatchMapBuildContext::setDiamonds, true)
            .parseAndConsume("diamondRegenerationTime", ModelDataValue::asInt, MatchMapBuildContext::setDiamondRegenTime, true);

}