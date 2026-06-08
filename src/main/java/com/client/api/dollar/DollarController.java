package com.client.api.dollar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange-rates/usd")
public class DollarController {

    @Autowired
    private DollarService dollarService;

    @GetMapping
    public Dollar getDollarPrice() {
        return dollarService.getOfficialDollar();
    }

}
