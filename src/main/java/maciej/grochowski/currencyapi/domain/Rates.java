package maciej.grochowski.currencyapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@JsonIgnoreProperties
@Getter
@Setter
public class Rates {

    private BigDecimal bid;
    private BigDecimal ask;
}
