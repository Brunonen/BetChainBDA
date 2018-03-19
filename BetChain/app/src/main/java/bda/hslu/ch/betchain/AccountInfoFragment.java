package bda.hslu.ch.betchain;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class AccountInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_account_info, container, false);

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap("0xf17f52151EbEF6C7334FAD080c5704D77216b732", BarcodeFormat.QR_CODE, 600, 600);
            ImageView imageViewQrCode = (ImageView) root.findViewById(R.id.qrFriendCode);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch(Exception e) {

        }

        EditText privateKey = (EditText) root.findViewById(R.id.privateKey);

        privateKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MainActivity activity = (MainActivity) getActivity();
                activity.changeFragment(new InsertPrivateKeyFragment());
            }
        });


        return root;
    }

}