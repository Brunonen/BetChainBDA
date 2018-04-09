package bda.hslu.ch.betchain.Database;

import android.content.Context;

/**
 * Created by ssj10 on 09/04/2018.
 */

public class DBSessionSingleton {


    private static SQLWrapper util;
    private static DBSessionSingleton instance;

    protected DBSessionSingleton() {
        //
    }

    public static DBSessionSingleton getInstance() {
        if (instance == null) {
            instance = new DBSessionSingleton();
        }
        return instance;
    }

    public void initDBSession(Context ctx){
        util = new SQLWrapper(ctx);
    }

    public SQLWrapper getDbUtil()
    {
        return util;
    }


}
