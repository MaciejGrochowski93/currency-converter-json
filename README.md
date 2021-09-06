# Currency Calculator

This is a SpringBoot, Maven project of a Currency Calculator, which allows you to convert given amount of one Currency
into another one.


## Description
The Calculator allows you to use four different Currencies - PLN/GBP/EUR/CHF.
The bid, and ask (buy, and sell) rates are different, and they are getting updated every day, as the rates are 
retrieved from the National Polish Bank's site. Each operation of exchange is affected by a margin multiplier.



## Technologies
The project uses RestTemplate to parse Json into an object. RestController allows to execute methods located in
Services (mostly on BigDecimals).


## Getting Started
In order to try out this application you could use a platform for APIs like Postman, and make your own calls. 
For example: http://localhost:8080/exchange/PLN/1000/EUR - This case shows how many Euros you could buy for 1000 PLN.


For more detailed information, you can visit the URL: http://localhost:8080/swagger-ui.html - you can also manually
test the application there.
