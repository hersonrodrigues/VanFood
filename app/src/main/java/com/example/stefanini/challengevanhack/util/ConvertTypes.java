package com.example.stefanini.challengevanhack.util;

import android.arch.persistence.room.TypeConverter;

import com.example.stefanini.challengevanhack.model.Customer;
import com.example.stefanini.challengevanhack.model.Product;
import com.example.stefanini.challengevanhack.model.Store;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Herson Rodrigues <hersonrodrigues@gmail.com> on 17/03/2018.
 */

public class ConvertTypes {

    public static ArrayList<Product> toListProduct(String value) {
        Type listType = new TypeToken<ArrayList<Product>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    public static String fromListProduct(List<Product> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    public static ArrayList<Store> toListStore(String value) {
            Type listType = new TypeToken<ArrayList<Store>>() {
            }.getType();
            return new Gson().fromJson(value, listType);
        }

    public static String fromCustomer(Customer customer) {
        Gson gson = new Gson();
        String json = gson.toJson(customer);
        return json;
    }

    public static Customer toCustomer(String value) {
        Type listType = new TypeToken<Customer>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }
}
