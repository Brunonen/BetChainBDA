package bda.hslu.ch.betchain.WebFunctions;

import java.util.ArrayList;
import java.util.List;

import bda.hslu.ch.betchain.DTO.Friend;
import bda.hslu.ch.betchain.R;

/**
 * Created by ssj10 on 12/03/2018.
 */

public class FriendFunctions {

    public static List<Friend> getUserFriendList(){

        List<Friend> friendList = new ArrayList<Friend>();

        friendList.add(new Friend("Bruno Fischlin", "0xf17f52151EbEF6C7334FAD080c5704D77216b732", R.drawable.bruno));
        friendList.add(new Friend("Damir Hodzic","0xC5fdf4076b8F3A5357c5E395ab970B5B54098Fef", R.drawable.damir));
        friendList.add(new Friend("Alex Neher" ,"0x821aEa9a577a9b44299B9c15c88cf3087F3b5544", R.drawable.alex));
        friendList.add(new Friend("Suki Kasipillai" , "0x0d1d4e623D10F9FBA5Db95830F7d3839406C6AF2", R.drawable.suki));

        return friendList;
    }
}
