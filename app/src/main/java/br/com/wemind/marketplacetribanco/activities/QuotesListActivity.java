package br.com.wemind.marketplacetribanco.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.Callback;
import br.com.wemind.marketplacetribanco.api.ListCallback;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.databinding.ContentQuotesListBinding;
import br.com.wemind.marketplacetribanco.databinding.FragmentClosedQuotesListBinding;
import br.com.wemind.marketplacetribanco.databinding.FragmentOngoingQuotesListBinding;
import br.com.wemind.marketplacetribanco.databinding.ItemQuoteManualBinding;
import br.com.wemind.marketplacetribanco.databinding.ItemQuoteRemoteBinding;
import br.com.wemind.marketplacetribanco.models.Quote;
import br.com.wemind.marketplacetribanco.utils.Alerts;
import retrofit2.Call;

public class QuotesListActivity extends BaseDrawerActivity {

    public static final int REQUEST_EDIT_QUOTE = 2;
    public static final String REMOTE_ONLY = "REMOTE_ONLY";
    private ContentQuotesListBinding cb;
    private ArrayList<Quote> data = new ArrayList<>();
    private boolean remoteOnly;
    private SectionsPagerAdapter sectionsPagerAdapter;

    public static void showReaddSnackbar(
            final Quote response,
            View snackbarView,
            final Callback<Quote> callback
    ) {
        Snackbar sb = Snackbar.make(
                snackbarView, R.string.text_quote_deleted, Snackbar.LENGTH_LONG);

        sb.setAction(R.string.text_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Re-add deleted supplier
                Api.api.editQuote(response, response.getId()).enqueue(
                        callback
                );
            }
        });
        sb.show();
    }

    public static void showApiError(ApiError response, Context context) {
        Toast.makeText(
                context,
                response.getMessage(),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        remoteOnly = getIntent().getBooleanExtra(REMOTE_ONLY, false);

        // Setup content view
        cb = ContentQuotesListBinding
                .inflate(getLayoutInflater(), b.contentFrame, true);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        cb.pager.setAdapter(sectionsPagerAdapter);

        cb.tabLayout.setupWithViewPager(cb.pager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_QUOTE) {
            if (resultCode == RESULT_OK) {
                Quote edited = data.getBundleExtra(QuoteCreateActivity.RESULT_BUNDLE)
                        .getParcelable(QuoteCreateActivity.RESULT_QUOTE);

                if (edited != null) {
                    Api.api.editQuote(edited, edited.getId()).enqueue(
                            new EditQuoteCallback(this)
                    );
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return BaseDrawerActivity.ID_NONE_VOLATILE;
    }

    public enum Page {
        Ongoing(R.string.text_tab_ongoing_quotes),
        Closed(R.string.text_tab_closed_quotes);

        private final int labelResId;

        Page(@StringRes int stringId) {
            labelResId = stringId;
        }

        public int getLabelId() {
            return labelResId;
        }
    }

    public static class OngoingQuotesFragment extends Fragment {
        private FragmentOngoingQuotesListBinding b;
        private OngoingQuotesAdapter adapter;

        public OngoingQuotesFragment() {
        }

        public static Fragment newInstance() {
            return new OngoingQuotesFragment();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            b = FragmentOngoingQuotesListBinding
                    .inflate(inflater, container, false);

            b.list.setHasFixedSize(true);
            b.list.setLayoutManager(new LinearLayoutManager(getActivity()));

            adapter = new OngoingQuotesAdapter();
            b.list.setAdapter(adapter);

            return b.getRoot();
        }

        @Override
        public void onResume() {
            super.onResume();
            adapter.refreshData();
        }

        private class OngoingQuotesAdapter
                extends RecyclerView.Adapter<OngoingQuotesAdapter.ViewHolder> {
            private List<Quote> filteredData = new ArrayList<>();

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolder vh = null;

                if (viewType == Quote.TYPE_MANUAL) {
                    vh = new ManualViewHolder(ItemQuoteManualBinding.inflate(
                            LayoutInflater.from(getActivity()),
                            parent,
                            false
                    ));
                } else if (viewType == Quote.TYPE_REMOTE) {
                    vh = new RemoteViewHolder(ItemQuoteRemoteBinding.inflate(
                            LayoutInflater.from(getActivity()),
                            parent,
                            false
                    ));
                }

                return vh;
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                holder.setup(filteredData.get(position));
            }

            @Override
            public int getItemCount() {
                return filteredData.size();
            }

            @Override
            public int getItemViewType(int position) {
                return filteredData.get(position).getType();
            }

            private void refreshData() {
                Api.api.getCurrentQuotes().enqueue(
                        new ListCallback<List<Quote>>(getContext()) {
                            @Override
                            public void onSuccess(List<Quote> responseBody) {
                                filteredData = responseBody;
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Call<List<Quote>> call,
                                                ApiError responseErrorBody) {
                                showApiError(responseErrorBody, context);
                            }
                        });
            }

            private void startQuoteEditingActivity(Quote data) {
                Intent i = new Intent(getActivity(), QuoteCreateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(QuoteCreateActivity.INPUT_QUOTE, data);
                i.putExtra(QuoteCreateActivity.INPUT_BUNDLE, bundle);

                getActivity().startActivityForResult(
                        i, QuotesListActivity.REQUEST_EDIT_QUOTE);
            }

            public void startQuoteDetailsActivity(Quote data) {
                Intent i = new Intent(getActivity(), OngoingQuoteActivity.class);
                i.putExtra(OngoingQuoteActivity.INPUT_QUOTE, (Parcelable) data);
                i.putExtra(
                        OngoingQuoteActivity.INPUT_IS_EDITABLE,
                        data.getType() == Quote.TYPE_MANUAL
                );
                startActivity(i);
            }

            public abstract class ViewHolder extends RecyclerView.ViewHolder {
                public ViewHolder(View v) {
                    super(v);
                }

                public abstract void setup(Quote data);
            }

            private class ManualViewHolder extends ViewHolder {
                private ItemQuoteManualBinding b;

                public ManualViewHolder(ItemQuoteManualBinding b) {
                    super(b.getRoot());
                    this.b = b;
                }

                @Override
                public void setup(final Quote data) {
                    b.txtListingName.setText(data.getName());
                    b.txtItemCount.setText(String.valueOf(data.getQuoteProducts().size()));

                    b.btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startQuoteEditingActivity(data);
                        }
                    });

                    b.btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alerts.getDeleteConfirmationAlert(
                                    data.getName(),
                                    getContext(),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Api.api.deleteQuote(data.getId())
                                                    .enqueue(new DeleteQuoteCallback());
                                        }
                                    },
                                    null
                            ).show();
                        }
                    });

                    b.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startQuoteDetailsActivity(data);
                        }
                    });
                }
            }

            private class RemoteViewHolder extends ViewHolder {
                private ItemQuoteRemoteBinding b;

                public RemoteViewHolder(ItemQuoteRemoteBinding b) {
                    super(b.getRoot());
                    this.b = b;
                }

                @Override
                public void setup(final Quote data) {
                    b.txtListingName.setText(data.getName());
                    b.txtItemCount.setText(String.valueOf(data.getQuoteProducts().size()));

                    b.btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startQuoteEditingActivity(data);
                        }
                    });

                    b.btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alerts.getDeleteConfirmationAlert(
                                    data.getName(),
                                    getContext(),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Api.api.deleteQuote(data.getId())
                                                    .enqueue(new DeleteQuoteCallback());
                                        }
                                    },
                                    null
                            ).show();
                        }
                    });

                    b.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startQuoteDetailsActivity(data);
                        }
                    });
                }
            }

            private class DeleteQuoteCallback extends Callback<Quote> {
                public DeleteQuoteCallback() {
                    super(getContext());
                }

                @Override
                public void onSuccess(Quote response) {
                    refreshData();
                    showReaddSnackbar(response, b.getRoot(),
                            new Callback<Quote>(getContext()) {
                                @Override
                                public void onSuccess(Quote response) {
                                    refreshData();
                                }

                                @Override
                                public void onError(Call<Quote> call, ApiError response) {
                                    refreshData();

                                    showApiError(response, context);
                                }
                            });
                }

                @Override
                public void onError(Call<Quote> call, ApiError response) {

                }
            }
        }
    }

    public static class ClosedQuotesFragment extends Fragment {
        private FragmentClosedQuotesListBinding b;
        private ClosedQuotesAdapter adapter;

        public ClosedQuotesFragment() {
        }

        public static Fragment newInstance() {
            return new ClosedQuotesFragment();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            b = FragmentClosedQuotesListBinding
                    .inflate(inflater, container, false);

            b.list.setHasFixedSize(true);
            b.list.setLayoutManager(new LinearLayoutManager(getActivity()));

            adapter = new ClosedQuotesAdapter();
            b.list.setAdapter(adapter);

            return b.getRoot();
        }

        @Override
        public void onResume() {
            super.onResume();
            adapter.refreshData();
        }

        private class ClosedQuotesAdapter
                extends RecyclerView.Adapter<ClosedQuotesAdapter.ViewHolder> {
            private List<Quote> filteredData = new ArrayList<>();

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolder vh = null;

                if (viewType == Quote.TYPE_MANUAL) {
                    vh = new ManualViewHolder(ItemQuoteManualBinding.inflate(
                            LayoutInflater.from(getActivity()),
                            parent,
                            false
                    ));
                } else if (viewType == Quote.TYPE_REMOTE) {
                    vh = new RemoteViewHolder(ItemQuoteRemoteBinding.inflate(
                            LayoutInflater.from(getActivity()),
                            parent,
                            false
                    ));
                }

                return vh;
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                holder.setup(filteredData.get(position));
            }

            @Override
            public int getItemCount() {
                return filteredData.size();
            }

            @Override
            public int getItemViewType(int position) {
                return filteredData.get(position).getType();
            }

            private void refreshData() {
                Api.api.getClosedQuotes().enqueue(
                        new ListCallback<List<Quote>>(getContext()) {
                            @Override
                            public void onSuccess(List<Quote> responseBody) {
                                filteredData = responseBody;
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Call<List<Quote>> call,
                                                ApiError responseErrorBody) {
                                showApiError(responseErrorBody, context);
                            }
                        });
            }

            public void startQuoteDetailsActivity(Quote data) {
                Intent i = new Intent(getActivity(), OngoingQuoteActivity.class);
                i.putExtra(OngoingQuoteActivity.INPUT_QUOTE, (Parcelable) data);
                startActivity(i);
            }

            private void startQuoteEditingActivity(Quote data) {
                Intent i = new Intent(getActivity(), QuoteCreateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(QuoteCreateActivity.INPUT_QUOTE, data);
                i.putExtra(QuoteCreateActivity.INPUT_BUNDLE, bundle);

                getActivity().
                        startActivityForResult(i, QuotesListActivity.REQUEST_EDIT_QUOTE);
            }

            public abstract class ViewHolder extends RecyclerView.ViewHolder {
                public ViewHolder(View v) {
                    super(v);
                }

                public abstract void setup(Quote data);
            }

            private class ManualViewHolder extends ViewHolder {
                private ItemQuoteManualBinding b;

                public ManualViewHolder(ItemQuoteManualBinding b) {
                    super(b.getRoot());
                    this.b = b;
                }

                @Override
                public void setup(final Quote data) {
                    b.txtListingName.setText(data.getName());
                    b.txtItemCount.setText(String.valueOf(data.getQuoteProducts().size()));

                    b.btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startQuoteEditingActivity(data);
                        }
                    });

                    b.btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Api.api.deleteQuote(data.getId())
                                    .enqueue(new DeleteQuoteCallback());
                        }
                    });

                    b.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startQuoteDetailsActivity(data);
                        }
                    });
                }
            }

            private class RemoteViewHolder extends ViewHolder {
                private ItemQuoteRemoteBinding b;

                public RemoteViewHolder(ItemQuoteRemoteBinding b) {
                    super(b.getRoot());
                    this.b = b;
                }

                @Override
                public void setup(final Quote data) {
                    b.txtListingName.setText(data.getName());
                    b.txtItemCount.setText(String.valueOf(data.getQuoteProducts().size()));

                    b.btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startQuoteEditingActivity(data);
                        }
                    });

                    b.btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Api.api.deleteQuote(data.getId())
                                    .enqueue(new DeleteQuoteCallback());
                        }
                    });

                    b.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startQuoteDetailsActivity(data);
                        }
                    });
                }
            }

            private class DeleteQuoteCallback extends Callback<Quote> {
                public DeleteQuoteCallback() {
                    super(getContext());
                }

                @Override
                public void onSuccess(Quote response) {
                    refreshData();
                    showReaddSnackbar(response, b.getRoot(),
                            new Callback<Quote>(getContext()) {
                                @Override
                                public void onSuccess(Quote response) {
                                    refreshData();
                                }

                                @Override
                                public void onError(Call<Quote> call, ApiError response) {
                                    refreshData();

                                    showApiError(response, context);
                                }
                            });
                }

                @Override
                public void onError(Call<Quote> call, ApiError response) {

                }
            }
        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public OngoingQuotesFragment ongoingFragment;
        public ClosedQuotesFragment closedFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment created = (Fragment) super.instantiateItem(container, position);
            switch (Page.values()[position]) {
                case Ongoing:
                    ongoingFragment = (OngoingQuotesFragment) created;
                    break;
                case Closed:
                    closedFragment = (ClosedQuotesFragment) created;
                    break;
            }

            return created;
        }

        @Override
        public Fragment getItem(int position) {
            switch (Page.values()[position]) {
                case Ongoing:
                    return OngoingQuotesFragment.newInstance();
                case Closed:
                    return ClosedQuotesFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return Page.values().length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < Page.values().length) {
                return getString(Page.values()[position].getLabelId());
            }

            return null;
        }
    }

    private class EditQuoteCallback extends Callback<Quote> {
        public EditQuoteCallback(Context context) {
            super(context);
        }

        @Override
        public void onSuccess(Quote response) {
            sectionsPagerAdapter.ongoingFragment.adapter.refreshData();
            sectionsPagerAdapter.closedFragment.adapter.refreshData();
        }

        @Override
        public void onError(Call<Quote> call, ApiError response) {
            showApiError(response, context);
        }
    }
}
