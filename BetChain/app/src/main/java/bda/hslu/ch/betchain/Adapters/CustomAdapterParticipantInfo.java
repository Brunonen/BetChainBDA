package bda.hslu.ch.betchain.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import bda.hslu.ch.betchain.R;

import bda.hslu.ch.betchain.DTO.Participant;

/**
 * Created by ssj10 on 11/03/2018.
 */

public class CustomAdapterParticipantInfo extends ArrayAdapter<Participant> {
    private final Activity context;
    private final List<Participant> items;

    public CustomAdapterParticipantInfo(Activity context, List<Participant> items) {

        super(context, R.layout.list_info_participant, items);
        this.context = context;
        this.items= items;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_info_participant, null, true);

        TextView username = (TextView) rowView.findViewById(R.id.listPartInfoBetTitle);
        TextView userRole = (TextView) rowView.findViewById(R.id.listPartInfoUserRole);
        ImageView profilePicture = (ImageView) rowView.findViewById(R.id.listPartInfoProfilePicture);
        ImageView infoIcon = (ImageView) rowView.findViewById(R.id.listPartInfoInfoIcon);
        Participant tmp = items.get(position);

        username.setText(tmp.getUsername());
        userRole.setText("" + tmp.getBetRole().toString());

        if(tmp.getProfilePicture() != 0){
            profilePicture.setImageResource(tmp.getProfilePicture());
        }else{
            profilePicture.setImageResource(R.drawable.ic_blank_avatar);
        }

        if(tmp.getInfoIcon() != 0) {
            infoIcon.setImageResource(tmp.getInfoIcon());
        }else{
            infoIcon.setVisibility(View.GONE);
        }

        return rowView;
    }
}
