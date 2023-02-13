package net.springboot.synpulse8challenges.model;

public enum Country {
    UNITED_KINGDOM("UNITED_KINGDOM", "GBP", "UK"),
    UNITED_STATES("UNITED_STATES", "USD", "US"),
    JAPAN("JAPAN", "JPY", "JP"),
    FRANCE("FRANCE", "EUR", "FR"),
    INDIA("INDIA", "INR", "IN"),
    CANADA("CANADA", "CAD", "CA"),
    SOUTH_KOREA("SOUTH_KOREA", "KRW", "SK"),
    RUSSIA("RUSSIA", "RUB", "RU"),
    BRAZIL("BRAZIL", "BRL", "BR"),
    CHINA("CHINA", "CNY", "CN"),
    AUSTRALIA("AUSTRALIA", "AUD", "AU"),
    SPAIN("SPAIN", "ESP", "SP"),
    INDONESIA("INDONESIA", "IDR", "ID");

    private String country;
    private String currency;
    private String accountPrefix;

    private Country(String country, String currency, String accountPrefix) {
        this.country = country;
        this.currency = currency;
        this.accountPrefix = accountPrefix;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCountry() {
        return country;
    }

    public String getAccountPrefix() {
        return accountPrefix;
    }
}
