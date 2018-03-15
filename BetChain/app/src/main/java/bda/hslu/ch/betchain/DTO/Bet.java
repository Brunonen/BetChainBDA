package bda.hslu.ch.betchain.DTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssj10 on 15/03/2018.
 */

public class Bet {

    private BetState betState;
    private String betTitle;
    private String betConditions;
    private List<Participant> participants;
    private float betEntryFee;
    private float betPrizePool;
    private String betAddress;

    public Bet(){
        this.betState = BetState.PENDING;
        this.betTitle = "";
        this.betConditions = "";
        this.participants = new ArrayList<Participant>();
        this.betEntryFee = 0.0f;
        this.betPrizePool = 0.0f;
        this.betAddress = "";
    }

    public Bet(BetState betState, String betTitle, String betConditions, float betEntryFee, float betPrizePool, List<Participant> participants, String betAddress){
        this.betState = betState;
        this.betTitle = betTitle;
        this.betConditions = betConditions;
        this.participants = participants;
        this.betEntryFee = betEntryFee;
        this.betPrizePool = betPrizePool;
        this.betAddress = betAddress;
    }

    public void setBetState(BetState betState) {
        this.betState = betState;
    }

    public void setBetTitle(String betTitle) {
        this.betTitle = betTitle;
    }

    public void setBetConditions(String betConditions) {
        this.betConditions = betConditions;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void setBetEntryFee(float betEntryFee) {
        this.betEntryFee = betEntryFee;
    }

    public void setBetPrizePool(float betPrizePool) {
        this.betPrizePool = betPrizePool;
    }

    public void setBetAddress(String betAddress) {
        this.betAddress = betAddress;
    }

    public BetState getBetState() {
        return betState;
    }

    public String getBetTitle() {
        return betTitle;
    }

    public String getBetConditions() {
        return betConditions;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public float getBetEntryFee() {
        return betEntryFee;
    }

    public float getBetPrizePool() {
        return betPrizePool;
    }

    public String getBetAddress() {
        return betAddress;
    }
}
