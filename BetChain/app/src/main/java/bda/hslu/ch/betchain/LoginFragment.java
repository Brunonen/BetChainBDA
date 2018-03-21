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

import bda.hslu.ch.betchain.WebFunctions.AuthenticationFunctions;


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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String userSaltResponse = AuthenticationFunctions.getUserSalt(username.getText().toString());

                try{
                    JSONObject userSaltJSON = new JSONObject(userSaltResponse);
                    if(userSaltJSON.getInt("is_error") == 0){
                        String userSalt = userSaltJSON.getString("data");
                        String hash = HashClass.bin2hex(HashClass.getHash(password.getText().toString() + userSalt));

                        String response = AuthenticationFunctions.loginUser(username.getText().toString(), hash);

                        MainActivity activity = (MainActivity) getActivity();

                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            if (responseJSON.getInt("is_error") == 0) {
                                activity.changeFragment(new MyBetsFragment());
                            }else{
                                Toast.makeText(activity,responseJSON.getString("message") , Toast.LENGTH_SHORT).show();
                            }
                        }catch(JSONException exec){
                            Toast.makeText(activity, "Please enter a Bet Title that is atleast 8 Characters long.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch(JSONException exec){
                    System.out.println(exec.getMessage());
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