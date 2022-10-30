package com.example.ism2022;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.math.BigInteger;

public class SMSEncryptActivity extends AppCompatActivity {

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
                android.support.constraint.R.layout.support_simple_spinner_dropdown_item,
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
                            BigInteger cypherText = rsa.encrypt(plainText);
                            Toast.makeText(getApplicationContext(), cypherText.toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}