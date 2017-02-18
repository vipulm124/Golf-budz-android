package com.golf.budz.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.golf.budz.auth.login.LoginActivity;
import com.golf.budz.utils.Const;
import com.golf.budz.utils.Pref;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    @BindView(R.id.tvText)
    TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = new Intent(this, WelcomeActivity.class);
        //code that displays the content in full screen mode
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/corporate.ttf");
        tvText.setTypeface(face);
        boolean isIntroDone = Pref.ReadBoolean(getApplicationContext(), Const.PREF_IS_INTRO_DONE, false);
        if (isIntroDone) {
            if (Pref.isLoggedIn(getApplicationContext())) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
        }
        final Intent finalIntent = intent;
        Thread background = new Thread() {
            public void run() {

                try {
                    sleep(3000 * 1);
                    openNextScreen(finalIntent);


                } catch (Exception e) {

                }
            }
        };
        background.start();
    }

    private void openNextScreen(Intent finalIntent) {
        startActivity(finalIntent);
        this.finish();
    }
}

