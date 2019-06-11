package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DisplayMessageActivity extends AppCompatActivity implements IAccess {
    private MySerializer serInstance;
    private Spinner myApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(getResources().getString(R.string.choix_app));
        serInstance = MySerializer.getInstance(this.getApplicationContext());
        JSONObject myObj = (JSONObject)serInstance.userTransfert;
        Intent intent = getIntent();
        String lang = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        setContentView(R.layout.activity_display_message);
        TextView textView = findViewById(R.id.textView);
        textView.setText(myObj.toString());

        String[] myEltIde;
        String[] myEltLib;
        try {
            JSONArray myApps = myObj.getJSONArray("listApp");
            int longueur = myApps.length();
            myEltIde = new String[longueur+1];
            myEltLib = new String[longueur+1];
            myEltIde[0] = "";
            myEltLib[0] = getResources().getString(R.string.choix_app);
            for (int index = 0 ; index < longueur ; index ++ ) {
                JSONObject myElt = myApps.getJSONObject(index);
                myEltIde[ index + 1 ] = myElt.getString("id");
                myEltLib[ index + 1 ] = myElt.getString("libelle");
            }
        } catch (JSONException e) {
            error(3,e.getMessage());
            return;
        }

        myApps = (Spinner)findViewById(R.id.choixApp);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, myEltLib);
        myApps.setAdapter(adapter);

    }

    @Override
    public void success(JSONObject response) {
    }

    @Override
    public void error(Integer typeError, String error) {
        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
        toast.show();
    }
}
