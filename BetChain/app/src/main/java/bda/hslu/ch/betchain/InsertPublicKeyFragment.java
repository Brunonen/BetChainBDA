package bda.hslu.ch.betchain;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import bda.hslu.ch.betchain.Database.SQLWrapper;


public class InsertPublicKeyFragment extends Fragment {
    private String qrData;
    private View rootView;
    private static final int PERMISSION_REQUEST_CAMERA = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_insert_public_key, container, false);


        Button scanPublicKey = (Button) rootView.findViewById(R.id.publicKeyScanQRButton);

        scanPublicKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_CAMERA);

                }else{
                    getQrCode();
                }
            }
        });

        Button savePublicKeyButton = (Button) rootView.findViewById(R.id.publicKeySaveButton);

        savePublicKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText publicKeyText = (EditText) rootView.findViewById(R.id.publicKeyTextBox);
                savePublicKey(publicKeyText.getText().toString());
                MainActivity activity = (MainActivity) getActivity();
                Fragment accountInfo = new AccountInfoFragment();
                activity.changeFragment(accountInfo);

            }
        });


        return rootView;
    }


    private void getQrCode(){

        IntentIntegrator.forSupportFragment(this).initiateScan();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String scanData = (scanningResult != null) ? scanningResult.getContents() : "";

        if (scanData == null || scanData.isEmpty()) {
            qrData = "Not found";
        } else {
            qrData = scanData;
        }

        EditText publicKeyText = (EditText) rootView.findViewById(R.id.publicKeyTextBox);
        publicKeyText.setText(qrData);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                getQrCode();
            } else {
                // Permission request was denied.
                MainActivity activity = (MainActivity) getActivity();
                Toast.makeText(activity,"Could not open Camera, due to permission being denied" , Toast.LENGTH_SHORT).show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void savePublicKey(String publicKey){
        String[] userInfo;
        MainActivity activity = (MainActivity) getActivity();
        SQLWrapper db = new SQLWrapper(activity);
        userInfo = db.getLoggedInUserInfo();
        try {
            db.changeUserPublicKey(userInfo[0], publicKey);
        }catch (LocalDBException e){
            Toast.makeText(activity,e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }
}