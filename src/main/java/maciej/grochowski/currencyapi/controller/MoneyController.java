package maciej.grochowski.currencyapi.controller;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.service.CalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static maciej.grochowski.currencyapi.currency.CurrencyType.PLN;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/exchange")
public class MoneyController {

    private final CalculationService calcService;

    @ApiOperation(value = "Allows you to calculate precise amount of money you could possibly get for exchanging currencies.")
    @GetMapping("/{sellCurrency}/{amount}/{buyCurrency}")
    public ResponseEntity<String> exchangeValues(@PathVariable CurrencyType sellCurrency, @PathVariable BigDecimal amount,
                                                 @PathVariable CurrencyType buyCurrency) {
        calcService.validateAmount(amount);
        if (buyCurrency.equals(sellCurrency)) {
            return ResponseEntity.badRequest().body("You cannot exchange a Currency with itself.");
        }
        if (buyCurrency == PLN || sellCurrency == PLN) {
            return ResponseEntity.ok(String.format("%.2f %s can be exchanged for %.2f %s.",
                    amount, sellCurrency, calcService.customToForeignCurrency(sellCurrency, amount, buyCurrency), buyCurrency));
        }
        return ResponseEntity.ok(String.format("%.2f %s can be exchanges for %.2f %s.",
                amount, sellCurrency, calcService.foreignToForeignCurrency(sellCurrency, amount, buyCurrency), buyCurrency));
    }
}