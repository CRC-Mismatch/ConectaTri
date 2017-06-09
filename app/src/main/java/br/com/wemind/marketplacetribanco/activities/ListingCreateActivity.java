package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
    private ContentListingCreateBinding cb;
    private ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cb = ContentListingCreateBinding.inflate(getLayoutInflater(),
                b.contentFrame, true);

        Intent input = getIntent();
        Listing listing = null;
        Bundle bundle = input.getBundleExtra(INPUT_BUNDLE);
        if (bundle != null) {
            listing = bundle.getParcelable(INPUT_LISTING);
        }

        String name = null;
        String description = null;
        products = new ArrayList<>();
        if (listing != null) {
            name = listing.getName();
            description = listing.getDescription();
            for (ListingProduct p : listing.getProducts())
            products.add(p.getProduct());
        }


        cb.edtName.setText(name);
        cb.edtDescription.setText(description);

        final ArrayList<Product> finalProducts = products;
        cb.btnSelectProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start product selection activity for result
                Bundle b = new Bundle();
                b.putParcelableArrayList(
                        ProductsSelectActivity.INPUT_PRODUCTS, finalProducts);

                Intent i = new Intent(
                        ListingCreateActivity.this, ProductsSelectActivity.class);
                i.putExtra(ProductsSelectActivity.INPUT_BUNDLE, b);
                startActivityForResult(i, REQUEST_SELECT_PRODUCTS);
            }
        });

        // TODO: what should this button do??
        /*
        cb.btnQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
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
                this.products = data.getBundleExtra(ProductsSelectActivity.RESULT_BUNDLE)
                    .getParcelableArrayList(ProductsSelectActivity.SELECTED_LIST);

                if (products == null) {
                    products = new ArrayList<>();
                }

                Toast.makeText(this, "Got " + products.get(0).getName(),
                        Toast.LENGTH_SHORT).show();
            } else if (RESULT_CANCELED == resultCode) {

            }
        }
    }

    @Override
    protected Intent getResultIntent() {
        return null;
    }

}
