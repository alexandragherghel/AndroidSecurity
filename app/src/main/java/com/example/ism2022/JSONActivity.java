package com.example.ism2022;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public static final String TAG_MOVIES = "movies";
    public static final String TAG_ID = "id";
    public static final String TAG_TITLE = "title";
    public static final String TAG_DURATION = "duration";
    public static final String TAG_RELEASE = "release";
    JSONArray movies = null;
    ArrayList<HashMap<String, String>> movieList;
    private ProgressDialog pDialog;

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

        GetMovies m = new GetMovies();

        m.setOnTaskFinishedEvent(new OnTaskExecutionFinished() {
            @Override
            public void onTaskFinishedEvent(String result) {
                if (pDialog.isShowing()) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pDialog.dismiss();
                }
                ListAdapter adapter = new SimpleAdapter(JSONActivity.this,
                        movieList, R.layout.list_item,
                        new String[]{TAG_TITLE, TAG_RELEASE, TAG_DURATION},
                        new int[]{R.id.title, R.id.release, R.id.duration});
                setListAdapter(adapter);
            }
        });
        m.execute(url);
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
                while ((newLine = br.readLine()) != null)
                    buffer += newLine + "\n";
                parseJSONObject(buffer);
                return buffer;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (this.event != null) {
                this.event.onTaskFinishedEvent(result);
            } else {
                Log.d("GetMovies", "task event is null!");
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(JSONActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        public void parseJSONObject(String jsonStr) throws JSONException {
            if (jsonStr != null) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                movies = jsonObject.getJSONArray(TAG_MOVIES);
                for (int i = 0; i < movies.length(); i++) {
                    JSONObject object = movies.getJSONObject(i);
                    String id = object.getString(TAG_ID);
                    String title = object.getString(TAG_TITLE);
                    String duration = object.getString(TAG_DURATION);
                    String release = object.getString(TAG_RELEASE);

                    HashMap<String, String> movie = new HashMap<>();
                    movie.put(TAG_ID, id);
                    movie.put(TAG_TITLE, title);
                    movie.put(TAG_DURATION, duration);
                    movie.put(TAG_RELEASE, release);

                    movieList.add(movie);
                }
            } else {
                Log.e("GetMovies", "JSON string is null");
            }
        }
    }
}