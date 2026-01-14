package com.jakuch.PartySheetShow.open5e;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "open5e.client")
@Data
@Component
public class Open5eProperties {

    private int pagination;

    private String defaultGamesystem;

    private boolean useDefaultGamesystem;
}
