package com.example.stefanini.challengevanhack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.stefanini.challengevanhack.model.Customer;
import com.example.stefanini.challengevanhack.model.Store;
import com.example.stefanini.challengevanhack.ui.ListProductsFragment;
import com.example.stefanini.challengevanhack.ui.LoginFragment;
import com.example.stefanini.challengevanhack.ui.SignInFragment;
import com.example.stefanini.challengevanhack.ui.StoreFragment;
import com.example.stefanini.challengevanhack.util.OnBackPress;
import com.example.stefanini.challengevanhack.util.SharedPrefs;

public class MainActivity extends AppCompatActivity {

    public static final String PRODUCTS_FROM_CART = "cart";
    public static final String PRODUCTS_BY_STORE = "store";
    public static final String CUSTOMER = "customer";
    private OnBackPress mBackPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SharedPrefs.recoveryToken(this) != null) {
            goToStore();
        } else {
            goToLogin(null);
        }
    }

    public void goToStore() {
        StoreFragment fragment = new StoreFragment();
        showFragment(fragment, "store", false);
    }

    public void goToLogin(Customer customer) {
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CUSTOMER, customer);
        fragment.setArguments(bundle);
        showFragment(fragment, "login", false);
    }

    public void goToSignIn() {
        SignInFragment fragment = new SignInFragment();
        showFragment(fragment, "signIn", true);
    }

    public void goToCart() {
        ListProductsFragment fragment = new ListProductsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(PRODUCTS_FROM_CART, true);
        fragment.setArguments(bundle);
        showFragment(fragment, "list", true);
    }

    public void goToProductsByStore(Store store) {
        ListProductsFragment fragment = new ListProductsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(PRODUCTS_FROM_CART, false);
        bundle.putSerializable(PRODUCTS_BY_STORE, store);
        fragment.setArguments(bundle);
        showFragment(fragment, "list", true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return false;
    }

    public void showFragment(Fragment fragment, String fragmentName, boolean addOnStack) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment, fragmentName);
        if (addOnStack) {
            transaction.addToBackStack(fragmentName);
        }
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.commitAllowingStateLoss();
    }
}
