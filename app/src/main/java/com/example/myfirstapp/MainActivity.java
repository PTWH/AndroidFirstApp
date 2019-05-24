package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements IAccess {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private MySerSingleton serInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serInstance = MySerSingleton.getInstance(this.getApplicationContext());
    }

    public void sendMessage(View view) {
        Button myButt = (Button)findViewById(R.id.button);
        myButt.setEnabled(false);
        httpAccess();
        /*
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        */
    }

    public void SqLiteAll(View view) {
        serInstance.showAll();
    }

    private void httpAccess() {
        final String[] message = new String[1];
        Map<String, String> params = new HashMap();
        params.put("user", "touret");
        params.put("pass", "Washere69+-");
        serInstance.doTheSerRequestWithJSON(this,"http://ssl3-dev.dev.local/MEREVA/acces_reserve/TokenWrapper.asmx/Init",params);
    }

    @Override
    public void success(JSONObject  response) {
        try
        {
            String token = null;
            token = response.get("d").toString();
            EditText editText = (EditText) findViewById(R.id.editText);
            editText.setText(token );
        }
        catch( JSONException jse1 ) {
            try {
                JSONArray jsa = response.getJSONArray("tab");
                for (int index = 0 ; index < jsa.length() ; index ++ ) {
                    JSONObject jso1 = jsa.getJSONObject(index );
                }
            }
            catch( JSONException jse2 ) {
                error(jse2.getMessage());
                return;
            }
        }
        Button myButt = (Button)findViewById(R.id.button);
        myButt.setEnabled(true);
    }

    @Override
    public void error(String error) {
        Button myButt = (Button)findViewById(R.id.button);
        myButt.setEnabled(true);
        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
        toast.show();
    }
}
