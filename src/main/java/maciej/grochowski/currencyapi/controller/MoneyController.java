package maciej.grochowski.currencyapi.controller;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.domain.Rates;
import maciej.grochowski.currencyapi.service.CalculationService;
import maciej.grochowski.currencyapi.service.NBPService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/exchange")
public class MoneyController {

    private final CalculationService calcService;
    private final NBPService nbpService;

    @ApiOperation(value = "Allows you to calculate precise amount of money you could possibly get for exchanging currencies.")
    @GetMapping("/{sellCurrency}/{sellAmount}/{buyCurrency}")
    public Rates exchangeValues(@PathVariable CurrencyType sellCurrency, @PathVariable BigDecimal sellAmount,
                                @PathVariable CurrencyType buyCurrency) {
        calcService.validateAmount(sellAmount);
        calcService.validateCurrencies(sellCurrency, buyCurrency);
        return calcService.provideCalculationsAndMessage(sellCurrency, sellAmount, buyCurrency);
    }
}