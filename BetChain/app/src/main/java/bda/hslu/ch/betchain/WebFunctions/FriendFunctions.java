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

        friendList.add(new Friend("Bruno Fischlin", R.drawable.bruno));
        friendList.add(new Friend("Damir Hodzic", R.drawable.damir));
        friendList.add(new Friend("Alex Neher" , R.drawable.alex));
        friendList.add(new Friend("Suki Kasipillai" , R.drawable.suki));
        
        return friendList;
    }
}
