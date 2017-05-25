package br.com.wemind.marketplacetribanco.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.SupplierAdapter;
import br.com.wemind.marketplacetribanco.databinding.ContentSuppliersListBinding;

public class SuppliersListActivity extends BaseDrawerActivity {

    private ContentSuppliersListBinding cb;
    /**
     * Entire data payload received from retrieveData()
     */
    private ArrayList<SupplierAdapter.Supplier> data;
    private SupplierAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        cb = DataBindingUtil.inflate(getLayoutInflater(), R.layout.content_suppliers_list,
                b.contentFrame, true);

        cb.list.setLayoutManager(new LinearLayoutManager(this));

        // FIXME: 24/05/2017 actually retrieve data
        retrieveData();
    }

    private void retrieveData() {
        // FIXME: 24/05/2017 start data retrieval here
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create and send dummy data
                ArrayList<SupplierAdapter.Supplier> data = new ArrayList<>(100);

                for (int i = 1; i <= 100; ++i) {
                    data.add(new SupplierAdapter.Supplier(
                            "Fornecedor " + i,
                            "Juvenil" + (char) ((int) ('a') - 1 + i),
                            "contato@fornecedor" + i + ".com.br",
                            "(11) 5666-666" + i
                    ));
                }
                onDataReceived(data);
            }
        }, 2000);
    }

    private void onDataReceived(ArrayList<SupplierAdapter.Supplier> data) {
        this.data = data;
        adapter = new SupplierAdapter(this, data);
        cb.list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_suppliers;
    }
}
