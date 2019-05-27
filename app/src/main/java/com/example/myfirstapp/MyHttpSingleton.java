package com.example.myfirstapp;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.Map;

public class MyHttpSingleton {
    private static MyHttpSingleton instance;
    private Context ctx;
    private RequestQueue requestQueue;


    private MyHttpSingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized MyHttpSingleton getInstance(Context context) {
        if (instance == null) instance = new MyHttpSingleton(context);
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    private <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void doThePostHttp(final IAccess cmp , String url , JSONObject parameters) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cmp.success(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = error.getMessage();
                if ( msg == null) msg = error.toString();
                cmp.error(msg);
            }
        });
        addToRequestQueue(jsonRequest);
    }
}
