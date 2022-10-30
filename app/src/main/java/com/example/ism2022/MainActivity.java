package com.example.ism2022;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final int SMSENCRYPT = 0;
    public static final int EXIT = 2;
    private static final int JSON = 1;
    TextView tvDate;
    EditText etEUR, etUSD, etGBP, etXAU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("lifecycle", "call of the method onCreate");
        tvDate = findViewById(R.id.tvDate);
        etEUR = findViewById(R.id.editTextEUR);
        etUSD = findViewById(R.id.editTextUSD);
        etGBP = findViewById(R.id.editTextGBP);
        etXAU = findViewById(R.id.editTextXAU);

        Button btnShow = findViewById(R.id.btnShow);
        Button btnSave = findViewById(R.id.btnSave);
        Spinner spinner = findViewById(R.id.spinner);

        String[] values = {"INTERNAL FILE SYSTEM", "LOCAL DATABASE"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.support.constraint.R.layout.support_simple_spinner_dropdown_item, values);
        spinner.setAdapter(adapter);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Network network = new Network() {
                    @Override
                    protected void onPostExecute(InputStream inputStream) {
                        tvDate.setText((fxRate.getDate()));
                        etEUR.setText((fxRate.getEuro()));
                        etUSD.setText((fxRate.getDolar()));
                        etGBP.setText((fxRate.getPound()));
                        etXAU.setText((fxRate.getGold()));
                    }
                };
                try {
                    network.execute(new URL("https://www.bnr.ro/nbrfxrates.xml"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = spinner.getSelectedItemPosition();
                FXRate fxRate = new FXRate(tvDate.getText().toString(), etEUR.getText().toString(), etUSD.getText().toString(), etGBP.getText().toString(), etXAU.getText().toString());

                if (position == 0) {
                    try {
                        writeFxRateToFile("file.dat", fxRate);
                        fxRate = null;
                        fxRate = readFxRateFromFile("file.dat");
                        Toast.makeText(getApplicationContext(), fxRate.toString(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
    }


    private void writeFxRateToFile(String fileName, FXRate fxRate) throws IOException {
        FileOutputStream fileOutputStream = openFileOutput(fileName, Activity.MODE_PRIVATE);
        DataOutputStream dos = new DataOutputStream(fileOutputStream);
        dos.writeUTF(fxRate.getDate());
        dos.writeUTF(fxRate.getEuro());
        dos.writeUTF(fxRate.getDolar());
        dos.writeUTF(fxRate.getPound());
        dos.writeUTF(fxRate.getGold());

        dos.flush();
        fileOutputStream.close();
    }

    private FXRate readFxRateFromFile(String fileName) throws IOException {
        FileInputStream fileInputStream = openFileInput(fileName);
        DataInputStream dis = new DataInputStream(fileInputStream);
        String date = dis.readUTF();
        String euro = dis.readUTF();
        String dolar = dis.readUTF();
        String pound = dis.readUTF();
        String gold = dis.readUTF();
        FXRate fxRate = new FXRate(date, euro, dolar, pound, gold);
        fileInputStream.close();
        return fxRate;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("lifecycle", "call of the method onStart()");

        Intent intent = getIntent();
        FXRate fxRate = (FXRate) intent.getSerializableExtra("fxRate");
        tvDate.setText((fxRate.getDate()));
        etEUR.setText((fxRate.getEuro()));
        etUSD.setText((fxRate.getDolar()));
        etGBP.setText((fxRate.getPound()));
        etXAU.setText((fxRate.getGold()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("lifecycle", "call of the method onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("lifecycle", "call of the method onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("lifecycle", "call of the method onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("lifecycle", "call of the method onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("lifecycle", "call of the method onDestroy()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, SMSENCRYPT, 0, "SMS Encrypt Activity");
        menu.add(0, JSON, 1, "JSON Activity");
        menu.add(0, EXIT, 2, "Close the application");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case SMSENCRYPT:
                Intent intent = new Intent(this, SMSEncryptActivity.class);
                startActivity(intent);
                break;
            case JSON:
                Intent intent1 = new Intent(getApplicationContext(),JSONActivity.class);
                startActivity(intent1);
                break;
            case EXIT:
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
        }
        return false;
    }

}