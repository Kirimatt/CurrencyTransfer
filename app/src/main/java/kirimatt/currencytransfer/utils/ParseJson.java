package kirimatt.currencytransfer.utils;

import static kirimatt.currencytransfer.CurrencyApp.GSON;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import kirimatt.currencytransfer.CurrencyApp;
import kirimatt.currencytransfer.R;
import kirimatt.currencytransfer.adapters.ListViewAdapter;
import kirimatt.currencytransfer.daos.CurrencyDao;
import kirimatt.currencytransfer.daos.JsonDao;
import kirimatt.currencytransfer.requests.CurrencyApiRequest;

public class ParseJson {

    private ParseJson() {
    }

    public static void loadJsonFromSharedPreferences(AppCompatActivity appCompatActivity,
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

            loadJSONFromURL(appCompatActivity, listView);
            return;
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


            loadJSONFromURL(appCompatActivity, listView);
            return;
        }

        //get list of currencies
        generateListView(jsonDao, appCompatActivity, listView);

        CurrencyApp.setJsonDao(jsonDao);

        progressBar.setVisibility(View.INVISIBLE);
    }

    public static void loadJSONFromURL(AppCompatActivity appCompatActivity, ListView listView) {

        CurrencyApiRequest.getJsonDao(appCompatActivity, listView);

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
