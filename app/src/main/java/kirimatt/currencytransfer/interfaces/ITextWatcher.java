package kirimatt.currencytransfer.interfaces;

import android.text.Editable;
import android.text.TextWatcher;

@FunctionalInterface
public interface ITextWatcher extends TextWatcher {

    default void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    default void afterTextChanged(Editable editable) {
    }

    void onTextChanged(CharSequence charSequence, int i, int i1, int i2);
}
