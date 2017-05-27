package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ContentListingCreateBinding;
import br.com.wemind.marketplacetribanco.models.Listing;

public class ListingCreateActivity extends BaseCreateActivity {

    public static final String RESULT_LISTING = "result_listing";
    public static final String RESULT_BUNDLE = "result_bundle";
    public static final String INPUT_LISTING = "input_listing";
    public static final String INPUT_BUNDLE = "input_bundle";
    private ContentListingCreateBinding cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cb = ContentListingCreateBinding.inflate(getLayoutInflater(),
                b.contentFrame, true);

        Intent input = getIntent();
        Listing listing =
                input.getBundleExtra(INPUT_BUNDLE).getParcelable(INPUT_LISTING);

        if (listing != null) {
            cb.edtName.setText(listing.getName());
            cb.edtDescription.setText(listing.getDescription());
            cb.btnSelectProducts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: start product selection activity for result
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
    protected Intent getResultIntent() {
        return null;
    }

}
