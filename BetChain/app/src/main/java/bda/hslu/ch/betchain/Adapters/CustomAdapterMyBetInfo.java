package bda.hslu.ch.betchain.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bda.hslu.ch.betchain.BetInfoFragment;
import bda.hslu.ch.betchain.DTO.Bet;
import bda.hslu.ch.betchain.DTO.Participant;
import bda.hslu.ch.betchain.MainActivity;
import bda.hslu.ch.betchain.R;

/**
 * Created by ssj10 on 15/03/2018.
 */

public class CustomAdapterMyBetInfo extends ArrayAdapter<Bet>{
    private final Activity context;
    private final List<Bet> items;

    public CustomAdapterMyBetInfo(Activity context, List<Bet> items) {

        super(context, R.layout.list_info_mybets, items);
        this.context = context;
        this.items= items;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.list_info_mybets, null, true);

        TextView betTitle = (TextView) rowView.findViewById(R.id.listMyBetInfoBetTitle);
        TextView betStatus = (TextView) rowView.findViewById(R.id.listMyBetInfoBetStatus);
        TextView betPrizePool = (TextView) rowView.findViewById(R.id.listMyBetInfoPrizePool);
        final Bet tmp = items.get(position);

        betTitle.setText(tmp.getBetTitle());
        betStatus.setText(tmp.getBetState().toString());
        betPrizePool.setText(String.valueOf(tmp.getBetPrizePool()) + " Eth");

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity activity = (MainActivity) context;
                activity.setSelectedBetAddress(tmp.getBetAddress());

                activity.changeFragment(new BetInfoFragment());
                System.out.println(activity.getSelectedBetAddress());
            }
        });


        return rowView;
    }
}
