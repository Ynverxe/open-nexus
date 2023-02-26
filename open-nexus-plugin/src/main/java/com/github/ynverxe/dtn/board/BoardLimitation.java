package com.github.ynverxe.dtn.board;

import com.github.ynverxe.util.Version;
import java.util.HashMap;
import java.util.Objects;
import java.util.Map;

public final class BoardLimitation {
    public static final BoardLimitation CURRENT;
    private static final Map<Integer, BoardLimitation> LIMITATION_MAP;
    private final int maxTitleChars;
    private final int maxScoreChars;
    private final int maxTeamPartChars;
    
    public BoardLimitation(int maxTitleChars, int maxScoreChars, int maxTeamPartChars) {
        this.maxTitleChars = maxTitleChars;
        this.maxScoreChars = maxScoreChars;
        this.maxTeamPartChars = maxTeamPartChars;
    }
    
    public int getMaxTitleChars() {
        return this.maxTitleChars;
    }
    
    public int getMaxScoreChars() {
        return this.maxScoreChars;
    }
    
    public int getMaxTeamPartChars() {
        return this.maxTeamPartChars;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BoardLimitation that = (BoardLimitation)o;
        return this.maxTitleChars == that.maxTitleChars && this.maxScoreChars == that.maxScoreChars && this.maxTeamPartChars == that.maxTeamPartChars;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.maxTitleChars, this.maxScoreChars, this.maxTeamPartChars);
    }
    
    public static BoardLimitation get1_8Schema() {
        return new BoardLimitation(32, 40, 16);
    }
    
    public static BoardLimitation get1_13Schema() {
        return new BoardLimitation(128, 40, 64);
    }
    
    public static BoardLimitation get1_18Schema() {
        return new BoardLimitation(128, 32767, 64);
    }
    
    static {
        (LIMITATION_MAP = new HashMap<>()).put(new Version("v1_8").minorVersionNumber(), get1_8Schema());
        BoardLimitation.LIMITATION_MAP.put(new Version("v1_13").minorVersionNumber(), get1_13Schema());
        BoardLimitation.LIMITATION_MAP.put(new Version("v1_18").minorVersionNumber(), get1_18Schema());
        BoardLimitation candidate = get1_8Schema();
        int candidateVersionNumber = new Version("v1_8").minorVersionNumber();
        final int currentVersionNumber = Version.current().minorVersionNumber();
        for (Map.Entry<Integer, BoardLimitation> entry : BoardLimitation.LIMITATION_MAP.entrySet()) {
            final int limitationVersion = entry.getKey();
            final boolean isLimitationSupported = currentVersionNumber >= limitationVersion;
            final boolean isGoodCandidate = limitationVersion >= candidateVersionNumber;
            if (isLimitationSupported && isGoodCandidate) {
                candidateVersionNumber = entry.getKey();
                candidate = entry.getValue();
            }
        }
        CURRENT = candidate;
    }
}
