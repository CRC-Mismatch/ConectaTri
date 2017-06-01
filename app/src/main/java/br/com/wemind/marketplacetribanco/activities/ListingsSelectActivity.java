package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.ListingsAdapter;
import br.com.wemind.marketplacetribanco.databinding.ContentListingsListBinding;
import br.com.wemind.marketplacetribanco.models.Listing;
import br.com.wemind.marketplacetribanco.models.Product;
import br.com.wemind.marketplacetribanco.models.Supplier;

public class ListingsSelectActivity extends BaseSelectActivity {

    public static final String SELECTED_LIST = "SELECTED_LIST";

    private ContentListingsListBinding cb;
    private ListingsAdapter adapter;
    private ArrayList<Listing> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b.fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_continue));
        b.fab.setVisibility(View.VISIBLE);
        // FIXME: Implement real list manipulation
        b.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListingsSelectActivity.this, SuppliersSelectActivity.class);
                i.putParcelableArrayListExtra(SELECTED_LIST, adapter.getSelectedList());
                startActivity(i);
            }
        });

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
                ArrayList<Supplier> dummySuppliers = new ArrayList<>(5);

                for (int i = 0; i < 100; ++i) {
                    dummyProducts.add(new Product());
                }

                for (int i = 0; i < 5; ++i) {
                    dummySuppliers.add(new Supplier(i, "Fornecedor " + i, "João Silva", "joao.silva@gmail.com", "11", "4645-6452"));
                }

                for (int i = 1; i <= 100; ++i) {
                    data.add(new Listing(i,
                            "Lista " + i,
                            1 + (1001 % (3 + i) % 3),
                            dummyProducts,
                            dummySuppliers
                    ));
                }
                onDataReceived(data);
            }
        }, 2000);
    }

    private void onDataReceived(ArrayList<Listing> data) {
        this.data = data;
        adapter = new ListingsAdapter(this, data, true);
        cb.list.setAdapter(adapter);
    }
}
