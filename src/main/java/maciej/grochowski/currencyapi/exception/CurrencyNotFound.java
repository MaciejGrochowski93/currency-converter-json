package maciej.grochowski.currencyapi.exception;

public class CurrencyNotFound extends RuntimeException {
    public CurrencyNotFound() {
    }

    public CurrencyNotFound(String message) {
        super(message);
    }
}
