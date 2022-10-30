package com.example.ism2022;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.List;

public class ViewDBActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HorizontalScrollView sv = new HorizontalScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ListView lv = new ListView(this);
        FXRatesDB database = FXRatesDB.getInstance(getApplicationContext());
        List<FXRate> fxRates = database.getFxRatesDao().getAll();
        ArrayAdapter<FXRate> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, fxRates);
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                FXRate fxRate = fxRates.get(position);
                ArrayAdapter adapter1 = (ArrayAdapter) lv.getAdapter();
                AlertDialog dialog =  new AlertDialog.Builder(ViewDBActivity.this)
                        .setTitle("Delete confirmation")
                        .setMessage("Are you sure?")
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(),
                                        "Nothing deleted",Toast.LENGTH_LONG).show();
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fxRates.remove(fxRate);
                                FXRatesDB fxRatesDB = FXRatesDB.getInstance(getApplicationContext());
                                fxRatesDB.getFxRatesDao().delete(fxRate);
                                adapter1.notifyDataSetChanged();
                                dialogInterface.cancel();
                            }
                        })
                        .create();
                dialog.show();
                return true;
            }
        });
        TextView tv1 = new TextView(this);
        tv1.setText("List of FX rates from the database:\n");
        TextView tv2 = new TextView(this);
        tv2.setText("DATE   EUR USD GBP XAU \n");
        ll.addView(tv1);
        ll.addView(tv2);
        ll.addView(lv);
        sv.addView(ll);
        setContentView(sv);

    }
}
