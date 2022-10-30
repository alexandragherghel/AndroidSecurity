package com.example.ism2022;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class JSONActivity extends ListActivity {

    public static final String EMPTY = "";
    private ProgressDialog pDialog;

    public static final String TAG_MOVIES = "movies";
    public static final String TAG_ID = "id";
    public static final String TAG_TITLE = "title";
    public static final String TAG_DURATION = "duration";
    public static final String TAG_RELEASE = "release";

    JSONArray movies = null;
    ArrayList<HashMap<String, String>> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonactivity);

        movieList = new ArrayList<>();
        URL url = null;
        try {
            url = new URL("http://movio.biblacad.ro/movies.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public interface OnTaskExecutionFinished {
        void onTaskFinishedEvent(String result);

    }

    public class GetMovies extends AsyncTask<URL, Void, String> {

        private OnTaskExecutionFinished event;

        public void setOnTaskFinishedEvent(OnTaskExecutionFinished _event) {
            if (_event != null) {
                this.event = _event;
            }
        }

        @Override
        protected String doInBackground(URL... urls) {

            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) urls[0].openConnection();
                conn.setRequestMethod("GET");
                InputStream ist = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(ist);
                BufferedReader br = new BufferedReader(isr);
                String buffer = EMPTY;
                String newLine = EMPTY;
                while((newLine = br.readLine())!=null)
                    buffer+= newLine+"\n";
                //parsing of json array
                return buffer;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}