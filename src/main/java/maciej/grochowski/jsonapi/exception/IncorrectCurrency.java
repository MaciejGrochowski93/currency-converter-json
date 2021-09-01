package maciej.grochowski.jsonapi.exception;

public class IncorrectCurrency extends RuntimeException{
    public IncorrectCurrency() {
    }

    public IncorrectCurrency(String message) {
        super(message);
    }
}
