package br.com.wemind.marketplacetribanco.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.wemind.marketplacetribanco.databinding.ContentProductSetQuantitiesBinding;
import br.com.wemind.marketplacetribanco.databinding.ItemProductQuantityBinding;
import br.com.wemind.marketplacetribanco.models.ListingProduct;

public class ProductSetQuantityActivity extends BaseCreateActivity {

    public static final String INPUT_QUANTITIES = "input_quantities";
    public static final String RESULT_QUANTITIES = "result_quantities";
    private ContentProductSetQuantitiesBinding cb;
    private ArrayList<ListingProduct> productQuantities = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove up button
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(false);
        }

        cb = ContentProductSetQuantitiesBinding.inflate(
                getLayoutInflater(), b.contentFrame, true
        );

        ArrayList<ListingProduct> inputList = getIntent().getParcelableArrayListExtra(
                ProductSetQuantityActivity.INPUT_QUANTITIES);
        if (inputList != null) {
            productQuantities = inputList;
        }

        cb.list.setHasFixedSize(true);
        cb.list.setLayoutManager(new LinearLayoutManager(this));
        cb.list.setAdapter(new ProductQuantitiesAdapter(this, productQuantities));
    }

    @Override
    protected boolean validateForm() {
        return true;
    }

    @Override
    protected Intent getResultIntent() {
        Intent result = new Intent();
        result.putParcelableArrayListExtra(RESULT_QUANTITIES, productQuantities);

        return result;
    }

    private static class ProductQuantitiesAdapter
            extends RecyclerView.Adapter<ProductQuantitiesAdapter.ViewHolder> {
        private Context context;
        private List<ListingProduct> data;

        public ProductQuantitiesAdapter(Context context,
                                        @NonNull List<ListingProduct> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(ItemProductQuantityBinding.inflate(
                    LayoutInflater.from(context), parent, false
            ));
        }

        @Override
        public void onBindViewHolder(ViewHolder vh, int position) {
            final ListingProduct listingProduct = data.get(position);
            vh.b.productName.setText(listingProduct.getProduct().getName());
            vh.b.edtQuantity.setText(String.valueOf(listingProduct.getQuantity()));

            vh.b.edtQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    listingProduct.setQuantity(Integer.valueOf(s.toString()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ItemProductQuantityBinding b;

            public ViewHolder(ItemProductQuantityBinding binding) {
                super(binding.getRoot());
                b = binding;
            }
        }
    }
}
