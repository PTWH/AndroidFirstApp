package com.example.myfirstapp;

import android.content.Context;
import android.provider.Settings;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MySerSingleton implements IAccess {
    private static MySerSingleton instance;
    private Context appCtx;
    private SimpleDateFormat sdf;
    private IAccess caller;

    private MySerSingleton(Context context) {
        appCtx = context;
        sdf = new SimpleDateFormat(" - yyyy/MM/dd HH:mm:ss");
    }

    public static synchronized MySerSingleton getInstance(Context context) {
        if (instance == null) instance = new MySerSingleton(context);
        return instance;
    }

    public void doHttpRequestSecure( IAccess demandeur , String theUrl,  Map<String, String> params ) {
        caller = demandeur;
        String Unique =  Settings.Secure.getString(appCtx.getContentResolver(), Settings.Secure.ANDROID_ID) + sdf.format(new Date());
        params.put("uniqueid", Unique);
        JSONObject parameters = new JSONObject(params);

        MyRoomSingleton roomInstance = MyRoomSingleton.getInstance(appCtx);
        roomInstance.writeThePostHttp(Unique,parameters,theUrl);

        MyHttpSingleton httpInstance = MyHttpSingleton.getInstance(appCtx);
        httpInstance.doThePostHttp( this, theUrl,parameters );
    }

    @Override
    public void success(JSONObject response) {
        MyRoomSingleton roomInstance = MyRoomSingleton.getInstance(appCtx);
        try
        {
            roomInstance.waitForFutureAndDelete();
        }
        catch( Exception e ) {
            error(e.getMessage());
            return;
        }
        caller.success(response);
    }

    @Override
    public void error(String error) {
        caller.error(error );
    }

    public JSONObject showAll( IAccess demandeur ) {
        caller = demandeur;
        try {
            MyRoomSingleton roomInstance = MyRoomSingleton.getInstance(appCtx);
            demandeur.success(roomInstance.getAbortedHttpRequest());
        }
        catch( Exception e ) {
            error(e.getMessage());
        }
        return null;
    }
}
