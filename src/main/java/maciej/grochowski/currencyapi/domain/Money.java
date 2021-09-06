package maciej.grochowski.currencyapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties
@Getter
public class Money {

    private String code;
    private List<Rates> rates;
}
