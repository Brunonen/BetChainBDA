package bda.hslu.ch.betchain;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import bda.hslu.ch.betchain.DTO.User;
import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.WebFunctions.AuthenticationFunctions;
import bda.hslu.ch.betchain.WebFunctions.UserFunctions;


public class LoginFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_login, container, false);


        Button registerButton = (Button) root.findViewById(R.id.registerButton);
        Button loginButton = (Button) root.findViewById(R.id.loginButton);
        final EditText username = (EditText) root.findViewById(R.id.inputUsernameLogin);
        final EditText password = (EditText) root.findViewById(R.id.inputPasswordLogin);

        SQLWrapper db = new SQLWrapper(getActivity());

        if(db.checkIfUserNeedsToBeLoggedIn()){
            String[] userInfo = db.getLoggedInUserInfo();
            MainActivity activity = (MainActivity) getActivity();
            try {
                if(AuthenticationFunctions.loginUser(userInfo[0], userInfo[1])){
                    activity.setDrawerState(true);
                    activity.changeFragmentNoBackstack(new MyBetsFragment());
                }
            } catch (WebRequestException e) {
                Toast.makeText(activity,e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                MainActivity activity = (MainActivity) getActivity();
                try{
                    String userSaltResponse = AuthenticationFunctions.getUserSalt(username.getText().toString());
                    if(!userSaltResponse.equals("")){
                        String hash = HashClass.bin2hex(HashClass.getHash(password.getText().toString() + userSaltResponse));

                        if(AuthenticationFunctions.loginUser(username.getText().toString(), hash)){

                            User userInfos = UserFunctions.getUserInfo(username.getText().toString());
                            SQLWrapper db = new SQLWrapper(activity);
                            db.addOrUpdateAppUser(userInfos.getUsername(), hash, userInfos.getAddress());
                            activity.setDrawerState(true);
                            activity.changeFragmentNoBackstack(new MyBetsFragment());
                        }
                    }
                } catch (WebRequestException e) {
                    Toast.makeText(activity,e.getMessage() , Toast.LENGTH_SHORT).show();
                } catch (LocalDBException e) {
                    Toast.makeText(activity,e.getMessage() , Toast.LENGTH_SHORT).show();
                }

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Fragment registerUserFragment = new RegisterUserFragment();
                MainActivity activity = (MainActivity) getActivity();
                activity.changeFragment(registerUserFragment);

            }
        });


        return root;
    }

}