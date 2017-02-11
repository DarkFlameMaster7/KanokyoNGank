package me.tomoya.kanojyongank.module.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import me.tomoya.kanojyongank.R;
import me.tomoya.kanojyongank.module.gank.ui.ShowActivity;
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent = new Intent(this, ShowActivity.class);
        startActivity(intent);
        finish();
    }
}
