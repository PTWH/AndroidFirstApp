package com.example.myfirstapp;

import org.json.JSONArray;
import org.json.JSONObject;

interface IAccess {
    public void success(JSONObject  response);
    public void error(String error);
}
