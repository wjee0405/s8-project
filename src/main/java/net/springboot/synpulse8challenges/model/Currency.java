package net.springboot.synpulse8challenges.model;

import lombok.NoArgsConstructor;

public enum Currency {
    UNITED_KINGDOM("GBP"),
    UNITED_STATES("USD"),
    JAPAN("JPY"),
    EURO("EUR"),
    INDIA("INR"),
    CANADA("CAD"),
    SOUTH_KOREA("KRW"),
    RUSSIA("RUB"),
    BRAZIL("BRL"),
    CHINA("CNY"),
    AUSTRALIA("AUD"),
    SPAIN("ESP"),
    INDONESIA("IDR");

    private String currency;

    private Currency(String currency){
        this.currency = currency;
    }

    public String getCurrency(){
        return currency;
    }
}
