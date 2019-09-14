package com.example.yeuns.project;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by yeuns on 2016-11-12.
 */

public class secActivity extends AppCompatActivity {

    listweather listweatheradapter;
    ListView listView;

    String sCategory;
    String sTm;
    String [] sHour,sDay,sTemp,sWdKor,sReh,sWfKor;
    int data;

    static boolean updated;
    boolean bCategory,bTm,bHour,bDay,bTemp,bWdKor,bReh,bWfKor;
    boolean tCategory;
    boolean tTm;
    boolean tItem;
    Handler handler;

    String name,code;
    TextView text;
    Button btn;
    String imgsearch,imgtemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec);

        Intent intent = getIntent();
        name = intent.getStringExtra("NAME");
        code = intent.getStringExtra("CODE");
        //System.out.println("createcode!!!!!!:"+code);
        listView = (ListView)findViewById(R.id.seclistview);
        handler=new Handler();
        bCategory=bTm=bHour=bTemp=bWdKor=bReh=bDay=bWfKor=tCategory=tTm=tItem=false;

        listweatheradapter = new listweather(getApplicationContext());
        listView.setAdapter(listweatheradapter);
        text = (TextView)findViewById(R.id.textView);

        weather_thread thread=new weather_thread();
        thread.start();

       // FirebaseMessaging.getInstance().subscribeToTopic("weather");
       // FirebaseInstanceId.getInstance().getToken();
       // Toast.makeText(this, FirebaseInstanceId.getInstance().getToken(),Toast.LENGTH_SHORT).show();
        /*if("구름 조금".equals(imgsearch))
        {
            message = "구름 조금";
            new fcmmsg().execute(message);
        }
        else if(imgsearch.equals("눈/비"))
            message = "우산 챙기세요";
        else if(imgsearch.equals("눈"))
            message = "바닥이 미끄러워요 조심하세요\n 우산도 챙기세요";
*/

        //Toast.makeText(secActivity.this,name+" "+code,Toast.LENGTH_LONG).show();
        btn = (Button)findViewById(R.id.button4);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(secActivity.this,ImgActivity.class);
                //intent1.putExtra("weather",imgsearch);
                //System.out.println("btnimgsearch : "+imgsearch);
                imgsearch = imgtemp;
                intent1.putExtra("temp",imgtemp);
                startActivity(intent1);
            }
        });

        //Toast.makeText(secActivity.this,imgsearch,Toast.LENGTH_LONG).show();
       // System.out.println("toastimage : "+imgsearch);



       // IntentSender sender
    }

    class weather_thread extends Thread{
        public void run(){
            try{
                updated=false;
                sHour=new String[20];
                sDay=new String[20];
                sTemp=new String[20];
                sWdKor=new String[20];
                sReh=new String[20];
               sWfKor=new String[20];
                data=0;
                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp=factory.newPullParser();

                String weatherUrl="http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone="+code;
                URL url=new URL(weatherUrl);
                InputStream is=url.openStream();
                xpp.setInput(is,"UTF-8");

                int eventType=xpp.getEventType();
                while(eventType!=XmlPullParser.END_DOCUMENT){

                    switch(eventType){
                        case XmlPullParser.START_TAG:
                            if(xpp.getName().equals("category")){
                                bCategory=true;
                            } if(xpp.getName().equals("pubDate")){
                            bTm=true; }
                         if(xpp.getName().equals("hour")){
                            bHour=true;
                        } if(xpp.getName().equals("day")){
                            bDay=true;
                        } if(xpp.getName().equals("temp")){
                            bTemp=true;
                        } if(xpp.getName().equals("wdKor")){
                            bWdKor=true;
                        } if(xpp.getName().equals("reh")){
                            bReh=true;
                        } if(xpp.getName().equals("wfKor")){
                            bWfKor=true;
                        }
                            break;

                        case XmlPullParser.TEXT:
                            if(bCategory){
                                sCategory=name;
                                bCategory=false;
                            } if(bTm){
                            sTm=xpp.getText();
                            bTm=false;
                        }  if(bHour){
                            sHour[data]=xpp.getText();
                            bHour=false;
                        }  if(bDay){
                            sDay[data]=xpp.getText();
                            bDay=false;
                        }  if(bTemp){
                           sTemp[data]=xpp.getText();
                            if(data==0)
                            {
                                imgtemp = sTemp[data];
                            }
                            bTemp=false;
                        }  if(bWdKor){
                           sWdKor[data]=xpp.getText();
                            bWdKor=false;
                        }  if(bReh){
                            sReh[data]=xpp.getText();
                            bReh=false;
                        } if(bWfKor){
                           sWfKor[data]=xpp.getText();
                            if(data ==0)
                            {
                                imgsearch = sWfKor[data];
                                if(imgsearch.equals("눈/비"))
                                {
                                    String str = "우산 챙겨가세요";
                                    NotificationSomethings(str);
                                }
                                else if(imgsearch.equals("비"))
                                {
                                    String str = "우산 챙겨가세요";
                                    NotificationSomethings(str);
                                }
                                else if(imgsearch.equals("눈"))
                                {
                                    String str = "우산 챙겨가세요";
                                    NotificationSomethings(str);
                                }
                                else if(imgsearch.equals("맑음"))
                                {
                                    String str = "날씨가 화창하내요";
                                    NotificationSomethings(str);
                                }
                                else if(imgsearch.equals("흐림"))
                                {
                                    String str = "비가 올 수도 있겠습니다";
                                    NotificationSomethings(str);
                                }
                                else if(imgsearch.equals("구름 많음"))
                                {
                                    String str = "비가 올 수도 있겠습니다";
                                    NotificationSomethings(str);
                                }
                                else if(imgsearch.equals("구름 조금"))
                                {
                                    String str = "햇빛이 따갑지는 않아요";
                                    NotificationSomethings(str);
                                }
                            }
                            bWfKor=false;
                        }
                            break;

                        case XmlPullParser.END_TAG:

                            if(xpp.getName().equals("item")){
                                tItem=true;
                                view_text();
                            } if(xpp.getName().equals("pubDate")){
                            tTm=true;
                            view_text();
                        } if(xpp.getName().equals("category")){
                            tCategory=true;
                            view_text();
                        } if(xpp.getName().equals("data")){
                            data++;
                        }
                            break;
                    }
                    eventType=xpp.next();
                }

            }catch(Exception e){
                e.printStackTrace();
            }

        }

        private void view_text(){
            handler.post(new Runnable() {
                @Override
                public void run() {

                    if(tCategory){
                        text.setText(text.getText()+"지역:"+sCategory+"\n");
                        tCategory=false;
                    }if((tTm)&&(sTm.length()>11)){
                        text.setText(text.getText()+"발표시각:"+sTm+"\n");
                        tTm=false;
                    }if(tItem){

                        for(int i=0;i<data;i++){
                            if(sDay[i]!=null){
                                if(sDay[i].equals("0")){
                                    sDay[i]="날짜:"+"오늘";

                                }else if(sDay[i].equals("1")){
                                    sDay[i]="날짜:"+"내일";

                                }else if(sDay[i].equals("2")){
                                    sDay[i]="날짜:"+"모레";

                                }
                            }

                        }
                        listweatheradapter.setDay(sDay);
                        listweatheradapter.setTime(sHour);
                        listweatheradapter.setTemp(sTemp);
                        listweatheradapter.setWind(sWdKor);
                        listweatheradapter.setHum(sReh);
                        listweatheradapter.setWeather(sWfKor);
                        updated=true;
                        listweatheradapter.notifyDataSetChanged();
                        tItem=false;
                        data=0;


                    }


                }
            });
        }
    }

    public void NotificationSomethings(String str) {


        Resources res = getResources();

       /*Intent notificationIntent = new Intent(this, NotificationSomethings.class);
        notificationIntent.putExtra("notificationId", 9999); //전달할 값
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
*/
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle("현재 날씨")
                .setContentText(str)
                .setTicker(str)
                .setSmallIcon(R.mipmap.clear)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.weather))
                //.setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0000, builder.build());
    }

    /*class fcmmsg extends AsyncTask<String, Void, Void>
    {
        String sendmsg;
        @Override
        protected Void doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://14.63.196.145/weather/push.jsp");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("POST");
                connection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                sendmsg = "message="+strings[0];
                System.out.println("message"+strings[0]);
                outputStreamWriter.write(sendmsg);
                outputStreamWriter.flush();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }*/

}
