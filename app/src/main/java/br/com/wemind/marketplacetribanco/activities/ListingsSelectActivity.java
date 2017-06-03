package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

import br.com.wemind.marketplacetribanco.adapters.ListingsAdapter;
import br.com.wemind.marketplacetribanco.databinding.ContentListingsListBinding;
import br.com.wemind.marketplacetribanco.models.Listing;
import br.com.wemind.marketplacetribanco.models.Product;
import br.com.wemind.marketplacetribanco.models.Supplier;

public class ListingsSelectActivity extends BaseSelectActivity {

    public static final String SELECTED_LIST = "SELECTED_LIST";
    public static final String RESULT_BUNDLE = "result_bundle";

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
    protected void packResultIntent() {
        Bundle result = new Bundle();
        result.putParcelableArrayList(SELECTED_LIST, adapter.getSelectedList());

        Intent i = new Intent();
        i.putExtra(RESULT_BUNDLE, result);

        setResult(RESULT_OK, i);
    }

    @Override
    protected boolean mayContinue() {
        return true;
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
                ArrayList<Supplier> dummySuppliers = new ArrayList<>(5);

                for (int i = 0; i < 5; ++i) {
                    dummySuppliers.add(new Supplier(i, "Fornecedor " + i, "João Silva", "joao.silva@gmail.com", "11", "4645-6452"));
                }

                for (int i = 1; i <= 100; ++i) {
                    ArrayList<Product> dummyProducts = new ArrayList<>();
                    for (int j = 0; j < i; ++j) {
                        Product p = new Product();
                        p.setId(j);
                        p.setName("Produto " + j);
                        dummyProducts.add(p);
                    }

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
