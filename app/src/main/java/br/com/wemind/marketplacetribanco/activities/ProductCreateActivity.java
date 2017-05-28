package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;

import br.com.wemind.marketplacetribanco.databinding.ContentProductCreateBinding;
import br.com.wemind.marketplacetribanco.dummy.DummyProductTypes;
import br.com.wemind.marketplacetribanco.models.Product;

public class ProductCreateActivity extends BaseCreateActivity {

    public static final String RESULT_BUNDLE = "test_result";
    public static final String RESULT_PRODUCT = "result_value_product";
    public static final String INPUT_PRODUCT = "input_product";
    public static final String INPUT_BUNDLE = "input_bundle";

    private ContentProductCreateBinding cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cb = ContentProductCreateBinding
                .inflate(getLayoutInflater(), b.contentFrame, true);

        cb.edtType.setItems(DummyProductTypes.list);
        Intent input = getIntent();
        if (input.getBundleExtra(INPUT_BUNDLE) != null) {
            Product product =
                    input.getBundleExtra(INPUT_BUNDLE).getParcelable(INPUT_PRODUCT);

            if (product != null) {
                // productId = product.getId();
                cb.edtProductName.setText(product.getSimpleDescription());
                cb.edtInfo.setText(product.getFullDescription());
                cb.edtType.setText(product.getCategory());
                cb.edtBrand.setText(product.getBrand());
                cb.edtQuantity.setText(String.format("%,.2f", product.getQuantity()));
                cb.edtUnit.setText(product.getUnit());
            }
        }
    }

    @Override
    protected boolean validateForm() {
        // FIXME: implement
        return true;
    }

    @Override
    protected Intent getResultIntent() {
        Intent result = new Intent();
        Bundle userBundle = new Bundle();
        userBundle.putParcelable(RESULT_PRODUCT, new Product()
                .setSimpleDescription(cb.edtProductName.getText().toString())
                .setFullDescription(cb.edtInfo.getText().toString())
                .setCategory(cb.edtInfo.getText().toString())
                .setBrand(cb.edtBrand.getText().toString())
                .setQuantity(Double.valueOf(cb.edtQuantity.getText().toString()))
                .setUnit(cb.edtUnit.getText().toString()));
        result.putExtra(RESULT_BUNDLE, userBundle);
        return result;
    }

}
