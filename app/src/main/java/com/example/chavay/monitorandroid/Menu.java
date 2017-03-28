package com.example.chavay.monitorandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Menu extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }

    public void SelectMonitor_0(View view){MainActivity.PIVOT=0;ViewMonitor();}
    public void SelectMonitor_1(View view){MainActivity.PIVOT=1;ViewMonitor();}
    public void SelectMonitor_2(View view){MainActivity.PIVOT=2;ViewMonitor();}
    public void SelectMonitor_3(View view){MainActivity.PIVOT=3;ViewMonitor();}

    public void ViewMonitor()
    {
        MainActivity.tableViewer=true;
        MainActivity.typeViewer="Table";
        Intent intent=new Intent(Menu.this,TableMonitor.class);
        startActivity(intent);
        finish();
    }

    public void ToExit(View view){

        new AlertDialog.Builder(this)
                    .setTitle("End Application")
                    .setMessage("Thank you for using the composit monitor!\n Are you sure you want to exit from the monitor?\n")
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

}
