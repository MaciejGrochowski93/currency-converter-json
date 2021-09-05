package maciej.grochowski.currencyapi.controller;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.service.CalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static maciej.grochowski.currencyapi.currency.CurrencyType.PLN;

@RestController
@AllArgsConstructor
public class MoneyController {

    private final CalculationService calcService;

    @ApiOperation(value = "Allows you to calculate precise amount of money you could possibly get for exchanging currencies.")
    @GetMapping("/{bid}/{amount}/{ask}")
    public ResponseEntity<String> exchangeValues(@PathVariable CurrencyType bid, @PathVariable BigDecimal amount, @PathVariable CurrencyType ask) {
        calcService.validateAmount(amount);
        if (ask.equals(bid)) {
            return ResponseEntity.badRequest().body("You cannot exchange a Currency with itself.");
        }
        if (ask == PLN || bid == PLN) {
            return ResponseEntity.ok(String.format("%.2f %s can be exchanged for %.2f %s.",
                    amount, bid, calcService.customToForeignCurrency(bid, amount, ask), ask));
        }
        return ResponseEntity.ok(String.format("%.2f %s can be exchanges for %.2f %s.",
                amount, bid, calcService.foreignToForeignCurrency(bid, amount, ask), ask));
    }
}