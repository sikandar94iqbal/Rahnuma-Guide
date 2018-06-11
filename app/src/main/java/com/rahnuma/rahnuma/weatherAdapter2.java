package com.rahnuma.rahnuma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sikandar on 4/18/2017.
 */

public class weatherAdapter2 extends ArrayAdapter<weather> {

    Context context;
    int layoutResourceId;
    ArrayList<weather> data=new ArrayList<weather>();


    String guide_first_name;
    String guide_last_name;
    String guide_id;

    public weatherAdapter2(Context context, int layoutResourceId, ArrayList<weather> data, String guide_first_name,String guide_last_name,String guide_id) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.guide_first_name=guide_first_name;
        this.guide_last_name=guide_last_name;
        this.guide_id=guide_id;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
           weatherHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new weatherHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.button =(Button) row.findViewById(R.id.choose);
           // holder.checkBox = (CheckBox) row.findViewById(R.id.checkBox);

            row.setTag(holder);
        }
        else
        {
            holder = (weatherHolder)row.getTag();
        }

        // weather weather = data[position];
        holder.txtTitle.setText(data.get(position).get_name());

//        holder.checkBox.setOnCheckedChangeListener(myCheckChangList);
  //      holder.checkBox.setTag(position);
        // holder.checkBox.setChecked(p.box);



        Uri myUri = Uri.parse(data.get(position).get_pic().toString());


        //String picture = "https://sikandariqbal.net/Rahnuma/images/pic1.jpg";
        final String picture = data.get(position).get_pic().toString();
        String tourist_id = data.get(position).get_tourist_id().toString();
        final String city_name = data.get(position).get_name();
        new DownLoadImageTask2(holder.imgIcon).execute(picture);


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context, city_name, Toast.LENGTH_LONG).show();

                Intent myintent=new Intent(context, individual_tour.class);
                myintent.putExtra("guide_id", guide_id);
                myintent.putExtra("guide_first_name", guide_first_name);
                myintent.putExtra("guide_last_name", guide_last_name);
                myintent.putExtra("city_pic", picture);
                myintent.putExtra("city_name", city_name);
                context.startActivity(myintent);
            }
        });
        //  holder.imgIcon.setImageBitmap(mIcon_val);

        // holder.imgIcon.setImageResource();

        return row;
    }

    static class weatherHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        Button button;
    }
    weather getProduct(int position) {
        return ((weather) getItem(position));
    }

    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            getProduct((Integer) buttonView.getTag()).box = isChecked;
        }
    };





}


class DownLoadImageTask2 extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;

    public DownLoadImageTask2(ImageView imageView){
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
