package space.personal.youssefalmkari.gobird;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create SplashScreen Configuration
        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(space.personal.youssefalmkari.gobird.SpeechToTextActivity.class)
                .withSplashTimeOut(5000)
                .withBackgroundColor(Color.parseColor("#FFFFFF"))
                .withLogo(R.drawable.kestrel2)
                .withFooterText("Copyright 2018")
                .withHeaderText("READY TO EXPLORE");

        // Set Text Color and Size
        config.getHeaderTextView().setTextColor(Color.BLACK);
        config.getHeaderTextView().setTextSize(30);
        config.getFooterTextView().setTextColor(Color.BLACK);
        config.getFooterTextView().setTextSize(10);

        // Set to View
        View view = config.create();
        //Set view to content view
        setContentView(view);
    }
}
