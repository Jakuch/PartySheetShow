package com.jakuch.PartySheetShow.open5e.races;

import com.jakuch.PartySheetShow.open5e.races.model.Race;
import com.jakuch.PartySheetShow.open5e.races.service.RaceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/races")
public class RaceController {

    private final RaceService raceService;


    @GetMapping
    public List<Race> getAll() {
        return raceService.getAll();
    }
}
