package bda.hslu.ch.betchain.WebFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import bda.hslu.ch.betchain.CallAPI;
import bda.hslu.ch.betchain.DTO.User;
import bda.hslu.ch.betchain.WebRequestException;

/**
 * Created by Bruno Fischlin on 23/03/2018.
 */

public class SettingsFunctions {
    private static final String SERVER_URL = "https://blockchaincontracts.enterpriselab.ch/index.php";

    public static boolean changeUserSetting(String settingName, String value) throws WebRequestException{
        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("method", "changeSetting");
            requestParams.put("settingName", settingName);
            requestParams.put("value", value);


            String response = CallAPI.makePOSTRequestToServer(SERVER_URL, requestParams.toString());
            JSONObject jsonResponse = new JSONObject(response);


            if (jsonResponse.getInt("is_error") == 0) {
                return true;
            } else {
                throw new WebRequestException(jsonResponse.getString("message"));
            }
        } catch (JSONException exec) {
            throw new WebRequestException(exec.getMessage());
        }
    }
}
