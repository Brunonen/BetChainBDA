package bda.hslu.ch.betchain.WebFunctions;

import android.support.annotation.CallSuper;
import android.telecom.Call;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import bda.hslu.ch.betchain.CallAPI;
import bda.hslu.ch.betchain.HashClass;
import bda.hslu.ch.betchain.MainActivity;
import bda.hslu.ch.betchain.WebRequestException;

/**
 * Created by Bruno Fischlin on 20/03/2018.
 */

public class AuthenticationFunctions {

    private static final String SERVER_URL = "http://blockchaincontracts.enterpriselab.ch/index.php";
    private static final String API_KEY = "sdkajdkaj2";

    public static boolean loginUser(String username, String pwd) throws WebRequestException{

        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "login");
            requestParams.put("username", username);
            requestParams.put("pwd", pwd);

            //System.out.println(response);

            JSONObject jsonResponse = new JSONObject(CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString()));

            if (jsonResponse.getInt("is_error") == 0) {
                return true;
            }else{
                throw new WebRequestException(jsonResponse.getString("message"));
            }

        }catch(JSONException exec){
            throw new WebRequestException(exec.getMessage());
        }

    }

    public static String generateUserSalt() throws WebRequestException{
        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "generateUserSalt");

            String response = CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString());
            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.getString("data");
            //System.out.println(response);
        }catch(JSONException exec){
            throw new WebRequestException(exec.getMessage());
        }
    }

    public static String getUserSalt(String username) throws WebRequestException{
        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "getUserSalt");
            requestParams.put("username", username);

            String response = CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString());
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.getInt("is_error") == 0) {
                return jsonResponse.getString("data");
            }else{
                throw new WebRequestException(jsonResponse.getString("message"));
            }
        }catch(JSONException exec){
            System.out.println(exec.getMessage());
            return "";
        }
    }

    public static boolean registerUser(String username, String pwd,  String address) throws WebRequestException{
        try {
            String userSalt = generateUserSalt();
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "register");
            requestParams.put("username", username);
            requestParams.put("userSalt", userSalt);
            requestParams.put("pwd", HashClass.bin2hex(HashClass.getHash(pwd + userSalt)));
            requestParams.put("address", address);
            requestParams.put("apiKey", API_KEY);

            JSONObject jsonResponse = new JSONObject(CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString()));
            if (jsonResponse.getInt("is_error") == 0) {
                return true;
            }else{
                throw new WebRequestException(jsonResponse.getString("message"));
            }

        }catch(JSONException exec){
            throw new WebRequestException(exec.getMessage());
        }

    }
}
