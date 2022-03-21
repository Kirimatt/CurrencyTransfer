package kirimatt.currencytransfer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import kirimatt.currencytransfer.R;
import kirimatt.currencytransfer.daos.JsonDao;
import kirimatt.currencytransfer.utils.ParseJson;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_CURRENCY = "kirimatt.currencyTransfer.EXTRA_CURRENCY";
    private JsonDao jsonDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(
                (parent, view, position, id) -> openTransferActivity(position)
        );

        Button buttonRefresh = findViewById(R.id.buttonRefresh);
        buttonRefresh.setOnClickListener(view -> {

            jsonDao = ParseJson.loadJSONFromURL(this, listView);

            Toast.makeText(
                    getApplicationContext(),
                    R.string.refreshed,
                    Toast.LENGTH_SHORT
            ).show();
        });

        jsonDao = ParseJson.loadJsonFromSharedPreferences(
                this,
                listView,
                false
        );

    }

    public void openTransferActivity(int position) {
        if (jsonDao != null) {
            Intent intent = new Intent(this, TransferActivity.class);
            intent.putExtra(
                    EXTRA_CURRENCY,
                    new ArrayList<>(jsonDao.getCurrencyMap().values()).get(position)
            );
            startActivity(intent);
        }
    }


}