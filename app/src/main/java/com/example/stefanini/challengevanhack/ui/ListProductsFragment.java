package com.example.stefanini.challengevanhack.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stefanini.challengevanhack.MainActivity;
import com.example.stefanini.challengevanhack.R;
import com.example.stefanini.challengevanhack.api.RestApi;
import com.example.stefanini.challengevanhack.model.Customer;
import com.example.stefanini.challengevanhack.model.Order;
import com.example.stefanini.challengevanhack.model.OrderItem;
import com.example.stefanini.challengevanhack.model.Product;
import com.example.stefanini.challengevanhack.model.Store;
import com.example.stefanini.challengevanhack.ui.adapter.ProductAdapter;
import com.example.stefanini.challengevanhack.util.SharedPrefs;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Herson Rodrigues <hersonrodrigues@gmail.com> on 17/03/2018.
 */

public class ListProductsFragment extends Fragment {

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiSGVyc29uIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvZW1haWxhZGRyZXNzIjoiaGVyc29uZGZAZ21haWwuY29tIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy91c2VyZGF0YSI6IjY1IiwibmJmIjoxNTIxMzI0OTY1LCJleHAiOjE1MjM3NDQxNjUsImlzcyI6InZhbmhhY2stc2FvcGF1bG8tZmFpci1hcGkiLCJhdWQiOiJ2YW5oYWNrLXNhb3BhdWxvLWZhaXItYXBpIn0.vgTGry-2PjBPO2kI6Apr-bl0sWXguQW8N1PhhfGixr4";
    private View mRootView;
    private Context mContext;
    private MainActivity mActivity;
    private boolean isProductsFromCart;
    private Store mStore;
    private Customer mCustomer;
    private List<Product> mListProducts;
    private ProductAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_logged, container, false);
        mContext = getActivity().getApplicationContext();
        mActivity = (MainActivity) getActivity();
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        isProductsFromCart = getArguments().getBoolean(MainActivity.PRODUCTS_FROM_CART);
        mStore = (Store) getArguments().getSerializable(MainActivity.PRODUCTS_BY_STORE);
        mCustomer = SharedPrefs.getCustomer(mContext);

        init();

        return mRootView;
    }

    private void init() {
        if (isProductsFromCart) {
            mListProducts = SharedPrefs.getProductsInCart(mContext);
            setHasOptionsMenu(false);
            renderOrder(mListProducts);
            renderCart(mListProducts);
            renderUI();
        } else {

            if (mListProducts != null) {
                renderUI();
            } else {
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage(getString(R.string.sending_data_message));
                dialog.show();

                RestApi.get().api(mContext).listProducts().enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        dialog.dismiss();
                        if (response.code() == 200) {
                            mListProducts = response.body();
                            mListProducts = filterProducts(mListProducts);
                            renderUI();
                        } else {
                            Toast.makeText(mContext, getString(R.string.api_error, response.message()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Toast.makeText(mContext, getString(R.string.api_error, t.getMessage()), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    private void renderUI() {
        mAdapter = new ProductAdapter(mActivity, mListProducts, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mActivity.invalidateOptionsMenu();
            }
        });

        final RecyclerView recyclerView = mRootView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);
    }

    private List<Product> filterProducts(List<Product> data) {
        if (mStore != null) {
            getActivity().setTitle(getString(R.string.products_by, mStore.getName()));
        } else {
            getActivity().setTitle(getString(R.string.products));
        }
        return filterByStore(data);
    }

    private void renderCart(final List<Product> data) {
        getActivity().setTitle(getString(R.string.cart));

        View buiBoxView = mRootView.findViewById(R.id.buy_box);
        buiBoxView.setVisibility(View.VISIBLE);

        View buyButton = mRootView.findViewById(R.id.buy_button);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage(getString(R.string.sending_data_message));
                dialog.show();

                Order order = createOrder(data);

                RestApi.get().api(mContext).submitStore(SharedPrefs.recoveryToken(mContext), order).enqueue(new Callback<JsonObject>() {

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        dialog.dismiss();
                        if (response.code() == 200) {
                            Toast.makeText(mContext, getString(R.string.api_error, response.message()), Toast.LENGTH_SHORT).show();
                            SharedPrefs.saveProductsInCart(mContext, new ArrayList<Product>());
                        } else {
                            Toast.makeText(mContext, getString(R.string.api_error, response.message()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(mContext, getString(R.string.api_error, t.getMessage()), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void renderOrder(List<Product> data) {
        double total = 0;
        int qty = 0;
        for (Product product : data) {
            total += product.getPrice();
            qty++;
        }
        TextView totalView = mRootView.findViewById(R.id.total);
        totalView.setText("Total: " + String.valueOf(total));
        TextView quantityView = mRootView.findViewById(R.id.quantity);
        quantityView.setText("Quantity: " + String.valueOf(qty));

    }

    private Order createOrder(List<Product> data) {
        Order order = new Order();
        double total = 0;

        List<OrderItem> ordemItens = new ArrayList<>();
        for (Product product : data) {
            total += product.getPrice();
            OrderItem oi = new OrderItem();
            oi.setProductId(product.getId());
            oi.setProduct(product);
            oi.setQuantity(1);
            ordemItens.add(oi);
        }

        order.setOrderItems(ordemItens);
        order.setContact(mCustomer.getName());
        order.setDeliveryAddress(mCustomer.getAddress());
        order.setTotal(total);

        return order;
    }

    private List<Product> filterByStore(List<Product> data) {
        if (mStore != null && data != null) {
            List<Product> filtered = new ArrayList<>();
            for (Product product : data) {
                if (product.getStoreId() == mStore.getId()) {
                    filtered.add(product);
                }
            }
            return filtered;
        } else {
            return data;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_products, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                onCartClick();
                return false;
            default:
                break;
        }
        return false;
    }

    private void onCartClick() {
        mActivity.goToCart();
    }
}