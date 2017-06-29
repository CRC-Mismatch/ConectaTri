package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
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
import br.com.wemind.marketplacetribanco.api.ListCallback;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.databinding.ContentSuppliersListBinding;
import br.com.wemind.marketplacetribanco.models.Supplier;
import retrofit2.Call;
import retrofit2.Response;

public class SuppliersSelectActivity extends BaseSelectActivity {

    public static final String RESULT_BUNDLE = "result_bundle";
    public static final String SELECTED_LIST = "selected_list";
    public static final String INPUT_BUNDLE = "input_bundle";
    public static final String INPUT_SUPPLIERS = "input_suppliers";
    public static final String INPUT_SELECTED = "input_selected";
    private static final int REQUEST_CREATE_SUPPLIER = 1;
    private ContentSuppliersListBinding cb;

    private Set<Supplier> suppliers;
    private SelectionSupplierAdapter adapter;
    private boolean isDataReady;

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
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        // End of search view setup

        // Setup FAB for on-the-go Supplier creation
        b.fab.setVisibility(View.VISIBLE);
        b.fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white));
        b.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCreateSupplier();
            }
        });
        // End

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


        List<Supplier> preSelected = new ArrayList<>();
        if (inputBundle != null) {
            preSelected = inputBundle.getParcelableArrayList(INPUT_SELECTED);
            if (preSelected == null) {
                preSelected = new ArrayList<>(suppliers);
            }
        }

        adapter = new SelectionSupplierAdapter(
                this, new ArrayList<>(suppliers), preSelected);

        cb.list.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CREATE_SUPPLIER) {
            if (resultCode == RESULT_OK) {
                Bundle resultBundle =
                        data.getBundleExtra(SupplierCreateActivity.RESULT_BUNDLE);
                if (resultBundle != null) {
                    Supplier newSupplier = resultBundle
                            .getParcelable(SupplierCreateActivity.RESULT_SUPPLIER);
                    if (newSupplier != null) {
                        Api.api.addSupplier(newSupplier)
                                .enqueue(new AddSupplierCallback());
                    }
                }
            }
        }
    }

    private void requestCreateSupplier() {
        Intent i = new Intent(this, SupplierCreateActivity.class);
        startActivityForResult(i, REQUEST_CREATE_SUPPLIER);
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
            retrieveAllSuppliers();
        } else {
            isDataReady = true;
        }
    }

    private void retrieveAllSuppliers() {
        // Disable "next" while data is not ready
        isDataReady = false;

        // Disable FAB while data is not ready
        b.fab.setEnabled(false);

        Api.api.getAllSuppliers().enqueue(new GetSuppliersCallback());
    }

    @Override
    protected boolean mayContinue() {
        if (!isDataReady) {
            return false;
        }

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

    private void onDataReceived(List<Supplier> data) {
        if (suppliers == null || suppliers.size() <= 0) {
            adapter = new SelectionSupplierAdapter(this, data, data);
            cb.list.setAdapter(adapter);
        }
        adapter.setData(data);
        adapter.notifyDataSetChanged();
        isDataReady = true;
        // Data's ready, enable FAB
        b.fab.setEnabled(true);
    }

    private class GetSuppliersCallback extends ListCallback<List<Supplier>> {
        public GetSuppliersCallback() {
            super(SuppliersSelectActivity.this);
        }

        @Override
        public void onSuccess(List<Supplier> response) {
            onDataReceived(response);
        }

        @Override
        public void onError(Call<List<Supplier>> call,
                            ApiError response) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private class AddSupplierCallback extends Callback<Supplier> {

        public AddSupplierCallback() {
            super(SuppliersSelectActivity.this);
        }

        @Override
        public void onSuccess(Supplier response) {
            retrieveAllSuppliers();
        }

        @Override
        public void onError(Call<Supplier> call, ApiError response) {
            Toast.makeText(context,
                    getString(R.string.error_creation_failed),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
