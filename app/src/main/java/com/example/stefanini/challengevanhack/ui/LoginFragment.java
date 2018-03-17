package com.example.stefanini.challengevanhack.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stefanini.challengevanhack.MainActivity;
import com.example.stefanini.challengevanhack.R;
import com.example.stefanini.challengevanhack.api.RestApi;
import com.example.stefanini.challengevanhack.model.Customer;
import com.example.stefanini.challengevanhack.util.SharedPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends FormFragment {

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiSGVyc29uIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvZW1haWxhZGRyZXNzIjoiaGVyc29uZGZAZ21haWwuY29tIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy91c2VyZGF0YSI6IjY1IiwibmJmIjoxNTIxMzI0OTY1LCJleHAiOjE1MjM3NDQxNjUsImlzcyI6InZhbmhhY2stc2FvcGF1bG8tZmFpci1hcGkiLCJhdWQiOiJ2YW5oYWNrLXNhb3BhdWxvLWZhaXItYXBpIn0.vgTGry-2PjBPO2kI6Apr-bl0sWXguQW8N1PhhfGixr4";
    private View mRootView;
    private Context mContext;
    private MainActivity mActivity;
    private Customer mCustomer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.login_title));
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_login, container, false);
        mContext = getActivity().getApplicationContext();
        mActivity = (MainActivity) getActivity();
        mCustomer = (Customer) getArguments().getSerializable(MainActivity.CUSTOMER);
        init();
        return mRootView;
    }

    private void init() {
        initViews();
    }

    private void initViews() {
        final EditText emailField = mRootView.findViewById(R.id.login);
        final EditText passwordField = mRootView.findViewById(R.id.password);

        // Came from SingIn
        if (mCustomer != null) {
            emailField.setText(mCustomer.getEmail());
        }

        Button submitButton = mRootView.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validator = true;

                validator = validateIsEmptyField(passwordField);
                validator = validateIsEmptyField(emailField);
                validator = validateIsEmailField(emailField);

                if (validator) {
                    Customer customer = new Customer();
                    customer.setEmail(emailField.getText().toString());
                    customer.setPassword(passwordField.getText().toString());

                    submitLoginService(customer);
                }
            }
        });

        View signInView = mRootView.findViewById(R.id.go_signin);
        signInView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.goToSignIn();
            }
        });
    }

    private void submitLoginService(final Customer customer) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.sending_data_message));
        dialog.show();

        RestApi.get().login(customer.getEmail(), customer.getPassword()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                dialog.dismiss();
                if (response.code() == 200) {
                    SharedPrefs.saveToken(mContext, (String) response.body());
                    SharedPrefs.saveCustomer(mContext, customer);
                    Toast.makeText(mContext, getString(R.string.logged), Toast.LENGTH_SHORT).show();
                    mActivity.goToStore();
                } else {
                    Toast.makeText(mContext, getString(R.string.api_error, response.message()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                // I needed to do this, API don't work as well
                SharedPrefs.saveToken(mContext, TOKEN);
                SharedPrefs.saveCustomer(mContext, customer);
                mActivity.goToStore();
                ///////

                Toast.makeText(mContext, getString(R.string.api_error, t.getMessage()), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
