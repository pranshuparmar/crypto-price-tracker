package com.pranshu.crypto.utils;

public final class Constants {

    public final static class SYMBOL {
        public final static String BITCOIN = "bitcoin";
    }

    public final static class CURRENCY {
        public final static String USD = "usd";
    }

    public final static class QUERY_PARAM {
        public final static String IDS = "ids";
        public final static String VS_CURRENCIES = "vs_currencies";
    }

    public final static class URL {
        public final static String PRICES = "/prices";
        public final static String BTC = "/btc";
        public final static String SIMPLE_PRICE = "/simple/price";
    }

    public final static class ERROR {
        public final static String INSUFFICIENT_DATA = "Insufficient details received from coingecko pricing API";
    }
}
