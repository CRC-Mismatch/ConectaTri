package br.com.wemind.marketplacetribanco.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.ListingsAdapter;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.Callback;
import br.com.wemind.marketplacetribanco.databinding.ContentListingsListBinding;
import br.com.wemind.marketplacetribanco.models.Listing;
import br.com.wemind.marketplacetribanco.models.ListingProduct;
import br.com.wemind.marketplacetribanco.models.Product;
import br.com.wemind.marketplacetribanco.models.Supplier;
import retrofit2.Call;
import retrofit2.Response;

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
        retrieveData();
    }

    private void retrieveData() {
        // Disable FAB while data hasn't been retrieved
        b.fab.setEnabled(false);
        Api.api.getAllListings().enqueue(new GetListingsCallback(this));
    }

    private void onDataReceived(List<Listing> data) {
        this.data = new ArrayList<>(data);
        adapter = new ListingsAdapter(this, data, true);
        cb.list.setAdapter(adapter);

        // Disable FAB while data hasn't been retrieved
        b.fab.setEnabled(true);
    }

    private class GetListingsCallback extends Callback<List<Listing>> {

        public GetListingsCallback(@NonNull Context context) {
            super(context);
        }

        @Override
        public void onSuccess(List<Listing> response) {
            onDataReceived(response);
        }

        @Override
        public void onError(Call<List<Listing>> call, Response<List<Listing>> response) {
            Toast.makeText(context,
                    getString(R.string.text_connection_failed), Toast.LENGTH_SHORT
            ).show();
        }
    }
}
