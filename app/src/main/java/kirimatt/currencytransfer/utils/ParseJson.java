package kirimatt.currencytransfer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import kirimatt.currencytransfer.R;
import kirimatt.currencytransfer.activities.MainActivity;
import kirimatt.currencytransfer.adapters.ListViewAdapter;
import kirimatt.currencytransfer.daos.CurrencyDAO;

public class ParseJson {

    public static Map parseJsonToMap(String s) {
        Gson gson = new Gson();
        return gson.fromJson(s, Map.class);
    }

    public static void loadJsonFromSharedPreferences(AppCompatActivity appCompatActivity,
                                                     List<CurrencyDAO> currencyList,
                                                     ListView listView) {
        final ProgressBar progressBar = appCompatActivity.findViewById(R.id.progressBar);
        progressBar.setVisibility(ListView.VISIBLE);

        SharedPreferences sharedPref = appCompatActivity.getPreferences(Context.MODE_PRIVATE);
        String defaultValue = appCompatActivity.getResources().getString(R.string.jsonCurrency);
        String jsonValue = sharedPref.getString(
                appCompatActivity.getString(R.string.jsonCurrency),
                defaultValue
        );

        Map map = ParseJson.parseJsonToMap(jsonValue);

        //step 1: check shared preferences to contains data
        if (jsonValue == null || jsonValue.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);

            loadJSONFromURL(MainActivity.JSON_URL, appCompatActivity, currencyList, listView);
            return;
        }


        //step 2: check date of shared data to expire
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ssZ",
                Locale.ROOT
        );

        Date date = null;

        try {
            date = dateFormat.parse((String) Objects.requireNonNull(map.get("Timestamp")));
        } catch (ParseException e) {

            Toast.makeText(
                    appCompatActivity.getApplicationContext(),
                    R.string.parseDateException,
                    Toast.LENGTH_SHORT
            ).show();

        }

        //24 hours expire
        if (date == null || TimeUnit.MILLISECONDS
                .toHours(System.currentTimeMillis() - date.getTime()) > 24) {

            progressBar.setVisibility(View.INVISIBLE);

            loadJSONFromURL(MainActivity.JSON_URL, appCompatActivity, currencyList, listView);
            return;
        }

        //get list of currencies
        Map valuteMap = (Map) map.get(appCompatActivity.getString(R.string.currencyInRussian));

        for (Object s : Objects.requireNonNull(valuteMap).keySet()) {
            Map bufferMap = (Map) valuteMap.get(s);
            currencyList.add(new CurrencyDAO(Objects.requireNonNull(bufferMap)));
        }

        ListAdapter adapter = new ListViewAdapter(
                appCompatActivity.getApplicationContext(),
                R.layout.row,
                R.id.textViewName,
                currencyList
        );
        listView.setAdapter(adapter);

        progressBar.setVisibility(View.INVISIBLE);
    }

    public static void loadJSONFromURL(String url, AppCompatActivity appCompatActivity,
                                       List<CurrencyDAO> currencyList, ListView listView) {
        final ProgressBar progressBar = appCompatActivity.findViewById(R.id.progressBar);
        progressBar.setVisibility(ListView.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    progressBar.setVisibility(View.INVISIBLE);

                    //set shared data json of currencies
                    SharedPreferences sharedPref = appCompatActivity
                            .getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(appCompatActivity.getString(R.string.jsonCurrency), response);
                    editor.apply();

                    Map map = ParseJson.parseJsonToMap(response);
                    Map valuteMap = (Map) map
                            .get(appCompatActivity.getString(R.string.currencyInRussian));

                    for (Object s : Objects.requireNonNull(valuteMap).keySet()) {
                        Map bufferMap = (Map) valuteMap.get(s);
                        currencyList.add(new CurrencyDAO(Objects.requireNonNull(bufferMap)));
                    }

                    ListAdapter adapter = new ListViewAdapter(
                            appCompatActivity.getApplicationContext(),
                            R.layout.row,
                            R.id.textViewName,
                            currencyList
                    );
                    listView.setAdapter(adapter);
                },
                error -> Toast.makeText(
                        appCompatActivity.getApplicationContext(),
                        error.getMessage(),
                        Toast.LENGTH_SHORT
                ).show()
        );

        RequestQueue requestQueue = Volley.newRequestQueue(appCompatActivity);
        requestQueue.add(stringRequest);
    }
}
