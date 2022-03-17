package kirimatt.currencytransfer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import kirimatt.currencytransfer.R;
import kirimatt.currencytransfer.daos.CurrencyDAO;
import kirimatt.currencytransfer.utils.ParseJson;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_CURRENCY = "kirimatt.currencyTransfer.EXTRA_CURRENCY";
    public static final String JSON_URL = "https://www.cbr-xml-daily.ru/daily_json.js";
    private final List<CurrencyDAO> currencyList = new ArrayList<>();
    private ListView listView;
    private Button buttonRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(
                (parent, view, position, id) -> openTransferActivity(position)
        );

        buttonRefresh = findViewById(R.id.buttonRefresh);
        buttonRefresh.setOnClickListener(view -> {
            currencyList.clear();

            ParseJson.loadJsonFromSharedPreferences(this, currencyList, listView);

            Toast.makeText(
                    getApplicationContext(),
                    R.string.refreshed,
                    Toast.LENGTH_SHORT
            ).show();
        });

        ParseJson.loadJsonFromSharedPreferences(this, currencyList, listView);
    }

    public void openTransferActivity(int position) {
        Intent intent = new Intent(this, TransferActivity.class);
        intent.putExtra(EXTRA_CURRENCY, currencyList.get(position));
        startActivity(intent);
    }


}