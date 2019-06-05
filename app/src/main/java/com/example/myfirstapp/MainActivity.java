package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements IAccess {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private MySerializer serInstance;
    private Spinner spin;
    private String[] languageArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serInstance = MySerializer.getInstance(this.getApplicationContext());

        spin = (Spinner)findViewById(R.id.choixPays);
        languageArray = getResources().getStringArray(R.array.code_lang_arrays);
        String displayLanguage = Locale.getDefault().getLanguage();
        for( int index = 0 ; index < languageArray.length ; index ++ ) {
            if ( languageArray[index].equals(displayLanguage)) {
                spin.setSelection(index);
                break;
            }
        }
    }

    public void sendMessage(View view) {
        Button myButt = (Button)findViewById(R.id.button);
        myButt.setEnabled(false);
        httpAccess();
    }

    public void SqLiteAll(View view) {
        serInstance.showAll(this);
    }

    private void httpAccess() {
        EditText editText = (EditText) findViewById(R.id.editText);
        String user = editText.getText().toString();
        editText = (EditText) findViewById(R.id.pass);
        String pass = editText.getText().toString();
        final String[] message = new String[1];
        Map<String, String> params = new HashMap();
        params.put("user", user);
        params.put("pass", pass);
        params.put("lang", languageArray[ spin.getSelectedItemPosition() ] );
        serInstance.doHttpRequest(this,"http://ssl3-dev.dev.local/MEREVA/Android/InitApp.asmx/Init",params,false);
    }

    @Override
    public void success(JSONObject  response) {
        String myMsg;
        try
        {
            JSONObject myObj = response.getJSONObject("d");
            Boolean etat = myObj.getBoolean("Etat");
            myMsg = myObj.getString("Resultat");
            if (!etat ) {
                error(3,myObj.getString("Resultat"));
                return;
            }
        }
        catch( JSONException jse1 ) {
            error(2,jse1.getMessage());
            return;
        }
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, myMsg);
        startActivity(intent);
    }

    @Override
    public void error(Integer typeError,String error) {
        Button myButt = (Button)findViewById(R.id.button);
        myButt.setEnabled(true);
        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
        toast.show();
    }
}
