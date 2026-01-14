package com.jakuch.PartySheetShow.open5e.services;

import com.jakuch.PartySheetShow.open5e.Open5eProperties;
import com.jakuch.PartySheetShow.open5e.Open5eServiceBase;
import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import com.jakuch.PartySheetShow.player.character.model.Language;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.jakuch.PartySheetShow.open5e.Open5eTypeReferences.LANGUAGE;

@Service
public class LanguageService extends Open5eServiceBase<Language> {
    public LanguageService(Open5eClient open5eClient, Open5eProperties open5eProperties) {
        super(open5eClient, open5eProperties, "/languages", LANGUAGE, Language.class);
    }

    @Override
    @Cacheable("languagesAll")
    public List<Language> getAll() {
        return super.getAll();
    }
}
