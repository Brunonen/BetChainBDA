package bda.hslu.ch.betchain;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bda.hslu.ch.betchain.Adapters.CustomAdapterAddFriend;
import bda.hslu.ch.betchain.Adapters.CustomAdapterFriendInfo;
import bda.hslu.ch.betchain.DTO.AppUser;
import bda.hslu.ch.betchain.DTO.Friend;
import bda.hslu.ch.betchain.DTO.User;
import bda.hslu.ch.betchain.Database.DBSessionSingleton;
import bda.hslu.ch.betchain.Database.SQLWrapper;
import bda.hslu.ch.betchain.WebFunctions.FriendFunctions;
import bda.hslu.ch.betchain.WebFunctions.UserFunctions;


public class SearchFriendOnlineFragment extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_search_friend_online, container, false);
        final EditText usernameToSearchFor = (EditText) rootView.findViewById(R.id.searchUserUsername);
        final MainActivity activity = (MainActivity) getActivity();
        ((EditText)rootView.findViewById(R.id.searchUserUsername)).setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                if(!AppUser.getLoggedInUserObject().getUsername().equals(usernameToSearchFor.getText().toString())) {
                                    try {
                                        List<User> foundUsers = UserFunctions.getUserByUsername(usernameToSearchFor.getText().toString());
                                        List<Friend> userFriends = new ArrayList<>();

                                        try {
                                            userFriends = FriendFunctions.getUserFriendList();
                                        } catch (WebRequestException e) {

                                        }

                                        ListView userList = (ListView) rootView.findViewById(R.id.usersToAddAsFriendList);

                                        //Remove all entries which are already friends
                                        if (userFriends.size() > 0) {
                                            for (int i = 0; i < userFriends.size(); i++) {
                                                for (int n = 0; n < foundUsers.size(); n++) {
                                                    if (userFriends.get(i).getUsername().equals(foundUsers.get(n).getUsername())) {
                                                        foundUsers.remove(n);
                                                        n = 0;
                                                    }
                                                }
                                            }
                                        }

                                        if (foundUsers.size() > 0) {


                                            CustomAdapterAddFriend adapter = new CustomAdapterAddFriend(activity, foundUsers);
                                            userList.setAdapter(adapter);
                                        } else {
                                            Toast.makeText(activity, "All users that match this criteria are already your friend!", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (WebRequestException e) {
                                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(activity, "You can't add yourself as a friend!", Toast.LENGTH_SHORT).show();
                                }

                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );

        final ImageView searchFromButton = (ImageView) rootView.findViewById(R.id.searchButtonMagnifying);
        searchFromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(!AppUser.getLoggedInUserObject().getUsername().equals(usernameToSearchFor.getText().toString())) {
                    try {
                        List<User> foundUsers = UserFunctions.getUserByUsername(usernameToSearchFor.getText().toString());
                        List<Friend> userFriends = new ArrayList<>();

                        try {
                            userFriends = FriendFunctions.getUserFriendList();
                        } catch (WebRequestException e) {

                        }

                        ListView userList = (ListView) rootView.findViewById(R.id.usersToAddAsFriendList);

                        //Remove all entries which are already friends
                        if (userFriends.size() > 0) {
                            for (int i = 0; i < userFriends.size(); i++) {
                                for (int n = 0; n < foundUsers.size(); n++) {
                                    if (userFriends.get(i).getUsername().equals(foundUsers.get(n).getUsername())) {
                                        foundUsers.remove(n);
                                        n = 0;
                                    }
                                }
                            }
                        }

                        if (foundUsers.size() > 0) {


                            CustomAdapterAddFriend adapter = new CustomAdapterAddFriend(activity, foundUsers);
                            userList.setAdapter(adapter);
                        } else {
                            Toast.makeText(activity, "All users that match this criteria are already your friend!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (WebRequestException e) {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity, "You can't add yourself as a friend!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

}