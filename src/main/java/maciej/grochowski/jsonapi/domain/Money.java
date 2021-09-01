package maciej.grochowski.jsonapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties
@Getter
@Setter
public class Money {

    private String code;
    private List<Rates> rates;
}
