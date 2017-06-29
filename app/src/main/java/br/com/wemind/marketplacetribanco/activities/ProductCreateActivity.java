package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.databinding.ContentProductCreateBinding;
import br.com.wemind.marketplacetribanco.dummy.DummyProductTypes;
import br.com.wemind.marketplacetribanco.models.Product;

public class ProductCreateActivity extends BaseCreateActivity {

    public static final String RESULT_BUNDLE = "test_result";
    public static final String RESULT_PRODUCT = "result_value_product";
    public static final String INPUT_PRODUCT = "input_product";
    public static final String INPUT_BUNDLE = "input_bundle";
    public static final String RESULT_EXISTING_PRODUCT = "result_existing_product";

    private ContentProductCreateBinding cb;
    private long productId;
    private boolean existingProduct = false;

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
                productId = product.getId();
                cb.edtProductName.setText(product.getName());
                cb.edtDescription.setText(product.getFullDescription());
                cb.edtType.setText(product.getType());
                cb.edtBrand.setText(product.getBrand());
                cb.edtQuantity.setText(String.format("%,.2f", product.getQuantity()));
                cb.edtUnit.setText(product.getUnit());
            }
        }

        // Setup AutoCompleteTextView
        ProductAutoCompleteAdapter adapter = new ProductAutoCompleteAdapter();
        cb.edtProductName.setAdapter(adapter);
        cb.edtProductName.setThreshold(3);
        cb.edtProductName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cb.edtProductName.length() < 3) {
                    cb.edtProductName.setError(getString(R.string.error_name_too_short));
                }
            }
        });
        cb.edtProductName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Product selectedProduct =
                        (Product) ((AdapterView<ProductAutoCompleteAdapter>) parent)
                                .getAdapter()
                                .getItem(position);
                existingProduct = true;
                productId = selectedProduct.getId();
                cb.edtProductName.setText(selectedProduct.getName());
                cb.edtDescription.setText(selectedProduct.getFullDescription());
                cb.edtType.setText(selectedProduct.getType());
                cb.edtBrand.setText(selectedProduct.getBrand());
                cb.edtQuantity.setText(String.format("%,.2f", selectedProduct.getQuantity()));
                cb.edtUnit.setText(selectedProduct.getUnit());
            }
        });
    }

    @Override
    protected boolean validateForm() {
        boolean errorOccurred = false;

        if (cb.edtProductName.length() < 3) {
            cb.edtProductName.setError(getString(R.string.error_name_too_short));
            _setErrorIfEmpty(cb.edtProductName);
            errorOccurred = true;
        }
        errorOccurred |= _setErrorIfEmpty(cb.edtBrand);
        errorOccurred |= _setErrorIfEmpty(cb.edtUnit);
        errorOccurred |= _setErrorIfEmpty(cb.edtQuantity);

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
        Bundle resultBundle = new Bundle();
        resultBundle.putParcelable(RESULT_PRODUCT, new Product()
                .setId(productId)
                .setName(cb.edtProductName.getText().toString())
                .setFullDescription(cb.edtDescription.getText().toString())
                .setBrand(cb.edtBrand.getText().toString())
                .setType(cb.edtType.getText().toString())
                .setQuantity(Double.valueOf(cb.edtQuantity.getText().toString()))
                .setUnit(cb.edtUnit.getText().toString())
        );
        resultBundle.putBoolean(RESULT_EXISTING_PRODUCT, existingProduct);
        result.putExtra(RESULT_BUNDLE, resultBundle);
        return result;
    }

    private class ProductAutoCompleteAdapter extends BaseAdapter implements Filterable {
        private List<Product> filteredData = new ArrayList<>();
        private Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Product> response = Api.syncSearchProduct(constraint);

                FilterResults results = new FilterResults();
                results.values = response;
                results.count = response.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (List<Product>) results.values;
                if (filteredData == null) {
                    filteredData = new ArrayList<>();
                }
                notifyDataSetChanged();
            }
        };

        @Override
        public int getCount() {
            return filteredData.size();
        }

        @Override
        public Object getItem(int position) {
            return filteredData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return filteredData.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        android.R.layout.simple_dropdown_item_1line, parent, false);
            }

            ((TextView) convertView.findViewById(android.R.id.text1)).setText(
                    filteredData.get(position).getName()
            );

            return convertView;
        }

        @Override
        public Filter getFilter() {
            return filter;
        }
    }
}
