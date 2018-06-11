package com.rahnuma.rahnuma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class guides_login extends Activity {

EditText ET_NAME,ET_PASS;
    String login_name,login_pass;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides_login);

        ET_NAME=(EditText) findViewById(R.id.G_Login_UN_ET);
        ET_PASS=(EditText) findViewById(R.id.G_login_pass_ET);
        TextView text = (TextView) findViewById(R.id.fuck);


        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(guides_login.this,guides_main.class));
            }
        });
    }
    public void user_login(View view)
    {

    }
    public void login_to_signup(View view) {
        Intent intent = new Intent(guides_login.this, guides_main.class);
        startActivity(intent);
    }

    public void checkLogin(View arg0) {

        final String name = ET_NAME.getText().toString();
//        if (!isValidName(name)) {
//            ET_NAME.setError("Invalid name");
//        }
//
//        final String pass = ET_PASS.getText().toString();
//        if (!isValidPassword(pass)) {
//            ET_PASS.setError("Password cannot be empty");
//        }
//
//        if(isValidName(name) && isValidPassword(pass))
//        {
            // Validation Completed

        login_name=ET_NAME.getText().toString();
        login_pass=ET_PASS.getText().toString();
        String method="login";
        BackgroundTask backgroundTask=new BackgroundTask(this);



if(login_name.isEmpty() || login_pass.isEmpty()){
    Toast.makeText(guides_login.this,"Fields missing..",Toast.LENGTH_LONG).show();
}
else {
    if (isNetworkAvailable()) {
        backgroundTask.execute(method, login_name, login_pass);
    } else {
        Toast.makeText(guides_login.this, "No network", Toast.LENGTH_LONG).show();
    }
}

//
//            Intent intent = new Intent(guides_login.this, MainActivity.class);
//            startActivity(intent);
        //}

    }
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 3) {
            return true;
        }
        return false;
    }
    private boolean isValidName(String Name) {
        String Name_PATTERN = "/^[a-zA-Z]+$/";

        Pattern pattern = Pattern.compile(Name_PATTERN);
        Matcher matcher = pattern.matcher(Name);
        return matcher.matches();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
