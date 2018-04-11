package bda.hslu.ch.betchain;


import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import bda.hslu.ch.betchain.Adapters.CustomAdapterFriendInfo;
import bda.hslu.ch.betchain.DTO.Friend;
import bda.hslu.ch.betchain.WebFunctions.FriendFunctions;


public class FriendsFragment extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        final MainActivity activity = (MainActivity) getActivity();

        List<Friend> friends = null;

        final LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.setCancelable(false);
        loadingScreen.show(activity.getSupportFragmentManager(), "Loading Screen");

        @SuppressLint("StaticFieldLeak") FriendFunctions getUserFriends = new FriendFunctions(){


            @Override
            public void onSuccess(Object result) {
                List<Friend> friends;
                friends = (List<Friend>) result;
                if(friends.size() > 0) {
                    ListView friendsList = (ListView) rootView.findViewById(R.id.friendList);
                    CustomAdapterFriendInfo adapter = new CustomAdapterFriendInfo(activity, friends);
                    friendsList.setAdapter(adapter);
                    loadingScreen.dismiss();
                }
            }

            @Override
            public void onFailure(Object e) {
                Exception exec = (Exception) e;
                loadingScreen.dismiss();
                Toast.makeText(activity, exec.getMessage() , Toast.LENGTH_SHORT).show();
            }
        };

        getUserFriends.execute("getUserFriendList");

        /*friends = FriendFunctions.getUserFriendList();

        if(friends.size() > 0) {
            ListView friendsList = (ListView) rootView.findViewById(R.id.friendList);
            CustomAdapterFriendInfo adapter = new CustomAdapterFriendInfo(activity, friends);
            friendsList.setAdapter(adapter);
        }*/




        Button addFriendButton = (Button) rootView.findViewById(R.id.addFriendsButton);

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Fragment addFriendFragment = new AddFriendFragment();
                MainActivity activity = (MainActivity) getActivity();
                activity.changeFragment(addFriendFragment);


            }
        });

        return rootView;
    }
}