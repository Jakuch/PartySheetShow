package com.jakuch.partySheetShow.open5e.client;

import java.util.List;

public record Open5eResponse<T>(
        int count,
        String next,
        String previous,
        List<T> results
) {
}
