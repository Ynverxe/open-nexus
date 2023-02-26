package com.github.ynverxe.dtn.command;

import com.github.ynverxe.dtn.command.part.DimensionPart;
import com.github.ynverxe.dtn.command.part.GameRoomPart;
import com.github.ynverxe.dtn.command.part.MapPart;
import com.github.ynverxe.dtn.command.part.TeamColorPart;
import com.github.ynverxe.dtn.command.part.BossTypePart;
import com.github.ynverxe.dtn.command.part.GameInstancePart;
import com.github.ynverxe.dtn.game.GameInstance;
import com.github.ynverxe.dtn.boss.BossType;
import com.github.ynverxe.dtn.team.TeamColor;
import java.util.Map;
import com.github.ynverxe.dtn.game.GameRoom;
import com.github.ynverxe.dtn.command.part.AnniPlayerPartFactory;
import com.github.ynverxe.dtn.player.APlayer;
import com.github.ynverxe.dtn.dimension.Dimension;
import com.github.ynverxe.dtn.command.part.MessageReceiverPartFactory;
import me.fixeddev.commandflow.annotated.part.Key;
import com.github.ynverxe.dtn.translation.ResourceReceiver;
import me.fixeddev.commandflow.annotated.part.AbstractModule;

public class DTNModule extends AbstractModule {
    public void configure() {
        this.bindFactory(new Key(ResourceReceiver.class), new MessageReceiverPartFactory());
        this.bindFactory(new Key(Dimension.class), (s, list) -> new DimensionPart(s));
        this.bindFactory(new Key(APlayer.class), new AnniPlayerPartFactory());
        this.bindFactory(new Key(GameRoom.class), (s, list) -> new GameRoomPart(s));
        this.bindFactory(new Key(Map.class), (s, list) -> new MapPart(s));
        this.bindFactory(new Key(TeamColor.class), (s, list) -> new TeamColorPart(s));
        this.bindFactory(new Key(BossType.class), (s, list) -> new BossTypePart(s));
        this.bindFactory(new Key(GameInstance.class), (s, list) -> new GameInstancePart(s));
    }
}
