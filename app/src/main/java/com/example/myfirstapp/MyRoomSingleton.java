package com.example.myfirstapp;

import android.content.Context;

import com.example.myfirstapp.Room.AppDatabase;
import com.example.myfirstapp.Room.FactoryRoom;
import com.example.myfirstapp.Room.JSONData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyRoomSingleton {
    private static MyRoomSingleton instance;
    private Context appCtx;
    private AppDatabase dataBase;
    private ExecutorService executorService;
    private Future future = null;

    public static synchronized MyRoomSingleton getInstance(Context context) {
        if (instance == null) instance = new MyRoomSingleton(context);
        return instance;
    }

    private MyRoomSingleton(Context context) {
        appCtx = context;
        dataBase = FactoryRoom.getInstance(appCtx);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void writeThePostHttp(String Unique, JSONObject parameters,String theUrl) {
        final JSONData jsd = new JSONData();
        jsd.uniqueid = Unique;
        jsd.JSONStr = parameters.toString();
        jsd.JSONUrl = theUrl;
        future = executorService.submit(new Callable(){
            @Override
            public Object call() throws Exception {
                dataBase.jsonDataDao().insert(jsd);
                return jsd;
            }
        });
    }

    public void waitForFutureAndDelete() throws InterruptedException, ExecutionException {
        if ( future == null ) return;
        final JSONData jsd = (JSONData) future.get();
        future = executorService.submit(new Callable(){
            @Override
            public Object call() throws Exception {
                dataBase.jsonDataDao().delete(jsd);
                return null;
            }
        });
    }

    public JSONObject getAbortedHttpRequest() throws InterruptedException, ExecutionException, JSONException {
        future = executorService.submit(new Callable(){
            @Override
            public Object call() throws Exception {
                return dataBase.jsonDataDao().getAll();
            }
        });
        List<JSONData> lst = (List<JSONData>) future.get();
        JSONObject jso = new JSONObject();
        JSONArray jsa = new JSONArray(lst);
        for (int index = 0; index < lst.size(); index++) {
            JSONData jsd = lst.get(index);
            JSONObject jso2 = new JSONObject(jsd.JSONStr);
            jsa.put(index, jso2);
        }
        jso.put("tab", jsa);
        return jso;
    }
}
