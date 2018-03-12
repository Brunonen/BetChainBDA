package bda.hslu.ch.betchain;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import bda.hslu.ch.betchain.Adapters.CustomAdapterFriendInfo;
import bda.hslu.ch.betchain.DTO.Friend;


public class FriendsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_friends, container, false);

        Friend kay = new Friend("Kay Hartmann",R.drawable.kay);
        Friend bruno = new Friend("Bruno Fischlin", R.drawable.bruno);
        Friend damir = new Friend("Damir Hodzic", R.drawable.damir);
        Friend alex = new Friend("Alex Neher", R.drawable.alex);
        Friend suki = new Friend("Suki Kasipillai", R.drawable.suki);

        List<Friend> friends = new ArrayList<Friend>();

        friends.add(kay);
        friends.add(bruno);
        friends.add(damir);
        friends.add(alex);
        friends.add(suki);

        MainActivity activity = (MainActivity) getActivity();
        ListView friendsList = (ListView) root.findViewById(R.id.friendList);
        CustomAdapterFriendInfo adapter = new CustomAdapterFriendInfo (activity, friends);
        friendsList.setAdapter(adapter);


        final Button unfriendButton = (Button) root.findViewById(R.id.listUnfriendButton);
        unfriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
               // MainActivity activity = (MainActivity) getActivity();
                //Fragment step2 = new CreateBetStep2Fragment();

                //Fragment step4 = new CreateBetStep4Fragment();

               // activity.changeFragment(step4);

            }
        });




        return root;
    }
}