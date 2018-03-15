package bda.hslu.ch.betchain.WebFunctions;

import android.provider.Telephony;

import java.util.ArrayList;
import java.util.List;

import bda.hslu.ch.betchain.DTO.Bet;
import bda.hslu.ch.betchain.DTO.BetRole;
import bda.hslu.ch.betchain.DTO.BetState;
import bda.hslu.ch.betchain.DTO.Participant;

/**
 * Created by Bruno Fischlin on 15/03/2018.
 */

public class BetFunctions {

    public static List<Bet> getBetsOfUser(String address){
        List<Bet> userBets = new ArrayList<Bet>();

        List<Participant> participants = new ArrayList<Participant>();
        //BetState betState, String betTitle, String betConditions, float betEntryFee, float betPrizePool, List<Participant> participants, String betAddress
        userBets.add(new Bet(BetState.PENDING, "Kay's Bet", "Kay bets that he can chuck an entire Pitcher in less then 3 seconds.", 2.0f, 8.0f, getParticipantsBet1() , "sldkslkdsldksldksl"));
        userBets.add(new Bet(BetState.LOCKED, "Ice Bucket", "Bruno bets he completes the Ice Bucket challenge with 5 Buckets and no shirt in winter.", 10.0f, 40.0f, getParticipantsBet2() , "sldkslkdsldksldksl"));
        userBets.add(new Bet(BetState.EVALUATION, "Test Bet", "Test", 0.5f, 1.5f, getParticipantsBet3() , "sldkslkdsldksldksl"));

        return userBets;
    }

    private static List<Participant> getParticipantsBet1(){
        List<Participant> participants = new ArrayList<Participant>();

        //String username, String address, boolean betAccepted, boolean voted, BetRole betRole
        participants.add(new Participant("Kay Hartmann", "0x627306090abaB3A6e1400e9345bC60c78a8BEf57", true, false, BetRole.OWNER));
        participants.add(new Participant("Bruno Fischlin", "0xf17f52151EbEF6C7334FAD080c5704D77216b732", true, false, BetRole.OPPOSER));
        participants.add(new Participant("Damir Hodzic", "0xC5fdf4076b8F3A5357c5E395ab970B5B54098Fef", true, false, BetRole.OPPOSER));
        participants.add(new Participant("Alex Neher", "0x821aEa9a577a9b44299B9c15c88cf3087F3b5544", false, false, BetRole.SUPPORTER));
        participants.add(new Participant("Suki Kasipillai", "0x0d1d4e623D10F9FBA5Db95830F7d3839406C6AF2", false, false, BetRole.NOTAR));

        return participants;
    }

    private static List<Participant> getParticipantsBet2(){
        List<Participant> participants = new ArrayList<Participant>();

        //String username, String address, boolean betAccepted, boolean voted, BetRole betRole
        participants.add(new Participant("Kay Hartmann", "0x627306090abaB3A6e1400e9345bC60c78a8BEf57", true, false, BetRole.OPPOSER));
        participants.add(new Participant("Bruno Fischlin", "0xf17f52151EbEF6C7334FAD080c5704D77216b732", true, false, BetRole.OWNER));
        participants.add(new Participant("Damir Hodzic", "0xC5fdf4076b8F3A5357c5E395ab970B5B54098Fef", true, false, BetRole.OPPOSER));
        participants.add(new Participant("Alex Neher", "0x821aEa9a577a9b44299B9c15c88cf3087F3b5544", true, false, BetRole.SUPPORTER));
        return participants;
    }


    private static List<Participant> getParticipantsBet3(){
        List<Participant> participants = new ArrayList<Participant>();

        //String username, String address, boolean betAccepted, boolean voted, BetRole betRole
        participants.add(new Participant("Kay Hartmann", "0x627306090abaB3A6e1400e9345bC60c78a8BEf57", true, true, BetRole.OWNER));
        participants.add(new Participant("Bruno Fischlin", "0xf17f52151EbEF6C7334FAD080c5704D77216b732", true, false, BetRole.SUPPORTER));
        participants.add(new Participant("Damir Hodzic", "0xC5fdf4076b8F3A5357c5E395ab970B5B54098Fef", true, false, BetRole.OPPOSER));
        return participants;
    }
}
