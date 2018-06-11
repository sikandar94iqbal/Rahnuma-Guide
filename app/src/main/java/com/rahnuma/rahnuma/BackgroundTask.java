package com.rahnuma.rahnuma;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.Context.MODE_PRIVATE;


public class BackgroundTask extends AsyncTask<String,String,String> {
AlertDialog alertDialog;
    Context ctx;
    ProgressDialog pd;
    public String first_name_data;
    public String last_name_data;
    String guide_id_data;

    BackgroundTask(Context ctx)
    {

        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        pd=new ProgressDialog(ctx);
        pd.setTitle("Please wait");
        pd.setMessage("");
        pd.show();
    }

    protected String doInBackground(String... params) {
        String reg_url="http://ennovayt.com/Rahnuma/guides_reg.php";
        String login_url="http://ennovayt.com/Rahnuma/guides_login.php";
        String method=params[0];
        if(method.equals("register")) {
            String first_name = params[1];
            String last_name = params[2];
            String user_name = params[3];
            String password = params[4];
            String mobile = params[5];
            String email = params[6];

            String main_city_id = params[7];
            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("first_name", "UTF-8")+"="+URLEncoder.encode(first_name, "UTF-8")+"&"+URLEncoder.encode("last_name", "UTF-8")+"="+URLEncoder.encode(last_name, "UTF-8")+"&"+
                        URLEncoder.encode("user_name", "UTF-8")+"="+URLEncoder.encode(user_name, "UTF-8")+"&"+URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")+"&"+
                URLEncoder.encode("mobile", "UTF-8")+"="+URLEncoder.encode(mobile, "UTF-8")+"&"+ URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"+URLEncoder.encode("main_city_id", "UTF-8")+"="+URLEncoder.encode(main_city_id, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();

                return "Registration";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else if(method.equals("login"))
        {
            String login_name = params[1];
            String login_pass = params[2];


            try {
                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("login_name", "UTF-8")+"="+URLEncoder.encode(login_name, "UTF-8")+"&"+URLEncoder.encode("login_pass", "UTF-8")+"="+URLEncoder.encode(login_pass, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(IS,"iso-8859-1"));
                String response="";
                String line="";
                while((line=bufferedReader.readLine())!=null)
                {
                    response+=line;
                }
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                return response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }



    @Override
    protected void onPostExecute(String result) {

if(result.contains("shit")) {
    Toast.makeText(ctx, "WRONG Cradentials", Toast.LENGTH_LONG).show();
    pd.dismiss();
    ;
}
else if(result.contains("Registration")){
    pd.dismiss();
    Toast.makeText(ctx, "Registered Successfully", Toast.LENGTH_LONG).show();
}
else {
//    SharedPreferences mPreferences;
//
//    mPreferences = ctx.getSharedPreferences("User", MODE_PRIVATE);
//    SharedPreferences.Editor editor = mPreferences.edit();
//    editor.putString("username", result);
//    editor.commit();
    try
    {
        JSONArray ja=new JSONArray(result);
        JSONObject jo=null;

        for (int i=0;i<ja.length();i++)
        {
            jo=ja.getJSONObject(i);
            first_name_data=jo.getString("first_name");
            last_name_data= jo.getString("last_name");
            guide_id_data= jo.getString("id_guide");


        }

        pd.dismiss();
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.putExtra("EXTRA_SESSION_ID", guide_id_data);
        intent.putExtra("guide_first_name", first_name_data);
        intent.putExtra("guide_last_name", last_name_data);
        ctx.startActivity(intent);

       // return 1;
    } catch (JSONException e) {
        e.printStackTrace();
    }







}
//    ctx.startActivity(new Intent(ctx,MainActivity.class));


//            alertDialog.setMessage(result);
//            alertDialog.show();


    }
}
