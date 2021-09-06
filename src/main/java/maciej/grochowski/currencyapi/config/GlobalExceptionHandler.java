package maciej.grochowski.currencyapi.config;

import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.exception.IncorrectAmount;
import maciej.grochowski.currencyapi.exception.SameCurrenciesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String ENUM_CONVERSION_FAILED_ERROR_MESSAGE = "Provided invalid currency: %s. \n" +
            "Please, provide currency from the list: %s.";

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleInvalidConversion(ConversionFailedException ex) {
        LOGGER.warn(ex.getMessage(), ex);
        if (Objects.equals(ex.getTargetType().getType(), CurrencyType.class)) {
            return new ResponseEntity<>(resolveProvidedInvalidCurrencyErrorMessage(ex.getValue()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    public String resolveProvidedInvalidCurrencyErrorMessage(Object object) {
        String providedValue = Optional.ofNullable(object)
                .map(Object::toString)
                .orElse("null");
        String availableCurrencies = CurrencyType.availableCurrencies().stream()
                .map(CurrencyType::name)
                .collect(Collectors.joining(", ", "[", "]"));
        return String.format(ENUM_CONVERSION_FAILED_ERROR_MESSAGE, providedValue, availableCurrencies);
    }

    private static final String SAME_CURRENCIES_CONVERSION_FAILED =
            "You cannot exchange Currency with itself.";

    @ExceptionHandler(SameCurrenciesException.class)
    public ResponseEntity<String> handleSameCurrencies(SameCurrenciesException ex) {
        LOGGER.warn(ex.getMessage(), ex);
        return new ResponseEntity<>(SAME_CURRENCIES_CONVERSION_FAILED, HttpStatus.BAD_REQUEST);
    }

    private static final String AMOUNT_CONVERSION_FAILED =
            "Provided amount must be a positive number, lower than 10 mln, and with maximum of 2 decimal places.";

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleNumberConflict(NumberFormatException ex) {
        LOGGER.warn(ex.getMessage(), ex);
        return new ResponseEntity<>(AMOUNT_CONVERSION_FAILED, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectAmount.class)
    public ResponseEntity<String> handleAmountConflict(IncorrectAmount ex) {
        LOGGER.warn(ex.getMessage(), ex);
        return new ResponseEntity<>(AMOUNT_CONVERSION_FAILED, HttpStatus.BAD_REQUEST);
    }
}
