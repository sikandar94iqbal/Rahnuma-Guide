package com.rahnuma.rahnuma;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Main2Activity extends Activity {
String Address = "http://ennovayt.com/Rahnuma/personal_info.php";
    String Addre_rating = "http://ennovayt.com/Rahnuma/get_rating.php";
String ID;
    @Override
    public void onBackPressed()
    {
        this.finish();
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ID = getIntent().getStringExtra("EXTRA_SESSION_ID");

        TextView firstname = (TextView) findViewById(R.id.namee);
        TextView username = (TextView) findViewById(R.id.user_name);
        TextView email = (TextView) findViewById(R.id.email);
        TextView mobile = (TextView) findViewById(R.id.mobile);
        TextView city = (TextView) findViewById(R.id.city);
        ImageView image = (ImageView) findViewById(R.id.img);

        final RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.simpleRatingBar);


        getActionBar().setTitle("Profile");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Guide profile");
//
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//        // TODO: Remove the redundant calls to getSupportActionBar()
//        //       and use variable actionBar instead
  getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

Downloader3 d = new Downloader3(this,ID,Address,firstname,username,email,mobile,city,image);
        d.execute();


        Downloader3_rating d1 = new Downloader3_rating(this,ID,Addre_rating,simpleRatingBar);
        d1.execute();



    }
}




class Downloader3 extends AsyncTask<Void,Integer,String> {

    Context c;
    String address;
    ListView lv;
    String CITY = null;
    ///ArrayList<String> check_box_data = new ArrayList<String>();
    ProgressDialog pd;

    TextView firstname;
    TextView username;
    TextView email;
    TextView city;
    TextView mobile;
    ImageView image;
    String ID;
    // publlistAdapter boxAdapter;

    public Downloader3(Context c,String ID, String address, TextView firstname,TextView username,TextView email,TextView mobile,TextView city,ImageView image) {
        this.c = c;
        this.address = address;
        this.lv = lv;
this.firstname= firstname;
        this.username=username;
        this.city = city;
        this.mobile=mobile;
        this.email=email;
        this.image = image;
        this.ID = ID;
        // this.check_box_data = check_box_data;

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Fetch Data");
        pd.setMessage("Fething data.....please wait");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        String data= null;
        try {
            data = DownloadData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

       pd.dismiss();
        if (s!=null)
        {
            Parser3 p=new Parser3(c,s,firstname,username,email,mobile,city,image);
            p.execute();
        }else
        {
            Toast.makeText(c,"Unable to download data",Toast.LENGTH_SHORT).show();

        }
    }

    private String DownloadData() throws IOException {
        InputStream is = null;
        String line = null;
        try {
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //add city post method
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream OS = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            int idd = Integer.parseInt(ID);
            String data = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(ID, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();


            //getting list of sub citites
            is = new BufferedInputStream(con.getInputStream());

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();

            if (br != null) {
                while ((line = br.readLine()) != null) {
                    sb.append((line+"\n"));
                }

            }
            else {
                return null;
            }
            return sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is!=null)
            {
                is.close();
            }
        }
        return null;
    }
}



class Parser3 extends AsyncTask <Void,Integer,Integer>{

    ///public listAdapter boxAdapter;
    TextView firstname;
    TextView username;
    TextView email;
    TextView city;
    TextView mobile;
    ImageView image;

    Context c;
    String data;
    ListView lv;
    ProgressDialog pd;
    //    ArrayList<String> check_box_data = new ArrayList<String>();
    ArrayList<String> players=new ArrayList<>();

    ArrayList<weather> data_list = new ArrayList<weather>();
    ArrayList<new_personal> data_list2 = new ArrayList<new_personal>();

    String firstname_data;
    String lastname_data ;
    String username_data ;
    String email_data ;
    String mobile_data;
    int city_data ;
    String pic_data;

    //  public listAdapter get_adapter(){
//        return this.boxAdapter;
    //  }

    public Parser3(Context c, String data, TextView firstname,TextView username,TextView email,TextView mobile,TextView city,ImageView image) {
        this.c = c;
        this.data = data;
        this.lv = lv;
        this.firstname= firstname;
        this.username=username;
        this.city = city;
        this.mobile=mobile;
        this.email=email;
        this.image = image;
        // this.check_box_data = check_box_data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        pd=new ProgressDialog(c);
//        pd.setTitle("Parsing Data");
//        pd.setMessage("Parsing.....please wait");
//        pd.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parser();


    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if(integer==1)
        {
//            ArrayAdapter<String> adapter=new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,players);
//            lv.setAdapter(adapter);

//            weatherAdapter2 Wadapter = new weatherAdapter2(c, R.layout.row2,data_list);
//            lv.setAdapter(Wadapter);

//
//           boxAdapter = new listAdapter(c, data_list);
//
//            lv.setAdapter(boxAdapter);


//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(c,"HELLOOO",Toast.LENGTH_LONG);
//
//                }
//            });


            firstname.setText(firstname_data+ " "+lastname_data);
            username.setText(username_data);
            email.setText(email_data);
            mobile.setText(mobile_data);
            String city_name="";
            if(city_data==1){
                city_name="islamabad";
            }
            else if(city_data==2){
                city_name="lahore";
            }
            else if(city_data==3){
                city_name="karachi";
            }
            city.setText(city_name);


            new DownLoadImageTask3(image).execute(pic_data);

//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position,
//                                        long id) {
//
//                    final long[] checkedIds = lv.getCheckItemIds();
//                    for (int i=0;i<checkedIds.length;i++)
//                        Toast.makeText(c,checkedIds.toString(),Toast.LENGTH_LONG);
//
//                }
//            });





//
//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//
//                }
//            });
        }
        else
        {
            Toast.makeText(c,"Unable to parse data",Toast.LENGTH_SHORT).show();


        }
      //  pd.dismiss();
    }
    private int parser()
    {
        try
        {
            JSONArray ja=new JSONArray(data);
            JSONObject jo=null;
            players.clear();
            for (int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                firstname_data=jo.getString("first_name");
                lastname_data = jo.getString("last_name");
                username_data = jo.getString("user_name");
                email_data = jo.getString("email");
                 mobile_data = jo.getString("mobile");
                city_data = jo.getInt("main_city_id");
                pic_data = jo.getString("pic");

                pic_data = "http://ennovayt.com "+pic_data;




                // players.add(name);
                //data_list.add(new weather(name,i,pic,false));
            }
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}



class DownLoadImageTask3 extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;

    public DownLoadImageTask3(ImageView imageView){
        this.imageView = imageView;
    }

    /*
        doInBackground(Params... params)
            Override this method to perform a computation on a background thread.
     */
    protected Bitmap doInBackground(String...urls){
        String urlOfImage = urls[0];
        Bitmap logo = null;
        try{
            InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
            logo = BitmapFactory.decodeStream(is);
        }catch(Exception e){ // Catch the download exception
            e.printStackTrace();
        }
        return logo;
    }

    /*
        onPostExecute(Result result)
            Runs on the UI thread after doInBackground(Params...).
     */
    protected void onPostExecute(Bitmap result){
        imageView.setImageBitmap(result);
    }
}







//for rating






class Downloader3_rating extends AsyncTask<Void,Integer,String> {

    Context c;
    String address;
    ListView lv;
    String CITY = null;
    ///ArrayList<String> check_box_data = new ArrayList<String>();
    ProgressDialog pd;

    TextView firstname;
    TextView username;
    TextView email;
    TextView city;
    TextView mobile;
    ImageView image;
    String ID;

    RatingBar r;
    // publlistAdapter boxAdapter;

    public Downloader3_rating(Context c,String ID, String address, RatingBar r) {
        this.c = c;
        this.address = address;
        this.lv = lv;
        this.firstname= firstname;
        this.username=username;
        this.city = city;
        this.mobile=mobile;
        this.email=email;
        this.image = image;
        this.ID = ID;
        this.r=r;
        // this.check_box_data = check_box_data;

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        pd=new ProgressDialog(c);
//        pd.setTitle("Fetch Data");
//        pd.setMessage("Fething data.....please wait");
//        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        String data= null;
        try {
            data = DownloadData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

//        pd.dismiss();
        if (s!=null)
        {
            Parser3_rating p=new Parser3_rating(c,s,ID,address,r);
            p.execute();
        }else
        {
            Toast.makeText(c,"Unable to download data",Toast.LENGTH_SHORT).show();

        }
    }

    private String DownloadData() throws IOException {
        InputStream is = null;
        String line = null;
        try {
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //add city post method
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream OS = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            int idd = Integer.parseInt(ID);
            String data = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(ID, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();


            //getting list of sub citites
            is = new BufferedInputStream(con.getInputStream());

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();

            if (br != null) {
                while ((line = br.readLine()) != null) {
                    sb.append((line+"\n"));
                }

            }
            else {
                return null;
            }
            return sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is!=null)
            {
                is.close();
            }
        }
        return null;
    }
}



class Parser3_rating extends AsyncTask <Void,Integer,Integer>{

    ///public listAdapter boxAdapter;
    TextView firstname;
    TextView username;
    TextView email;
    TextView city;
    TextView mobile;
    ImageView image;

    Context c;
    String data;
    ListView lv;
    ProgressDialog pd;
    //    ArrayList<String> check_box_data = new ArrayList<String>();
    ArrayList<String> players=new ArrayList<>();

    ArrayList<weather> data_list = new ArrayList<weather>();
    ArrayList<new_personal> data_list2 = new ArrayList<new_personal>();

    String firstname_data;
    String lastname_data ;
    String username_data ;
    String email_data ;
    String mobile_data;
    int city_data ;
    String pic_data;
RatingBar r;
    String ID;
    String address;

    String Rating_data;
    //  public listAdapter get_adapter(){
//        return this.boxAdapter;
    //  }

    public Parser3_rating(Context c, String data, String ID, String address, RatingBar r) {
        this.c = c;
        this.data = data;
        this.lv = lv;
        this.firstname= firstname;
        this.username=username;
        this.city = city;
        this.mobile=mobile;
        this.email=email;
        this.image = image;
        this.ID=ID;
        this.address= address;
        this.r=r;
        // this.check_box_data = check_box_data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        pd=new ProgressDialog(c);
//        pd.setTitle("Parsing Data");
//        pd.setMessage("Parsing.....please wait");
//        pd.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parser();


    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if(integer==1)
        {
//            ArrayAdapter<String> adapter=new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,players);
//            lv.setAdapter(adapter);

//            weatherAdapter2 Wadapter = new weatherAdapter2(c, R.layout.row2,data_list);
//            lv.setAdapter(Wadapter);

//
//           boxAdapter = new listAdapter(c, data_list);
//
//            lv.setAdapter(boxAdapter);


//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(c,"HELLOOO",Toast.LENGTH_LONG);
//
//                }
//            });


//            firstname.setText(firstname_data+ " "+lastname_data);
//            username.setText(username_data);
//            email.setText(email_data);
//            mobile.setText(mobile_data);
//            String city_name="";
//            if(city_data==1){
//                city_name="islamabad";
//            }
//            else if(city_data==2){
//                city_name="lahore";
//            }
//            else if(city_data==3){
//                city_name="karachi";
//            }
//            city.setText(city_name);

            float f = Float.parseFloat(Rating_data);
            if(f==0){
                r.setRating(0);
            }
            else
            r.setRating(f);



           // new DownLoadImageTask3(image).execute(pic_data);

//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position,
//                                        long id) {
//
//                    final long[] checkedIds = lv.getCheckItemIds();
//                    for (int i=0;i<checkedIds.length;i++)
//                        Toast.makeText(c,checkedIds.toString(),Toast.LENGTH_LONG);
//
//                }
//            });





//
//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//
//                }
//            });
        }
        else
        {
            Toast.makeText(c,"Unable to parse data",Toast.LENGTH_SHORT).show();


        }
        //  pd.dismiss();
    }
    private int parser()
    {
        try
        {
            JSONArray ja=new JSONArray(data);
            JSONObject jo=null;
            players.clear();
            for (int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                Rating_data = jo.getString("Rating");
//                firstname_data=jo.getString("first_name");
//                lastname_data = jo.getString("last_name");
//                username_data = jo.getString("user_name");
//                email_data = jo.getString("email");
//                mobile_data = jo.getString("mobile");
//                city_data = jo.getInt("main_city_id");
//                pic_data = jo.getString("pic");




                // players.add(name);
                //data_list.add(new weather(name,i,pic,false));
            }
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

