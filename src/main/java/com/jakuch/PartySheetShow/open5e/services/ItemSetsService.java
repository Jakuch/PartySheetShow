package com.jakuch.PartySheetShow.open5e.services;

import com.jakuch.PartySheetShow.open5e.Open5eProperties;
import com.jakuch.PartySheetShow.open5e.Open5eServiceBase;
import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import com.jakuch.PartySheetShow.open5e.model.Open5eItemSet;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.jakuch.PartySheetShow.open5e.Open5eTypeReferences.ITEM_SET;

@Service
public class ItemSetsService extends Open5eServiceBase<Open5eItemSet> {
    public ItemSetsService(Open5eClient open5eClient, Open5eProperties open5eProperties) {
        super(open5eClient, open5eProperties, "/itemsets/", ITEM_SET, Open5eItemSet.class);
    }

    public Optional<Open5eItemSet> getSet(ItemSetType type) {
        var itemSet = getByKey(type.getSrdKey());
        itemSet.ifPresent(el -> el.setType(type));
        return itemSet;
    }

    @Getter
    public enum ItemSetType {
        MEDIUM_ARMOR("medium-armor"),
        ENTERTAINERS_PACK("entertainers-pack"),
        DIPLOMATS_PACK("diplomats-pack"),
        PRIESTS_PACK("priests-pack"),
        BURGLARS_PACK("burglars-pack"),
        EXPLORERS_PACK("explorers-pack"),
        SCHOLARS_PACK("scholars-pack"),
        SIMPLE_MELEE_WEAPONS("simple-melee-weapons"),
        SIMPLE_RANGED_WEAPONS("simple-ranged-weapons"),
        MARTIAL_MELEE_WEAPONS("martial-melee-weapons"),
        MARTIAL_RANGED_WEAPONS("martial-ranged-weapons"),
        LIGHT_ARMOR("light-armor"),
        HEAVY_ARMOR("heavy-armor"),
        ARTISANS_TOOLS("artisans-tools"),
        GAMING_SETS("gaming-sets"),
        MUSICAL_INSTRUMENTS("musical-instruments"),
        ARCANE_FOCUSES("arcane-focuses"),
        DRUIDIC_FOCUSES("druidic-focuses"),
        HOLY_SYMBOLS("holy-symbols"),
        DUNGEONEERS_PACK("dungeoneers-pack");

        private final String srdKey;

        ItemSetType(String srdKey) {
            this.srdKey = srdKey;
        }
    }
}
