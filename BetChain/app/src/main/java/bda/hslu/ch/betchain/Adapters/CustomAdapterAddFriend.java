package bda.hslu.ch.betchain.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import bda.hslu.ch.betchain.DTO.User;
import bda.hslu.ch.betchain.FriendsFragment;
import bda.hslu.ch.betchain.MainActivity;
import bda.hslu.ch.betchain.R;
import bda.hslu.ch.betchain.WebFunctions.FriendFunctions;
import bda.hslu.ch.betchain.WebRequestException;

/**
 * Created by Bruno Fischlin on 23/03/2018.
 */

public class CustomAdapterAddFriend extends  ArrayAdapter<User> {
    private final Activity context;
    private final List<User> items;


    public CustomAdapterAddFriend(Activity context, List<User> items){

        super(context, R.layout.list_info_addfriend, items);
        this.context = context;
        this.items= items;


    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_info_addfriend, null, true);

        final TextView username = (TextView) rowView.findViewById(R.id.listAddFriendUsername);
        ImageView profilePicture = (ImageView) rowView.findViewById(R.id.listAddFriendProfilePicture);
        Button addFriendButton = (Button) rowView.findViewById(R.id.listAddFriendButton);
        final User tmp = items.get(position);

        username.setText(tmp.getUsername());

        if(tmp.getProfilePicture() != 0){
            profilePicture.setImageResource(tmp.getProfilePicture());
        }else{
            profilePicture.setImageResource(R.drawable.ic_blank_avatar);
        }


        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                MainActivity activity = (MainActivity) context;
                try {
                    FriendFunctions.addFriend(tmp.getUsername());
                    activity.changeFragmentNoBackstack(new FriendsFragment());
                    Toast.makeText(activity, "User sucessfully Added" , Toast.LENGTH_SHORT).show();
                } catch (WebRequestException e) {

                    Toast.makeText(activity,e.getMessage() , Toast.LENGTH_SHORT).show();


                }


            }
        });


        return rowView;
    }

}
