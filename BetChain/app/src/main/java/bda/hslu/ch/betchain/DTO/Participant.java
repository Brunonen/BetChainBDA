package bda.hslu.ch.betchain.DTO;

import java.io.Serializable;

/**
 * Created by Bruno Fischlin on 11/03/2018.
 */

public class Participant implements Serializable {
    private String username = "";
    private String address = "";
    private boolean betAccepted = false;
    private boolean voted = false;

    private BetRole betRole = null;

    private int profilePicture = 0;
    private int infoIcon = 0;

    public Participant() {

    }

    public Participant(String username, String address, boolean betAccepted, boolean voted, BetRole betRole) {
        this.username = username;
        this.address = address;
        this.betAccepted = betAccepted;
        this.voted = voted;
        this.betRole = betRole;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setBetAccept(boolean hasAccepted){
        this.betAccepted = hasAccepted;
    }

    public void setBetVoted(boolean hasVoted){
        this.voted = hasVoted;
    }

    public void setBetRole(BetRole betRole){
        this.betRole = betRole;
    }

    public void setProfilePicture(int profilePicture){
        this.profilePicture = profilePicture;
    }

    public void setInfoIcon(int infoIcon){
        this.infoIcon = infoIcon;
    }

    public String getUsername(){
        return this.username;
    }

    public String getAddress(){
        return this.address;
    }

    public boolean hasBetAccepted(){
        return this.betAccepted;
    }

    public boolean hasVoted(){
        return this.voted;
    }

    public BetRole getBetRole(){
        return this.betRole;
    }

    public int getInfoIcon(){
        return this.infoIcon;
    }

    public int getProfilePicture(){
        return this.profilePicture;
    }

}