package com.example.jpwp_git;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayMessageActivity extends AppCompatActivity {

    boolean Aktualnywidok = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Aktualnywidok = true;

        ArrayList<String> NazwaLista = getIntent().getStringArrayListExtra("nazwa");
        ArrayList<String> OdlegloscLista = getIntent().getStringArrayListExtra("odleglosc");



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }


    //obsługa klawiszy menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.MenuOpcjaLista:
            {
                return true;
            }

            case R.id.MenuOpcjaMapa:
            {
                finish();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}