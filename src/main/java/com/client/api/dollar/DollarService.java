package com.client.api.dollar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DollarService {

    @Autowired
    private DollarApiClient dollarApiClient;

    public Dollar getOfficialDollar() {
        return dollarApiClient.getOfficialDollar();
    }
}