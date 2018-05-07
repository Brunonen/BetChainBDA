package bda.hslu.ch.betchain.WebFunctions;

import android.os.AsyncTask;
import android.support.annotation.CallSuper;
import android.telecom.Call;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import bda.hslu.ch.betchain.CallAPI;
import bda.hslu.ch.betchain.DTO.User;
import bda.hslu.ch.betchain.Database.DBSessionSingleton;
import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.HashClass;
import bda.hslu.ch.betchain.LocalDBException;
import bda.hslu.ch.betchain.MainActivity;
import bda.hslu.ch.betchain.WebRequestException;

/**
 * Created by Bruno Fischlin on 20/03/2018.
 */

public abstract class AuthenticationFunctions extends AsyncTask<String, Void, Object>{

    private static final String SERVER_URL = "https://blockchaincontracts.enterpriselab.ch/index.php";
    private static final String API_KEY = "sdkajdkaj2";

    public static Exception mException;
    public abstract void onSuccess(Object result);
    public abstract void onFailure(Object result);

    public AuthenticationFunctions(){

    }

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

    @Override
    protected Object doInBackground(String... params) {
        mException = null;
        try {
            switch (params[0]) {
                //If event was triggered by user, we have to get the Salt first and hash the PW before sending it to the server.
                case "loginUser":
                    String userSaltResponse = AuthenticationFunctions.getUserSalt(params[1]);

                    if(!userSaltResponse.equals("")) {
                        String hash = HashClass.bin2hex(HashClass.getHash(params[2] + userSaltResponse));

                        //Check if Login is successfull
                        if(loginUser(params[1],hash)){
                            //Login was a success, now update the Users information on the local DB.
                            User userInfos = UserFunctions.getUserInfo(params[1]);
                            SQLWrapper db = DBSessionSingleton.getInstance().getDbUtil();
                            db.addOrUpdateAppUser(userInfos.getUsername(), hash, userInfos.getAddress());
                            return true;
                        }
                        return false;
                    }else{
                        throw new WebRequestException("Could not get User Information from Server.");
                    }

                case "loginUserAutomatically":
                    return loginUser(params[1], params[2]);
                default:
                    return null;
            }
        }catch(WebRequestException e){
            mException = e;
        } catch (LocalDBException e) {
            mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (mException == null) {
            onSuccess(result);
        } else {
            onFailure(mException);
        }

    }
}
