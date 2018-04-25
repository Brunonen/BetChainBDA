package bda.hslu.ch.betchain;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import bda.hslu.ch.betchain.Adapters.CustomAdapterMyBetInfo;
import bda.hslu.ch.betchain.BlockChainFunctions.BlockChainFunctions;
import bda.hslu.ch.betchain.DTO.Bet;
import bda.hslu.ch.betchain.Database.DBSessionSingleton;
import bda.hslu.ch.betchain.WebFunctions.BetFunctions;


public class MyBetsFragment extends Fragment {
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my_bets, container, false);
        final MainActivity activity = (MainActivity) getActivity();

        final LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.setCancelable(false);
        loadingScreen.show(activity.getSupportFragmentManager(), "Loading Screen");

        List<Bet> bets = null;
        try {
            bets = BetFunctions.getUserBets();
        } catch (WebRequestException e) {
            Toast.makeText(activity, "Could not load bets from Server", Toast.LENGTH_SHORT).show();
        }

        String[] loggedInUserInfo = DBSessionSingleton.getInstance().getDbUtil().getLoggedInUserInfo();
        if(loggedInUserInfo[2].equals("")){
            Toast.makeText(activity, "Your account does not have a private Key set! you need one in order to interact with your Contracts", Toast.LENGTH_LONG).show();
        }

        loadingScreen.dismiss();

        ListView betList = (ListView) rootView.findViewById(R.id.myBetsListViewBox);
        if(bets != null) {
            CustomAdapterMyBetInfo adapter = new CustomAdapterMyBetInfo(activity, bets);
            betList.setAdapter(adapter);
        }else{
            Toast.makeText(activity, "You have no registered bets yet!", Toast.LENGTH_SHORT).show();
        }


        final Button startBetCreation = (Button) rootView.findViewById(R.id.buttonGoToCreateNewBet);
        startBetCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                MainActivity activity = (MainActivity) getActivity();

                Fragment createBet = new CreateBetStep1Fragment();

                activity.changeFragment(createBet);

            }
        });


        return rootView;
    }
}