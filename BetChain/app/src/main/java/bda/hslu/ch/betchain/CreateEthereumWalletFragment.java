package bda.hslu.ch.betchain;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bda.hslu.ch.betchain.BlockChainFunctions.BlockChainFunctions;
import bda.hslu.ch.betchain.Database.DBSessionSingleton;
import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.WebFunctions.SettingsFunctions;


public class CreateEthereumWalletFragment extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_ethereum_wallet, container, false);


        Button createEthereumWalletButton = (Button) rootView.findViewById(R.id.buttonCreateEthereumWallet);
        final EditText password = (EditText) rootView.findViewById(R.id.createEthereumWalletPassword);
        final EditText passwordConfirm = (EditText) rootView.findViewById(R.id.createEthereumWalletPasswordConfirm);


        createEthereumWalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                MainActivity activity = (MainActivity) getActivity();
                String pwd = password.getText().toString();
                String confPwd = passwordConfirm.getText().toString();
                String[] addresses = new String[2];

                if(pwd.equals(confPwd)){
                    if(pwd.length() > 0){
                        //Create new Wallet and write Addresses to Database
                        SQLWrapper db = DBSessionSingleton.getInstance().getDbUtil();
                        BlockChainFunctions bc = new BlockChainFunctions();
                        addresses = bc.createNewEthereumWallet(activity, pwd);
                        try {
                            db.changeUserPublicKey(db.getLoggedInUserInfo()[0], addresses[1]);
                            db.changeUserPrivateKey(db.getLoggedInUserInfo()[0], addresses[0]);
                        } catch (LocalDBException e) {
                            e.printStackTrace();
                        }
                        //Update address on Webserver
                        try{
                            SettingsFunctions.changeUserSetting("address", addresses[1]);
                        }catch (WebRequestException e){
                            e.printStackTrace();
                        }
                        Fragment myBets = new MyBetsFragment();
                        activity.changeFragmentNoBackstack(myBets);

                    }else {
                        Toast.makeText(activity, "Password can not be empty", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(activity, "Password and Confirmation do not match!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        return rootView;
    }
}