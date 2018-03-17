package com.example.stefanini.challengevanhack.ui;

import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.widget.EditText;

import com.example.stefanini.challengevanhack.R;

/**
 * Created by Herson Rodrigues <hersonrodrigues@gmail.com> on 17/03/2018.
 */

public class FormFragment extends Fragment {

    public boolean validateIsEmailField(EditText field) {
        boolean valid = Patterns.EMAIL_ADDRESS.matcher(field.getText().toString()).matches();
        if (!valid) {
            field.setError(getString(R.string.field_email_error));
        }
        return valid;
    }

    public boolean validateIsEmptyField(EditText field) {
        String value = field.getText().toString();
        if (value.isEmpty()) {
            field.setError(getString(R.string.field_empry_error));
            return false;
        }
        return true;
    }
}
