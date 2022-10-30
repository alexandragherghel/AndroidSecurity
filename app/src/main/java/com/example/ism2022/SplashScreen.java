package com.example.ism2022;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashScreen extends AppCompatActivity {
    FXRate fxRateSplash = new FXRate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splah_screen);
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    Network network = new Network() {
                        @Override
                        protected void onPostExecute(InputStream inputStream) {
                            fxRateSplash = fxRate;
                        }
                    };
                    network.execute(new URL("https://www.bnr.ro/nbrfxrates.xml"));
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("fxRate", fxRateSplash);
                    startActivity(intent);
                    finish();
                }
            }
        };
        splashTread.start();
    }
}