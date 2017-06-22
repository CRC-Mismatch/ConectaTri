package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import java.util.ArrayList;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ContentListingCreateBinding;
import br.com.wemind.marketplacetribanco.models.Listing;
import br.com.wemind.marketplacetribanco.models.ListingProduct;
import br.com.wemind.marketplacetribanco.models.Product;

public class ListingCreateActivity extends BaseCreateActivity {

    public static final String RESULT_LISTING = "result_listing";
    public static final String RESULT_BUNDLE = "result_bundle";
    public static final String INPUT_LISTING = "input_listing";
    public static final String INPUT_BUNDLE = "input_bundle";

    public static final int REQUEST_SELECT_PRODUCTS = 1;
    public static final int REQUEST_SET_QUANTITIES = 2;
    private ContentListingCreateBinding cb;
    private Listing listing = new Listing();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cb = ContentListingCreateBinding.inflate(getLayoutInflater(),
                b.contentFrame, true);

        Intent input = getIntent();
        Bundle bundle = input.getBundleExtra(INPUT_BUNDLE);
        if (bundle != null) {
            Listing inputListing = bundle.getParcelable(INPUT_LISTING);
            if (inputListing != null) {
                listing = inputListing;
            }
        }


        cb.edtName.setText(listing.getName());

        cb.edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    cb.edtName.setError(getString(R.string.error_field_required));
                }

                listing.setName(s.toString());
            }
        });

        cb.edtDescription.setText(listing.getDescription());

        cb.edtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                listing.setDescription(s.toString());
            }
        });


        cb.btnSelectProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Product> products =
                        new ArrayList<>(listing.getProducts().size());
                for (ListingProduct p : listing.getProducts()) {
                    products.add(p.getProduct());
                }

                // Start product selection activity for result
                Bundle b = new Bundle();
                /*b.putParcelableArrayList(
                        ProductsSelectActivity.INPUT_PRODUCTS, products);*/
                b.putParcelableArrayList(
                        ProductsSelectActivity.INPUT_SELECTED, products);

                Intent i = new Intent(
                        ListingCreateActivity.this, ProductsSelectActivity.class);
                i.putExtra(ProductsSelectActivity.INPUT_BUNDLE, b);
                startActivityForResult(i, REQUEST_SELECT_PRODUCTS);
            }
        });

        cb.btnQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListingCreateActivity.this,
                        ProductSetQuantityActivity.class);

                i.putExtra(ProductSetQuantityActivity.INPUT_QUANTITIES,
                        listing.getProducts());

                startActivityForResult(i, REQUEST_SET_QUANTITIES);
            }
        });
    }

    @Override
    protected boolean validateForm() {
        if (cb.edtName.length() <= 0) {
            cb.edtName.setError(getString(R.string.error_field_required));
            cb.edtName.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_SELECT_PRODUCTS == requestCode) {
            if (RESULT_OK == resultCode) {
                ArrayList<Product> selectedProducts = data
                        .getBundleExtra(ProductsSelectActivity.RESULT_BUNDLE)
                        .getParcelableArrayList(ProductsSelectActivity.SELECTED_LIST);

                if (selectedProducts == null) {
                    selectedProducts = new ArrayList<>();
                }

                ArrayList<ListingProduct> newListingProducts =
                        new ArrayList<>(selectedProducts.size());

                for (ListingProduct lp : listing.getProducts()) {
                    if (selectedProducts.contains(lp.getProduct())) {
                        // If product already in listing's ListingProduct list,
                        // add the existing entry to the new list and
                        // remove the product from pending list
                        newListingProducts.add(lp);
                        selectedProducts.remove(lp.getProduct());
                    }
                }

                // Add the remaining products to the new list
                for (Product p : selectedProducts) {
                    newListingProducts.add(new ListingProduct(0, p, 1));
                }
                listing.setProducts(newListingProducts);

            } else if (RESULT_CANCELED == resultCode) {

            }
        } else if (REQUEST_SET_QUANTITIES == requestCode) {
            if (RESULT_OK == resultCode) {
                ArrayList<ListingProduct> listingProducts = data
                        .getParcelableArrayListExtra(
                                ProductSetQuantityActivity.RESULT_QUANTITIES);

                if (listingProducts != null) {
                    listing.setProducts(listingProducts);
                }

            }
        }
    }

    @Override
    protected Intent getResultIntent() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_LISTING, listing);
        Intent result = new Intent();
        result.putExtra(RESULT_BUNDLE, bundle);
        return result;
    }

}
