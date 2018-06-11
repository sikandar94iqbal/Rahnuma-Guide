package com.rahnuma.rahnuma;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class guides_main extends Activity {

   // EditText ET_NAME, ET_USER_NAME, ET_PASS;


    EditText first_name;
    EditText last_name;
    EditText user_name;
    EditText password;
    EditText mobile;
    EditText email;
    EditText main_city;

    String first_name_data;
    String last_name_data;
    String user_name_data;
    String password_data;
    String mobile_data;
    String email_data;
    String main_city_data;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides_main);

        first_name = (EditText)findViewById(R.id.G_first_name_ET);
        last_name = (EditText) findViewById(R.id.G_last_name_ET);
        user_name = (EditText)findViewById(R.id.G_user_name_ET);
        password = (EditText) findViewById(R.id.G_pass_ET);
        mobile = (EditText) findViewById(R.id.G_contact_ET);
        email = (EditText) findViewById(R.id.G_email_ET);
        main_city = (EditText) findViewById(R.id.G_maincity_ET);





    }

    public void user_reg(View view) {
//        name = ET_NAME.getText().toString();
//        user_name = ET_USER_NAME.getText().toString();
//        user_pass = ET_PASS.getText().toString();

    first_name_data = first_name.getText().toString();
       last_name_data = last_name.getText().toString();
        user_name_data = user_name.getText().toString();
        password_data = password.getText().toString();
        mobile_data = mobile.getText().toString();
        email_data = email.getText().toString();
        main_city_data = main_city.getText().toString();

        String method = "register";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, first_name_data, last_name_data, user_name_data,password_data,mobile_data,email_data,main_city_data);
    }

    public void user_login(View view) {
        Intent intent = new Intent(guides_main.this, guides_login.class);
        startActivity(intent);
    }


}
