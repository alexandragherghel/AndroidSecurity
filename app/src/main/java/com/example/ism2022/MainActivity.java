package com.example.ism2022;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final int SMSENCRYPT = 0;
    public static final int JSON = 1;
    public static final int VIEWDB = 2;
    public static final int EXIT = 3;

    TextView tvDate;
    EditText etEUR, etUSD, etGBP, etXAU;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseDatabase=FirebaseDatabase.getInstance();

        Log.e("lifecycle", "call of the method onCreate()");

        //TextView tvDate1 = new TextView(this);
        tvDate = findViewById(R.id.tvDate);
        etEUR = findViewById(R.id.editTextEUR);
        etUSD = findViewById(R.id.editTextUSD);
        etGBP = findViewById(R.id.editTextGBP);
        etXAU = findViewById(R.id.editTextXAU);

        Button btnShow = findViewById(R.id.btnShow);
        Button btnSave = findViewById(R.id.btnSave);
        Spinner spinner = findViewById(R.id.spinner);

        String[] values = {"INTERNAL FILE SYSTEM", "LOCAL DATABASE"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                values);
        spinner.setAdapter(adapter);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*tvDate.setText("Date: 2022-10-28");
                etEUR.setText("4.91");
                etUSD.setText("5.21");
                etGBP.setText("5.55");
                etXAU.setText("232.71");
                Toast.makeText(getApplicationContext(), "FX rates displayed succesfully!",
                        Toast.LENGTH_LONG).show();*/
                Network network = new Network()
                {
                    @Override
                    protected void onPostExecute(InputStream inputStream) {

                        tvDate.setText(fxRate.getDate());
                        etEUR.setText(fxRate.getEuro());
                        etUSD.setText(fxRate.getDolar());
                        etGBP.setText(fxRate.getPound());
                        etXAU.setText(fxRate.getGold());
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

                FXRate fxRate = new FXRate(tvDate.getText().toString(),
                        etEUR.getText().toString(), etUSD.getText().toString(),
                        etGBP.getText().toString(), etXAU.getText().toString());

                if(position==0)
                {
                    //save to internal file
                    try {
                        writeFXRateToFile("file.dat", fxRate);
                        fxRate = null;
                        fxRate = readFXRateFromFile("file.dat");
                        Toast.makeText(getApplicationContext(), fxRate.toString(),
                                Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    //save to database
                    FXRatesDB database = FXRatesDB.getInstance(getApplicationContext());
                    database.getFxRatesDao().insert(fxRate);
                    Toast.makeText(getApplicationContext(),
                            "Saved in Room database!", Toast.LENGTH_LONG).show();
                    writeFXRateInFirebase(fxRate);
                    Toast.makeText(getApplicationContext(),
                            "Saved in Firebase database!", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void writeFXRateToFile(String fileName, FXRate fxRate) throws IOException {

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

    private FXRate readFXRateFromFile(String fileName) throws IOException
    {
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

    private void writeFXRateInFirebase(FXRate fxRate) {
        DatabaseReference myRef = firebaseDatabase.getReference("https://androidsecurity-e3745-default-rtdb");
        myRef.child("androidsecurity-e3745-default-rtdb").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fxRate.setUid(myRef.child("androidsecurity-e3745-default-rtdb").push().getKey());
                myRef.child("androidsecurity-e3745-default-rtdb").child(fxRate.getUid()).setValue(fxRate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.e("lifecycle", "call of the method onStart()");

        Intent intent = getIntent();
        FXRate fxRate = (FXRate) intent.getSerializableExtra("fxRate");
        tvDate.setText(fxRate.getDate());
        etEUR.setText(fxRate.getEuro());
        etUSD.setText(fxRate.getDolar());
        etGBP.setText(fxRate.getPound());
        etXAU.setText(fxRate.getGold());
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
        menu.add(0, VIEWDB, 2, "View database");
        menu.add(0, EXIT, 3, "Close the application");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case SMSENCRYPT:
                Intent intent = new Intent(this, SMSEncryptActivity.class);
                startActivity(intent);
                break;
            case JSON:
                Intent intent1 = new Intent(getApplicationContext(), JSONActivity.class);
                startActivity(intent1);
                break;
            case VIEWDB:
                Intent intent2 = new Intent(getApplicationContext(), ViewDBActivity.class);
                startActivity(intent2);
                break;
            case EXIT:
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
        }

        return false;
    }
}