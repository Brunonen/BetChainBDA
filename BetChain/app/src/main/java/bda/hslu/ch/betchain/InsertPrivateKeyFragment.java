package bda.hslu.ch.betchain;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import bda.hslu.ch.betchain.DTO.AppUser;
import bda.hslu.ch.betchain.Database.DBSessionSingleton;
import bda.hslu.ch.betchain.Database.SQLWrapper;


public class InsertPrivateKeyFragment extends Fragment {
    private String qrData;
    private View rootView;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private boolean showPKey = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_insert_private_key, container, false);
        final EditText privateKeyText = (EditText) rootView.findViewById(R.id.privateKeyTextBox);

        AppUser userInfo = AppUser.getLoggedInUserObject();
        final String userPKey = userInfo.getPrivateKey();

        if(!userPKey.equals("")){
            privateKeyText.setText("*******************");
        }


        Button scanPrivateKey = (Button) rootView.findViewById(R.id.privateKeyScanQRButton);

        scanPrivateKey.setOnClickListener(new View.OnClickListener() {
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

        Button savePrivateKeyButton = (Button) rootView.findViewById(R.id.privateKeySaveButton);

        savePrivateKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();

                if(privateKeyText.getText().length() == 64) {
                    savePrivateKey(privateKeyText.getText().toString());


                    Fragment accountInfo = new AccountInfoFragment();
                    activity.changeFragment(accountInfo);
                }else{
                    Toast.makeText(activity, "Invalid Private Key!\n\n Must be 64 Characters long!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        final Button showPKeyButton = (Button) rootView.findViewById(R.id.showPrivateKeyButton);

        showPKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!showPKey) {
                    if (!userPKey.equals("")) {
                        privateKeyText.setText(userPKey);
                        showPKeyButton.setText("HIDE");
                        showPKey = true;
                    }
                }else{
                    if (!userPKey.equals("")) {
                        privateKeyText.setText("*******************");
                        showPKeyButton.setText("SHOW");
                        showPKey = false;
                    }
                }

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

        EditText privateKeyText = (EditText) rootView.findViewById(R.id.privateKeyTextBox);
        privateKeyText.setText(qrData);

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

    private void savePrivateKey(String privateKey){
        String[] userInfo;
        MainActivity activity = (MainActivity) getActivity();
        SQLWrapper db = DBSessionSingleton.getInstance().getDbUtil();
        userInfo = db.getLoggedInUserInfo();
        try {
            db.changeUserPrivateKey(userInfo[0], privateKey);
        }catch (LocalDBException e){
            Toast.makeText(activity,e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }
}