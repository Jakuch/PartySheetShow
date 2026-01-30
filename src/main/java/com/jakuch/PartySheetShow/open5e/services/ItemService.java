package com.jakuch.PartySheetShow.open5e.services;

import com.jakuch.PartySheetShow.open5e.Open5eProperties;
import com.jakuch.PartySheetShow.open5e.Open5eServiceBase;
import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import com.jakuch.PartySheetShow.open5e.model.Open5eItem;
import com.jakuch.PartySheetShow.open5e.model.Open5eItemSet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.jakuch.PartySheetShow.open5e.Open5eTypeReferences.ITEM;

@Service
public class ItemService extends Open5eServiceBase<Open5eItem> {

    private final ItemSetsService itemSetsService;

    public ItemService(Open5eClient open5eClient, Open5eProperties open5eProperties, ItemSetsService itemSetsService) {
        super(open5eClient, open5eProperties, "/items/", ITEM, Open5eItem.class);
        this.itemSetsService = itemSetsService;
    }

    public List<Open5eItemSet> getAllSets() {
        return itemSetsService.getAll();
    }

    public Optional<Open5eItemSet> getSet(ItemSetsService.ItemSetType key) {
        return itemSetsService.getSet(key);
    }
}
