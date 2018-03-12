package bda.hslu.ch.betchain;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
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



        List<Friend> friends = FriendFunctions.getUserFriendList();


        MainActivity activity = (MainActivity) getActivity();
        ListView friendsList = (ListView) rootView.findViewById(R.id.friendList);
        CustomAdapterFriendInfo adapter = new CustomAdapterFriendInfo (activity, friends);
        friendsList.setAdapter(adapter);


        final Button unfriendButton = (Button) rootView.findViewById(R.id.listUnfriendButton);
        unfriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                System.out.println("Button Test");


            }
        });




        return rootView;
    }
}