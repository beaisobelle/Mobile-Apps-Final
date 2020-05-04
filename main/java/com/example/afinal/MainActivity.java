package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText name, password;
    private TextView info;
    private Button loginbtn;
    private int count = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        name = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        info = (TextView)findViewById(R.id.info);
        loginbtn = (Button)findViewById(R.id.button);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                String passwd = password.getText().toString();
                validate(username, passwd);
            }
        });
    }

    private void validate(String userName, String userPassoword) {
        if((userName.equals("admin")) && (userPassoword.equals("admin"))) {
            Intent intent  = new Intent(this, MainApp.class);
            startActivity(intent);
        }
        else if ((userName.equals("Bea")) && (userPassoword.equals("bmontilla2015"))) {
            Intent intent  = new Intent(this, MainApp.class);
            startActivity(intent);
        }
        else {
            count--;
            info.setText("Wrong credentials, please try again\nNumber of Attempts Remaining: " + String.valueOf(count));
            if(count == 0) {
                loginbtn.setEnabled(false);
            }
        }
    }
}
