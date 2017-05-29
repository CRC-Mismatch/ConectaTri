package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.SelectionSupplierAdapter;
import br.com.wemind.marketplacetribanco.adapters.SupplierAdapter;
import br.com.wemind.marketplacetribanco.databinding.ContentSuppliersListBinding;
import br.com.wemind.marketplacetribanco.models.Listing;
import br.com.wemind.marketplacetribanco.models.Supplier;

public class SuppliersSelectActivity extends BaseDrawerActivity {

    public static final int EDIT_USER = 1;
    private ContentSuppliersListBinding cb;

    /**
     * Data received from parent activity
     */
    private ArrayList<Listing> data;
    private Set<Supplier> suppliers;
    private SelectionSupplierAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b.fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_continue));
        b.fab.setVisibility(View.VISIBLE);
        b.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Implement Products selection
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
        cb = DataBindingUtil.inflate(getLayoutInflater(), R.layout.content_suppliers_list,
                b.contentFrame, true);

        cb.list.setLayoutManager(new LinearLayoutManager(this));

        suppliers = new TreeSet<>();
        data = getIntent().getParcelableArrayListExtra(ListingsSelectActivity.SELECTED_LIST);
        for (Listing l : data) {
            suppliers.addAll(l.getSuppliers());
        }
        adapter = new SelectionSupplierAdapter(this, new ArrayList<>(suppliers));

        cb.list.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_USER) {
            if (resultCode == RESULT_OK) {
                Supplier edited = data.getBundleExtra(SupplierCreateActivity.RESULT_BUNDLE)
                        .getParcelable(SupplierCreateActivity.RESULT_SUPPLIER);

                if (edited != null) {
                    // FIXME: 25/05/2017 send new data to server
                    Toast.makeText(
                            this,
                            edited.getSupplierName() + " foi editado",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            } else {

            }
        }
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
