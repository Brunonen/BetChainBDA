package bda.hslu.ch.betchain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import bda.hslu.ch.betchain.Adapters.CustomAdapterMyBetInfo;
import bda.hslu.ch.betchain.BlockChainFunctions.BlockChainFunctions;
import bda.hslu.ch.betchain.DTO.AppUser;
import bda.hslu.ch.betchain.DTO.Bet;
import bda.hslu.ch.betchain.DTO.User;
import bda.hslu.ch.betchain.Database.DBSessionSingleton;
import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.WebFunctions.AuthenticationFunctions;
import bda.hslu.ch.betchain.WebFunctions.BetFunctions;
import bda.hslu.ch.betchain.WebFunctions.CurrencyExchangerSingleton;
import bda.hslu.ch.betchain.WebFunctions.UserFunctions;


public class LoginFragment extends Fragment {

    private MainActivity activity;
    private LoadingScreen loadingScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_login, container, false);


        Button registerButton = (Button) root.findViewById(R.id.registerButton);
        Button loginButton = (Button) root.findViewById(R.id.loginButton);
        final EditText username = (EditText) root.findViewById(R.id.inputUsernameLogin);
        final EditText password = (EditText) root.findViewById(R.id.inputPasswordLogin);

        SQLWrapper db = DBSessionSingleton.getInstance().getDbUtil();

        this.activity = (MainActivity) getActivity();




        if(db.checkIfUserNeedsToBeLoggedIn()){

            AppUser userInfo = AppUser.getLoggedInUserObject();
            tryToLoginUser(userInfo.getUsername(), userInfo.getPwd(), true);

        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                tryToLoginUser(username.getText().toString(), password.getText().toString(), false);
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

    private void tryToLoginUser(final String username, final String pwd, boolean isAutomatedLogin){

        loadingScreen = new LoadingScreen();
        loadingScreen.setCancelable(false);
        loadingScreen.setTitle("Logging In");
        loadingScreen.show(activity.getSupportFragmentManager(), "Loading Screen");


        //Get Bets of user in an Async Task
        @SuppressLint("StaticFieldLeak") AuthenticationFunctions loginUser = new AuthenticationFunctions(){


            @Override
            public void onSuccess(Object result) {
                boolean loginSuccessfull;
                loginSuccessfull = (boolean) result;

                if (loginSuccessfull) {

                    AppUser loggedInUser = AppUser.getLoggedInUserObject();

                    //Initialize Exchange Singleton
                    CurrencyExchangerSingleton.getInstance();
                    if (loggedInUser.getPrivateKey().length() != 64 && loggedInUser.getPublicAddress().length() != 42) {
                        NoUserAddressDialog noUserAddressDialog = new NoUserAddressDialog();
                        noUserAddressDialog.show(activity.getSupportFragmentManager(), "No Ethereum Wallet found");
                    }

                    loadingScreen.dismiss();
                    activity.setDrawerState(true);
                    activity.changeFragmentNoBackstack(new MyBetsFragment());
                }

                loadingScreen.dismiss();

            }

            @Override
            public void onFailure(Object e) {
                Exception exec = (Exception) e;
                loadingScreen.dismiss();
                Toast.makeText(activity, exec.getMessage() , Toast.LENGTH_SHORT).show();
            }
        };

        if(isAutomatedLogin) {
            loginUser.execute("loginUserAutomatically", username, pwd);
        }else{
            loginUser.execute("loginUser", username, pwd);
        }
    }

}