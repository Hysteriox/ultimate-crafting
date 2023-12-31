package mc.ultimatecore.runes.enums;

import mc.ultimatecore.runes.HyperRunes;

import java.util.ArrayList;
import java.util.List;

public enum RuneState {

    WAITING(new ArrayList<>()),
    NO_REQUIRED_LEVEL(HyperRunes.getInstance().getMessages().getDescription("noRequiredLevel")),
    INCOMPATIBLE_ITEMS(HyperRunes.getInstance().getMessages().getDescription("incompatibleItemAndRune")),
    INCOMPATIBLE_RUNE_LEVEL(HyperRunes.getInstance().getMessages().getDescription("incompatibleRunesTier")),
    INCOMPATIBLE_RUNE_TYPE(HyperRunes.getInstance().getMessages().getDescription("incompatibleRunesType")),
    MAX_RUNE_LEVEL(HyperRunes.getInstance().getMessages().getDescription("maxRuneLevel")),
    NO_ERROR_ITEMS(new ArrayList<>()),
    NO_ERROR_RUNES(new ArrayList<>());

    public final List<String> description;

    RuneState(List<String> description){
        this.description = description;
    }

}
