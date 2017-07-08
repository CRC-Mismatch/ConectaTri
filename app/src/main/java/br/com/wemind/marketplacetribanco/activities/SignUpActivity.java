package br.com.wemind.marketplacetribanco.activities;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.Callback;
import br.com.wemind.marketplacetribanco.api.ValidationCallback;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.api.objects.GetCep;
import br.com.wemind.marketplacetribanco.databinding.ActivitySignUpBinding;
import br.com.wemind.marketplacetribanco.models.UserInfo;
import br.com.wemind.marketplacetribanco.utils.BrPhoneFormattingTextWatcher;
import br.com.wemind.marketplacetribanco.utils.BrazilianStates;
import br.com.wemind.marketplacetribanco.utils.BrazilianStates.StateListable;
import br.com.wemind.marketplacetribanco.utils.Formatting;
import br.com.wemind.marketplacetribanco.utils.FormattingTextWatcher;
import br.com.wemind.marketplacetribanco.utils.Validation;
import br.com.wemind.marketplacetribanco.views.SelectableEditText;
import retrofit2.Call;

public class SignUpActivity extends AppCompatActivity {

    public static final String RESULT_SIGN_UP_INFO = "result_sign_up_info";
    public static final int CEP_MAX_LENGTH = Formatting.CEP_NUMBER_MAX_DIGITS + 1;
    private static final int CNPJ_MAX_LENGTH = Formatting.CNPJ_NUMBER_MAX_DIGITS + 4;
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
                new Formatting.CnpjFormatter(), CNPJ_MAX_LENGTH));

        b.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    validateEmail();
                }
            }
        });

        b.cep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //   updateCep();
                }
            }
        });

        // Setup CEP formatting
        b.cep.addTextChangedListener(new FormattingTextWatcher(
                new Formatting.CepFormatter(), CEP_MAX_LENGTH));

        // Setup Brazil-only phone number formatting
        b.phone.addTextChangedListener(new BrPhoneFormattingTextWatcher());
        b.cellphone.addTextChangedListener(new BrPhoneFormattingTextWatcher());

        // Setup list of brazilian states
        final SelectableEditText<StateListable> stateEditText = b.state;
        stateEditText.setItems(BrazilianStates.getList());
        stateEditText.setOnItemSelectedListener(
                new SelectableEditText.OnItemSelectedListener<StateListable>() {
                    @Override
                    public void onItemSelectedListener(StateListable item,
                                                       int selectedIndex) {
                        stateEditText.setError(null);
                    }
                });

        // Parse html text for agreement checkboxf
        Spanned text;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text = Html.fromHtml(
                    getString(R.string.activity_sign_up_agreement),
                    Html.FROM_HTML_MODE_LEGACY
            );
        } else {
            text = Html.fromHtml(getString(R.string.activity_sign_up_agreement));
        }
        b.agreementText.setText(text);
        b.agreementText.setClickable(true);
        b.agreementText.setMovementMethod(LinkMovementMethod.getInstance());


        b.agreementCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.agreementCheckBox.setError(null);
            }
        });
    }

    private void updateCep() {
        Api.cepapi.getCepResponse(b.cep.getText().toString()).enqueue(new CepInfoCallBack());
    }

    private void sendRequest() {
        UserInfo userInfo = new UserInfo()
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
                .setState(b.state.getText().toString());

        Api.api.register(userInfo).enqueue(new RegistrationCallback());
    }

    private void validateEmail() {
        Api.api.validateEmail(b.email.getText().toString()).enqueue(new ValidateMailCallback());
    }

    private boolean validateForm() {
        boolean isValid = true;
        View errorView = null;

        // Check if user agreed to whatever terms are listed at
        // the bottom of the page
        if (!b.agreementCheckBox.isChecked()) {
            b.agreementCheckBox.setError(getString(R.string.error_must_accept_agreement));
            errorView = b.agreementCheckBox;
        }

        errorView = setErrorIfEmpty(b.cellphone) ? b.cellphone : errorView;
        errorView = setErrorIfEmpty(b.phone) ? b.phone : errorView;
        errorView = setErrorIfEmpty(b.address) ? b.address : errorView;
        errorView = setErrorIfEmpty(b.city) ? b.city : errorView;
        errorView = setErrorIfEmpty(b.state) ? b.state : errorView;
        errorView = setErrorIfEmpty(b.cep) ? b.cep : errorView;

        // Validate e-mail
        if (b.email.length() <= 0) {
            b.email.setError(getString(R.string.error_field_required));
            errorView = b.email;

        } else if (!b.email.getText().toString().contains("@")) {
            b.email.setError(getString(R.string.error_invalid_email));
            errorView = b.email;
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

        errorView = setErrorIfEmpty(b.fantasyName) ? b.fantasyName : errorView;
        errorView = setErrorIfEmpty(b.companyName) ? b.companyName : errorView;

        // Validate CNPJ
        if (b.cnpj.length() <= 0) {
            b.cnpj.setError(getString(R.string.error_field_required));
            errorView = b.cnpj;

        } else if (Formatting.onlyNumbers(b.cnpj.getText().toString()).length()
                != Formatting.CNPJ_NUMBER_MAX_DIGITS) {
            b.cnpj.setError(getString(R.string.error_invalid_cnpj));
            errorView = b.cnpj;

        } else if (!Validation.hasValidCnpjCheckDigits(b.cnpj.getText().toString())) {
            // If check digits are incorrect

            int[] correctDigits =
                    Validation.calcCnpjCheckDigits(b.cnpj.getText().toString());

            b.cnpj.setError(getString(R.string.error_invalid_cnpj)
            );
            errorView = b.cnpj;
        }

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

    private class RegistrationCallback extends Callback<ApiError> {
        public RegistrationCallback() {
            super(SignUpActivity.this);
        }

        @Override
        public void onSuccess(ApiError response) {
            finish();
        }

        @Override
        public void onError(Call<ApiError> call, ApiError response) {
            Toast.makeText(context,
                    response.getMessage(),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    private class CepInfoCallBack extends Callback<GetCep> {

        public CepInfoCallBack() {
            super(SignUpActivity.this);
        }

        public void onSuccess(GetCep response) {
            b.address.setText(response.getLogradouro());
        }

        @Override
        public void onError(Call<GetCep> call, ApiError response) {

        }
    }

    private class ValidateMailCallback extends ValidationCallback {

        public ValidateMailCallback() {
            super(SignUpActivity.this);
        }

        @Override
        public void onSuccess(Boolean response) {
            if (response == true) {
                sendRequest();
            } else {
                Toast.makeText(context,
                        R.string.invalid_mail,
                        Toast.LENGTH_LONG
                ).show();
            }
        }

        @Override
        public void onError(Call<Boolean> call, ApiError response) {
            //não deveria acontecer.
            Toast.makeText(context,
                    R.string.error_internal_server_error,
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}
