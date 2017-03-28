package com.example.chavay.monitorandroid;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class TableMonitor extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     */
    TextView typeMonitor;
    int[] scrollableColumnWidths = new int[]{14, 14, 14, 14, 14, 14, 14};
    int fixedRowHeight = 98;
    LayoutParams wrapWrapTableRowParams;
    TableRow row;
    TableLayout header;
    TableLayout fixedColumn;
    TableLayout scrollablePart;
    private TextView recyclableTextView;
    int [] colors = {Color.parseColor("#CAE1FF"),Color.WHITE};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor_table);
        typeMonitor = (TextView) findViewById(R.id.textView);
        callApi_Table();
    }

    public void TAgent(View view) {MainActivity.PIVOT = 0;callApi_Table();}

    public void TCampaign(View view) {MainActivity.PIVOT = 1;callApi_Table();}

    public void TQueue(View view) {MainActivity.PIVOT = 2;callApi_Table();}

    public void TIVR(View view) {MainActivity.PIVOT = 3;callApi_Table();}

    public void TtableViewer(View view) {
        MainActivity.tableViewer = false;
        MainActivity.typeViewer = "Graph";
        Intent intent = new Intent(TableMonitor.this, GraphMonitor.class);
        startActivity(intent);
        finish();
    }

    public void TtoExit(View view) {
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
    private void callApi_Table(){
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("on callApi func----","on callApi_Table!");
        String URL= "http://10.70.50.96/WebMonitorAndroid/WebAndroidMonitor.asmx/GetMonitorData?MonitorType="+MainActivity.PIVOT+"&IsTable=True";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("request sucsses!!",response);

                        String [] temp=response.split(">");
                        temp=temp[2].split("<");

                        drawTable(temp[0]);
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


    public void drawTable(String tablecontent) {

        typeMonitor.setText(MainActivity.TYPEMONITORS[MainActivity.PIVOT] + " " + MainActivity.typeViewer);

        wrapWrapTableRowParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        row = new TableRow(this);
        //header (fixed vertically)
        header = (TableLayout) findViewById(R.id.table_header);
        header.removeAllViews();
        row.setLayoutParams(wrapWrapTableRowParams);
        row.setGravity(Gravity.CENTER);
        row.setBackgroundColor(Color.parseColor("#505B75"));

        String[] LineResp = tablecontent.split("\\|");
        String[] headerResp = LineResp[0].split("~");
        for (int i = 0; i < headerResp.length; i++)
            row.addView(makeTableRowWithText(headerResp[i], scrollableColumnWidths[i], fixedRowHeight,Color.WHITE));
        header.addView(row);
        //header (fixed horizontally)
        fixedColumn = (TableLayout) findViewById(R.id.fixed_column);
        fixedColumn.removeAllViews();
        //rest of the table (within a scroll view)
        scrollablePart = (TableLayout) findViewById(R.id.scrollable_part);
        scrollablePart.removeAllViews();

        for (int i = 1; i < LineResp.length; i++) {
            String[] newLine = LineResp[i].split("~");
            row = new TableRow(this);
            row.setLayoutParams(wrapWrapTableRowParams);
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(colors[i%2]);
            for (int j = 0; j < newLine.length; j++)
                row.addView(makeTableRowWithText(newLine[j], scrollableColumnWidths[j], fixedRowHeight,Color.parseColor("#505B75")));
            scrollablePart.addView(row);
        }
    }

    public TextView makeTableRowWithText(String text, int widthInPercentOfScreenWidth, int fixedHeightInPixels,int TextColor) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        recyclableTextView = new TextView(this);
        recyclableTextView.setText(text);
        recyclableTextView.setTextColor(TextColor);
        recyclableTextView.setTextSize(10);
        recyclableTextView.setWidth(widthInPercentOfScreenWidth * screenWidth / 100);
        recyclableTextView.setHeight(fixedHeightInPixels);
        recyclableTextView.setGravity(Gravity.CENTER);
        return recyclableTextView;
    }


}