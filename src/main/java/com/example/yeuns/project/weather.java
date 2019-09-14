package com.example.yeuns.project;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by yeuns on 2016-11-13.
 */

public class weather extends LinearLayout {

    TextView date;
    TextView time;
    TextView temp;
    TextView wind;
    TextView hum;
    TextView weather;
    ImageView Iweather;

    public weather(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public weather(Context context) {
        super(context);
        init(context);
    }

    public void setDate(String data) {
        date.setText(data+" ");
    }
    public void setTime(String data) {
        time.setText(data+"시 ");
    }
    public void setTemp(String data) {
        temp.setText(data+"도 ");
    }
    public void setWind(String data) {
        wind.setText(data+"풍 ");
    }
    public void setHum(String data) {
        hum.setText(data+"% ");
    }
    public void setWeather(String data) { weather.setText(data);
        if(data.toString().equals("맑음")){
            Iweather.setImageResource(R.drawable.clear);
        }else if(data.toString().equals("흐림")){
            Iweather.setImageResource(R.drawable.cloudy);
        }else if(data.toString().equals("구름 많음")){
            Iweather.setImageResource(R.drawable.mostly_cloudly);
        }else if(data.toString().equals("구름 조금")){
            Iweather.setImageResource(R.drawable.partly_cloudy);
        }else if(data.toString().equals("눈")){
            Iweather.setImageResource(R.drawable.snow);
        }else if(data.toString().equals("비")){
            Iweather.setImageResource(R.drawable.rain);
        }else if(data.toString().equals("눈/비")){
            Iweather.setImageResource(R.drawable.snow_rain);
        }else{
            Iweather.setImageResource(R.drawable.ic_launcher);
        }if(data.toString()==null){
            Iweather.setImageResource(R.drawable.ic_launcher);
        }
    }

    public void init(Context context){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.weather,this,true);

        date=(TextView)findViewById(R.id.textView2);
        time=(TextView)findViewById(R.id.textView3);
        temp=(TextView)findViewById(R.id.textView4);
        wind=(TextView)findViewById(R.id.textView5);
        hum=(TextView)findViewById(R.id.textView7);
        weather=(TextView)findViewById(R.id.textView6);
        Iweather=(ImageView) findViewById(R.id.imageView);

    }

}
