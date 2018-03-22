package bda.hslu.ch.betchain.WebFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bda.hslu.ch.betchain.CallAPI;
import bda.hslu.ch.betchain.DTO.Friend;
import bda.hslu.ch.betchain.DTO.User;
import bda.hslu.ch.betchain.WebRequestException;

/**
 * Created by ssj10 on 22/03/2018.
 */

public class UserFunctions {
    private static final String SERVER_URL = "http://blockchaincontracts.enterpriselab.ch/index.php";

    public static User getUserInfo(String username) throws WebRequestException{
        User userInfo = new User();
        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "addFriend");
            requestParams.put("username", username);


            String response = CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString());
            JSONObject jsonResponse = new JSONObject(response);


            if (jsonResponse.getInt("is_error") == 0) {
                return parseJSONResponseToUserObject(jsonResponse.getJSONObject("data"));
            }else{
                throw new WebRequestException(jsonResponse.getString("message"));
            }
        }catch(JSONException exec){
            System.out.println(exec.getMessage());
            //return "";
        }

        return userInfo;
    }

    public static User getUserByQR(String address) throws WebRequestException{
        User userInfo = new User();
        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "getUserByQR");
            requestParams.put("hash", address);


            String response = CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString());
            JSONObject jsonResponse = new JSONObject(response);


            if (jsonResponse.getInt("is_error") == 0) {
                return parseJSONResponseToUserObject(jsonResponse.getJSONObject("data"));
            }else{
                throw new WebRequestException(jsonResponse.getString("message"));
            }
        }catch(JSONException exec){
            System.out.println(exec.getMessage());
            //return "";
        }

        return userInfo;
    }

    public static User getUserByUsername(String username) throws WebRequestException{
        User userInfo = new User();
        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "getUserByQR");
            requestParams.put("username", username);


            String response = CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString());
            JSONObject jsonResponse = new JSONObject(response);


            if (jsonResponse.getInt("is_error") == 0) {
                return parseJSONResponseToUserObject(jsonResponse.getJSONObject("data"));
            }else{
                throw new WebRequestException(jsonResponse.getString("message"));
            }
        }catch(JSONException exec){
            System.out.println(exec.getMessage());
            //return "";
        }

        return userInfo;
    }

    private static User parseJSONResponseToUserObject(JSONObject data){
        String username = "";
        String userAddress = "";
        int userShowOnline;

        try {

                username = data.getString("username");
                userAddress = data.getString("address");

                boolean showOnline = false;
                if(data.has("showOnline")) {
                    userShowOnline = data.getInt("showOnline");
                    if(userShowOnline != 0){
                        showOnline = true;
                    }
                }

                return new User(username, userAddress, showOnline, 0);
        } catch(JSONException exec) {
            return null;
        }
    }
}
