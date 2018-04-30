package bda.hslu.ch.betchain;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import bda.hslu.ch.betchain.DTO.AppUser;
import bda.hslu.ch.betchain.DTO.CurrencySelector;
import bda.hslu.ch.betchain.DTO.User;
import bda.hslu.ch.betchain.Database.DBSessionSingleton;
import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.WebFunctions.CurrencyExchangeAPI;
import bda.hslu.ch.betchain.WebFunctions.SettingsFunctions;
import bda.hslu.ch.betchain.WebFunctions.UserFunctions;


public class SettingsFragment extends Fragment {

    private View rootView;
    private AppUser loggedInUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        final MainActivity activity = (MainActivity) getActivity();
        Switch showOnline = (Switch) rootView.findViewById(R.id.switchShowOnline);
        Switch enablePushNotifications = (Switch) rootView.findViewById(R.id.switchEnablePushNotifications);
        Spinner currencySpinner = (Spinner) rootView.findViewById(R.id.CurrencySpinnerSettings);

        try {
            loggedInUser = AppUser.getLoggedInUserObject();
            User userInfos = UserFunctions.getUserInfo(loggedInUser.getUsername());
            showOnline.setChecked(userInfos.isShowOnline());
            currencySpinner.setAdapter(new ArrayAdapter<CurrencySelector>(activity, android.R.layout.simple_spinner_item, CurrencySelector.values()));
            currencySpinner.setSelection(loggedInUser.getPrefferedCurrency().getCurrency());
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

        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    DBSessionSingleton.getInstance().getDbUtil().changeUserPrefferedCurrency(loggedInUser.getUsername(), CurrencySelector.valueOfStirng(parent.getItemAtPosition(position).toString().toLowerCase()));
                } catch (LocalDBException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Errow while changing Currency on local DB" , Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

}