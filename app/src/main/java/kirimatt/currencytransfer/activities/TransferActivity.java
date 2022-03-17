package kirimatt.currencytransfer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

import kirimatt.currencytransfer.R;
import kirimatt.currencytransfer.daos.CurrencyDAO;
import kirimatt.currencytransfer.interfaces.ITextWatcher;

public class TransferActivity extends AppCompatActivity {

    private final DecimalFormat FORMATTER = new DecimalFormat("#.###");
    private TextView textViewCurrency;
    private TextView textViewDescription;
    private Button buttonTransfer;
    private EditText editTextRub;
    private EditText editTextCurrency;
    private boolean isLastCurrency;
    private CurrencyDAO currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        Intent intent = getIntent();
        currency = (CurrencyDAO) intent.getSerializableExtra(MainActivity.EXTRA_CURRENCY);

        setTitle(currency.getName());

        textViewCurrency = findViewById(R.id.textViewCurrency);
        textViewCurrency.setText(currency.getCharCode());

        textViewDescription = findViewById(R.id.textViewDescription);

        String nominal = FORMATTER.format(currency.getValue() / currency.getPrevious()
                * 100 / currency.getPrevious());

        String description = "Номинал: " + currency.getNominal() + "  "
                + (currency.getPrevious() > currency.getValue() ? "↓" : "↑") + nominal + "%";

        textViewDescription.setText(description);

        editTextRub = findViewById(R.id.editTextNumberDecimalRub);
        editTextRub.addTextChangedListener(
                (ITextWatcher) (charSequence, i, i1, i2) -> {
                    if (editTextRub.length() > 0) {
                        isLastCurrency = false;
                    }
                }
        );

        editTextCurrency = findViewById(R.id.editTextNumberDecimalCurrency);
        editTextCurrency.addTextChangedListener(
                (ITextWatcher) (charSequence, i, i1, i2) -> {
                    if (editTextCurrency.length() > 0) {
                        isLastCurrency = true;
                    }
                }
        );

        buttonTransfer = findViewById(R.id.buttonTransfer);
        buttonTransfer.setOnClickListener(onClick -> {
            if (editTextCurrency.length() == 0 && editTextRub.length() == 0)
                return;

            if (isLastCurrency) {
                currencyToRub();
            } else {
                rubToCurrency();
            }
        });
    }

    public void currencyToRub() {

        if (editTextCurrency.length() == 0) {
            rubToCurrency();
        }

        Double currencyCount = Double.valueOf(editTextCurrency.getText().toString());

        editTextRub.setText(FORMATTER.format(currencyCount * currency.getValue()));
    }

    public void rubToCurrency() {

        if (editTextRub.length() == 0) {
            currencyToRub();
        }

        Double rubCount = Double.valueOf(editTextRub.getText().toString());

        editTextCurrency.setText(FORMATTER.format(rubCount / currency.getValue()));
    }
}