package com.mastercoding.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //Views
    Button forwardBtn, playBtn, pauseBtn, backwardBtn;
    TextView songNameTxt, timeTxt;
    SeekBar songSeekbar;
    ImageView coverArtImg;

    //MediaPlayer
    MediaPlayer mediaPlayer;

    //Handler
    Handler handler = new Handler();

     //Variables
    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000; //10 seconds
    int backwardTime = 10000; //10 seconds
    static int oneTimeOnly = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = findViewById(R.id.play_btn);
        pauseBtn = findViewById(R.id.pause_btn);
        forwardBtn = findViewById(R.id.forward_btn);
        backwardBtn = findViewById(R.id.rewind_btn);
        songNameTxt = findViewById(R.id.song_title);
        timeTxt = findViewById(R.id.time_left_text);
        songSeekbar = findViewById(R.id.seekbar);
        coverArtImg = findViewById(R.id.image1);

        //Media Player
        mediaPlayer = MediaPlayer.create(this, R.raw.astronaut);

        songNameTxt.setText(getResources().getIdentifier("astronaut", "raw", getPackageName()) + ".mp3");
        //songNameTxt.setText("Astronaut in the Ocean.mp3");
        songSeekbar.setClickable(false);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Playing music", Toast.LENGTH_SHORT).show();
                PlayMusic();
                playBtn.setEnabled(false);
                pauseBtn.setEnabled(true);
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                Toast.makeText(MainActivity.this, "Pausing music", Toast.LENGTH_SHORT).show();
                pauseBtn.setEnabled(false);
                playBtn.setEnabled(true);
            }
        });

        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp = (int) startTime; //get the current position of the song
                if ((tmp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }else{
                    Toast.makeText(MainActivity.this, "Cannot jump forward!", Toast.LENGTH_SHORT).show();
                    mediaPlayer.seekTo((int) finalTime);
                }
            }
        });

        backwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp = (int) startTime;
                if ((tmp - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }else{
                    Toast.makeText(MainActivity.this, "Cannot jump backward!", Toast.LENGTH_SHORT).show();
                    mediaPlayer.seekTo(0);
                }
            }
        });

    }
    private void PlayMusic() {
        mediaPlayer.start(); //start the music
        finalTime = mediaPlayer.getDuration(); //get the duration of the song
        startTime = mediaPlayer.getCurrentPosition(); //get the current position of the song

        if (oneTimeOnly == 0) { //if song is not playing
            songSeekbar.setMax((int) finalTime); //set max value of seekbar to final time
            oneTimeOnly = 1; //set oneTimeOnly to 1
        }

        timeTxt.setText(String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)) //convert finalTime to minutes and seconds
        ));
        songSeekbar.setProgress((int)startTime); //set progress of seekbar to startTime

        handler.postDelayed(UpdateSongTime, 100); //update song time every 100 milliseconds
        };

    // Create the Runnable object
    private Runnable UpdateSongTime =  new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition(); //get the current position of the song
            timeTxt.setText(String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
            ));
            songSeekbar.setProgress((int)startTime); //set progress of seekbar to startTime
            handler.postDelayed(this, 100); //update song time every 100 milliseconds

        }
    };
}
