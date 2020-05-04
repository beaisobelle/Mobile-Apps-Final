package com.example.afinal;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;


public class StatsFragment extends Fragment {

    public static final String TAG = "StatsFragment";


    private RequestQueue queue;
    private View view;
    private String country, url;
    private TextView txtName, txtDate, txtTotalCases, txtTested, txtActiveCases, txtCriticalCases, txtRecovered, txtRecoveryRatio, txtDeaths, txtDeathRatio;
    private JSONObject data, summary, spots, main;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stats, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtName = (TextView) view.findViewById(R.id.name);
        txtDate = (TextView) view.findViewById(R.id.date);
        txtTotalCases = (TextView) view.findViewById(R.id.total_cases);
        txtTested = (TextView) view.findViewById(R.id.tested);
        txtActiveCases = (TextView) view.findViewById(R.id.active_cases);
        txtCriticalCases = (TextView) view.findViewById(R.id.critical);
        txtRecovered = (TextView) view.findViewById(R.id.recovered);
        txtRecoveryRatio = (TextView) view.findViewById(R.id.recovery_ratio);
        txtDeaths = (TextView) view.findViewById(R.id.deaths);
        txtDeathRatio = (TextView) view.findViewById(R.id.death_ratio);
    }

    protected void displayReceivedData(String message)
    {

        country = message;
        Log.d(TAG, "Message recieved: " + country);

        if(country.toLowerCase().equals("nonvalid")) {
            country = "nonvalid";
        }

        url = "https://api.quarantine.country/api/v1/summary/region?region=" + country;

        queue = MySingleton.getInstance(this.getContext()).getRequestQueue();
        queue.start();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            data = response.getJSONObject("data");
                            spots = data.getJSONObject("spots");
                            Iterator<String> keys = spots.keys();
                            String key = keys.next();
                            txtDate.setText(key);
                            main = spots.getJSONObject(key);
                            txtName.setText(main.get("name") + " Stats");
                            //COVID-19 stats
                            summary = data.getJSONObject("summary");
                            txtTotalCases.setText("Total Number of Cases: " + summary.get("total_cases").toString());
                            txtTested.setText("Number of People Tested: " + summary.get("tested").toString());
                            txtActiveCases.setText("Number of Active Cases: " + summary.get("active_cases").toString());
                            txtCriticalCases.setText("Number of Critical Conditions: " + summary.get("critical").toString());
                            txtRecovered.setText("Recovered Patients: " + summary.get("recovered").toString());
                            txtRecoveryRatio.setText("Recovery Ratio: " + summary.get("recovery_ratio").toString().substring(0,4));
                            txtDeaths.setText("Total Deaths: " + summary.get("deaths").toString());
                            txtDeathRatio.setText("Death Ratio: " + summary.get("death_ratio").toString().substring(0,4));


                        } catch (JSONException e) {
                            Log.d(TAG, "JSONException error: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "VolleyError error: " + error);
                    }
                });
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }


}
