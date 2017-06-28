package br.com.wemind.marketplacetribanco.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.ListingsAdapter;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.Callback;
import br.com.wemind.marketplacetribanco.databinding.ContentListingsListBinding;
import br.com.wemind.marketplacetribanco.models.Listing;
import retrofit2.Call;
import retrofit2.Response;

public class ListingsSelectActivity extends BaseSelectActivity {

    public static final String SELECTED_LIST = "SELECTED_LIST";
    public static final String RESULT_BUNDLE = "result_bundle";
    public static final int QUERY_CHANGED_TIMEOUT = 700;

    private ContentListingsListBinding cb;
    private ListingsAdapter adapter;
    private ArrayList<Listing> data;
    private Timer preQueryTimer = newTimer();

    private Timer newTimer() {
        return new Timer("preQueryTimer");
    }

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
            public int cancelledTasks = 0;
            public TimerTask queryTask;

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
            public boolean onQueryTextChange(final String newText) {
                if (queryTask != null) {
                    queryTask.cancel();
                    cancelledTasks++;

                    if (cancelledTasks >= 100) {
                        // Purge Timer every 100 cancelled tasks
                        preQueryTimer.purge();
                        cancelledTasks = 0;
                    }
                }

                queryTask = new TimerTask() {
                    @Override
                    public void run() {
                        adapter.getFilter().filter(newText);
                    }
                };
                preQueryTimer.schedule(queryTask, QUERY_CHANGED_TIMEOUT);
                return true;
            }
        });
        // End of search view setup

        // Setup content view
        cb = ContentListingsListBinding
                .inflate(getLayoutInflater(), b.contentFrame, true);

        cb.list.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListingsAdapter(this, new ArrayList<Listing>(), true);
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
        Api.api.getAllListings().enqueue(new GetListingsCallback(this));
    }

    private void onDataReceived(List<Listing> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        this.data = new ArrayList<>(data);
        adapter = new ListingsAdapter(this, data, true);
        cb.list.setAdapter(adapter);
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
