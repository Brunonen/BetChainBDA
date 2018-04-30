package bda.hslu.ch.betchain.WebFunctions;

import bda.hslu.ch.betchain.DTO.CurrencySelector;


/**
 * Created by Bruno Fischlin on 30/04/2018.
 */

public class CurrencyExchangerSingleton {

    private static CurrencyExchangerSingleton instance;
    private static String eth = "1.0";
    private static String usd;
    private static String chf;
    private static String eur;


    protected CurrencyExchangerSingleton(){
        usd = CurrencyExchangeAPI.getCurrenyValue("usd");
        chf = CurrencyExchangeAPI.getCurrenyValue("chf");
        eur = CurrencyExchangeAPI.getCurrenyValue("eur");
    }

    public static CurrencyExchangerSingleton getInstance(){
        if(instance == null){
            instance = new CurrencyExchangerSingleton();
        }

        return instance;
    }

    public static String exchangeCurrency(String value, CurrencySelector fromCurrency, CurrencySelector toCurrency){

        if(fromCurrency == CurrencySelector.ETH){
            switch(toCurrency){
                case USD : return String.valueOf((Float.parseFloat(value) * Float.parseFloat(usd)));
                case CHF : return String.valueOf((Float.parseFloat(value) * Float.parseFloat(chf)));
                case EUR : return String.valueOf((Float.parseFloat(value) * Float.parseFloat(eur)));
                case ETH : return String.valueOf((Float.parseFloat(value) * Float.parseFloat(eth)));
                default : return "1.0";

            }
        }
        else if(toCurrency == CurrencySelector.ETH){
            switch(fromCurrency){
                case USD : return String.valueOf((Float.parseFloat(value) / Float.parseFloat(usd)));
                case CHF : return String.valueOf((Float.parseFloat(value) / Float.parseFloat(chf)));
                case EUR : return String.valueOf((Float.parseFloat(value) / Float.parseFloat(eur)));
                case ETH : return String.valueOf((Float.parseFloat(value) / Float.parseFloat(eth)));
                default : return "1.0";
            }

        }else {
            return String.valueOf(exchangeCurrency(exchangeCurrency(value, fromCurrency, CurrencySelector.ETH), CurrencySelector.ETH, toCurrency));

        }
    }
}
