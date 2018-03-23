package bda.hslu.ch.betchain;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import bda.hslu.ch.betchain.Adapters.CustomAdapterAddFriend;
import bda.hslu.ch.betchain.Adapters.CustomAdapterFriendInfo;
import bda.hslu.ch.betchain.DTO.User;
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
                                try {
                                    List<User> foundUsers = UserFunctions.getUserByUsername(usernameToSearchFor.getText().toString());
                                    ListView userList = (ListView) rootView.findViewById(R.id.usersToAddAsFriendList);
                                    CustomAdapterAddFriend adapter = new CustomAdapterAddFriend(activity, foundUsers);
                                    userList.setAdapter(adapter);
                                } catch (WebRequestException e) {
                                    Toast.makeText(activity,e.getMessage() , Toast.LENGTH_SHORT).show();
                                }

                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );
        return rootView;
    }
}