package com.shahedrahim.downloadintentservice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (haveStoragePermission()) {
            Toast.makeText(this, "Write Permission Granted.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Write Permission Denied.", Toast.LENGTH_SHORT).show();
        }

        DownloadIntentService.startDownloadVideo(
                this,
                "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
                "movies/big_buck_bunny.mp4",
                true);
        Toast.makeText(this, "Downloaded big_buck_bunny.mp4", Toast.LENGTH_SHORT).show();

        DownloadIntentService.startDownloadMusic(
                this,
                "http://www.noiseaddicts.com/samples_1w72b820/2541.mp3",
                "songs/2541.mp3",
                true);
        Toast.makeText(this, "Downloaded 2541.mp3", Toast.LENGTH_SHORT).show();
    }

    private boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG,"You have permission to write");
                return true;
            } else {

                Log.e(TAG,"You have asked for write permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e(TAG,"You already have the permission");
            return true;
        }
    }
}
