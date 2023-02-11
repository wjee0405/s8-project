package net.springboot.synpulse8challenges.model;

import lombok.NoArgsConstructor;

public enum Currency {
    UNITED_KINGDOM("GBP","UK"),
    UNITED_STATES("USD","US"),
    JAPAN("JPY","JP"),
    FRANCE("EUR","FR"),
    INDIA("INR","IN"),
    CANADA("CAD","CA"),
    SOUTH_KOREA("KRW","SK"),
    RUSSIA("RUB","RU"),
    BRAZIL("BRL","BR"),
    CHINA("CNY","CN"),
    AUSTRALIA("AUD","AU"),
    SPAIN("ESP","SP"),
    INDONESIA("IDR","ID");

    private String currency;
    private String accountPrefix;

    private Currency(String currency, String accountPrefix){

        this.currency = currency;
        this.accountPrefix = accountPrefix;
    }

    public String getCurrency(){
        return currency;
    }

    public String getAccountPrefix(){
        return accountPrefix;
    }
}
