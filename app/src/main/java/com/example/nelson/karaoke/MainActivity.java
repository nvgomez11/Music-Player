package com.example.nelson.karaoke;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String[] list = new String[] {"house_of_gold","karma_police","sex_on_fire"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list_view);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(arrayAdapter);

        // ListView on item selected listener.
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, list[position], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),songActivity.class);
                intent.putExtra("canciones",list);
                intent.putExtra("valor_escogido",position);
                if(songActivity.mediaPlayer!=null) {
                    if(songActivity.mediaPlayer.isPlaying()){
                        songActivity.mediaPlayer.stop();
                    }
                }
                startActivity(intent);
            }
        });
    }
}
