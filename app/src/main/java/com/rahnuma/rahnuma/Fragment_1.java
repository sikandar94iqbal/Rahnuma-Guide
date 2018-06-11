package com.rahnuma.rahnuma;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.util.zip.Inflater;

/**
 * Created by Sikandar on 4/23/2017.
 */

public class Fragment_1 extends Fragment {
    String address="http://ennovayt.com/Rahnuma/get_tour.php";

    public String guide_first_name;
    public String guide_last_name;
    public String guide_id;



    //constructor
    public Fragment_1(String guide_first_name,String guide_last_name, String guide_id){
        this.guide_first_name=guide_first_name;
        this.guide_last_name=guide_last_name;
        this.guide_id=guide_id;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_1_layout, null);

        ListView lv = (ListView) view.findViewById(R.id.listview);



     //   Toast.makeText(getContext(), guide_first_name, Toast.LENGTH_LONG).show();
        Downloader d2 = new Downloader(getActivity(),address,lv,guide_first_name,guide_last_name,guide_id);
        d2.execute();

        return view;
    }
}



class Downloader extends AsyncTask<Void,Integer,String> {

    Context c;
    String address;
    ListView lv;
    String CITY = null;
    ///ArrayList<String> check_box_data = new ArrayList<String>();
    ProgressDialog pd;


    String guide_first_name;
    String guide_last_name;
    String guide_id;


    // publlistAdapter boxAdapter;

    public Downloader(Context c, String address, ListView lv, String guide_first_name,String guide_last_name, String guide_id) {
        this.c = c;
        this.address = address;
        this.lv = lv;

        this.guide_first_name=guide_first_name;
        this.guide_last_name=guide_last_name;
        this.guide_id=guide_id;

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
            Parser2 p=new Parser2(c,s,lv,guide_first_name,guide_last_name,guide_id);
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
            String data = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(guide_id, "UTF-8");
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



class Parser2 extends AsyncTask <Void,Integer,Integer>{

    ///public listAdapter boxAdapter;

    String guide_first_name;
    String guide_last_name;
    String guide_id;



    Context c;
    String data;
    ListView lv;
    ProgressDialog pd;
    //    ArrayList<String> check_box_data = new ArrayList<String>();
    ArrayList<String> players=new ArrayList<>();

    ArrayList<weather> data_list = new ArrayList<weather>();

  //  public listAdapter get_adapter(){
//        return this.boxAdapter;
  //  }

    public Parser2(Context c, String data, ListView lv, String guide_first_name,String guide_last_name, String guide_id) {
        this.c = c;
        this.data = data;
        this.lv = lv;
        this.guide_first_name=guide_first_name;
        this.guide_last_name=guide_last_name;
        this.guide_id=guide_id;
        // this.check_box_data = check_box_data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd=new ProgressDialog(c);
        pd.setTitle("Parsing Data");
        pd.setMessage("Parsing.....please wait");
        pd.show();
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

            weatherAdapter2 Wadapter = new weatherAdapter2(c, R.layout.row2,data_list,guide_first_name,guide_last_name,guide_id);
            lv.setAdapter(Wadapter);

//
//           boxAdapter = new listAdapter(c, data_list);
//
//            lv.setAdapter(boxAdapter);


            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(c,"HELLOOO",Toast.LENGTH_LONG);

                }
            });

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






            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                }
            });
        }
        else
        {
            Toast.makeText(c,"Unable to parse data",Toast.LENGTH_SHORT).show();


        }
        pd.dismiss();
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
                String name=jo.getString("main_city_name");
                String pic = jo.getString("pic");
                pic = "http://ennovayt.com"+pic;
                String tourist_id = jo.getString("tourist_id");
                // players.add(name);
                data_list.add(new weather(name,i,pic,false,tourist_id));
            }
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
