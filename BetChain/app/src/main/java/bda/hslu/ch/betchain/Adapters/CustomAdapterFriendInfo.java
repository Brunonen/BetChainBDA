package bda.hslu.ch.betchain.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bda.hslu.ch.betchain.DTO.Friend;
import bda.hslu.ch.betchain.R;

/**
 * Created by Kay on 12/03/2018.
 */

public class CustomAdapterFriendInfo extends  ArrayAdapter<Friend> {
    private final Activity context;
    private final List<Friend> items;


    public CustomAdapterFriendInfo(Activity content, List<Friend> items){

        super(context, R.layout.list_info_friends, items);
        this.context = context;
        this.items= items;


    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_info_friends, null, true);

        TextView username = (TextView) rowView.findViewById(R.id.listFriendInfoUsername);
        ImageView profilePicture = (ImageView) rowView.findViewById(R.id.listFriendInfoProfilePicture);
        Button unfriendButton = (Button) rowView.findViewById(R.id.listUnfriendButton);
        Friend tmp = items.get(position);

        username.setText(tmp.getUsername());

        if(tmp.getProfilePicture() != 0){
            profilePicture.setImageResource(tmp.getProfilePicture());
        }else{
            profilePicture.setImageResource(R.drawable.ic_blank_avatar);
        }

        return rowView;
    }

}
