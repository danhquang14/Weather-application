/**
 * Name: Quang Khai Nguyen (13410893)
 * Assignment: 1
 * Application: Weather application in Singapore
 * In this assignment, I am going to create Weather application in Singapore including the temperature(°C), status of the day, humidity(%),
 * cloud and wind (m/s) that allows the users can see how the day is
 * MainActivity contains the functionality to connect and get the data and picture from API weather on the website (openwethermap)
 * The Next step is that i will pass it to display the correct elements in activity_main.xml. Then I used intent code to make MainActivity
 * Main2Activity(Setting page) connect together. Therefore, the inner class is able to interact with Main2Activity to perform any set up in
 * Main2Activity. In MainActivity, i also used some method to make the app be able to use fingers to swipe through the left and right
 * and right to left to change between two pages.
 */

package com.example.dell.weatherappversion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView txtName,txtTemp,txtStatus,txtHumidity,txtCloud,txtWind,txtDay;
    private ImageView imgIcon;
    private LinearLayout linearLayout;
    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout=(LinearLayout) findViewById(R.id.linearLayoutMain);
        setup();
        GetcurrenWeatherData("Singapore");
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

    }
    // To swipe the screen.
    public boolean onTouchEvent(MotionEvent touchevent){
        switch (touchevent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1=touchevent.getX();
                y1=touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2=touchevent.getX();
                y2=touchevent.getY();
                if (x1>x2){
                    Intent i =new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
    @Override
    public void onStart() {
        final String PREFS_NAME = "PREFS_FILE";

        //Shared Preferences with Setting activity
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        boolean Colorvalue = sharedPreferences.getBoolean("CHANGE_COLOR", false);
        boolean enlargedSize=sharedPreferences.getBoolean("ENLARGED_SWITCH", false);
        Changecolors(Colorvalue);
        EnlargedTextScreen(enlargedSize);
        super.onStart();}

    private void Changecolors(boolean colorvalue) {
        if(colorvalue){
            linearLayout.setBackgroundColor(Color.parseColor("#e2c9ed"));
        }
        else {
            linearLayout.setBackgroundColor(Color.parseColor("#F3C677"));
        }

    }
    private void EnlargedTextScreen(boolean enlargedSize){
        float sp = getResources().getDisplayMetrics().scaledDensity;
        final int enlargeVal = 15;

        //Get current text size in actual pixel
        float cityTextSize = txtName.getTextSize();
        float conditionTextSize = txtStatus.getTextSize();
        float dateTextSize = txtDay.getTextSize();

        if (enlargedSize) {
            if ((conditionTextSize / sp) != 30) {
                txtName.setTextSize((cityTextSize + enlargeVal) / sp);
                txtStatus.setTextSize((conditionTextSize + enlargeVal) / sp);
                txtDay.setTextSize((dateTextSize + enlargeVal) / sp);
            }
        } else {
            if ((conditionTextSize / sp) != 29) {
                txtName.setTextSize((conditionTextSize - enlargeVal) / sp);
                txtStatus.setTextSize((conditionTextSize - enlargeVal) / sp);
                txtDay.setTextSize((dateTextSize - enlargeVal) / sp);
            }
        }
    }


    protected void onResume() {
        super.onResume();
        GetcurrenWeatherData("Singapore");
    }
    public void GetcurrenWeatherData(String data){
        RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);
        String url="https://api.openweathermap.org/data/2.5/weather?q=Singapore&units=metric&appid=71ea20d64880fa79a63ae6a09915c1eb";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject =  new JSONObject(response);
                            String day=jsonObject.getString("dt");
                            String name=jsonObject.getString("name");
                            txtName.setText("Country name: "+name);

                            long l =Long.valueOf(day);
                            Date date= new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEEE yyyy-MM-dd HH-mm-ss");
                            String Day=simpleDateFormat.format(date);

                            txtDay.setText(Day);
                            JSONArray jsonArrayWeather=jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather= jsonArrayWeather.getJSONObject(0);
                            String status =jsonObjectWeather.getString("main");
                            String icon=jsonObjectWeather.getString("icon");
                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/"+icon+".png").into(imgIcon);
                            txtStatus.setText(status);

                            JSONObject jsonObjectMain=jsonObject.getJSONObject("main");
                            String temperature= jsonObjectMain.getString("temp");
                            String humid=jsonObjectMain.getString("humidity");

                            Double a=Double.valueOf(temperature);
                            String Temperature=String.valueOf(a.intValue());

                            txtTemp.setText(Temperature+"°C");
                            txtHumidity.setText(humid+"%");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String windy=jsonObjectWind.getString("speed");
                            txtWind.setText(windy+"m/s");

                            JSONObject jsonObjectClouds=jsonObject.getJSONObject("clouds");
                            String cloudy=jsonObjectClouds.getString("all");
                            txtCloud.setText(cloudy+"%");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
         requestQueue.add(stringRequest);

    }
    private void setup() {
        txtName=(TextView) findViewById(R.id.textviewName);
        txtTemp=(TextView) findViewById(R.id.textviewTemp);
        txtStatus=(TextView) findViewById(R.id.textviewStatus);
        txtHumidity=(TextView) findViewById(R.id.textviewHumidity);
        txtCloud=(TextView) findViewById(R.id.textviewCloud);
        txtWind=(TextView) findViewById(R.id.textviewWind);
        txtDay=(TextView) findViewById(R.id.textviewDay);
        imgIcon=(ImageView) findViewById(R.id.imageIcon);


    }   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        //Action Bar menu
        switch (item.getItemId()) {
            case R.id.setting:
                intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        }

        }


