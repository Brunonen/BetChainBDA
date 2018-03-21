package bda.hslu.ch.betchain.WebFunctions;

import android.support.annotation.CallSuper;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import bda.hslu.ch.betchain.CallAPI;
import bda.hslu.ch.betchain.MainActivity;

/**
 * Created by ssj10 on 20/03/2018.
 */

public class AuthenticationFunctions {

    private static final String SERVER_URL = "http://blockchaincontracts.enterpriselab.ch/index.php";

    public static String loginUser(String username, String pwd){

        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "login");
            requestParams.put("username", username);
            requestParams.put("pwd", pwd);

            String response = CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString());
            //System.out.println(response);

            JSONObject jsonResponse = new JSONObject(response);
            System.out.println(jsonResponse.toString());
            return response;

        }catch(JSONException exec){
            System.out.println(exec.getMessage());
            return "";
        }

    }

    public static String generateUserSalt(){
        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "generateUserSalt");

            return CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString());
            //System.out.println(response);
        }catch(JSONException exec){
            System.out.println(exec.getMessage());
            return "";
        }
    }

    public static String getUserSalt(String username){
        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "getUserSalt");
            requestParams.put("username", username);

            return CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString());
            //System.out.println(response);
        }catch(JSONException exec){
            System.out.println(exec.getMessage());
            return "";
        }
    }
}
