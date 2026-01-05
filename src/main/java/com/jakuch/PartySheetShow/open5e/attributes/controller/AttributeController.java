package com.jakuch.PartySheetShow.open5e.attributes.controller;

import com.jakuch.PartySheetShow.open5e.attributes.model.Attribute;
import com.jakuch.PartySheetShow.open5e.attributes.service.AttributeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/attributes")
@AllArgsConstructor
public class AttributeController {

    private AttributeService attributeService;

    @GetMapping
    public List<Attribute> getAttributes() {
        return attributeService.getAll();
    }
}
