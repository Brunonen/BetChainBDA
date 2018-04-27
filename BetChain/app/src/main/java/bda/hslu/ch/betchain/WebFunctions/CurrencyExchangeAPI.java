package bda.hslu.ch.betchain.WebFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Kay on 25/04/2018.
 */

public class CurrencyExchangeAPI {


    public CurrencyExchangeAPI() {

    }


    /***
     *
     * @param currency  The Currency Code u wish to get the Info(chf, usd, eur).
     * @return          The Currency Value as String. If nothing was found it will return an empty String
     */
    public static String getCurrenyValue(String currency) {
        final String URL = "https://api.coinmarketcap.com/v1/ticker/ethereum/?convert=";
        HttpURLConnection urlConnection = null;
        URL url = null;
        try {
            url = new URL(URL + currency);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            String jsonString = sb.toString();

            JSONArray jsonArray = new JSONArray(jsonString);

            JSONObject returnValue = jsonArray.getJSONObject(0);


            String currencyValue = returnValue.getString("price_" + currency);

            return currencyValue;



        } catch (Exception e) {
            e.printStackTrace();

            return "";


        }

    }

    public static String exchangeCurrency(String value, String fromCurrency, String toCurrency){
        String returnCurrency = "";
        if(fromCurrency.equals("ETH") || fromCurrency.equals("eth")){
            returnCurrency = String.valueOf((Float.parseFloat(value) * Float.parseFloat(getCurrenyValue(toCurrency))));

        }
        else if(toCurrency.equals("ETH") || toCurrency.equals("eth")){
            returnCurrency = String.valueOf((Float.parseFloat(value) / Float.parseFloat(getCurrenyValue(fromCurrency))));

        }else {
            String toEth = String.valueOf((Float.parseFloat(value) / Float.parseFloat(getCurrenyValue(fromCurrency))));
            returnCurrency = String.valueOf((Float.parseFloat(toEth) * Float.parseFloat(getCurrenyValue(toCurrency))));


        }
        return returnCurrency;
    }


}


