package com.jakuch.PartySheetShow.player.character.model.skill;

import com.jakuch.PartySheetShow.open5e.dataParser.ParserHelper;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public enum ToolName {
    ALCHEMISTS_SUPPLIES("Alchemist's Supplies", "srd_alchemists-supplies", Type.TOOL),
    BAGPIPES("Bagpipes", "srd_bagpipes", Type.INSTRUMENT),
    BREWERS_SUPPLIES("Brewer's Supplies", "srd_brewers-supplies", Type.TOOL),
    CALLIGRAPHERS_SUPPLIES("Calligrapher's supplies", "srd_calligraphers-supplies", Type.TOOL),
    CARPENTERS_TOOLS("Carpenter's Tools", "srd_carpenters-tools", Type.TOOL),
    CARTOGRAPHERS_TOOLS("Cartographer's Tools", "srd_cartographers-tools", Type.TOOL),
    COBBLERS_TOOLS("Cobbler's Tools", "srd_cobblers-tools", Type.TOOL),
    COOKS_UTENSILS("Cook's utensils", "srd_cooks-utensils", Type.TOOL),
    DICE_SET("Dice set", "srd_dice-set", Type.GAMING_SET),
    DISGUISE_KIT("Disguise kit", "srd_disguise-kit", Type.TOOL),
    DRUM("Drum", "srd_drum", Type.INSTRUMENT),
    DULCIMER("Dulcimer", "srd_dulcimer", Type.INSTRUMENT),
    FLUTE("Flute", "srd_flute", Type.INSTRUMENT),
    FORGERY_KIT("Forgery kit", "srd_forgery-kit", Type.TOOL),
    GLASSBLOWERS_TOOLS("Glassblower's Tools", "srd_glassblowers-tools", Type.TOOL),
    HERBALISM_KIT("Herbalism Kit", "srd_herbalism-kit", Type.TOOL),
    HORN("Horn", "srd_horn", Type.INSTRUMENT),
    JEWELERS_TOOLS("Jeweler's Tools", "srd_jewelers-tools", Type.TOOL),
    LEATHERWORKERS_TOOLS("Leatherworker's Tools", "srd_leatherworkers-tools", Type.TOOL),
    LUTE("Lute", "srd_lute", Type.INSTRUMENT),
    LYRE("Lyre", "srd_lyre", Type.INSTRUMENT),
    MASONS_TOOLS("Mason's Tools", "srd_masons-tools", Type.TOOL),
    NAVIGATORS_TOOLS("Navigator's tools", "srd_navigators-tools", Type.TOOL),
    PAINTERS_SUPPLIES("Painter's Supplies", "srd_painters-supplies", Type.TOOL),
    PAN_FLUTE("Pan flute", "srd_pan-flute", Type.INSTRUMENT),
    PLAYING_CARD_SET("Playing card set", "srd_playing-card-set", Type.GAMING_SET),
    POISONERS_KIT("Poisoner's kit", "srd_poisoners-kit", Type.TOOL),
    POTTERS_TOOLS("Potter's tools", "srd_potters-tools", Type.TOOL),
    SHAWM("Shawm", "srd_shawm", Type.INSTRUMENT),
    SMITHS_TOOLS("Smith's tools", "srd_smiths-tools", Type.TOOL),
    THIEVES_TOOLS("Thieves' tools", "srd_thieves-tools", Type.TOOL),
    TINKERS_TOOLS("Tinker's tools", "srd_tinkers-tools", Type.TOOL),
    VIOL("Viol", "srd_viol", Type.INSTRUMENT),
    WEAVERS_TOOLS("Weaver's tools", "srd_weavers-tools", Type.TOOL),
    WOODCARVERS_TOOLS("Woodcarver's tools", "srd_woodcarvers-tools", Type.TOOL);

    private final String displayName;
    private final String srdKey;
    private final Type type;

    ToolName(String displayName, String srdKey, Type type) {
        this.displayName = displayName;
        this.srdKey = srdKey;
        this.type = type;
    }

    public static Optional<ToolName> toolsFromName(String name) {
        if (name == null) return Optional.empty();
        var normalized = normalize(name);
        return tools().stream()
                .filter(el -> normalize(el.displayName).equals(normalized))
                .findFirst();
    }

    public static List<ToolName> fromDescription(String description) {
        var results = Arrays.stream(values())
                .filter(tool -> description.toLowerCase().contains(tool.getDisplayName().toLowerCase()))
                .toList();
        if (results.isEmpty()) {
            results = Arrays.stream(values())
                    .filter(tool -> {
                        var first = Arrays.stream(ParserHelper.removeSpecialCharacters(tool.getDisplayName()).split(" ")).toList().getFirst();
                        return description.toLowerCase().contains(first.toLowerCase());
                    })
                    .toList();
        }
        return results;
    }

    public static List<ToolName> instrumentsFromDescription(String description) {
        return instruments().stream()
                .filter(tool -> description.toLowerCase().contains(tool.getDisplayName().toLowerCase()))
                .toList();
    }

    public static List<ToolName> toolsFromDescription(String description) {
        return tools().stream()
                .filter(tool -> description.toLowerCase().contains(tool.getDisplayName().toLowerCase()))
                .toList();
    }

    public static List<ToolName> all() {
        return Arrays.stream(values()).toList();
    }
    public static List<ToolName> instruments() {
        return Arrays.stream(values()).filter(el -> el.type.equals(Type.INSTRUMENT)).toList();
    }

    public static List<ToolName> tools() {
        return Arrays.stream(values()).filter(el -> el.type.equals(Type.TOOL)).toList();
    }

    public static List<ToolName> gamingSet() {
        return Arrays.stream(values()).filter(el -> el.type.equals(Type.GAMING_SET)).toList();
    }

    private static String normalize(String s) {
        return s.toLowerCase().replaceAll("[^a-z]", "");
    }

    public enum Type {
        TOOL,
        INSTRUMENT,
        GAMING_SET
    }
}
