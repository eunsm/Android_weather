package com.example.yeuns.project;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by yeuns on 2016-11-13.
 */

class listweather extends BaseAdapter {

    secActivity sec;
   String[] day,time,temp,wind,hum,liweather;
    Context Context;
    String temp_data[]=new String [15];  //갯수결정과 널 방지


    public listweather(Context context) {
        Context = context;
    }

    public void setDay(String[] data){
        day=data;
    }
    public void setTime(String[] data){
        time=data;
    }
    public void setTemp(String[] data){
        temp=data;
    }
    public void setWind(String[] data){
        wind=data;
    }
    public void setHum(String[] data){
        hum=data;
    }
    public void setWeather(String[] data){
        liweather=data;
    }

    @Override
    public int getCount() {
        return temp_data.length;
    }

    @Override
    public Object getItem(int i) {
        return temp_data[i];
        //return temp_data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        weather layout = null;
        if(view!=null){
            layout=(weather)view;
        }
        else{
            layout=new weather(Context.getApplicationContext());
        }
        if(sec.updated)  {
            try{
                layout.setDate(day[i]);
                layout.setTime(time[i]);
                layout.setTemp(temp[i]);
                layout.setWind(wind[i]);
                layout.setHum(hum[i]);
                layout.setWeather(liweather[i]);
            }catch(Exception e){
                Log.d("getview", String.valueOf(e));

            }
        }
        return layout;
    }
}
