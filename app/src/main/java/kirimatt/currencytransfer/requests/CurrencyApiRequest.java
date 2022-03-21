package kirimatt.currencytransfer.requests;

import static kirimatt.currencytransfer.CurrencyApp.GSON;
import static kirimatt.currencytransfer.utils.ParseJson.loadJsonFromSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import kirimatt.currencytransfer.CurrencyApp;
import kirimatt.currencytransfer.R;
import kirimatt.currencytransfer.daos.JsonDao;
import kirimatt.currencytransfer.utils.ParseJson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyApiRequest {

    private CurrencyApiRequest() {

    }

    public static void getJsonDao(AppCompatActivity appCompatActivity, ListView listView) {

        CurrencyApp.getCurrencyApi().getJsonDto().enqueue(new Callback<JsonDao>() {
            @Override
            public void onResponse(@NonNull Call<JsonDao> call,
                                   @NonNull Response<JsonDao> response) {

                if (response.isSuccessful()) {
                    final ProgressBar progressBar = appCompatActivity.findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.VISIBLE);

                    CurrencyApp.setJsonDao(response.body());

                    //set shared data json of currencies
                    SharedPreferences sharedPref = appCompatActivity
                            .getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(
                            appCompatActivity.getString(R.string.jsonCurrency),
                            GSON.toJson(CurrencyApp.getJsonDao())
                    );
                    editor.apply();

                    ParseJson.generateListView(CurrencyApp.getJsonDao(), appCompatActivity, listView);

                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    failure();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonDao> call, @NonNull Throwable t) {
                failure();
            }

            public void failure() {
                Toast.makeText(
                        appCompatActivity.getApplicationContext(),
                        R.string.connectionError,
                        Toast.LENGTH_SHORT
                ).show();

                loadJsonFromSharedPreferences(
                        appCompatActivity,
                        listView,
                        true
                );
            }
        });

    }
}
