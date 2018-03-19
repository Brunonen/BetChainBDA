package bda.hslu.ch.betchain;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class InsertPrivateKeyFragment extends Fragment {
    private String qrData;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_insert_private_key, container, false);


        Button scanPrivateKey = (Button) rootView.findViewById(R.id.privateKeyScanQRButton);

        scanPrivateKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQrCode();
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
}