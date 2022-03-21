package kirimatt.currencytransfer.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import kirimatt.currencytransfer.R;
import kirimatt.currencytransfer.adapters.ListViewAdapter;
import kirimatt.currencytransfer.daos.CurrencyDao;
import kirimatt.currencytransfer.daos.JsonDao;
import kirimatt.currencytransfer.services.CurrencyService;

public class ParseJson {

    private static final Gson GSON = new Gson();

    private ParseJson() {
    }

    public static JsonDao loadJsonFromSharedPreferences(AppCompatActivity appCompatActivity,
                                                        ListView listView, boolean isConnectionError) {
        final ProgressBar progressBar = appCompatActivity.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences sharedPref = appCompatActivity.getPreferences(Context.MODE_PRIVATE);
        String defaultValue = appCompatActivity.getResources().getString(R.string.jsonCurrency);
        String jsonValue = sharedPref.getString(
                appCompatActivity.getString(R.string.jsonCurrency),
                defaultValue
        );

        JsonDao jsonDao = GSON.fromJson(jsonValue, JsonDao.class);

        //step 1: check shared preferences to contains data
        if ((jsonValue == null || jsonValue.isEmpty()) && !isConnectionError) {
            progressBar.setVisibility(View.INVISIBLE);

            return loadJSONFromURL(appCompatActivity, listView);
        }


        //step 2: check date of shared data to expire
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ssZ",
                Locale.ROOT
        );

        Date date = null;

        try {
            date = dateFormat.parse(jsonDao.getDate());
        } catch (ParseException | NullPointerException e) {

            Toast.makeText(
                    appCompatActivity.getApplicationContext(),
                    R.string.parseDateException,
                    Toast.LENGTH_SHORT
            ).show();

        }

        //24 hours expire and no connection error
        if ((date == null || TimeUnit.MILLISECONDS
                .toHours(System.currentTimeMillis() - date.getTime()) > 0)
                && !isConnectionError) {

            progressBar.setVisibility(View.INVISIBLE);

            Toast.makeText(
                    appCompatActivity.getApplicationContext(),
                    "Время действия списка валют истекло, обновление...",
                    Toast.LENGTH_SHORT
            ).show();


            return loadJSONFromURL(appCompatActivity, listView);
        }

        //get list of currencies
        generateListView(jsonDao, appCompatActivity, listView);

        progressBar.setVisibility(View.INVISIBLE);

        return jsonDao;
    }

    public static JsonDao loadJSONFromURL(AppCompatActivity appCompatActivity, ListView listView) {

        final ProgressBar progressBar = appCompatActivity.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        JsonDao jsonDao = CurrencyService.getJsonDao();

        if (jsonDao.getCurrencyMap() == null) {
            Toast.makeText(
                    appCompatActivity.getApplicationContext(),
                    "Ошибка при подключении",
                    Toast.LENGTH_SHORT
            ).show();

            return loadJsonFromSharedPreferences(appCompatActivity, listView, true);
        }

        //set shared data json of currencies
        SharedPreferences sharedPref = appCompatActivity
                .getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(
                appCompatActivity.getString(R.string.jsonCurrency),
                GSON.toJson(jsonDao)
        );
        editor.apply();

        ParseJson.generateListView(jsonDao, appCompatActivity, listView);

        progressBar.setVisibility(View.INVISIBLE);

        return jsonDao;
    }

    public static void generateListView(JsonDao jsonDao, AppCompatActivity appCompatActivity,
                                        ListView listView) {

        Map<String, CurrencyDao> currencyMap = jsonDao.getCurrencyMap();

        ListAdapter adapter = new ListViewAdapter(
                appCompatActivity.getApplicationContext(),
                R.layout.row,
                R.id.textViewName,
                new ArrayList<>(currencyMap.values())
        );

        listView.setAdapter(adapter);
    }
}
