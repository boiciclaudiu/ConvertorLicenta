package com.exemple.converter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.exemple.converter.utils.Converter;

/**
 * Converts the input to local currency
 * Created by Claudiu on 23-Nov-16.
 */

public class LocationBasedConverter extends AppCompatActivity{

    private EditText mEditText;
    private Spinner mSpinner;
    private TextView mResult;
    private String mCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.location_based_converter);

        Button button = (Button) this.findViewById(R.id.convertLocationBased);

        //array for populating the currency dropdown
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency, R.layout.spinner_item);

        mEditText = (EditText) this.findViewById(R.id.editText);
        mResult = (TextView) this.findViewById(R.id.textView);
        mSpinner = (Spinner) this.findViewById(R.id.spinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(8); //default position for dropdown: EUR

        Context context = getBaseContext();
        String country = Converter.getUserCountry(context);
        final String currencySymbol = Converter.getCurrencySymbolFromCountry(country);
        mCurrency = Converter.getCurrencyCode(country);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                convert(currencySymbol);
            }
        });

        Intent intent = getIntent();
        String value = intent.getStringExtra("value");
        if(value != null) {
            convert(currencySymbol, value);
        }
    }

    private void convert(String currencySimbol) {
        double amount = 0;
        if (!mEditText.getText().toString().equals("")) {
            amount = Double.parseDouble(mEditText.getText().toString());
        }
        String wantedCurrency = mSpinner.getSelectedItem().toString();
        Converter converter = new Converter(amount, wantedCurrency, mCurrency);

        if (wantedCurrency == mCurrency) {
            mResult.setText("Same currency");
        } else if(amount == 0.0){
            mResult.setText("Wrong amount");
        } else {
            Thread t = new Thread(converter);
            t.start();
            while (t.isAlive()) {
            }

            String value = Double.toString(converter.getResult());
            mResult.setText(value + " " + currencySimbol);
        }
    }

    private void convert(String currencySimbol, String stringValue){
        mEditText.setText(stringValue);
        double amount = Double.parseDouble(stringValue);
        String wantedCurrency = mSpinner.getSelectedItem().toString();
        Converter converter = new Converter(amount, wantedCurrency, mCurrency);

        if (wantedCurrency == mCurrency) {
            mResult.setText("Same currency");
        } else if(amount == 0.0){
            mResult.setText("Wrong amount");
        } else {
            Thread t = new Thread(converter);
            t.start();
            while (t.isAlive()) {
            }

            String value = Double.toString(converter.getResult());
            mResult.setText(value + " " + currencySimbol);
        }
    }
}