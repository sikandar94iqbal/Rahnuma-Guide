package com.rahnuma.rahnuma;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class individual_tour extends AppCompatActivity {


    //data

    public String guide_first_name;
    public String guide_last_name;
    public String guide_id;
    public String city_pic;
    public String city_name;

    public Context context;

    public String Address = "http://ennovayt.com/Rahnuma/get_individual_tour.php";


    public ImageView img;
    public TextView tourist_name;
    public TextView guide_name;
    public TextView visited_city;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_tour);

        //getting data from previous intent
        guide_first_name=getIntent().getStringExtra("guide_first_name");
        guide_last_name=getIntent().getStringExtra("guide_last_name");
        guide_id=getIntent().getStringExtra("guide_id");
        city_name =getIntent().getStringExtra("city_name");
        city_pic=getIntent().getStringExtra("city_pic");
        context=this;

        toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);

        img = (ImageView) findViewById(R.id.img);
        tourist_name = (TextView) findViewById(R.id.tourist_name);
        guide_name = (TextView) findViewById(R.id.guide_name);
        visited_city = (TextView) findViewById(R.id.city);

        //data to send
        String guide_full_name = guide_first_name+" "+guide_last_name;
        setSupportActionBar(toolbar);
        toolbar.setTitle("Tour Detail");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tour Detail");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.md_white_1000), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Downloader3_ind india = new Downloader3_ind(guide_id,context,Address,img,tourist_name,guide_name,visited_city,guide_full_name,city_name,city_pic);

india.execute();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}




//tasks





class Downloader3_ind extends AsyncTask<Void,Integer,String> {

    Context c;
    String address;
    ListView lv;
    String CITY = null;
    ///ArrayList<String> check_box_data = new ArrayList<String>();
    ProgressDialog pd;



    public ImageView img;
    public TextView tourist_name;
    public TextView guide_name;
    public TextView visited_city;



String city_pic;
    String guide_full_name;
    String city_name;
    String gid;

    // publlistAdapter boxAdapter;

    public Downloader3_ind(String gid,Context c, String address, ImageView img,TextView tourist_name,TextView guide_name,TextView visited_city,String guide_full_name,String city_name,String city_pic) {
        this.c = c;
        this.address = address;
        this.lv = lv;
this.gid=gid;
        this.guide_name=guide_name;
        this.tourist_name=tourist_name;
        this.img=img;
        this.visited_city = visited_city;

      this.guide_full_name=guide_full_name;
        this.city_name=city_name;
        this.city_pic=city_pic;




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
            Parser3_ind p=new Parser3_ind(c,s,img,tourist_name,guide_name,visited_city,guide_full_name,city_name,city_pic);
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
//            int idd = Integer.parseInt(ID);
            String data = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(gid, "UTF-8");
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



class Parser3_ind extends AsyncTask <Void,Integer,Integer>{

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


    public ImageView img;
    public TextView tourist_name;
    public TextView guide_name;
    public TextView visited_city;



    String city_pic;
    String guide_full_name;
    String city_name;



    String tourist_full_name;
    String tourist_email;

    //  public listAdapter get_adapter(){
//        return this.boxAdapter;
    //  }

    public Parser3_ind(Context c, String data, ImageView img,TextView tourist_name,TextView guide_name,TextView visited_city,String guide_full_name,String city_name,String city_pic) {
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

        this.guide_name=guide_name;
        this.tourist_name=tourist_name;
        this.img=img;
        this.visited_city = visited_city;

        this.guide_full_name=guide_full_name;
        this.city_name=city_name;
        this.city_pic=city_pic;

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

//
//            firstname.setText(firstname_data+ " "+lastname_data);
//            username.setText(username_data);
//            email.setText(email_data);
//            mobile.setText(mobile_data);



            tourist_name.setText("Tourist Name  :  "+tourist_full_name);
            guide_name.setText("Guide Name  :  "+guide_full_name);



    visited_city.setText("City  :  "+city_name);




            new DownLoadImageTask3(img).execute(city_pic);

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
//                firstname_data=jo.getString("first_name");
//                lastname_data = jo.getString("last_name");
//                username_data = jo.getString("user_name");
//                email_data = jo.getString("email");
//                mobile_data = jo.getString("mobile");
//                city_data = jo.getInt("main_city_id");
//                pic_data = jo.getString("pic");


                tourist_full_name = jo.getString("first_name") + " " + jo.getString("last_name");
                tourist_email = jo.getString("email");



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



class DownLoadImageTask3_ind extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;

    public DownLoadImageTask3_ind(ImageView imageView){
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
