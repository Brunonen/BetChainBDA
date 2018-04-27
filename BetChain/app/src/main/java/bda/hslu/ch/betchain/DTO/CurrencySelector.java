package bda.hslu.ch.betchain.DTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruno Fischlin on 27/04/2018.
 */

public enum CurrencySelector {
    ETH(0), CHF(1), EUR(2), USD(3);

    private final int currency;

    private static Map<Integer, CurrencySelector> map = new HashMap<Integer, CurrencySelector>();

    static {
        for (CurrencySelector currencyEnum : CurrencySelector.values()) {
            map.put(currencyEnum.currency, currencyEnum);
        }
    }


    CurrencySelector(final int currency) {
        this.currency = currency;
    }

    public static CurrencySelector valueOfInt(int currency) {
        return map.get(currency);
    }

    public static CurrencySelector valueOfStirng(String currency){
        switch(currency){
            case "eth": return CurrencySelector.ETH;
            case "chf": return CurrencySelector.CHF;
            case "eur": return CurrencySelector.EUR;
            case "usd": return CurrencySelector.USD;
            default: return CurrencySelector.ETH;
        }
    }


    public int getCurrency() {
        return this.currency;
    }

    @Override
    public String toString(){
        switch(this.getCurrency()){
            case 0: return "ETH";
            case 1: return "CHF";
            case 2: return "EUR";
            case 3: return "USD";
            default: return "unknown";
        }
    }
}
