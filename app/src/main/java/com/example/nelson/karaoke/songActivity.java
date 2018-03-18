package com.example.nelson.karaoke;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebHistoryItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class songActivity extends AppCompatActivity {

    ArrayList<Integer> songList = new ArrayList<Integer>();
    ArrayList<Integer> lyrics_list = new ArrayList<Integer>();
    String[] canciones = new String[]{};
    public static MediaPlayer mediaPlayer;
    int valor;
    SeekBar seekBar;
    Runnable runnable;
    TextView textView_lyrics;
    boolean play=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        Intent intent = getIntent();
        canciones = intent.getStringArrayExtra("canciones");
        valor = intent.getIntExtra("valor_escogido", 0);

        //lleno el array con el id de canciones
        for(int i=0;i<canciones.length; i++){
            //obtiene el identificador del recurso
            int resId = getResources().getIdentifier(canciones[i],"raw", this.getPackageName());
            String lyrics_name = "_lyrics";
            int lyrics_id = getResources().getIdentifier(canciones[i]+lyrics_name,"raw",this.getPackageName());
            songList.add(resId);
            lyrics_list.add(lyrics_id);
        }

        textView_lyrics = findViewById(R.id.textView_lyrics);
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            Log.i("ENTRO AQUI","WHY");
            mediaPlayer.stop();
        }

        //mediaPlayer.reset();
        //mediaPlayer.release();
        //mediaPlayer = MediaPlayer.create(this, songList.get(valor));

        mediaPlayer = MediaPlayer.create(this, songList.get(valor));

        seekBar = findViewById(R.id.seekBar);
        final TextView tiempoInicial = findViewById(R.id.tiempoInicial);
        final TextView tiempoTotal = findViewById(R.id.tiempoTotal);
        String letra = readRawResource(this,lyrics_list.get(valor));
        textView_lyrics.setText(letra);
        textView_lyrics.animate().translationY(-4000f).setDuration(50000);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        mediaPlayer.start();
        final Handler handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    int mediaPos_new = mediaPlayer.getCurrentPosition();
                    int mediaMax_new = mediaPlayer.getDuration();
                    seekBar.setMax(mediaMax_new);
                    seekBar.setProgress(mediaPos_new);
                    String tiempo_inicial = conviertMilisegundos(mediaPos_new);
                    String tiempo_total = conviertMilisegundos(mediaMax_new);
                    tiempoInicial.setText(tiempo_inicial);
                    tiempoTotal.setText(tiempo_total);
                    handler.postDelayed(this, 100); //Looping the thread after 0.1 second
                }
            }
        };runnable.run();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean usuario) {
                if(mediaPlayer != null && usuario){
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    String conviertMilisegundos(int milisegundos){
        String resultado="";
        int segundos = milisegundos/1000;
        int minutos= segundos/60;
        String.format("%d:%d", segundos, minutos);
        return resultado;
    }


    public void play_stop_song(View view){
        Button buttonPlayStop = findViewById(R.id.play_button);
        Button buttonPause = findViewById(R.id.pauseButton);
        //int id = getResources().getIdentifier("android:drawable/" + StringGenerated, null, null);
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            buttonPlayStop.setVisibility(View.VISIBLE);
            buttonPause.setVisibility(View.INVISIBLE);
        }else{
            mediaPlayer.getCurrentPosition();
            mediaPlayer.start();
            buttonPause.setVisibility(View.VISIBLE);
            buttonPlayStop.setVisibility(View.INVISIBLE);
        }
    }

    public void change_previous_song(View view){
        mediaPlayer.stop();
        if(valor-1 < 0){
            valor = canciones.length-1;
        }else{
            valor--;
        }
        mediaPlayer = MediaPlayer.create(this, songList.get(valor));
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        String letra = readRawResource(this,lyrics_list.get(valor));
        textView_lyrics.setText(letra);
        mediaPlayer.start();
        runnable.run();

    }

    public void change_next_song(View view){
        mediaPlayer.stop();
        if(valor==canciones.length-1){
            valor = 0;
        }else{
            valor++;
        }
        mediaPlayer = MediaPlayer.create(this, songList.get(valor));
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        seekBar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        String letra = readRawResource(this,lyrics_list.get(valor));
        textView_lyrics.setText(letra);
        mediaPlayer.start();
        runnable.run();
    }

    private String readRawResource(Context i, int resId)
    {
        InputStream inputStream = i.getResources().openRawResource(resId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return sb.toString();
    }
}
