package br.com.wemind.marketplacetribanco.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.Callback;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.databinding.ActivityEditUserInfoBinding;
import br.com.wemind.marketplacetribanco.models.UserInfo;
import br.com.wemind.marketplacetribanco.utils.BrPhoneFormattingTextWatcher;
import br.com.wemind.marketplacetribanco.utils.BrazilianStates;
import br.com.wemind.marketplacetribanco.utils.Formatting;
import br.com.wemind.marketplacetribanco.utils.FormattingTextWatcher;
import br.com.wemind.marketplacetribanco.utils.Validation;
import br.com.wemind.marketplacetribanco.views.SelectableEditText;
import retrofit2.Call;

public class EditUserInfoActivity extends AppCompatActivity {

    private ActivityEditUserInfoBinding b;
    private boolean isDataReady = false;
    private Handler retryGetDataHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = DataBindingUtil.setContentView(this, R.layout.activity_edit_user_info);

        // Setup password masking button
        b.btnShowPassword.setOnClickListener(new View.OnClickListener() {
            private boolean isShowing = false;

            @Override
            public void onClick(View v) {
                int sStart = b.currentPasswordText.getSelectionStart();
                int sEnd = b.currentPasswordText.getSelectionEnd();

                if (isShowing) {
                    isShowing = false;
                    b.currentPasswordText.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    isShowing = true;
                    b.currentPasswordText.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }

                b.currentPasswordText.setSelection(sStart, sEnd);
            }
        });

        // User may never edit CNPJ
        b.cnpj.setEnabled(false);

        b.cnpj.addTextChangedListener(new FormattingTextWatcher(new Formatting.CnpjFormatter(), 18));
        b.phone.addTextChangedListener(new BrPhoneFormattingTextWatcher());
        b.cellphone.addTextChangedListener(new BrPhoneFormattingTextWatcher());
        b.cep.addTextChangedListener(new FormattingTextWatcher(
                new Formatting.CepFormatter(), 9));

        // Setup states list
        ((SelectableEditText<BrazilianStates.StateListable>) b.state)
                .setItems(BrazilianStates.getList());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (retryGetDataHandler != null) {
            retryGetDataHandler.removeCallbacksAndMessages(null);
            retryGetDataHandler = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isDataReady) {
            retrieveData();
        }
    }

    private void retrieveData() {
        isDataReady = false;
        Api.api.getUserInfo().enqueue(new GetUserInfoCallback());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_save) {
            if (isDataReady && validateForm()) {
                Api.api.editUserInfo(new UserInfo.Edit.Request()
                        .setPassword(b.currentPasswordText.getText().toString())
                        .setUserInfo(new UserInfo()
                                .setCompanyName(b.companyName.getText().toString())
                                .setFantasyName(b.fantasyName.getText().toString())
                                .setPassword(b.newPasswordText.getText().toString())
                                .setCellphone(b.cellphone.getText().toString())
                                .setPhone(b.phone.getText().toString())
                                .setEmail(b.email.getText().toString())
                                .setCep(b.cep.getText().toString())
                                .setState(b.state.getText().toString())
                                .setCity(b.city.getText().toString())
                                .setAddress(b.address.getText().toString())
                        )
                ).enqueue(new EditUserInfoCallback());
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean validateForm() {
        boolean isValid = true;
        View errorView = null;

        // This shouldn't be done here.
        // CNPJ validation must be done during sign up.
        // Once a user is registered, it's done.
        /*// Validate CNPJ
        if (b.cnpj.length() <= 0) {
            b.cnpj.setError(getString(R.string.error_field_required));
            errorView = b.cnpj;

        } else if (Formatting.onlyNumbers(b.cnpj.getText().toString()).length()
                > Formatting.CNPJ_NUMBER_MAX_DIGITS) {
            b.cnpj.setError(getString(R.string.error_invalid_cnpj));
            errorView = b.cnpj;
        }*/

        // Validate passwords
        if (b.newPasswordText.length() > 0 && b.newPasswordText.length() < 8) {
            // If password is not at least 8 characters long
            b.newPasswordText.setError(getString(R.string.error_invalid_password_min_chars));

        } else if (!b.newPasswordConfText.getText().toString()
                .equals(b.newPasswordConfText.getText().toString())) {
            // If passwords don't match
            b.newPasswordConfText.setError(getString(R.string.error_passwords_dont_match));
            errorView = b.newPasswordConfText;
        }

        // Validate e-mail
        if (b.email.length() <= 0) {
            b.email.setError(getString(R.string.error_field_required));
            errorView = b.email;

        } else if (!b.email.getText().toString().contains("@")) {
            b.email.setError(getString(R.string.error_invalid_email));
            errorView = b.email;
        }

        errorView = setErrorIfEmpty(b.currentPasswordText) ? b.currentPasswordText : errorView;
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

    private void bindData(UserInfo data) {
        b.cnpj.setText(data.getCnpj());
        b.cellphone.setText(data.getCellphone());
        b.phone.setText(data.getPhone());
        b.cep.setText(data.getCep());
        b.state.setText(data.getState());
        b.city.setText(data.getCity());
        b.address.setText(data.getAddress());
        b.email.setText(data.getEmail());
        b.companyName.setText(data.getCompanyName());
        b.fantasyName.setText(data.getFantasyName());
    }

    private void onDataReceived(UserInfo data) {
        if (data != null) {
            bindData(data);
            isDataReady = true;
        }
    }

    private class EditUserInfoCallback extends Callback<ApiError> {
        public EditUserInfoCallback() {
            super(EditUserInfoActivity.this);
        }

        @Override
        public void onSuccess(ApiError response) {
            Toast.makeText(context,
                    R.string.text_edit_user_success,
                    Toast.LENGTH_LONG
            ).show();
            finish();
        }

        @Override
        public void onError(Call<ApiError> call, ApiError response) {
            Toast.makeText(context,
                    response.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private class GetUserInfoCallback extends Callback<UserInfo> {
        public GetUserInfoCallback() {
            super(EditUserInfoActivity.this);
        }

        @Override
        public void onSuccess(UserInfo response) {
            onDataReceived(response);
        }

        @Override
        public void onError(Call<UserInfo> call, ApiError response) {
            Toast.makeText(context,
                    response.getMessage(),
                    Toast.LENGTH_SHORT
            ).show();

            // Retry after 1 second
            retryGetDataHandler = new Handler();
            retryGetDataHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    retrieveData();
                }
            }, 1000);
        }
    }
}
