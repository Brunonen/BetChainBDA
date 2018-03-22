package bda.hslu.ch.betchain.WebFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bda.hslu.ch.betchain.CallAPI;
import bda.hslu.ch.betchain.DTO.Friend;
import bda.hslu.ch.betchain.R;
import bda.hslu.ch.betchain.WebRequestException;

/**
 * Created by Bruno Fischlin on 12/03/2018.
 */

public class FriendFunctions {

    private static final String SERVER_URL = "http://blockchaincontracts.enterpriselab.ch/index.php";
    public static List<Friend> getUserFriendList() throws WebRequestException {
        List<Friend> friendList = new ArrayList<Friend>();

        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "getFriendList");


            String response = CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString());
            JSONObject jsonResponse = new JSONObject(response);


            if (jsonResponse.getInt("is_error") == 0) {
                JSONArray friendJSONS = jsonResponse.getJSONArray("data");

                return parseDataResponseToFriends(friendJSONS);

            }else{
                throw new WebRequestException(jsonResponse.getString("message"));
            }
        }catch(JSONException exec){
            System.out.println(exec.getMessage());
            //return "";
        }

        return friendList;

    }

    private static List<Friend> parseDataResponseToFriends(JSONArray data) {
        List<Friend> friendList = new ArrayList<Friend>();
        String friendName = "";
        String friendAddress = "";

        try {
            for (int i = 0; i < data.length(); i++) {
                JSONObject friendO = data.getJSONObject(i);
                friendName = friendO.keys().next();
                friendAddress = friendO.getString(friendName);

                friendList.add(new Friend(friendName, friendAddress, 0));
            }

            return friendList;
        } catch(JSONException exec) {
            return friendList;
        }
    }


}
