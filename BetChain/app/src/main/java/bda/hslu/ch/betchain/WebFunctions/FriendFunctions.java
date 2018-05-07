package bda.hslu.ch.betchain.WebFunctions;

import android.os.AsyncTask;

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

public abstract class FriendFunctions extends AsyncTask<String, Void, Object>{

    private static final String SERVER_URL = "https://blockchaincontracts.enterpriselab.ch/index.php";

    public static Exception mException;
    public abstract void onSuccess(Object result);
    public abstract void onFailure(Object result);

    public FriendFunctions(){

    }

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

    public static boolean addFriend(String userToAddAsFriend) throws WebRequestException{
        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "addFriend");
            requestParams.put("userToAdd", userToAddAsFriend);


            String response = CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString());
            JSONObject jsonResponse = new JSONObject(response);


            if (jsonResponse.getInt("is_error") == 0) {
                return true;
            }else{
                throw new WebRequestException(jsonResponse.getString("message"));
            }
        }catch(JSONException exec){
            System.out.println(exec.getMessage());
            //return "";
        }

        return false;
    }

    public static boolean removeFriend(String userToRemove) throws WebRequestException{
        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "removeFriend");
            requestParams.put("userToRemove", userToRemove);


            String response = CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString());
            JSONObject jsonResponse = new JSONObject(response);


            if (jsonResponse.getInt("is_error") == 0) {
                return true;
            }else{
                throw new WebRequestException(jsonResponse.getString("message"));
            }
        }catch(JSONException exec){
            System.out.println(exec.getMessage());
            //return "";
        }

        return false;
    }

    private static List<Friend> parseDataResponseToFriends(JSONArray data) {
        List<Friend> friendList = new ArrayList<Friend>();
        String friendName = "";
        String friendAddress = "";

        try {
            for (int i = 0; i < data.length(); i++) {

                JSONObject friendO = data.getJSONObject(i);

                Iterator<?> keys = friendO.keys();

                while( keys.hasNext() ) {
                    friendName = (String)keys.next();
                    friendAddress = friendO.getString(friendName);
                    friendList.add(new Friend(friendName, friendAddress, 0));
                }


            }

            return friendList;
        } catch(JSONException exec) {
            return friendList;
        }
    }


    @Override
    protected Object doInBackground(String... params) {
        mException = null;
        try {
            switch (params[0]) {
                case "getUserFriendList":
                    return getUserFriendList();


                case "addFriend":
                    return addFriend(params[1]);


                case "removeFriend":
                    return removeFriend(params[1]);
            }
        }catch(WebRequestException e){
            mException = e;
        }catch(Exception e){
            mException = e;
        }

        return null;
    }


    protected void onPostExecute(Object result) {
        if (mException == null) {
            onSuccess(result);
        } else {
            onFailure(mException);
        }

    }
}
