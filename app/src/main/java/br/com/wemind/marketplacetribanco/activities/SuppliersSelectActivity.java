package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.SelectionSupplierAdapter;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.Callback;
import br.com.wemind.marketplacetribanco.databinding.ContentSuppliersListBinding;
import br.com.wemind.marketplacetribanco.models.Listing;
import br.com.wemind.marketplacetribanco.models.Supplier;
import retrofit2.Call;
import retrofit2.Response;

public class SuppliersSelectActivity extends BaseSelectActivity {

    public static final String RESULT_BUNDLE = "result_bundle";
    public static final String SELECTED_LIST = "selected_list";
    public static final String INPUT_BUNDLE = "input_bundle";
    public static final String INPUT_SUPPLIERS = "input_suppliers";
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

        Bundle inputBundle = getIntent().getBundleExtra(INPUT_BUNDLE);
        if (inputBundle != null) {
            List<Supplier> inputSuppliers =
                    inputBundle.getParcelableArrayList(INPUT_SUPPLIERS);

            if (inputSuppliers != null) {
                suppliers = new TreeSet<>(inputSuppliers);
            }
        }

        adapter = new SelectionSupplierAdapter(this, new ArrayList<>(suppliers));

        cb.list.setAdapter(adapter);
    }

    @Override
    protected void packResultIntent() {
        Bundle resultBundle = new Bundle();
        resultBundle.putParcelableArrayList(SELECTED_LIST, adapter.getSelectedData());

        Intent i = new Intent();
        i.putExtra(RESULT_BUNDLE, resultBundle);

        setResult(RESULT_OK, i);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (suppliers == null || suppliers.size() <= 0) {
            // Disable FAB while data is not ready
            b.fab.setEnabled(false);

            Api.api.getAllSuppliers().enqueue(new GetSuppliersCallback());
        }
    }

    @Override
    protected boolean mayContinue() {
        List selected = adapter.getSelectedData();
        if (selected.size() <= 0) {
            // The user hasn't selected any suppliers,
            // show error
            Toast.makeText(this,
                    getString(R.string.error_selection_required),
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        return true;
    }

    private void onDataReceived(List<Supplier> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();

        // Data's ready, enable FAB
        b.fab.setEnabled(true);
    }

    private class GetSuppliersCallback extends Callback<List<Supplier>> {
        public GetSuppliersCallback() {
            super(SuppliersSelectActivity.this);
        }

        @Override
        public void onSuccess(List<Supplier> response) {
            onDataReceived(response);
        }

        @Override
        public void onError(Call<List<Supplier>> call,
                            Response<List<Supplier>> response) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
