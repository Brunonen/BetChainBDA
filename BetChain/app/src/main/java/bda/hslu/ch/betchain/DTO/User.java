package bda.hslu.ch.betchain.DTO;

/**
 * Created by ssj10 on 22/03/2018.
 */

public class User {
    private String username ="";
    private int profilePicture = 0;
    private String address = "";
    private boolean showOnline = true;
    public User(){

    }


    public User(String username, String address, boolean showOnline, int profilePicture){
        this.username = username;
        this.address = address;
        this.profilePicture = profilePicture;
        this.showOnline = showOnline;
    }


    public void setUsername(String username){
        this.username = username;
    }

    public void setProfilePicture(int profilePicture){
        this.profilePicture = profilePicture;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getUsername(){
        return this.username;
    }

    public int getProfilePicture(){
        return this.profilePicture;
    }

    public String getAddress(){ return this.address;}

    public boolean isShowOnline() {
        return showOnline;
    }

    public void setShowOnline(boolean showOnline) {
        this.showOnline = showOnline;
    }
}
