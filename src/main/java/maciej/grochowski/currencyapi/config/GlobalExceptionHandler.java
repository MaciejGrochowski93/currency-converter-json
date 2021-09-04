package maciej.grochowski.currencyapi.config;

import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.exception.IncorrectAmount;
//import maciej.grochowski.currencyapi.exception.InvalidCurrency;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final static String ENUM_CONVERSION_FAILED = "Please, provide correct currencies: "
            + Arrays.toString(Arrays
            .stream(CurrencyType.values())
            .toArray());

    private final static String AMOUNT_CONVERSION_FAILED =
            "Provided amount must be a positive number, lower than 10 mln, and with maximum of 2 decimal places.";

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleEnumConflict(RuntimeException ex) {
        LOGGER.warn(ex.getMessage());
        return new ResponseEntity<>(ENUM_CONVERSION_FAILED, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleNumberConflict(RuntimeException ex) {
        LOGGER.warn(ex.getMessage());
        return new ResponseEntity<>(AMOUNT_CONVERSION_FAILED, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectAmount.class)
    public ResponseEntity<String> handleAmountConflict(RuntimeException ex) {
        LOGGER.warn(ex.getMessage());
        return new ResponseEntity<>(AMOUNT_CONVERSION_FAILED, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(InvalidCurrency.class)
//    public ResponseEntity<String> handleCurrencyConflict(RuntimeException ex) {
//        LOGGER.warn(ex.getMessage());
//        return new ResponseEntity<>(AMOUNT_CONVERSION_FAILED, HttpStatus.BAD_REQUEST);
//    }
}
