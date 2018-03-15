package bda.hslu.ch.betchain;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import bda.hslu.ch.betchain.Adapters.CustomAdapterFriendInfo;
import bda.hslu.ch.betchain.Adapters.CustomAdapterMyBetInfo;
import bda.hslu.ch.betchain.DTO.Bet;
import bda.hslu.ch.betchain.DTO.Friend;
import bda.hslu.ch.betchain.WebFunctions.BetFunctions;
import bda.hslu.ch.betchain.WebFunctions.FriendFunctions;


public class MyBetsFragment extends Fragment {
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my_bets, container, false);

        List<Bet> bets = BetFunctions.getBetsOfUser("0x627306090abaB3A6e1400e9345bC60c78a8BEf57");

        MainActivity activity = (MainActivity) getActivity();
        ListView betList = (ListView) rootView.findViewById(R.id.myBetsListViewBox);
        CustomAdapterMyBetInfo adapter = new CustomAdapterMyBetInfo (activity, bets);
        betList.setAdapter(adapter);


        return rootView;
    }
}