package com.iakprojek.endah.movielist;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class Splash extends AppCompatActivity implements Loading.LoadingTaskFinishedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar_Horizontal);
        new Loading(progressBar, this).execute("");
    }

    @Override
    public void onTaskFinished() {
        completeSplash();

    }

    private void completeSplash() {
        startApp();
        finish();

    }

    private void startApp() {
        Intent i = new Intent(Splash.this, MainActivity.class);
        startActivity(i);

    }
}
