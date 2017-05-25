package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;

import br.com.wemind.marketplacetribanco.databinding.ContentSupplierCreateBinding;
import br.com.wemind.marketplacetribanco.models.Supplier;

public class SupplierCreateActivity extends BaseCreateActivity {

    public static final String RESULT_BUNDLE = "test_result";
    public static final String RESULT_SUPPLER = "result_value_supplier";
    public static final String INPUT_SUPPLIER = "input_supplier";
    public static final String INPUT_BUNDLE = "input_bundle";

    private ContentSupplierCreateBinding cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cb = ContentSupplierCreateBinding
                .inflate(getLayoutInflater(), b.contentFrame, true);
        Intent input = getIntent();
        Supplier supplier =
                input.getBundleExtra(INPUT_BUNDLE).getParcelable(INPUT_SUPPLIER);

        if (supplier != null) {
            // supplierId = supplier.getId();
            cb.edtCnpj.setText(supplier.getCnpj());
            cb.edtSupplierName.setText(supplier.getSupplierName());
            cb.edtInfo.setText(supplier.getInfo());
            cb.edtContactName.setText(supplier.getContactName());
            cb.edtContactEmail.setText(supplier.getContactEmail());
            cb.edtContactPhone.setText(supplier.getContactPhone());
        }
    }

    @Override
    protected Intent getResultIntent() {
        Intent result = new Intent();
        Bundle userBundle = new Bundle();
        userBundle.putParcelable(RESULT_SUPPLER, new Supplier(
                // this.supplierId,
                cb.edtSupplierName.getText().toString(),
                // cb.edtInfo.getText().toString(),
                cb.edtContactName.getText().toString(),
                cb.edtContactEmail.getText().toString(),
                cb.edtContactPhone.getText().toString()
        ));
        result.putExtra(RESULT_BUNDLE, userBundle);
        return result;
    }

}
