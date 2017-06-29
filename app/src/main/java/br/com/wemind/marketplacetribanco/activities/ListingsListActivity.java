package br.com.wemind.marketplacetribanco.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.ListingsAdapter;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.Callback;
import br.com.wemind.marketplacetribanco.api.ListCallback;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.databinding.ContentListingsListBinding;
import br.com.wemind.marketplacetribanco.models.Listing;
import br.com.wemind.marketplacetribanco.utils.TimerManager;
import retrofit2.Call;

public class ListingsListActivity extends BaseDrawerActivity {

    public static final int CREATE_LISTING = 1;
    public static final int EDIT_LISTING = 2;
    public static final int TIMER_DELAY = 500;
    private ContentListingsListBinding cb;
    private ListingsAdapter adapter;
    private ArrayList<Listing> data;
    private TimerManager timerManager;

    @Override
    protected void onPause() {
        timerManager.cancel();
        super.onPause();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        b.fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white));
        b.fab.setVisibility(View.VISIBLE);

        timerManager = new TimerManager("preQueryTimer");
        b.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListingsListActivity.this, ListingCreateActivity.class);
                startActivityForResult(i, CREATE_LISTING);
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
            public int cancelledTasks = 0;
            public TimerTask timerTask;

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
                if (newText.length() > 2 || newText.length() <= 0) {
                    timerManager.schedule(TIMER_DELAY, new TimerTask() {
                        @Override
                        public void run() {
                            adapter.getFilter().filter(newText);
                        }
                    });
                }

                return true;
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
        timerManager.restart();
        // FIXME: 24/05/2017 actually retrieve data
        retrieveData();
    }

    private void retrieveData() {
        Api.api.getAllListings().enqueue(new GetListingsCallback(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_LISTING) {
            if (resultCode == RESULT_OK) {
                Listing edited = data.getBundleExtra(ListingCreateActivity.RESULT_BUNDLE)
                        .getParcelable(ListingCreateActivity.RESULT_LISTING);

                if (edited != null) {
                    Api.api.addListing(edited).enqueue(new CreateListingCallback(this));
                }
            } else {
                // FIXME: 27/05/2017 handle cancellation

            }
        } else if (requestCode == EDIT_LISTING) {
            if (resultCode == RESULT_OK) {
                Listing edited = data.getBundleExtra(ListingCreateActivity.RESULT_BUNDLE)
                        .getParcelable(ListingCreateActivity.RESULT_LISTING);

                if (edited != null) {
                    Api.api.editListing(edited, edited.getId())
                            .enqueue(new CreateListingCallback(this));
                }
            } else {
                // FIXME: 27/05/2017 handle cancellation

            }
        }
    }

    private void onDataReceived(List<Listing> data) {
        this.data = new ArrayList<>(data);
        adapter = new ListingsAdapter(this, data, false);
        cb.list.setAdapter(adapter);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_listings;
    }

    private class GetListingsCallback extends ListCallback<List<Listing>> {
        public GetListingsCallback(Context context) {
            super(context);
        }

        @Override
        public void onSuccess(List<Listing> response) {
            onDataReceived(response);
        }

        @Override
        public void onError(Call<List<Listing>> call, ApiError response) {
            Toast.makeText(context,
                    getString(R.string.text_connection_failed),
                    Toast.LENGTH_SHORT
            ).show();
            finish();
        }
    }

    private class CreateListingCallback extends Callback<Listing> {
        public CreateListingCallback(@NonNull Context context) {
            super(context);
        }

        @Override
        public void onSuccess(Listing response) {
            retrieveData();
        }

        @Override
        public void onError(Call<Listing> call, ApiError response) {
            retrieveData();
        }
    }

    public class DeleteListingCallback extends Callback<Listing> {

        public DeleteListingCallback(@NonNull Context context) {
            super(context);
        }

        @Override
        public void onSuccess(final Listing response) {
            retrieveData();

            // Show UNDO Snackbar
            Snackbar sb = Snackbar
                    .make(b.contentFrame, R.string.text_listing_deleted,
                            Snackbar.LENGTH_LONG);
            sb.setAction(R.string.text_undo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Re-add deleted list
                    Api.api.editListing(response, response.getId()).enqueue(
                            new CreateListingCallback(ListingsListActivity.this));
                }
            });
            sb.show();
        }

        @Override
        public void onError(Call<Listing> call, ApiError response) {
            retrieveData();
        }
    }
}
