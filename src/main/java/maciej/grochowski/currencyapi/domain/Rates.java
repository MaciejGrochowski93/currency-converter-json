package maciej.grochowski.currencyapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;

@JsonIgnoreProperties
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rates {

    private BigDecimal bid;
    private BigDecimal ask;
}
