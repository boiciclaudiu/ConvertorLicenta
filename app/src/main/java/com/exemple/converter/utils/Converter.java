package com.exemple.converter.utils;

import android.content.Context;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Class used for converting betweeen currencies
 * Created by Celo on 05.01.2017.
 */

public class Converter implements Runnable {
    private double mAmount;
    private String mFrom;
    private String mTo;
    private double mExchangeRate;

    public Converter(double amount, String from, String to){
        mAmount = amount;
        mFrom = from;
        mTo = to;
    }

    public void run(){
        String urlString = "http://api.fixer.io/latest?base=" + mFrom + "&symbols=" + mTo;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());

            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            String value = extractExchangeRate(total, mFrom, mTo);
            mExchangeRate = Double.parseDouble(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }
    }

    public double getResult(){
        double result = mExchangeRate * mAmount;
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(result));
    }


    private String extractExchangeRate(StringBuilder jason, String from, String to){
        String intermediate = jason.substring(jason.indexOf(to));
        String value = intermediate.substring((from.length() + 2), (intermediate.length() - 4));
        return value;
    }

    public static String getCurrencyCode(String country){
        Locale locale = new Locale("", country);
        Currency currency = Currency.getInstance(locale);
        return currency.getCurrencyCode();
    }

    public static String getCurrencySymbolFromCountry(String country){
        Locale locale = new Locale("", country);
        Currency currency = Currency.getInstance(locale);
        return currency.getSymbol();
    }

    public static String getCurrencySymbolFromCurrencyCode(String currencyCode){
        Currency currency = Currency.getInstance(currencyCode);
        return currency.getSymbol();
    }

    @NonNull
    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "RO";
    }


}
