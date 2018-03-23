package bda.hslu.ch.betchain;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import bda.hslu.ch.betchain.DTO.User;
import bda.hslu.ch.betchain.WebFunctions.FriendFunctions;
import bda.hslu.ch.betchain.WebFunctions.UserFunctions;


public class AddFriendInfoFragment extends Fragment {

    User usertoAddInfo;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_friend_info, container, false);

        final MainActivity activity = (MainActivity) getActivity();

        String addressToGetInfosFrom = activity.getUserAddressToGetInfoFrom();
        EditText userInfoUsername = (EditText) rootView.findViewById(R.id.addFriendInfoUsername);
        ImageView userInfoPofilePicture = (ImageView) rootView.findViewById(R.id.addFriendInfoProfilePic);
        Button addFriendbutton = (Button) rootView.findViewById(R.id.buttonUserInfoAddUserAsFriend);


        try {
            usertoAddInfo = UserFunctions.getUserByQR(addressToGetInfosFrom);

            userInfoUsername.setText(usertoAddInfo.getUsername());
            if(usertoAddInfo.getProfilePicture() != 0){
                userInfoPofilePicture.setImageResource(usertoAddInfo.getProfilePicture());
            }else{
                userInfoPofilePicture.setImageResource(R.drawable.ic_blank_avatar);
            }

        } catch (WebRequestException e) {
            Toast.makeText(activity,e.getMessage() , Toast.LENGTH_SHORT).show();
            activity.changeFragment(new AddFriendFragment());
        }

        addFriendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(!usertoAddInfo.getUsername().equals("")){
                    try {
                        if(FriendFunctions.addFriend(usertoAddInfo.getUsername())){
                            Toast.makeText(activity,"Friend Successfully added!" , Toast.LENGTH_SHORT).show();
                            activity.changeFragment(new FriendsFragment());
                        }
                    } catch (WebRequestException e) {
                        Toast.makeText(activity,e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return rootView;
    }
}