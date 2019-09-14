package com.example.yeuns.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    Spinner spsido, spgugun, spdong;
    Button add, remove, show;
    String sido, gugun, dong, code;
    ArrayAdapter<String> sidoAdapter, gugunAdapter, dongAdapter;
    ArrayList<String> sidolist, gugunlist, donglist;
    private static Context context;
  //----------스피너 변수
  String temp,namelist,uuid,select,deletetemp,listcode;
   ListView listView;
   ArrayList<String> items;
   ArrayAdapter<String> itemsadapter;
    private final static String CACHE_DEVICE_ID = "CacheDeviceID";
    //--------------------------



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();

        listView = (ListView)findViewById(R.id.listview);

        uuid = GetDeviceUUID();

        try {
            select = new getlist().execute(uuid,"","","list").get();
            items = new ArrayList<>();
            StringTokenizer token = new StringTokenizer(select,",");
            while (token.hasMoreElements()) {
                String t = token.nextToken();
                items.add(t);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        itemsadapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,items);
        listView.setAdapter(itemsadapter);

        spsido = (Spinner) findViewById(R.id.spinner);
        spsido.setPrompt("시/도");
        spgugun = (Spinner) findViewById(R.id.spinner2);
        spgugun.setPrompt("구/군");
        spdong = (Spinner) findViewById(R.id.spinner3);

        try {
            sido = new jspconn().execute().get();
            sidolist = new ArrayList<>();
            StringTokenizer token2 = new StringTokenizer(sido, " ");
            while (token2.hasMoreElements()) {
                String t = token2.nextToken();
                sidolist.add(t);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        sidoAdapter = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,sidolist);   // selected 안에 넣기 context 바꿔서
        sidoAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spsido.setAdapter(sidoAdapter);

        spsido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sido = sidoAdapter.getItem(i);
                //Toast.makeText(MainActivity.this, sido, Toast.LENGTH_LONG).show();

                try {
                    gugun = new nquery().execute(sido, "", "", "sido").get();
                    gugunlist = new ArrayList<String>();
                    StringTokenizer token = new StringTokenizer(gugun, " ");
                    while (token.hasMoreElements()) {
                        String t = token.nextToken();
                        gugunlist.add(t);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                gugunAdapter = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,gugunlist);
                gugunAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spgugun.setAdapter(gugunAdapter);
            }
             @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

       spgugun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gugun = gugunAdapter.getItem(i);
               // Toast.makeText(MainActivity.this,gugun,Toast.LENGTH_LONG).show();

                try
                {
                    dong= new nquery().execute("",gugun,"","gugun").get();
                    //System.out.println("dong"+ dong);
                    donglist=  new ArrayList<String>();
                    StringTokenizer token = new StringTokenizer(dong," ");
                    while (token.hasMoreElements()) {
                        String t = token.nextToken();
                        donglist.add(t);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                dongAdapter = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,donglist);
                dongAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spdong.setAdapter(dongAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spdong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dong = dongAdapter.getItem(i);
                //Toast.makeText(MainActivity.this,dong,Toast.LENGTH_LONG).show();

                try
                {
                    code = new getcode().execute(dong,"dong").get();
                    //Toast.makeText(MainActivity.this,code,Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //----------spinner end
        add = (Button)findViewById(R.id.button) ;
        remove = (Button)findViewById(R.id.button2);
        show = (Button)findViewById(R.id.button3);


        add.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                temp = sido+" "+gugun+" "+dong;
                try
                {
                   namelist  =  new getlist().execute(uuid,temp,code,"insert").get();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                items.add(temp);
                itemsadapter.notifyDataSetChanged();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checked;
                int count = itemsadapter.getCount();
                if(count > 0)
                {
                    checked = listView.getCheckedItemPosition();
                    if(checked > -1)
                    {
                        deletetemp = items.get(checked);
                        try
                        {
                            namelist  =  new getlist().execute(uuid,deletetemp,"","delete").get();
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        items.remove(checked);
                    }
                }
                listView.clearChoices();
               itemsadapter.notifyDataSetChanged();
            }
        });
        //listView.setItemsCanFocus(true);
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String put = itemsadapter.getItem(i);
                System.out.println("put:::" + put);
                try
                {
                    listcode = new getlist().execute("",put,"","code").get();
                    System.out.println("listcode:::" + listcode);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(MainActivity.this,secActivity.class);
                intent.putExtra("NAME",put);
                intent.putExtra("CODE",code);
                startActivity(intent);

                listView.clearChoices();
            }
        });*/

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count, checked;
                String put = "";
                count = itemsadapter.getCount();
                if (count > 0) {
                    checked = listView.getCheckedItemPosition();
                    if (checked > -1) {
                        put = items.get(checked);
                        System.out.println("put:::" + put);
                        try {
                            listcode = new getlist().execute("", put, "", "code").get();
                            System.out.println("listcode:::" + listcode);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        put = sido+" "+gugun+" "+dong;
                        System.out.println("put:::" + put);
                        try {
                            listcode = new getcode().execute(dong,"dong").get();
                            System.out.println("listcode:::" + listcode);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent intent = new Intent(MainActivity.this, secActivity.class);
                    intent.putExtra("NAME", put);
                    intent.putExtra("CODE", listcode);
                    startActivity(intent);
                    listView.clearChoices();
                }
            }
        });
        listView.clearChoices();
    }

    class jspconn extends AsyncTask<Void, Void, String> {
        String recv;

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String str;
                URL url = new URL("http://14.63.196.145/weather/codetest.jsp");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("POST");
                connection.connect();

                if (connection.getResponseCode() == connection.HTTP_OK) {
                    InputStreamReader in = new InputStreamReader(connection.getInputStream(), "UTF-8");
                    StringBuffer stringBuffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(in);
                    while ((str = reader.readLine()) != null) {
                        System.out.println("str::::" + str);
                        stringBuffer.append(str);
                        stringBuffer.append(" ");
                    }
                    recv = stringBuffer.toString();
                } else {
                    Log.i("통신 결과", connection.getResponseCode() + "에러");
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

    class nquery extends AsyncTask<String, Void, String>
    {
        String sendmsg,recv;
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://14.63.196.145/weather/codetest.jsp");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("POST");
                connection.connect();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                sendmsg = "sido="+strings[0]+"&gugun="+strings[1]+"&dong="+strings[2]+"&type="+strings[3];
                outputStreamWriter.write(sendmsg);
                outputStreamWriter.flush();

                if (connection.getResponseCode() == connection.HTTP_OK)
                {
                    InputStream is = connection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    while ((str = reader.readLine())!= null)
                    {
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
            return recv;
        }
    }
    class getcode extends AsyncTask<String, Void, String>
    {
        String sendmsg;
        String code;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://14.63.196.145/weather/codetest.jsp");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("POST");
                connection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                sendmsg = "dong="+strings[0]+"&type="+strings[1];
                outputStreamWriter.write(sendmsg);
                outputStreamWriter.flush();
                if (connection.getResponseCode() == connection.HTTP_OK)
                {
                    InputStream is = connection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    while ((str = reader.readLine())!= null)
                    {
                        stringBuffer.append(str);
                    }
                    code = stringBuffer.toString();
                }
                else
                {
                    Log.i("code통신 결과",connection.getResponseCode()+"에러");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return code;
        }
    }

    class getlist extends AsyncTask<String, Void, String>
    {
        String sendmsg,recv;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://14.63.196.145/weather/userlist.jsp");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("POST");
                connection.connect();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                sendmsg = "uuid="+strings[0]+"&name="+strings[1]+"&code="+strings[2]+"&type="+strings[3];
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

    public static String GetDeviceUUID()
    {
        UUID deviceUUID = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String cachedDeviceID = sharedPreferences.getString(CACHE_DEVICE_ID, "");
        if ( cachedDeviceID != "" )
        {
            deviceUUID = UUID.fromString( cachedDeviceID );
        }
        else
        {
            final String androidUniqueID = Settings.Secure.getString( context.getContentResolver(), Settings.Secure.ANDROID_ID );
            try
            {
                if ( androidUniqueID != "" )
                {
                    deviceUUID = UUID.nameUUIDFromBytes( androidUniqueID.getBytes("utf8") );
                }
                else
                {
                    final String anotherUniqueID = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    if ( anotherUniqueID != null )
                    {
                        deviceUUID = UUID.nameUUIDFromBytes( anotherUniqueID.getBytes("utf8") );
                    }
                    else
                    {
                        deviceUUID = UUID.randomUUID();
                    }
                }
            }
            catch ( UnsupportedEncodingException e )
            {
                throw new RuntimeException(e);
            }
        }
        sharedPreferences.edit().putString(CACHE_DEVICE_ID, deviceUUID.toString()).apply();
        return deviceUUID.toString();
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }
}

