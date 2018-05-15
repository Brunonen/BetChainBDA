package bda.hslu.ch.betchain;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.concurrent.ExecutionException;

import bda.hslu.ch.betchain.BlockChainFunctions.BlockChainFunctions;
import bda.hslu.ch.betchain.DTO.AppUser;
import bda.hslu.ch.betchain.DTO.User;
import bda.hslu.ch.betchain.Database.DBSessionSingleton;
import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.WebFunctions.CurrencyExchangeAPI;
import bda.hslu.ch.betchain.WebFunctions.UserFunctions;
import bda.hslu.ch.betchain.BlockChainFunctions.AccountBalance;

public class AccountInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_account_info, container, false);
        String usernameString = "";
        String addressString = "";
        String privateKeyString = "";
        final MainActivity activity = (MainActivity) getActivity();


        EditText privateKey = (EditText) root.findViewById(R.id.privateKey);
        EditText username = (EditText) root.findViewById(R.id.accountInfoUsername);
        ImageView profilePic = (ImageView) root.findViewById(R.id.accountInfoProfilePic);
        ImageView qrFriendCode = (ImageView) root.findViewById(R.id.qrFriendCode);
        EditText ethValue = (EditText) root.findViewById(R.id.accountInfoETH);
        TextView publicAdress = (TextView) root.findViewById(R.id.publicAddressText);


        //Get User unfo from Database
        AppUser userInfoString = AppUser.getLoggedInUserObject();
        usernameString = userInfoString.getUsername();
        try {
            //Get additional User Info from Server
            User serverInfo = UserFunctions.getUserInfo(usernameString);
            if(serverInfo.getProfilePicture() != 0){
                profilePic.setImageResource(serverInfo.getProfilePicture());
            }else{
                profilePic.setImageResource(R.drawable.ic_blank_avatar);
            }
        }catch(WebRequestException e){
            profilePic.setImageResource(R.drawable.ic_blank_avatar);
        }

        privateKeyString = userInfoString.getPrivateKey();
        addressString = userInfoString.getPublicAddress();
        publicAdress.setText(addressString);

        username.setText(usernameString);


        if(privateKeyString.length() == 64){
            privateKey.setText("A Private Key ist Set");
        }


        privateKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MainActivity activity = (MainActivity) getActivity();
                activity.changeFragment(new InsertPrivateKeyFragment());
            }
        });

        //QR Code generator
        try {
            if(addressString.equals("")) {
                Toast.makeText(activity,"No Public-Address found" , Toast.LENGTH_SHORT).show();
                addressString = "No Address";
            }
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(addressString, BarcodeFormat.QR_CODE, 600, 600);
            ImageView imageViewQrCode = (ImageView) root.findViewById(R.id.qrFriendCode);
            imageViewQrCode.setImageBitmap(bitmap);

        } catch(Exception e) {
            Toast.makeText(activity,"QR Code could not be generated" , Toast.LENGTH_SHORT).show();
        }

        qrFriendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.changeFragment(new InsertPublicKeyFragment());
            }
        });

        try {
            String eth = String.valueOf(BlockChainFunctions.getAccountBalance());
            if(eth.length() > 12){
                ethValue.setText(eth.substring(0, 12));
            }else {
                ethValue.setText(eth);
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Eth Value could not be loaded", Toast.LENGTH_SHORT).show();
        }

        return root;
    }

}