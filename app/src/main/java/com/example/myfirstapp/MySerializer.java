package com.example.myfirstapp;

        import android.content.Context;
        import android.provider.Settings;
        import org.json.JSONObject;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Map;

public class MySerializer implements IAccess {
    private static MySerializer instance;
    private Context appCtx;
    private SimpleDateFormat sdf;
    private IAccess caller;
    private Boolean isSecureRequest;

    private MySerializer(Context context) {
        appCtx = context;
        sdf = new SimpleDateFormat(" - yyyy/MM/dd HH:mm:ss");
    }

    public static synchronized MySerializer getInstance(Context context) {
        if (instance == null) instance = new MySerializer(context);
        return instance;
    }

    public void doHttpRequest(IAccess demandeur , String theUrl, Map<String, String> params , Boolean isSecureRequest ) {
        caller = demandeur;
        this.isSecureRequest = isSecureRequest;
        String Unique =  Settings.Secure.getString(appCtx.getContentResolver(), Settings.Secure.ANDROID_ID) + sdf.format(new Date());
        params.put("uniqueid", Unique);
        JSONObject parameters = new JSONObject(params);
        if ( isSecureRequest ) {
            MyRoomSingleton roomInstance = MyRoomSingleton.getInstance(appCtx);
            roomInstance.writeThePostHttp(Unique,parameters,theUrl);
        }

        MyHttpSingleton httpInstance = MyHttpSingleton.getInstance(appCtx);
        httpInstance.doThePostHttp( this, theUrl,parameters );
    }

    @Override
    public void success(JSONObject response) {
        if ( isSecureRequest ) {
            MyRoomSingleton roomInstance = MyRoomSingleton.getInstance(appCtx);
            try
            {
                roomInstance.waitForFutureAndDelete();
            }
            catch( Exception e ) {
                error(1,e.getMessage());
                return;
            }
        }
        caller.success(response);
    }

    @Override
    public void error(Integer typeError , String error) {
        caller.error(typeError,error );
    }

    public JSONObject showAll( IAccess demandeur ) {
        caller = demandeur;
        try {
            MyRoomSingleton roomInstance = MyRoomSingleton.getInstance(appCtx);
            demandeur.success(roomInstance.getAbortedHttpRequest());
        }
        catch( Exception e ) {
            error(1,e.getMessage());
        }
        return null;
    }
}

