package com.example.stefanini.challengevanhack.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.stefanini.challengevanhack.model.Customer;
import com.example.stefanini.challengevanhack.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Herson Rodrigues <hersonrodrigues@gmail.com> on 17/03/2018.
 */

public class SharedPrefs {

    private static final String TOKEN = "token";
    private static final String CART = "cart";
    private static final String COSTUMER = "costumer";

    public static void saveToken(Context context, String token) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public static String recoveryToken(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = preferences.getString(TOKEN, null);
        return token;
    }

    public static void saveProductsInCart(Context context, List<Product> cartProducts) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(CART, ConvertTypes.fromListProduct(cartProducts));
        editor.commit();
    }

    public static List<Product> getProductsInCart(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String data = preferences.getString(CART, null);
        ArrayList<Product> list = new ArrayList<>();
        if (data != null) {
            list = ConvertTypes.toListProduct(data);
        }
        return list;
    }

    public static void saveCustomer(Context context, Customer customer) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(COSTUMER, ConvertTypes.fromCustomer(customer));
        editor.commit();
    }

    public static Customer getCustomer(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String data = preferences.getString(COSTUMER, null);
        if (data != null) {
            Customer costumer = ConvertTypes.toCustomer(data);
            return costumer;
        }
        return null;
    }
}
