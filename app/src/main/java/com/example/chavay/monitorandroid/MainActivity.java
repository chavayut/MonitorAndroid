package com.example.chavay.monitorandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public static int PIVOT;
    public static boolean tableViewer;
    public static String TYPEMONITORS []={"Agent","Campaign","Queue","Ivr"};
    public static String typeViewer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(MainActivity.this,LoginPage.class);
        startActivity(intent);
        finish();
    }
}
