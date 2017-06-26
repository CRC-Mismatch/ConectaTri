package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ActivitySignUpBinding;
import br.com.wemind.marketplacetribanco.models.SignUpInfo;
import br.com.wemind.marketplacetribanco.utils.Formatting;
import br.com.wemind.marketplacetribanco.utils.FormattingTextWatcher;

public class SignUpActivity extends AppCompatActivity {

    public static final String RESULT_SIGN_UP_INFO = "result_sign_up_info";
    private static final
    int CNPJ_FORMATTED_MAX_LENGTH = Formatting.CNPJ_NUMBER_MAX_DIGITS + 4;
    private ActivitySignUpBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        setSupportActionBar(b.toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Setup CNPJ formatting
        b.cnpj.addTextChangedListener(new FormattingTextWatcher(
                new Formatting.CnpjFormatter(), CNPJ_FORMATTED_MAX_LENGTH));

        b.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    setResult(RESULT_OK, packResultIntent());
                    finish();
                }
            }
        });
    }

    @NonNull
    private Intent packResultIntent() {
        Intent result = new Intent();
        result.putExtra(RESULT_SIGN_UP_INFO,
                (Parcelable) new SignUpInfo()
                        .setCnpj(b.cnpj.getText().toString())
                        .setPassword(b.passwordText.getText().toString())
                        .setEmail(b.email.getText().toString())
                        .setPhone(b.phone.getText().toString())
                        .setCellphone(b.cellphone.getText().toString())
                        .setCompanyName(b.companyName.getText().toString())
                        .setFantasyName(b.fantasyName.getText().toString())
                        .setAddress(b.address.getText().toString())
                        .setCity(b.city.getText().toString())
                        .setCep(b.cep.getText().toString())
                        .setState(b.state.getText().toString()));
        return result;
    }

    private boolean validateForm() {
        boolean isValid = true;
        View errorView = null;

        // Validate CNPJ
        if (b.cnpj.length() <= 0) {
            b.cnpj.setError(getString(R.string.error_field_required));
            errorView = b.cnpj;

        } else if (Formatting.onlyNumbers(b.cnpj.getText().toString()).length()
                > Formatting.CNPJ_NUMBER_MAX_DIGITS) {
            b.cnpj.setError(getString(R.string.error_invalid_cnpj));
            errorView = b.cnpj;
        }

        // Validate passwords
        if (b.passwordText.length() <= 0) {
            // If password is empty
            b.passwordText.setError(getString(R.string.error_field_required));
            errorView = b.passwordText;

        } else if (b.passwordText.length() < 8) {
            // If password is not at least 8 characters long
            b.passwordText.setError(getString(R.string.error_invalid_password_min_chars));

        } else if (!b.passwordText.getText().toString()
                .equals(b.passwordConfText.getText().toString())) {
            // If passwords don't match
            b.passwordConfText.setError(getString(R.string.error_passwords_dont_match));
            errorView = b.passwordConfText;
        }

        // Validate e-mail
        if (b.email.length() <= 0) {
            b.email.setError(getString(R.string.error_field_required));
            errorView = b.email;

        } else if (!b.email.getText().toString().contains("@")) {
            b.email.setError(getString(R.string.error_invalid_email));
            errorView = b.email;
        }

        errorView = setErrorIfEmpty(b.cep) ? b.cep : errorView;
        errorView = setErrorIfEmpty(b.state) ? b.state : errorView;
        errorView = setErrorIfEmpty(b.city) ? b.city : errorView;
        errorView = setErrorIfEmpty(b.address) ? b.address : errorView;
        errorView = setErrorIfEmpty(b.companyName) ? b.companyName : errorView;
        errorView = setErrorIfEmpty(b.fantasyName) ? b.fantasyName : errorView;
        errorView = setErrorIfEmpty(b.phone) ? b.phone : errorView;
        errorView = setErrorIfEmpty(b.cellphone) ? b.cellphone : errorView;

        if (errorView != null) {
            isValid = false;
            errorView.requestFocus();
        }

        return isValid;
    }

    private boolean setErrorIfEmpty(EditText editText) {
        if (editText.length() <= 0) {
            editText.setError(getString(R.string.error_field_required));
            return true;

        } else {
            return false;
        }
    }
}
