package com.app.MyWeatherBroadcaster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends FragmentActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.container, new WeatherFragment()).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.change_city){
            showInputCityDialog();
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void showInputCityDialog(){


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        Drawable drawable = new ColorDrawable(Color.BLACK);
        drawable.setAlpha(130);
        dialog.getWindow().setBackgroundDrawable(drawable);
        Button submitButton = (Button)dialog.findViewById(R.id.submit_button);
        final EditText inputText = (EditText)dialog.findViewById(R.id.city_input);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCity(inputText.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void changeCity(String city){
        WeatherFragment fragment = (WeatherFragment)getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.changeCity(city);
        new CityPreference(this).setCity(city);
    }
}
