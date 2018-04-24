package bda.hslu.ch.betchain;

import android.app.ExpandableListActivity;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class CallAPI  {

    static final String COOKIES_HEADER = "Set-Cookie";
    static CookieManager msCookieManager = new CookieManager();

    public CallAPI(){
        //set context variables if required
    }


    public static String makePOSTRequestToServer(String... params) {

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String response = "";
        String urlString = params[0]; // URL to call

        String data = params[1]; //data to post


        OutputStream out = null;
        try {
            URL url = new URL(urlString);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                urlConnection.setRequestProperty("Cookie",
                        TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
            }


            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            //urlConnection.setFixedLengthStreamingMode(data.getBytes().length);
            //urlConnection.setChunkedStreamingMode(0);



            urlConnection.connect();

            //OutputStream os = urlConnection.getOutputStream();

            out = new BufferedOutputStream(urlConnection.getOutputStream());

            BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));


            writer.write(data);

            writer.flush();

            writer.close();

            out.close();


            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }

            Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

            if (cookiesHeader != null && msCookieManager.getCookieStore().getCookies().size() <= 0 ) {
                for (String cookie : cookiesHeader) {
                    msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                }
            }

            response = sb.toString();
            urlConnection.disconnect();



        }catch(MalformedURLException error) {
            System.out.println("URL EXCEPTION: " + error.getMessage());
        }
        catch(SocketTimeoutException error) {
            System.out.println("Socket Timeout EXCEPTION: " + error.getMessage());
        }
        catch (IOException error) {
            error.printStackTrace();
            System.out.println("IO EXCEPTION: " + error.getMessage());
        }

        return response;
    }

}