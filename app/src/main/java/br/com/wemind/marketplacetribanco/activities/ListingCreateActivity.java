package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;
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
        cb.edtDescription.setText(listing.getDescription());

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
                b.putParcelableArrayList(
                        ProductsSelectActivity.INPUT_PRODUCTS, products);
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
        // TODO: 27/05/2017 are there any other requirements?
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
                ArrayList<Product> products = data
                        .getBundleExtra(ProductsSelectActivity.RESULT_BUNDLE)
                        .getParcelableArrayList(ProductsSelectActivity.SELECTED_LIST);

                if (products == null) {
                    products = new ArrayList<>();
                }

                ArrayList<ListingProduct> listingProducts =
                        new ArrayList<>(products.size());
                for (Product p : products) {
                    listingProducts.add(new ListingProduct(0, p, 0));
                }
                listing.setProducts(listingProducts);

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
