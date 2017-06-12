package com.exemple.converter;

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
 * Classic converter between currencies
 * Created by Claudiu on 23-Nov-16.
 */

public class ClassicConverter extends AppCompatActivity {

    private Spinner mSpinnerFrom;
    private Spinner mSpinnerTo;
    private EditText mEditText;
    private TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classic_converter);

        Button button =  (Button) this.findViewById(R.id.convertClassic);

        //array for populating the currency dropdowns
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency, R.layout.spinner_item);

        mResult = (TextView) this.findViewById(R.id.resultClassicConverter);
        mEditText = (EditText) this.findViewById(R.id.editTextFrom);

        mSpinnerFrom = (Spinner) findViewById(R.id.spinnerFrom);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerFrom.setAdapter(adapter);
        mSpinnerFrom.setSelection(24); //default position for dropdown: RON

        mSpinnerTo = (Spinner) findViewById(R.id.spinnerTo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTo.setAdapter(adapter);
        mSpinnerTo.setSelection(8); //default position for dropdown: EUR

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                convert();
            }
        });
    }

    private void convert() {
        double amount = 0;
        if (!mEditText.getText().toString().equals("")) {
            amount = Double.parseDouble(mEditText.getText().toString());
            }
        String fromCurrency = mSpinnerFrom.getSelectedItem().toString();
        String toCurrency = mSpinnerTo.getSelectedItem().toString();

        String currencySymbol = Converter.getCurrencySymbolFromCurrencyCode(toCurrency);

        Converter converter = new Converter(amount, fromCurrency, toCurrency);

        if (fromCurrency == toCurrency) {
            mResult.setText("Same currency");
        } else if(amount == 0.0){
            mResult.setText("Wrong amount");
        }else{

            Thread t = new Thread(converter);
            t.start();
            while (t.isAlive()) {
            }

            String value = Double.toString(converter.getResult());
            mResult.setText(value + " " + currencySymbol);
        }

    }

}