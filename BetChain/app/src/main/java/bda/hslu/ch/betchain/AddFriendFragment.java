package bda.hslu.ch.betchain;

import android.Manifest;
import android.app.LauncherActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class AddFriendFragment extends Fragment {

    private View rootView;
    private String qrData;
    private static final int PERMISSION_REQUEST_CAMERA = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_friend, container, false);
        final MainActivity activity = (MainActivity) getActivity();
        ListView addFriends = (ListView) rootView.findViewById(R.id.addFriendsList);

        addFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                if(parent.getItemAtPosition(position).equals("Search Online")){
                    activity.changeFragment(new SearchFriendOnlineFragment());
                }else if(parent.getItemAtPosition(position).equals("Via QR-Code")){
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(
                                new String[]{Manifest.permission.CAMERA},
                                PERMISSION_REQUEST_CAMERA);

                    }else{
                        getQrCode();
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
            qrData = "";
        } else {
            qrData = scanData;
        }

        if(!qrData.equals("")) {

            final MainActivity activity = (MainActivity) getActivity();
            activity.setUserAddressToGetInfoFrom(qrData);
            activity.changeFragment(new AddFriendInfoFragment());
        }

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