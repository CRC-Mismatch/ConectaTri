package br.com.wemind.marketplacetribanco.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.SupplierAdapter;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.Callback;
import br.com.wemind.marketplacetribanco.api.ListCallback;
import br.com.wemind.marketplacetribanco.api.ValidationCallback;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.databinding.ContentSuppliersListBinding;
import br.com.wemind.marketplacetribanco.models.Supplier;
import retrofit2.Call;
import retrofit2.Response;

public class SuppliersListActivity extends BaseDrawerActivity {

    public static final int EDIT_SUPPLIER = 1;
    private static final int CREATE_SUPPLIER = 2;
    private ContentSuppliersListBinding cb;
    /**
     * Entire data payload received from retrieveData()
     */
    private ArrayList<Supplier> data;
    private SupplierAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup FAB
        b.fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white));
        b.fab.setVisibility(View.VISIBLE);
        b.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SuppliersListActivity.this,
                        SupplierCreateActivity.class);
                startActivityForResult(i, CREATE_SUPPLIER);
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Disable FAB while data isn't ready
        b.fab.setEnabled(false);

        retrieveData();
    }

    private void retrieveData() {
        Api.api.getAllSuppliers().enqueue(new GetSuppliersCallback());
    }

    void validateEmail(Supplier edited, int requestCode) {
        Api.api.validateEmail(edited.getContactEmail()).enqueue(new EmailValidation(edited, requestCode));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_SUPPLIER || requestCode == CREATE_SUPPLIER) {
            if (resultCode == RESULT_OK) {
                Supplier edited = data.getBundleExtra(SupplierCreateActivity.RESULT_BUNDLE)
                        .getParcelable(SupplierCreateActivity.RESULT_SUPPLIER);
                if (edited != null) {
                    validateEmail(edited, requestCode);
                }
            } else {

            }
        }
    }

    private void onDataReceived(ArrayList<Supplier> data) {
        this.data = data;
        adapter = new SupplierAdapter(this, data);
        cb.list.setAdapter(adapter);

        // Data's ready, enable FAB
        b.fab.setEnabled(true);
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

    public class DeleteSupplierCallback extends Callback<Supplier> {

        public DeleteSupplierCallback() {
            super(SuppliersListActivity.this);
        }

        @Override
        public void onSuccess(final Supplier response) {
            refreshData();

            // Show UNDO Snackbar
            Snackbar sb = Snackbar
                    .make(b.contentFrame, R.string.text_supplier_deleted, Snackbar.LENGTH_LONG);
            sb.setAction(R.string.text_undo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Re-add deleted supplier
                    Api.api.editSupplier(response, response.getId()).enqueue(
                            new CreateSupplierCallback(SuppliersListActivity.this));
                }
            });
            sb.show();
        }

        @Override
        public void onError(Call<Supplier> call, ApiError response) {
            refreshData();
        }

        private void refreshData() {
            if (context != null) {
                ((SuppliersListActivity) context).retrieveData();
            }
        }
    }

    private class GetSuppliersCallback extends ListCallback<List<Supplier>> {
        public GetSuppliersCallback() {
            super(SuppliersListActivity.this);
        }

        @Override
        public void onSuccess(List<Supplier> response) {
            onDataReceived(new ArrayList<>(response));
        }

        @Override
        public void onError(Call<List<Supplier>> call,
                            ApiError response) {
            Toast.makeText(
                    context,
                    getString(R.string.text_connection_failed),
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        }
    }

    private class EditSupplierCallback extends Callback<Supplier> {
        public EditSupplierCallback(@NonNull Context context) {
            super(context);
        }

        @Override
        public void onSuccess(Supplier response) {
            refreshData();
        }

        private void refreshData() {
            // Refresh suppliers list
            retrieveData();
        }

        @Override
        public void onError(Call<Supplier> call, ApiError response) {
            refreshData();
        }
    }

    private class CreateSupplierCallback extends Callback<Supplier> {
        public CreateSupplierCallback(@NonNull Context context) {
            super(context);
        }

        private void refreshData() {
            retrieveData();
        }

        @Override
        public void onSuccess(Supplier response) {
            refreshData();
        }

        @Override
        public void onError(Call<Supplier> call, ApiError response) {
            refreshData();
        }
    }

    private class EmailValidation extends ValidationCallback {
        private final int requestCode;
        private final Supplier edited;

        public EmailValidation(Supplier edited, int requestCode) {
            super(SuppliersListActivity.this);
            this.edited = edited;
            this.requestCode = requestCode;
        }

        @Override
        public void onSuccess(Boolean response) {
            if (response == true) {
                switch (requestCode) {
                    case EDIT_SUPPLIER:
                        Api.api.editSupplier(edited, edited.getId())
                                .enqueue(new EditSupplierCallback(context));
                        break;
                    case CREATE_SUPPLIER:
                        Api.api.addSupplier(edited).enqueue(
                                new CreateSupplierCallback(context));
                        break;
                }
            } else {
                //show error message
                Toast.makeText(context,
                        R.string.invalid_mail,
                        Toast.LENGTH_LONG
                ).show();
                //return to edit/create supplier activity.
                Bundle toEdit = new Bundle();
                toEdit.putParcelable(SupplierCreateActivity.INPUT_SUPPLIER, edited);
                Intent edit = new Intent(context, SupplierCreateActivity.class);
                edit.putExtra(SupplierCreateActivity.INPUT_BUNDLE, toEdit);
                ((Activity) context).startActivityForResult(edit, SuppliersListActivity.EDIT_SUPPLIER);

            }
        }

        @Override
        public void onError(Call<Boolean> call, ApiError response) {

        }
    }
}
