package kirimatt.currencytransfer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kirimatt.currencytransfer.R;
import kirimatt.currencytransfer.daos.CurrencyDao;

public class ListViewAdapter extends ArrayAdapter<CurrencyDao> {
    private final int listLayout;
    private final List<CurrencyDao> currencyList;
    private final Context context;

    public ListViewAdapter(Context context, int listLayout,
                           int field, List<CurrencyDao> currencyList) {
        super(context, listLayout, field, currencyList);
        this.context = context;
        this.listLayout = listLayout;
        this.currencyList = currencyList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listViewItem = inflater.inflate(listLayout, null, false);
            TextView name = listViewItem.findViewById(R.id.textViewName);
            TextView charsName = listViewItem.findViewById(R.id.textViewCharsName);
            name.setText(currencyList.get(position).getName());
            charsName.setText(currencyList.get(position).getCharCode());

            return listViewItem;
        } else {
            return convertView;
        }
    }

    @Override
    public CurrencyDao getItem(int position) {
        return currencyList.get(position);
    }

}