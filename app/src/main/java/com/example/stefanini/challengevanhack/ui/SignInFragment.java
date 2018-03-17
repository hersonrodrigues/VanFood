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
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInFragment extends FormFragment {

    private View mRootView;
    private Context mContext;
    private MainActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.signin_title));
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mContext = getActivity().getApplicationContext();
        mActivity = (MainActivity) getActivity();
        init();
        return mRootView;
    }

    private void init() {
        initViews();
    }

    private void initViews() {
        final EditText nameField = mRootView.findViewById(R.id.name);
        final EditText emailField = mRootView.findViewById(R.id.login);
        final EditText addesssField = mRootView.findViewById(R.id.address);
        final EditText passwordField = mRootView.findViewById(R.id.password);

        Button submitButton = mRootView.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validator = true;

                validator = validateIsEmptyField(nameField);
                validator = validateIsEmptyField(addesssField);
                validator = validateIsEmptyField(passwordField);
                validator = validateIsEmptyField(emailField);

                validator = validateIsEmailField(emailField);

                if (validator) {

                    Customer customer = new Customer();
                    customer.setEmail(emailField.getText().toString());
                    customer.setName(nameField.getText().toString());
                    customer.setAddress(addesssField.getText().toString());
                    customer.setPassword(passwordField.getText().toString());

                    submitCustomerService(customer);
                }
            }
        });
    }

    private void submitCustomerService(final Customer customer) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.sending_data_message));
        dialog.show();

        RestApi.get().api(getContext()).customerForm(customer).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.code() == 200) {
                    Toast.makeText(mContext, getString(R.string.user_created), Toast.LENGTH_SHORT).show();
                    mActivity.goToLogin(customer);
                } else if (response.code() == 400) {
                    Toast.makeText(mContext, "There is already an account with this email!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, getString(R.string.api_error, response.message()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(mContext, getString(R.string.api_error, t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
