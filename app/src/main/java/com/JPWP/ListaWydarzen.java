package com.JPWP;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.JPWP.R;

import java.util.ArrayList;

public class ListaWydarzen extends AppCompatActivity {

    public class DaneWydarzenia {

        private String _Nazwa;
        private String _Odleglosc;

        public DaneWydarzenia(String NazwaWydarzenia, String Odleglosc)
        {
            _Nazwa = NazwaWydarzenia;
            _Odleglosc = Odleglosc;
        }

        public String getNazwa() {
            return _Nazwa;
        }
        public String getOdleglosc(){
            return _Odleglosc;
        }
    }


    public class DaneWydarzeniaAdapter extends ArrayAdapter<DaneWydarzenia>{

        private Context mContext;
        int mResource;


        public DaneWydarzeniaAdapter(Context context, int resource,  ArrayList<DaneWydarzenia> objects) {
            super(context, resource, objects);
            mContext = context;
            mResource = resource;


        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            String Nazwa = getItem(position).getNazwa();
            String Odleglosc = getItem(position).getOdleglosc();


            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent,false);

            TextView TvNazwa = convertView.findViewById(R.id.nazwa_wydarzenia);
            TextView TvOdlegosc = convertView.findViewById(R.id.odleglosc);


            TvNazwa.setText(Nazwa);
            TvOdlegosc.setText(Odleglosc + " km");
            return convertView;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_message);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ArrayList<String> NazwaLista = getIntent().getStringArrayListExtra("nazwa");
        ArrayList<String> OdlegloscLista = getIntent().getStringArrayListExtra("odleglosc");

        ArrayList<DaneWydarzenia> ListaWydarzen = new ArrayList<>();

        for(int i = 0; i < NazwaLista.size(); i ++)
        {
            ListaWydarzen.add(new DaneWydarzenia(NazwaLista.get(i),String.format("%.2f", Float.valueOf(OdlegloscLista.get(i)))));
        }



        DaneWydarzeniaAdapter adapter = new DaneWydarzeniaAdapter(this, R.layout.item, ListaWydarzen);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);



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