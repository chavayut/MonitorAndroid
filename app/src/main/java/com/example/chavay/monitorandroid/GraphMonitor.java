package com.example.chavay.monitorandroid;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;


public class GraphMonitor extends AppCompatActivity {

    TextView typeMonitor;
    BarChart barChart;
    ArrayList<BarEntry> entries;
    ArrayList<String> labels;
    ArrayList<BarEntry> group1;
    ArrayList<BarEntry> group2;
    BarDataSet barDataSet1;
    BarDataSet barDataSet2;
    ArrayList<BarDataSet> dataset;
    int [] colors = {Color.RED,Color.BLUE,Color.YELLOW,Color.GREEN,Color.BLACK,Color.GRAY};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor_graph);
        typeMonitor=(TextView) findViewById(R.id.textView);
        barChart= (BarChart) findViewById(R.id.barChart);


        MainActivity.tableViewer=false;
        MainActivity.typeViewer="Graph";


        callApi_Graph();
    }


    public void GAgent(View view) {MainActivity.PIVOT = 0;callApi_Graph();}

    public void GCampaign(View view) {MainActivity.PIVOT = 1;callApi_Graph();}

    public void GQueue(View view) {MainActivity.PIVOT = 2;callApi_Graph();}

    public void GIVR(View view) {MainActivity.PIVOT = 3;callApi_Graph();}

    public void GtableViewer(View view) {
        MainActivity.tableViewer = true;
        MainActivity.typeViewer = "Table";
        Intent intent = new Intent(GraphMonitor.this, TableMonitor.class);
        startActivity(intent);
        finish();
    }

    public void GtoExit(View view) {
        Alert("End Application",
                "Thank you for using the composit monitor!\n Are you sure you want to exit from the monitor?\n");
    }

    private void Alert(String Title,String msg ){
        new AlertDialog.Builder(this)
                .setTitle(Title)
                .setMessage(msg)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Some stuff to do when ok got clicked
                        finish();
                        System.exit(0);

                    }
                })
                .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Some stuff to do when cancel got clicked
                    }
                })
                .show();
    }
    private void callApi_Graph(){
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("on callApi func----","on callApi_Table!");
        String URL= "http://10.70.50.96/WebMonitorAndroid/WebAndroidMonitor.asmx/GetMonitorData?MonitorType="+MainActivity.PIVOT+"&IsTable=False";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("request sucsses!!",response);
                        String [] temp=response.split(">");
                        temp=temp[2].split("<");
                        drawGraph(temp[0]);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
                Alert("Error API",error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void drawGraph(String content) {
        typeMonitor.setText(MainActivity.TYPEMONITORS[MainActivity.PIVOT]+" "+MainActivity.typeViewer);
        String[] headerResp = content.split("\\|");
        String[][] contentResp = new String[headerResp.length][];
        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<BarEntry>[] groups;
        contentResp[0] = headerResp[0].split("~");
        groups = new ArrayList[contentResp[0].length-1];

        for (int i = 1; i <headerResp.length ; i++){
            contentResp[i] = headerResp[i].split("~");
            labels.add(contentResp[i][contentResp[0].length - 1]);
        }
        for (int i = 0; i <contentResp[0].length-1 ; i++) {
            groups[i]=new ArrayList<>();
            for (int j = 0; j <contentResp.length-1 ; j++)
                groups[i].add(new BarEntry(Float.parseFloat(contentResp[j+1][i]),j));
        }
        BarDataSet [] barDataSet= new BarDataSet [contentResp[0].length-1];
        ArrayList<BarDataSet> dataset = new ArrayList<>();
        for (int i = 0; i <barDataSet.length ; i++) {
            barDataSet[i]= new BarDataSet(groups[i], contentResp[0][i]);
            barDataSet[i].setColor(colors[i%colors.length]);
            dataset.add(barDataSet[i]);
        }
        BarData data = new BarData(labels, dataset);
        barChart.setData(data);
        barChart.animateY(5000);
    }
}
