package maciej.grochowski.currencyapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;

@JsonIgnoreProperties
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Rates {

    public Rates(BigDecimal bid, BigDecimal ask) {
        this.bid = bid;
        this.ask = ask;
        if (bid.equals(BigDecimal.ONE) || ask.equals(BigDecimal.ONE)) {
            this.margin = BigDecimal.valueOf(1.02);
        } else {
            this.margin = BigDecimal.valueOf(1.0404);
        }
    }

    public Rates(BigDecimal bid, BigDecimal ask, String message) {
        this.bid = bid;
        this.ask = ask;
        if (bid.equals(BigDecimal.ONE) || ask.equals(BigDecimal.ONE)) {
            this.margin = BigDecimal.valueOf(1.02);
        } else {
            this.margin = BigDecimal.valueOf(1.0404);
        }
        this.message = message;
    }

    private BigDecimal bid;
    private BigDecimal ask;
    private BigDecimal margin;
    private String message;
}
