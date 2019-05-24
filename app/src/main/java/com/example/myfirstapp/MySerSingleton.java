package com.example.myfirstapp;

import android.content.Context;
import android.provider.Settings;
import com.example.myfirstapp.Room.AppDatabase;
import com.example.myfirstapp.Room.FactoryRoom;
import com.example.myfirstapp.Room.JSONData;
import com.example.myfirstapp.Room.JSONDataDao;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MySerSingleton implements IAccess {
    private static MySerSingleton instance;
    private Context ctx;
    private SimpleDateFormat sdf;
    private MyHttpSingleton httpInstance;
    private AppDatabase db;
    private ExecutorService executorService;
    private Future future;

    private MySerSingleton(Context context) {
        executorService = Executors.newSingleThreadExecutor();
        ctx = context;
        db = FactoryRoom.getInstance(ctx);
        sdf = new SimpleDateFormat(" - yyyy/MM/dd HH:mm:ss");
    }

    public static synchronized MySerSingleton getInstance(Context context) {
        if (instance == null) instance = new MySerSingleton(context);
        return instance;
    }

    public void doTheSerRequestWithJSON( String theUrl,  Map<String, String> params ) {
        String Unique =  Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID) + sdf.format(new Date());
        params.put("uniqueid", Unique);
        JSONObject parameters = new JSONObject(params);


        final JSONData jsd = new JSONData();
        jsd.uniqueid = Unique;
        jsd.JSONStr = parameters.toString();
        jsd.JSONUrl = theUrl;
        future = executorService.submit(new Callable(){
            @Override
            public Object call() throws Exception {
                JSONDataDao jsonDataDao;
                jsonDataDao = db.jsonDataDao();
                jsonDataDao.insert(jsd);
                return jsd;
            }
        });

        MyHttpSingleton httpInstance = MyHttpSingleton.getInstance(ctx);
        httpInstance.doTheHttpRequestWithJSON( this, jsd.JSONUrl,parameters );
    }

    @Override
    public void success(JSONObject response) {
        JSONData jsd =null;
        try
        {
            jsd = (JSONData) future.get();
        }
        catch( Exception e) {
            error(e.getMessage());
            return;
        }

        final JSONData finalJsd = jsd;
        future = executorService.submit(new Callable(){
            @Override
            public Object call() throws Exception {
                JSONDataDao jsonDataDao;
                jsonDataDao = db.jsonDataDao();
                jsonDataDao.delete(finalJsd);
                return null;
            }
        });

        ((IAccess) ctx).success(response);
    }

    @Override
    public void error(String error) {
        ((IAccess) ctx).error(error );
    }

    public void showAll() {
        future = executorService.submit(new Callable(){
            @Override
            public Object call() throws Exception {
                JSONDataDao jsonDataDao = db.jsonDataDao();
                return jsonDataDao.getAll();
            }
        });

        try
        {
            List<JSONData> lst = (List<JSONData>) future.get();
            JSONObject jso = new JSONObject();
            JSONArray jsa = new JSONArray(lst);
            for (int index = 0; index < lst.size(); index++) {
                JSONData jsd = lst.get(index);
                JSONObject jso2 = new JSONObject(jsd.JSONStr);
                jsa.put(index, jso2);
            }
            jso.put("tab", jsa);
            ((IAccess) ctx).success(jso);
        }
        catch( Exception e ) {((IAccess) ctx).error(e.getMessage());}
    }
}
