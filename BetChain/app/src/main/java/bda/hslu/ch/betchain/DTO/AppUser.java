package bda.hslu.ch.betchain.DTO;

import bda.hslu.ch.betchain.Database.DBSessionSingleton;

/**
 * Created by Bruno Fischlin on 27/04/2018.
 */

public class AppUser {
    private String publicAddress;   //Public Ethereum Address
    private String privateKey;      //Encrypted Private KEy
    private String username;        //Username
    private String pwd;             //Hasehd PW
    private boolean automaticLogin;
    private CurrencySelector prefferedCurrency;

    public AppUser(){

    }

    public static AppUser getLoggedInUserObject(){
        AppUser user = new AppUser();
        String[] loggedInUserInfo = DBSessionSingleton.getInstance().getDbUtil().getLoggedInUserInfo();
        user.setUsername(loggedInUserInfo[0]);
        user.setPwd(loggedInUserInfo[1]);
        user.setPrivateKey(loggedInUserInfo[2]);
        user.setPublicAddress(loggedInUserInfo[3]);
        user.setPrefferedCurrency(CurrencySelector.vallueOfStirng(loggedInUserInfo[4]));
        user.setAutomaticLogin(DBSessionSingleton.getInstance().getDbUtil().checkIfUserNeedsToBeLoggedIn());

        return user;
    }

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean isAutomaticLogin() {
        return automaticLogin;
    }

    public void setAutomaticLogin(boolean automaticLogin) {
        this.automaticLogin = automaticLogin;
    }

    public CurrencySelector getPrefferedCurrency() {
        return prefferedCurrency;
    }

    public void setPrefferedCurrency(CurrencySelector prefferedCurrency) {
        this.prefferedCurrency = prefferedCurrency;
    }
}
