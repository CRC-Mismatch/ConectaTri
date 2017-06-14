package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.ListingsAdapter;
import br.com.wemind.marketplacetribanco.adapters.QuotesAdapter;
import br.com.wemind.marketplacetribanco.databinding.ContentListingsListBinding;
import br.com.wemind.marketplacetribanco.models.Listing;
import br.com.wemind.marketplacetribanco.models.Product;
import br.com.wemind.marketplacetribanco.models.Quote;
import br.com.wemind.marketplacetribanco.models.Supplier;

public class QuotesListActivity extends BaseDrawerActivity {

    public static final int CREATE_LISTING = 1;
    public static final int EDIT_LISTING = 2;
    public static final String REMOTE_ONLY = "REMOTE_ONLY";
    private ContentListingsListBinding cb;
    private QuotesAdapter adapter;
    private ArrayList<Quote> data = new ArrayList<>();
    private boolean remoteOnly;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        remoteOnly = getIntent().getBooleanExtra(REMOTE_ONLY, false);

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
                ArrayList<Product> dummyProducts = new ArrayList<>(50);
                ArrayList<Supplier> dummySuppliers = new ArrayList<>(5);

                for (int i = 0; i < 50; ++i) {
                    dummyProducts.add(new Product().setId(i).setName("Produto " + i));
                }

                for (int i = 0; i < 5; ++i) {
                    dummySuppliers.add(new Supplier(i, "Fornecedor " + i, "João Silva", "joao.silva@gmail.com", "11", "4645-6452"));
                }

                for (int i = 1; i <= 10; ++i) {
                    data.add(new Quote(i,
                            "Cotação " + i,
                            1 + (1001 % (2 + i) % 2),
                            dummyProducts,
                            dummySuppliers
                    ));
                }
                onDataReceived(data);
            }
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_LISTING) {
            if (resultCode == RESULT_OK) {
                Listing edited = data.getBundleExtra(ListingCreateActivity.RESULT_BUNDLE)
                        .getParcelable(ListingCreateActivity.RESULT_LISTING);

                if (edited != null) {
                    // FIXME: 25/05/2017 send new data to server
                    Toast.makeText(
                            this,
                            edited.getName() + " foi adicionado",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            } else {
                // FIXME: 27/05/2017 handle cancellation

            }
        }
        else if (requestCode == EDIT_LISTING) {
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

    private void onDataReceived(ArrayList<Quote> data) {
        this.data = data;
        adapter = new QuotesAdapter(this, data);
        cb.list.setAdapter(adapter);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return BaseDrawerActivity.ID_NONE_VOLATILE;
    }
}
