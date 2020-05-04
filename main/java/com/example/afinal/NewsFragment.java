package com.example.afinal;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class NewsFragment extends Fragment {

    public static final String TAG = "NewsFragment";

    private RequestQueue queue;
    //TextView update;
    private ListView update;
    public ArrayList<String> list;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String url = "https://covid19-api.org/api/timeline";

        update = (ListView) view.findViewById(R.id.list);

        queue = MySingleton.getInstance(this.getContext()).getRequestQueue();
        queue.start();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        list = new ArrayList<String>();

                        for(int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                String str = "Total Cases: " + obj.getString("total_cases")
                                            + "\n\t\t\t\t\tTotal Deaths: " + obj.getString("total_deaths")
                                            + "\n\t\t\t\t\t\t\t\t\t\tTotal Recovered: " + obj.getString("total_recovered")
                                            + "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t( " + obj.getString("last_update").substring(0,10) + " " + obj.getString("last_update").substring(11,19) + " )";
                                list.add(str);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
                        update.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        list.add("error: " + error);
                        adapter.notifyDataSetChanged();
                    }
                });
        MySingleton.getInstance(this.getContext()).addToRequestQueue(jsonArrayRequest);
    }

}
