package com.example.ism2022;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.math.BigInteger;

public class SMSEncryptActivity extends AppCompatActivity {
BigInteger cyphertext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsencrypt);

        EditText etText = findViewById(R.id.textToEncrypt);
        EditText phoneNo = findViewById(R.id.phoneNumber);
        Spinner spinner = findViewById(R.id.spinnerContacts);
        Button btnEncrypt = findViewById(R.id.btnEncrypt);
        Button btnSend = findViewById(R.id.btnSend);

        String[] contacts = {"1 Gigel 0721345678", "2 Dorel 0764765770"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                contacts);
        spinner.setAdapter(adapter);

        btnSend.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phoneNumber = phoneNo.getText().toString();
                        if (phoneNumber.equals("")) {
                            String[] arr = spinner.getSelectedItem().toString().split(" ");
                            phoneNumber = arr[arr.length - 1];
                            phoneNo.setText(phoneNumber);
                        }

                        RSA rsa = new RSA(1024);
                        String text = etText.getText().toString();
                        if (text.equals("")) {
                            Toast.makeText(getApplicationContext(), "Text is empty",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            BigInteger plainText = new BigInteger(text.getBytes());
                            cyphertext = rsa.encrypt(plainText);
                            Toast.makeText(getApplicationContext(), cyphertext.toString(),
                                    Toast.LENGTH_LONG).show();
                        }

                        if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(SMSEncryptActivity.this, new String[]{Manifest.permission.SEND_SMS},0);
                        }
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber,null,
                                cyphertext.toString(),null,null);
                        Toast.makeText(getApplicationContext(), cyphertext.toString(),
                                Toast.LENGTH_LONG).show();

                    }
                }
        );
    }
}