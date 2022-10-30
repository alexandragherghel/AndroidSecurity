package com.example.ism2022;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class SenzoriGPS extends Activity {

    TextView textViewLog = null;
    Button buttonSenzori = null;
    Button buttonGPS = null;

    //senzori
    SensorManager sm = null;
    Sensor senzor = null;
    SenzoriListener sl = null;

    //gps
    LocationManager lm = null;
    GPSListener gl = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        textViewLog = new TextView(this);
        buttonSenzori = new Button(this);
        buttonSenzori.setText("Senzori");
        buttonSenzori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sm.unregisterListener(sl);
                sm.registerListener(sl, senzor, SensorManager.SENSOR_DELAY_UI);
                textViewLog.setText("");
            }
        });


        buttonGPS = new Button(this);
        buttonGPS.setText("GPS");
        buttonGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sm.unregisterListener(sl);
                lm.removeUpdates(gl);

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(SenzoriGPS.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                }
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gl);

                textViewLog.setText("");
            }
        });

        ll.addView(buttonSenzori);
        ll.addView(buttonGPS);
        ll.addView(textViewLog);
        sv.addView(ll);

        setContentView(sv);

        sl = new SenzoriListener();
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        //List<Sensor> senzori = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        //List<Sensor> senzori = sm.getSensorList(Sensor.TYPE_GYROSCOPE);
        List<Sensor> senzori = sm.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        senzor = senzori.get(0);

        gl = new GPSListener();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    class SenzoriListener implements SensorEventListener
    {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            textViewLog.append("Coordonatele pe cele trei axe:\r\n");
            textViewLog.append("X: "+sensorEvent.values[0]);
            textViewLog.append(" Y: "+sensorEvent.values[1]);
            textViewLog.append(" Z: "+sensorEvent.values[2]);
            textViewLog.append("\r\n");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    class GPSListener implements LocationListener
    {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            if(location!=null)
            {
                String coord = "latitude: "+location.getLatitude()+", "+"longitude: "+
                        location.getLongitude()+", "+"altitude: "+location.getAltitude();
                textViewLog.append("Pozitia: \r\n");
                textViewLog.append(coord);
                textViewLog.append("\r\n");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }
}
