package bda.hslu.ch.betchain;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import bda.hslu.ch.betchain.DTO.User;
import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.WebFunctions.SettingsFunctions;
import bda.hslu.ch.betchain.WebFunctions.UserFunctions;


public class SettingsFragment extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        final MainActivity activity = (MainActivity) getActivity();
        Switch showOnline = (Switch) rootView.findViewById(R.id.switchShowOnline);
        Switch enablePushNotifications = (Switch) rootView.findViewById(R.id.switchEnablePushNotifications);

        try {
            User userInfos = UserFunctions.getUserInfo(getLoggedInUserInfo()[0]);
            showOnline.setChecked(userInfos.isShowOnline());
        } catch (WebRequestException e) {
            e.printStackTrace();
        }
        showOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int showOnlineValue = (isChecked) ? 1 : 0;
                try {
                    SettingsFunctions.changeUserSetting("showOnline", String.valueOf(showOnlineValue));
                } catch (WebRequestException e) {
                    Toast.makeText(activity,e.getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });

        enablePushNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Enable / Disable Push notiifactions
            }
        });

        return rootView;
    }

    private String[] getLoggedInUserInfo(){
        String[] returnString;
        MainActivity activity = (MainActivity) getActivity();
        SQLWrapper db = new SQLWrapper(activity);
        returnString = db.getLoggedInUserInfo();
        return returnString;
    }
}