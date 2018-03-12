package bda.hslu.ch.betchain.DTO;

/**
 * Created by Kay on 12/03/2018.
 */

public class Friend {

    private String username ="";
    private int profilePicture = 0;
    private String address = "";

    public Friend(){

    }


    public Friend(String username, String address, int profilePicture){
        this.username = username;
        this.address = address;
        this.profilePicture = profilePicture;
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
}
