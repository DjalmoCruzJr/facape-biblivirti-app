package org.sysmob.biblivirti.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;

/**
 * @author djalmocruzjr
 * @version 1.0
 *          <p>
 *          Tela de Splash
 * @since 22/12/2016
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);*/
                finish();
            }
        }, BiblivirtiConstants.ACTIVITY_SPLASH_TIME_OUT);
    }
}
