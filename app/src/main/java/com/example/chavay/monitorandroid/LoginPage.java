package com.example.chavay.monitorandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class LoginPage  extends AppCompatActivity {
    EditText  name ,password;
    boolean isEnd=false;
    int MaxAttempt=3,currentAttempt=0;
    String[] savedVords={" ","select","drop","delete","update","alter","create","exc"};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        name=(EditText) findViewById(R.id.input_email);
        password=(EditText) findViewById(R.id.input_password);
    }

    public void LoginBtn(View v) throws InterruptedException {
        boolean flag=false;
        if(currentAttempt<MaxAttempt) {
            for (int i = 0; i < savedVords.length; i++)
                if (name.getText().toString().toLowerCase().contains(savedVords[i].toLowerCase()) ||
                        password.getText().toString().toLowerCase().contains(savedVords[i].toLowerCase()))
                    flag = true;
            currentAttempt++;
            if (!flag)
                callApi(name.getText().toString(), password.getText().toString());
            else
                Alert("A username and password can not contain a space or a saved word\n");
        }
        else {
            isEnd=true;
            Alert("Too many wrong attempts,\n The system is finished!\n");
        }
    }

    private void Alert(String msg ){
            new AlertDialog.Builder(this)
                .setTitle("Error Login")
                .setMessage(msg)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Some stuff to do when ok got clicked
                        finish();System.exit(0);
                    }
                })
                .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Some stuff to do when cancel got clicked
                        if(isEnd){
                            finish();System.exit(0);
                        }
                    }
                })
                .show();
    }

    private void callApi(String user,String pass) {
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("on callApi func----","on callApi_Login!");
        String URL= "http://10.70.50.96/WebMonitorAndroid/WebAndroidMonitor.asmx/Login?eUser="+user+"&ePassword="+pass;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("request sucsses!!",response);
                        String [] temp=response.split(">");
                        temp=temp[2].split("<");
                        String data=temp[0];
                        if(data.equals("true")){
                            Intent intent=new Intent(LoginPage.this,Menu.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                            Alert("User Name Or Password Error!\n Please enter again!\n Do you want to Exit?\n\n");
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
                Alert("Error Call API\n "+error.toString()+"\n");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
