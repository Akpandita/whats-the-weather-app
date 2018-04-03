package com.hp.whatstheweatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityname;
    TextView resulttextview;
    public void findweather(View view){
        try {
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(cityname.getWindowToken(),0);
            String encodedcityname= URLEncoder.encode(cityname.getText().toString(),"UTF-8");
            DownloadTask task = new DownloadTask();
            task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encodedcityname + "&appid=45a1d6aea8e84d98cfc793b367a29218");
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityname=(EditText)findViewById(R.id.cityname);
        resulttextview=(TextView)findViewById(R.id.resulttextview);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlconnection = null;
            try {
                url = new URL(urls[0]);
                urlconnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlconnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                super.onPostExecute(s);
                String message="";
                JSONObject jsonObject = new JSONObject(s);
                String wetherinfo = jsonObject.getString("weather");
                Log.i("webcontent", jsonObject.getString("weather"));
                JSONArray jsonArray = new JSONArray(wetherinfo);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String main="";
                    String description="";
                    main=jsonObject1.getString("main");
                    description=jsonObject1.getString("description");

                    if(main!="" && description!="")
                    {
                        message+=main+":"+description+"\r\n";
                    }
                    if(message!=""){
                        resulttextview.setText(message);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);
            }

        }
    }
}
