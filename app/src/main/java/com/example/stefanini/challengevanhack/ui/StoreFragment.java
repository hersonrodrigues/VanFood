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
import android.widget.Toast;

import com.example.stefanini.challengevanhack.MainActivity;
import com.example.stefanini.challengevanhack.R;
import com.example.stefanini.challengevanhack.api.RestApi;
import com.example.stefanini.challengevanhack.model.Store;
import com.example.stefanini.challengevanhack.ui.adapter.StoreAdapter;
import com.example.stefanini.challengevanhack.util.ItemClick;
import com.example.stefanini.challengevanhack.util.SharedPrefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Herson Rodrigues <hersonrodrigues@gmail.com> on 17/03/2018.
 */

public class StoreFragment extends Fragment {
    private View mRootView;
    private Context mContext;
    private MainActivity mActivity;
    private List<Store> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_logged, container, false);
        mContext = getActivity().getApplicationContext();
        mActivity = (MainActivity) getActivity();
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);
        mActivity.setTitle(getString(R.string.choose_store));
        setHasOptionsMenu(true);
        init();
        return mRootView;
    }

    private void init() {
        if (mList != null) {
            renderUI();
        } else {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getString(R.string.sending_data_message));
            dialog.show();

            RestApi.get().api(mContext).listStories().enqueue(new Callback<List<Store>>() {
                @Override
                public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                    dialog.dismiss();
                    if (response.code() == 200) {
                        mList = response.body();
                        renderUI();
                    } else {
                        Toast.makeText(mContext, getString(R.string.api_error, response.message()), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Store>> call, Throwable t) {
                    Toast.makeText(mContext, getString(R.string.api_error, t.getMessage()), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }

    private void renderUI() {
        RecyclerView recyclerView = mRootView.findViewById(R.id.list);
        StoreAdapter adapter = new StoreAdapter(mActivity, mList, new ItemClick() {

            @Override
            public void click(Object o) {
                Store store = (Store) o;
                mActivity.goToProductsByStore(store);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_logout, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                onLogoutClick();
                return false;
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

    private void onLogoutClick() {
        SharedPrefs.saveToken(mContext, null);
        SharedPrefs.saveCustomer(mContext, null);
        mActivity.goToLogin(null);
    }
}
