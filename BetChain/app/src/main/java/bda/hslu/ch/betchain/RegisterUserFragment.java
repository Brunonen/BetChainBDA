package bda.hslu.ch.betchain;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import bda.hslu.ch.betchain.WebFunctions.AuthenticationFunctions;


public class RegisterUserFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback{

    private String qrData;
    private View rootView;

    private static final int PERMISSION_REQUEST_CAMERA = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_register_user, container, false);


        Button scanQrButton = (Button) rootView.findViewById(R.id.scanQrButton);
        Button registerUser = (Button) rootView.findViewById(R.id.buttonRegisterUser);

        final EditText username = (EditText) rootView.findViewById(R.id.registerUserUsername);
        final EditText password = (EditText) rootView.findViewById(R.id.registerUserPassword);
        final EditText passwordConfirm = (EditText) rootView.findViewById(R.id.registerUserPasswordConfirm);
        final EditText address = (EditText) rootView.findViewById(R.id.ethereumAddressTextField);


        scanQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_CAMERA);

                }else{
                    getQrCode();
                }
                //getQrCode();
            }
        });

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                MainActivity activity = (MainActivity) getActivity();

                String pwd = password.getText().toString();
                String confPwd = passwordConfirm.getText().toString();
                String addr = address.getText().toString();
                String usr = username.getText().toString();
                if(usr.length() > 0){
                    if (pwd.equals(confPwd.toString())) {
                        if (addr.length() == 42) {
                            try {
                                if(AuthenticationFunctions.registerUser(usr, pwd, addr)){
                                    Toast.makeText(activity, "User Successfully Registered!", Toast.LENGTH_SHORT).show();
                                    activity.changeFragmentNoBackstack(new LoginFragment());
                                }
                            } catch (WebRequestException e) {
                                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(activity, "Invalid Address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "Password and Confirmation do not match!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity, "Username can not be empty!", Toast.LENGTH_SHORT).show();
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
            qrData = "";
        } else {
            qrData = scanData;
        }

        EditText ethereumAddressText = (EditText) rootView.findViewById(R.id.ethereumAddressTextField);
        ethereumAddressText.setText(qrData);

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


}