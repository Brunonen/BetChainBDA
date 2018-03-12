package bda.hslu.ch.betchain.DTO;

/**
 * Created by Kay on 12/03/2018.
 */

public class Friends {

    private String username ="";
    private int profilePicture = 0;

    public Friends(){

    }


    public Friends(String username, int profilePicture){
        this.username = username;
        this.profilePicture = profilePicture;
    }


    public void setUsername(String username){
        this.username = username;
    }

    public void setProfilePicture(int profilePicture){
        this.profilePicture = profilePicture;
    }

    public String getUsername(){
        return this.username;
    }

    public int getProfilePicture(){
        return this.profilePicture;
    }
}
