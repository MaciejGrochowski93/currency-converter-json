package maciej.grochowski.jsonapi.controller;

import lombok.AllArgsConstructor;
import maciej.grochowski.jsonapi.currency.Currency;
import maciej.grochowski.jsonapi.domain.Money;
import maciej.grochowski.jsonapi.service.JsonParseService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static maciej.grochowski.jsonapi.service.JsonParseService.CHF;

@RestController
@AllArgsConstructor
public class MoneyController {

    private final JsonParseService service;

    @GetMapping("/{bid}/{ask}")
    public String CHFRateEuro(@PathVariable Currency bid, @PathVariable Currency ask) {
        return service.calculateExchange(bid, ask);
    }

    @GetMapping("/chf")
    public Money CHFRate() {
        return service.parse(CHF);
    }
}
