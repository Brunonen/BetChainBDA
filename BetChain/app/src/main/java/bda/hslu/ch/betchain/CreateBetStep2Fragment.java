package bda.hslu.ch.betchain;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bda.hslu.ch.betchain.DTO.AppUser;
import bda.hslu.ch.betchain.DTO.CurrencySelector;
import bda.hslu.ch.betchain.DTO.Participant;
import bda.hslu.ch.betchain.WebFunctions.BetFunctions;
import bda.hslu.ch.betchain.WebFunctions.CurrencyExchangeAPI;
import bda.hslu.ch.betchain.WebFunctions.CurrencyExchangerSingleton;


public class CreateBetStep2Fragment extends Fragment {
    private CurrencySelector fromCurrency = CurrencySelector.CHF;

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_bet_step2, container, false);

        final MainActivity activity = (MainActivity) getActivity();

        activity.setBetCreationParticipants(new ArrayList<Participant>());

        TextView betConditions = (TextView) rootView.findViewById(R.id.betConditions);
        betConditions.setText(activity.getBetCreationBetConditions());

        EditText betEntryFees = (EditText) rootView.findViewById(R.id.betEntryFee);
        betEntryFees.setText(String.valueOf(activity.getBetCreationBetEntryFee()));


        final Button goToStep3 = (Button) rootView.findViewById(R.id.button_goToStep3);
        goToStep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EditText betConditions = (EditText) rootView.findViewById(R.id.betConditions);
                EditText betEntryFees = (EditText) rootView.findViewById(R.id.betEntryFee);
                MainActivity activity = (MainActivity) getActivity();
                //Fragment step2 = new CreateBetStep2Fragment();
                String inputError = BetFunctions.checkIfBetInputsAreValid(null, betConditions.getText().toString(), null, betEntryFees.getText().toString());
                if (inputError.equals("")) {
                    activity.setBetCreationBetConditions(betConditions.getText().toString());
                    activity.setBetCreationBetEntryFee(Float.valueOf(betEntryFees.getText().toString()));
                    Fragment step3 = new CreateBetStep3Fragment();
                    activity.changeFragment(step3);
                }else{
                    Toast.makeText(activity, inputError, Toast.LENGTH_SHORT).show();
                }

            }
        });



        Spinner spinner = (Spinner) rootView.findViewById(R.id.currencySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.currenciesSpinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(new ArrayAdapter<CurrencySelector>(activity, android.R.layout.simple_spinner_item, CurrencySelector.values()));
        spinner.setSelection(AppUser.getLoggedInUserObject().getPrefferedCurrency().getCurrency());
        fromCurrency = AppUser.getLoggedInUserObject().getPrefferedCurrency();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EditText betEntryFees = (EditText) rootView.findViewById(R.id.betEntryFee);
                String betEntryFeeText = betEntryFees.getText().toString();


                if(!fromCurrency.toString().equals("") && !parent.getItemAtPosition(position).toString().equals("")) {
                    String newValue = CurrencyExchangerSingleton.getInstance().exchangeCurrency(betEntryFeeText, fromCurrency, CurrencySelector.valueOfStirng(parent.getItemAtPosition(position).toString()));
                    fromCurrency = CurrencySelector.valueOfStirng(parent.getItemAtPosition(position).toString());
                    activity.setSelectedCurrency(CurrencySelector.valueOfStirng(parent.getItemAtPosition(position).toString()));

                    betEntryFees.setText(newValue);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }
}