package com.example.afinal;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GraphFragment extends Fragment {

    public static final String TAG = "GraphFragment";

    private RequestQueue queue;
    private View view;
    private String url, urlHistory, urlPrediction;
    private PieChart pieChart;
    private LineChart historyLine, predictionLine;
    private BarChart barChart;
    private ArrayList<PieEntry> list;
    private List<Entry> historylist[], predictionlist[];
    private LineDataSet lineDataSet[], lineDataSetPrediction[];
    private ArrayList<ILineDataSet> dataSets, dataSetsPrediction;
    private LineData data, dataPrediction;
    private XAxis xAxis, xAxis1, xAxisBar;
    private String[] xLabels, xLabelsPrediction, xLabelsBar;
    private ArrayList <BarEntry> barlist;
    private BarDataSet barDataSet;
    private int barColor[];
    private String countries[];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_graph, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //volley request urls
        url = "https://covid19-api.org/api/status";
        urlHistory = "https://covid19-api.org/api/timeline/";
        urlPrediction = "https://covid19-api.org/api/prediction/";

        countries = new String[] {"US", "ES", "IT", "GB", "FR"};

        pieChart = view.findViewById(R.id.piechart);
        pieChart.setUsePercentValues(false);

        historyLine = view.findViewById(R.id.historychart);
        historylist = new List[5];
        lineDataSet = new LineDataSet[5];
        dataSets = new ArrayList<>();
        xLabels = new String[5];
        xAxis = historyLine.getXAxis();

        predictionLine = view.findViewById(R.id.predictionchart);
        xAxis1 = predictionLine.getXAxis();
        xLabelsPrediction = new String[5];
        predictionlist = new List[5];
        lineDataSetPrediction = new LineDataSet[5];
        dataSetsPrediction = new ArrayList<>();

        barChart = view.findViewById(R.id.barchart);
        xLabelsBar = new String[15];
        xAxisBar = barChart.getXAxis();
        barColor = new int[] {Color.rgb(140,165,191), Color.rgb(187,156,128), Color.rgb(211,205,200)};

        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        CreatePieBarChart();
        CreateHistoryChart();
        CreatePredictionChart();
    }

    public void CreatePieBarChart() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        list = new ArrayList<PieEntry>();
                        barlist = new ArrayList<BarEntry>();


                        for(int i = 0; i < 15; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                String name = obj.get("country").toString();

                                list.add(new PieEntry(obj.getInt("cases"), name));

                                xLabelsBar[i] = name;

                                float cases = (float) obj.getInt("cases");
                                float deaths = (float) obj.getInt("deaths");
                                float recovered = (float) obj.getInt("recovered");
                                cases = cases - deaths - recovered;

                                barlist.add(new BarEntry(i, new float[]{cases, recovered, deaths}));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        PieDataSet pieDataSet = new PieDataSet(list, "");
                        pieDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
                        PieData pieData = new PieData(pieDataSet);
                        pieChart.setData(pieData);
                        pieChart.getDescription().setText("Total Reported Cases of COVID-19");
                        pieChart.getDescription().setTextColor(Color.rgb(161, 161, 161));
                        pieChart.getLegend().setEnabled(false);
                        pieChart.setCenterText("COVID-19 Cases");
                        pieChart.setCenterTextSize(20f);

                        barDataSet = new BarDataSet(barlist, "");
                        barDataSet.setStackLabels(new String[] {"Unresolved Cases", "Recovered", "Deaths"});
                        barDataSet.setColors(barColor);
                        xAxisBar.setValueFormatter(new IndexAxisValueFormatter(xLabelsBar));
                        xAxisBar.setTextColor(Color.rgb(161, 161, 161));
                        BarData barData = new BarData(barDataSet);
                        barChart.setData(barData);
                        barChart.getAxisLeft().setEnabled(false);
                        barChart.getAxisRight().setTextColor(Color.rgb(161, 161, 161));
                        barChart.getDescription().setText("Recoverey to Death Comparison per Total Number of Cases");
                        barChart.getDescription().setTextColor(Color.rgb(161, 161, 161));
                        barChart.getLegend().setTextColor(Color.rgb(161, 161, 161));
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "VolleyError: " + error);
                    }
                });

        queue.add(jsonArrayRequest);
    }

    public void CreateHistoryChart() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, urlHistory+ countries[0], null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        historylist[0] = new ArrayList<>();

                        for(int i = 0; i < 5; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                xLabels[i] = obj.get("last_update").toString().substring(0,10);
                                historylist[0].add(new Entry(i, obj.getInt("cases")));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lineDataSet[0] = new LineDataSet(historylist[0], countries[0]);
                        lineDataSet[0].setColors(ColorTemplate.rgb("#f54260"));
                        dataSets.add(lineDataSet[0]);


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "VolleyError History Line Chart: " + error);
                    }
                });
        queue.add(jsonArrayRequest);

        jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, urlHistory+ countries[1], null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        historylist[1] = new ArrayList<>();

                        for(int i = 0; i < 5; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                historylist[1].add(new Entry(i, obj.getInt("cases")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lineDataSet[1] = new LineDataSet(historylist[1], countries[1]);
                        lineDataSet[1].setColors(ColorTemplate.rgb("#f56260"));
                        dataSets.add(lineDataSet[1]);



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "VolleyError History Line Chart: " + error);
                    }
                });
        queue.add(jsonArrayRequest);

        jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, urlHistory+ countries[2], null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        historylist[2] = new ArrayList<>();

                        for(int i = 0; i < 5; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                historylist[2].add(new Entry(i, obj.getInt("cases")));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lineDataSet[2] = new LineDataSet(historylist[2], countries[2]);
                        lineDataSet[2].setColors(ColorTemplate.rgb("#f56290"));
                        dataSets.add(lineDataSet[2]);



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "VolleyError History Line Chart: " + error);
                    }
                });
        queue.add(jsonArrayRequest);

        jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, urlHistory+ countries[3], null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        historylist[3] = new ArrayList<>();

                        for(int i = 0; i < 5; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                historylist[3].add(new Entry(i, obj.getInt("cases")));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lineDataSet[3] = new LineDataSet(historylist[3], countries[3]);
                        lineDataSet[3].setColors(ColorTemplate.rgb("#f76290"));
                        dataSets.add(lineDataSet[3]);



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "VolleyError History Line Chart: " + error);
                    }
                });
        queue.add(jsonArrayRequest);

        jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, urlHistory+ countries[4], null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        historylist[4] = new ArrayList<>();

                        for(int i = 0; i < 5; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                historylist[4].add(new Entry(i, obj.getInt("cases")));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lineDataSet[4] = new LineDataSet(historylist[4], countries[4]);
                        lineDataSet[4].setColors(ColorTemplate.rgb("#f76200"));
                        dataSets.add(lineDataSet[4]);


                        //make the chart herer
                        data = new LineData(dataSets);
                        data.setValueTextColor(Color.WHITE);
                        historyLine.setData(data);
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
                        xAxis.setTextColor(Color.rgb(161, 161, 161));
                        historyLine.getDescription().setText("Total Reported Cases From the Past 5 Days");
                        historyLine.getDescription().setTextColor(Color.rgb(161, 161, 161));
                        historyLine.getLegend().setTextColor(Color.rgb(161, 161, 161));
                        historyLine.getAxisLeft().setTextColor(Color.rgb(161, 161, 161));
                        historyLine.getAxisRight().setEnabled(false);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "VolleyError History Line Chart: " + error);
                    }
                });
        queue.add(jsonArrayRequest);
    }

    public void CreatePredictionChart() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, urlPrediction + countries[0], null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        predictionlist[0] = new ArrayList<>();

                        for(int i = 0; i < 5; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                xLabelsPrediction[i] = obj.get("date").toString();
                                predictionlist[0].add(new Entry(i, obj.getInt("cases")));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lineDataSetPrediction[0] = new LineDataSet(predictionlist[0], countries[0]);
                        lineDataSetPrediction[0].setColors(ColorTemplate.rgb("#f56290"));
                        dataSetsPrediction.add(lineDataSetPrediction[0]);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "VolleyError History Line Chart: " + error);
                    }
                });
        queue.add(jsonArrayRequest);

        jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, urlPrediction + countries[1], null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        predictionlist[1] = new ArrayList<>();

                        for(int i = 0; i < 5; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                predictionlist[1].add(new Entry(i, obj.getInt("cases")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lineDataSetPrediction[1] = new LineDataSet(predictionlist[1], countries[1]);
                        lineDataSetPrediction[1].setColors(ColorTemplate.rgb("#f56260"));
                        dataSetsPrediction.add(lineDataSetPrediction[1]);



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "VolleyError History Line Chart: " + error);
                    }
                });
        queue.add(jsonArrayRequest);

        jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, urlPrediction + countries[2], null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        predictionlist[2] = new ArrayList<>();

                        for(int i = 0; i < 5; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                predictionlist[2].add(new Entry(i, obj.getInt("cases")));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lineDataSetPrediction[2] = new LineDataSet(predictionlist[2], countries[2]);
                        lineDataSetPrediction[2].setColors(ColorTemplate.rgb("#f56290"));
                        dataSetsPrediction.add(lineDataSetPrediction[2]);



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "VolleyError History Line Chart: " + error);
                    }
                });
        queue.add(jsonArrayRequest);

        jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, urlPrediction + countries[3], null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        predictionlist[3] = new ArrayList<>();

                        for(int i = 0; i < 5; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                predictionlist[3].add(new Entry(i, obj.getInt("cases")));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lineDataSetPrediction[3] = new LineDataSet(predictionlist[3], countries[3]);
                        lineDataSetPrediction[3].setColors(ColorTemplate.rgb("#f76290"));
                        dataSetsPrediction.add(lineDataSetPrediction[3]);



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "VolleyError History Line Chart: " + error);
                    }
                });
        queue.add(jsonArrayRequest);

        jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, urlPrediction + countries[4], null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        predictionlist[4] = new ArrayList<>();

                        for(int i = 0; i < 5; i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                predictionlist[4].add(new Entry(i, obj.getInt("cases")));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lineDataSetPrediction[4] = new LineDataSet(predictionlist[4], countries[4]);
                        lineDataSetPrediction[4].setColors(ColorTemplate.rgb("#f76200"));
                        dataSetsPrediction.add(lineDataSetPrediction[4]);


                        //make the chart herer
                        dataPrediction = new LineData(dataSetsPrediction);
                        dataPrediction.setValueTextColor(Color.WHITE);
                        predictionLine.setData(dataPrediction);
                        xAxis1.setValueFormatter(new IndexAxisValueFormatter(xLabelsPrediction));
                        xAxis1.setTextColor(Color.rgb(161, 161, 161));
                        predictionLine.getDescription().setText("Predicted COVID-19 Cases for the Next 5 Days");
                        predictionLine.getDescription().setTextColor(Color.rgb(161, 161, 161));
                        predictionLine.getLegend().setTextColor(Color.rgb(161, 161, 161));
                        predictionLine.getAxisLeft().setTextColor(Color.rgb(161, 161, 161));
                        predictionLine.getAxisRight().setEnabled(false);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "VolleyError History Line Chart: " + error);
                    }
                });
        queue.add(jsonArrayRequest);
    }
}
