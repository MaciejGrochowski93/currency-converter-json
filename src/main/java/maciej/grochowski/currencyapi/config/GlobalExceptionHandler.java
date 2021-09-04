package maciej.grochowski.currencyapi.config;

import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.exception.IncorrectAmount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String ENUM_CONVERSION_FAILED = "Please, provide correct currencies: "
            + Arrays.toString(Arrays
            .stream(CurrencyType.values())
            .toArray());

    private static final String AMOUNT_CONVERSION_FAILED =
            "Provided amount must be a positive number, lower than 10 mln, and with maximum of 2 decimal places.";

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleEnumConflict(ConversionFailedException ex) {
        LOGGER.warn(ex.getMessage());
        return new ResponseEntity<>(ENUM_CONVERSION_FAILED, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleNumberConflict(NumberFormatException ex) {
        LOGGER.warn(ex.getMessage());
        return new ResponseEntity<>(AMOUNT_CONVERSION_FAILED, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectAmount.class)
    public ResponseEntity<String> handleAmountConflict(IncorrectAmount ex) {
        LOGGER.warn(ex.getMessage());
        return new ResponseEntity<>(AMOUNT_CONVERSION_FAILED, HttpStatus.BAD_REQUEST);
    }
}
