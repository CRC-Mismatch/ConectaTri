package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ContentSupplierCreateBinding;
import br.com.wemind.marketplacetribanco.models.Supplier;
import br.com.wemind.marketplacetribanco.utils.BrPhoneFormattingTextWatcher;
import br.com.wemind.marketplacetribanco.utils.Formatting;
import br.com.wemind.marketplacetribanco.utils.FormattingTextWatcher;

public class SupplierCreateActivity extends BaseCreateActivity {

    public static final String RESULT_BUNDLE = "test_result";
    public static final String RESULT_SUPPLIER = "result_value_supplier";
    public static final String INPUT_SUPPLIER = "input_supplier";
    public static final String INPUT_BUNDLE = "input_bundle";

    public long supplierId;

    private ContentSupplierCreateBinding cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cb = ContentSupplierCreateBinding
                .inflate(getLayoutInflater(), b.contentFrame, true);

        // Setup CNPJ text formatter
        cb.edtCnpj.addTextChangedListener(
                new FormattingTextWatcher(new Formatting.CnpjFormatter()));

        cb.edtContactPhone.addTextChangedListener(new BrPhoneFormattingTextWatcher());

        Intent input = getIntent();
        Bundle inputBundle = input.getBundleExtra(INPUT_BUNDLE);

        if (inputBundle != null) {
            Supplier supplier =
                    inputBundle.getParcelable(INPUT_SUPPLIER);

            if (supplier != null) {
                supplierId = supplier.getId();
                cb.edtCnpj.setText(supplier.getCnpj());
                cb.edtSupplierName.setText(supplier.getSupplierName());
                cb.edtContactName.setText(supplier.getContactName());
                cb.edtContactEmail.setText(supplier.getContactEmail());
                cb.edtContactPhone.setText(supplier.getContactPhone());
            }
        }
    }

    @Override
    protected boolean validateForm() {
        boolean errorOccurred = false;
        if (cb.edtCnpj.length() <= 0) {
            errorOccurred = true;
            cb.edtCnpj.setError(getString(R.string.error_field_required));
            cb.edtCnpj.requestFocus();

        } else {
            boolean hasCorrectSize =
                    cb.edtCnpj.getText().toString().replaceAll("[^0-9]", "").length()
                            == Formatting.CNPJ_NUMBER_MAX_DIGITS;
            if (!hasCorrectSize) {
                errorOccurred = true;
                cb.edtCnpj.setError(getString(R.string.error_invalid_cnpj));
                cb.edtCnpj.requestFocus();
            }
        }

        errorOccurred |= _setErrorIfEmpty(cb.edtSupplierName);
        errorOccurred |= _setErrorIfEmpty(cb.edtContactName);

        errorOccurred |= _setErrorIfEmpty(cb.edtContactEmail);
        // Check if email address contains @ symbol
        if (cb.edtContactEmail.length() > 0
                && !cb.edtContactEmail.getText().toString().contains("@")) {
            errorOccurred = true;
            cb.edtContactEmail.setError(getString(R.string.error_invalid_email));
            cb.edtContactEmail.requestFocus();
        }

        errorOccurred |= _setErrorIfEmpty(cb.edtContactPhone);

        return !errorOccurred;
    }

    /**
     * @param et
     * @return true if error occurred; false otherwise
     */
    private boolean _setErrorIfEmpty(EditText et) {
        if (et.length() <= 0) {
            et.setError(getString(R.string.error_field_required));
            et.requestFocus();
            return true;
        }
        return false;
    }

    @Override
    protected Intent getResultIntent() {
        Intent result = new Intent();
        Bundle userBundle = new Bundle();
        userBundle.putParcelable(RESULT_SUPPLIER, new Supplier()
                .setId(supplierId)
                .setName(cb.edtSupplierName.getText().toString())
                .setCnpj(cb.edtCnpj.getText().toString())
                .setContactName(cb.edtContactName.getText().toString())
                .setContactEmail(cb.edtContactEmail.getText().toString())
                .setContactPhone(cb.edtContactPhone.getText().toString())
        );

        result.putExtra(RESULT_BUNDLE, userBundle);
        return result;
    }

}
