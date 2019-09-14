package com.example.yeuns.project;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yeuns on 2016-11-22.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseIIDService";


    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);

        // 각자 핸드폰 토큰값을 핸드폰으로 전송한다
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {

        HttpURLConnection connection;

        try {
            URL url = new URL("http://14.63.196.145/weather/register.jsp");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true); //서버에 데이터 보낼 때, Post의 경우 꼭 사용
            connection.setDoInput(true); //서버에서 데이터 가져올 때
            connection.setRequestMethod("POST"); // POST방식을

            StringBuffer buffer = new StringBuffer();
            buffer = buffer.append("Token").append("=").append(token);
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(buffer.toString());
            wr.flush(); // 서버에 작성
            //wr.close(); // 객체를 닫음

           // if (connection.getResponseCode() == connection.HTTP_OK) {
                // 서버에서 값을 받아오지 않더라도 작성해야함
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                connection.disconnect();
          /*  }
            else {
                Log.i("통신 결과",connection.getResponseCode()+"에러");
            }*/
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
