package com.jakuch.PartySheetShow.open5e.races.service;

import com.jakuch.PartySheetShow.open5e.Open5eServiceBase;
import com.jakuch.PartySheetShow.open5e.client.Open5eClient;
import com.jakuch.PartySheetShow.open5e.client.Open5eResponse;
import com.jakuch.PartySheetShow.open5e.races.model.Race;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceService extends Open5eServiceBase<Race> {

    private final static ParameterizedTypeReference<Open5eResponse<Race>> TYPE = new ParameterizedTypeReference<>() {
    };

    public RaceService(Open5eClient open5eClient) {
        super(open5eClient, "/species/", TYPE, Race.class);
    }

    @Override
    @Cacheable("racesAll")
    public List<Race> getAll() {
        return super.getAll();
    }
}
