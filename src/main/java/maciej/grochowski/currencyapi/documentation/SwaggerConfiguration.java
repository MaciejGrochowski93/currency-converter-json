package maciej.grochowski.currencyapi.documentation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket swaggerConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("maciej.grochowski"))
                .build()
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails() {
        final String DESCRIPTION = "This is a project of Rest Api, which allows you to calculate how much money " +
                "would you get for exchanging specified amount of the first Currency into another one. " +
                "There are 4 available Currencies - PLN, CHF, EUR, and GBP. " +
                "The exchanging rates - different for bid, and ask - come from the site of National Bank of Poland: " +
                "http://api.nbp.pl/en.html#info";

        return new ApiInfo(
                "Currency Converter",
                DESCRIPTION,
                "1.00",
                "https://www.linkedin.com/in/maciej-grochowski-477b62149",
                "Maciej Grochowski",
                "My GitHub",
                "https://github.com/MaciejGrochowski93"
        );
    }
}
