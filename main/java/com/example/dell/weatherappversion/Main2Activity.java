/**  Main2Activity is a setting pages containing two functions which are changing color of the app
 * and making the text size bigger.
 */


package com.example.dell.weatherappversion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.MotionEvent;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private final String PREFS_NAME = "PREFS_FILE";
    private LinearLayout layout;
    private SwitchCompat enlargedtext;
    private boolean enlargedBoolean;
    private SwitchCompat changeColor;
    private boolean IsRestored;
    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    layout=findViewById(R.id.linearlayout);
    changeColor=findViewById(R.id.change_color);
    changeColor.setOnCheckedChangeListener(this);
    enlargedtext = findViewById(R.id.enlarged_text);
    enlargedtext.setOnCheckedChangeListener(this);
    setupSwitchCompatEventListener();
    }
    public boolean onTouchEvent(MotionEvent touchevent){
        switch (touchevent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1=touchevent.getX();
                y1=touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2=touchevent.getX();
                y2=touchevent.getY();
                if (x1<x2){
                    Intent i =new Intent(Main2Activity.this,MainActivity.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
    private void setupSwitchCompatEventListener() {
        changeColor.setOnCheckedChangeListener(this);
    }
    @Override
    public void onStart() {
        //Restore instance state from last session
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        boolean IsRestored = sharedPreferences.getBoolean("CHANGE_COLOR", false);
        boolean enlargedBoolean = sharedPreferences.getBoolean("ENLARGED_SWITCH", false);
        changeColor.setChecked(IsRestored);
        enlargedtext.setChecked(enlargedBoolean);
    }
    @Override
    public void onPause() {
        //Save instance state of current session
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("CHANGE_COLOR", IsRestored);
        editor.putBoolean("ENLARGED_SWITCH", enlargedBoolean);
        editor.apply();
        super.onPause();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    switch (buttonView.getId()){
        case (R.id.change_color):
            if (isChecked){
                IsRestored=true;
                layout.setBackgroundColor(Color.parseColor("#e2c9ed"));
                Toast.makeText(getApplicationContext(),
                        "Switch Change Color is on"+isChecked,
                Toast.LENGTH_LONG).show();
            }else{
                IsRestored=false;
                layout.setBackgroundColor(Color.parseColor("#F3C677"));
                Toast.makeText(getApplicationContext(),
                        "Switch Change Color is on"+isChecked,
                        Toast.LENGTH_LONG).show();
            }
            break;
        case (R.id.enlarged_text):  //"Enlarged Text" SwitchCompat
            if (!isChecked) {
                enlargedBoolean = false;
                Toast.makeText(Main2Activity.this, "Enlarged Text is off", Toast.LENGTH_SHORT).show();
            } else {
                enlargedBoolean = true;
                Toast.makeText(Main2Activity.this, "Enlarged Text is on", Toast.LENGTH_SHORT).show();
            }
            break;
    }
    }
}
