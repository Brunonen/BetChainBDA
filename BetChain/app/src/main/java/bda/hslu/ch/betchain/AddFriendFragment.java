package bda.hslu.ch.betchain;

import android.app.LauncherActivity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class AddFriendFragment extends Fragment {

    private View rootView;
    private String qrData;
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
                    getQrCode();
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
            activity.changeFragment(new AddFriendInfoFragment());
        }

    }
}