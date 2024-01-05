package dev.imlukas.partycrackersplugin.util.item.trim;

import org.bukkit.inventory.meta.trim.TrimPattern;

public enum TrimPatternType {

    SENTRY(TrimPattern.SENTRY),
    DUNE(TrimPattern.DUNE),
    COAST(TrimPattern.COAST),
    WILD(TrimPattern.WILD),
    WARD(TrimPattern.WARD),
    EYE(TrimPattern.EYE),
    VEX(TrimPattern.VEX),
    TIDE(TrimPattern.TIDE),
    SNOUT(TrimPattern.SNOUT),
    RIB(TrimPattern.RIB),
    SPIRE(TrimPattern.SPIRE),
    WAYFINDER(TrimPattern.WAYFINDER),
    SHAPER(TrimPattern.SHAPER),
    SILENCE(TrimPattern.SILENCE),
    RAISER(TrimPattern.RAISER),
    HOST(TrimPattern.HOST);


    private final TrimPattern pattern;

    TrimPatternType(TrimPattern pattern) {
        this.pattern = pattern;
    }

    public TrimPattern getPattern() {
        return pattern;
    }
}
