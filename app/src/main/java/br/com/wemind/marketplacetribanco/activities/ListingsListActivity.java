package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.ListingsAdapter;
import br.com.wemind.marketplacetribanco.databinding.ContentListingsListBinding;
import br.com.wemind.marketplacetribanco.models.Listing;
import br.com.wemind.marketplacetribanco.models.Product;

public class ListingsListActivity extends BaseDrawerActivity {

    public static final int EDIT_LISTING = 1;
    private ContentListingsListBinding cb;
    private ListingsAdapter adapter;
    private ArrayList<Listing> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup response to search query
        // Reset filtered data when user closes search view
        b.search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter.getFilter().filter("");
                return false;
            }
        });

        // Search payload data and update filtered data upon user input
        b.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);

                // Hide keyboard and clear focus
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                b.search.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        // End of search view setup

        // Setup content view
        cb = ContentListingsListBinding
                .inflate(getLayoutInflater(), b.contentFrame, true);

        cb.list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // FIXME: 24/05/2017 actually retrieve data
        retrieveData();
    }

    private void retrieveData() {
        // FIXME: 24/05/2017 start data retrieval here
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create and send dummy data
                ArrayList<Listing> data = new ArrayList<>(100);
                ArrayList<Product> dummyProducts = new ArrayList<>(100);

                for (int i = 0; i < 100; ++i) {
                    dummyProducts.add(new Product());
                }

                for (int i = 1; i <= 100; ++i) {
                    data.add(new Listing(
                            "Lista " + i,
                            1 + (1001 % (3 + i) % 3),
                            dummyProducts
                    ));
                }
                onDataReceived(data);
            }
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_LISTING) {
            if (resultCode == RESULT_OK) {
                Listing edited = data.getBundleExtra(ListingCreateActivity.RESULT_BUNDLE)
                        .getParcelable(ListingCreateActivity.RESULT_LISTING);

                if (edited != null) {
                    // FIXME: 25/05/2017 send new data to server
                    Toast.makeText(
                            this,
                            edited.getName() + " foi editado",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            } else {
                // FIXME: 27/05/2017 handle cancellation

            }
        }
    }

    private void onDataReceived(ArrayList<Listing> data) {
        this.data = data;
        adapter = new ListingsAdapter(this, data);
        cb.list.setAdapter(adapter);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_listings;
    }
}