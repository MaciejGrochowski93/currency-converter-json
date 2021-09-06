package maciej.grochowski.currencyapi.exception;

public class SameCurrenciesException extends RuntimeException{
    public SameCurrenciesException() {
    }

    public SameCurrenciesException(String message) {
        super(message);
    }
}
