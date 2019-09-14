package com.example.yeuns.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

/**
 * Created by yeuns on 2016-11-14.
 */

public class ImgActivity extends AppCompatActivity implements View.OnTouchListener{

    String wkfor, wtemp, stemp, param;
    int temp, count = 0;
    ArrayList<String> list;

    WebView webview1,webview2,webview3,webview4,webview5;
   ViewFlipper viewFlipper;
    float xAtDown;
    float xAtUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();
        wtemp = intent.getStringExtra("temp");
        //Toast.makeText(ImgActivity.this,wkfor,Toast.LENGTH_LONG).show();
        temp = (int) Float.parseFloat(wtemp);
        System.out.println("temp!!" + temp);

        if (temp < 10)
            param = "1";
        else if (temp < 20)
            param = "2";
        else if (temp < 30)
            param = "3";
        else
            param = "4";

        try {
            stemp = new geturl().execute(param).get();
            list = new ArrayList<>();
            StringTokenizer token = new StringTokenizer(stemp, " ");
            while (token.hasMoreElements()) {
                String t = token.nextToken();
                list.add(t);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        webview1 = (WebView)findViewById(R.id.webview1);
        webview2 = (WebView)findViewById(R.id.webview2);
        webview3 = (WebView)findViewById(R.id.webview3);
        webview4 = (WebView)findViewById(R.id.webview4);
        webview5 = (WebView)findViewById(R.id.webview5);

        webview1.getSettings().setJavaScriptEnabled(true);
        webview1.loadUrl(list.get(0));
        //webview1.getSettings().setUseWideViewPort(true);
        //webview1.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //webview1.setWebViewClient(new WebViewClientClass());

        webview2.getSettings().setJavaScriptEnabled(true);
        webview2.loadUrl(list.get(1));
        //webview2.getSettings().setUseWideViewPort(true);

        //  webview2.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
       // webview2.setWebViewClient(new WebViewClientClass());

        webview3.getSettings().setJavaScriptEnabled(true);
        webview3.loadUrl(list.get(2));
       // webview3.getSettings().setUseWideViewPort(true);

        //webview3.getSettings().setLoadsImagesAutomatically(true);
       // webview3.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //webview3.setWebViewClient(new WebViewClientClass());

        webview4.getSettings().setJavaScriptEnabled(true);
        webview4.loadUrl(list.get(3));
        //webview4.getSettings().setUseWideViewPort(true);

        //webview4.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //webview4.setWebViewClient(new WebViewClientClass());

        webview5.getSettings().setJavaScriptEnabled(true);
        webview5.loadUrl(list.get(4));
        //webview5.getSettings().setUseWideViewPort(true);

        // webview5.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //webview5.setWebViewClient(new WebViewClientClass());

        viewFlipper = (ViewFlipper)findViewById(R.id.viewflipper);
        viewFlipper.setOnTouchListener(this);
        webview1.setOnTouchListener(this);
        webview2.setOnTouchListener(this);
        webview3.setOnTouchListener(this);
        webview4.setOnTouchListener(this);
        webview5.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view!=webview1 &&view !=webview2&&view !=webview3 &&view !=webview4 &&view !=webview5 && view!=viewFlipper) return false;
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            xAtDown=motionEvent.getX();
        }
        else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
            xAtUp = motionEvent.getX();
            if(xAtDown > xAtUp){
                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.left_in));
                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
                count++;
                if(count < 5)
                    viewFlipper.showNext();
                else{
                    Toast.makeText(this, "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show();
                    count--;
                }
            }
            else if(xAtDown < xAtUp){
                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.right_out));
                count--;
                if(count >- 1)
                    viewFlipper.showPrevious();
                else{
                    Toast.makeText(this, "첫번째 페이지 입니다.", Toast.LENGTH_SHORT).show();
                    count++;
                }
            }
        }
        return true;
    }

    class geturl extends AsyncTask<String, Void, String>
    {
        String sendmsg,recv;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://14.63.196.145/weather/imgurl.jsp");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("POST");
                connection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                sendmsg = "temp="+strings[0];
                outputStreamWriter.write(sendmsg);
                outputStreamWriter.flush();

                if (connection.getResponseCode() == connection.HTTP_OK)
                {
                    InputStream is = connection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    while ((str = reader.readLine())!= null)
                    {
                        System.out.println("str::::" + str);
                        stringBuffer.append(str);
                        stringBuffer.append(" ");
                    }
                    recv = stringBuffer.toString();
                }
                else
                {
                    Log.i("통신 결과",connection.getResponseCode()+"에러");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("MSG:::" + recv);
            return recv;
        }

    }
    /*public class ImageRoader {

        private final String serverUrl = "http://14.63.196.145/weather/aimage/"+count+"/";

        public ImageRoader() {

            new imagethread();
        }

        public Bitmap getBitmapImg(String imgStr) {

            Bitmap bitmapImg = null;

            try {
                URL url = new URL(serverUrl +URLEncoder.encode(imgStr, "utf-8"));

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                bitmapImg = BitmapFactory.decodeStream(is);

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return bitmapImg;
        }
    }

    public class imagethread {

        public imagethread() {

            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
        }
    }*/

}
