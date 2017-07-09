package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.QuoteProductAdapter;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.Callback;
import br.com.wemind.marketplacetribanco.api.ListCallback;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.databinding.ActivityOngoingQuoteBinding;
import br.com.wemind.marketplacetribanco.databinding.FragmentOngoingQuoteProductListBinding;
import br.com.wemind.marketplacetribanco.databinding.FragmentOngoingQuoteRequestsBinding;
import br.com.wemind.marketplacetribanco.databinding.FragmentOngoingQuoteStatusBinding;
import br.com.wemind.marketplacetribanco.databinding.ItemOngoingQuoteRequestBinding;
import br.com.wemind.marketplacetribanco.databinding.ItemQuoteStatusBinding;
import br.com.wemind.marketplacetribanco.models.Offer;
import br.com.wemind.marketplacetribanco.models.PurchaseOrder;
import br.com.wemind.marketplacetribanco.models.Quote;
import br.com.wemind.marketplacetribanco.models.QuoteProduct;
import br.com.wemind.marketplacetribanco.models.QuoteStatus;
import br.com.wemind.marketplacetribanco.models.Supplier;
import br.com.wemind.marketplacetribanco.utils.QuoteAnalyser;
import retrofit2.Call;

public class OngoingQuoteActivity extends AppCompatActivity {

    public static final String INPUT_QUOTE = "input_quote";
    public static final String INPUT_IS_EDITABLE = "input_is_editable";
    public static final int REQUEST_EDIT_QUOTE_PRODUCT = 1;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ActivityOngoingQuoteBinding b;
    private boolean isEditable = false;
    private Quote quote;
    private TreeSet<Supplier> suppliers = new TreeSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_ongoing_quote);

        setSupportActionBar(b.toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        b.pager.setAdapter(sectionsPagerAdapter);

        b.tabLayout.setupWithViewPager(b.pager);

        isEditable = getIntent().getBooleanExtra(INPUT_IS_EDITABLE, false);

        quote = getIntent().getParcelableExtra(INPUT_QUOTE);
        if (quote == null)
            throw new IllegalStateException("INPUT_QUOTE must not be null");
        Collections.sort(quote.getQuoteProducts(), new QuoteProductComparator());

        if (quote.getName().length() > 0) {
            setTitle(quote.getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ongoing_quote, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (suppliers == null || suppliers.size() <= 0) {
            Api.api.getAllSuppliers().enqueue(new ListCallback<List<Supplier>>(this) {
                @Override
                public void onSuccess(List<Supplier> responseBody) {
                    suppliers = new TreeSet<>(responseBody);
                    onSuppliersReady(suppliers);
                }

                @Override
                public void onError(Call<List<Supplier>> call, ApiError responseErrorBody) {

                }
            });
        }
    }

    private void onSuppliersReady(TreeSet<Supplier> suppliers) {
        sectionsPagerAdapter.requestsFragment.setSupplierData(suppliers);
        sectionsPagerAdapter.requestsFragment.adapter.notifyDataSetChanged();
        sectionsPagerAdapter.statusFragment.setSupplierData(suppliers);
        sectionsPagerAdapter.statusFragment.adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_menu_save) {
            // Save quote
            Api.api.editQuote(quote, quote.getId())
                    .enqueue(new EditQuoteCallback());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isEditable && requestCode == REQUEST_EDIT_QUOTE_PRODUCT) {
            if (resultCode == RESULT_OK) {
                Quote resultQuote = data.getParcelableExtra(
                        QuoteProductActivity.RESULT_QUOTE);
                if (resultQuote != null) {
                    quote.setQuoteProducts(resultQuote.getQuoteProducts());

                    Collections.sort(quote.getQuoteProducts(),
                            new QuoteProductComparator());

                    // Update List adapter
                    QuoteProductAdapter adapter =
                            sectionsPagerAdapter.listFragment.adapter;
                    adapter.setData(quote);
                    adapter.notifyDataSetChanged();

                    // Update Status adapter
                    sectionsPagerAdapter.statusFragment
                            .inputStatus(QuoteStatus.fromQuote(quote));

                    // Update Requests adapter
                    QuoteAnalyser qa = new QuoteAnalyser(quote);
                    sectionsPagerAdapter.requestsFragment
                            .inputOffers(quote.getSuppliers(), qa.getBestOffers());
                }
            }
        }
    }

    public enum Page {
        Requests(R.string.text_tab_requests),
        Status(R.string.text_tab_status),
        List(R.string.text_tab_list);

        private final int labelResId;

        Page(@StringRes int stringId) {
            labelResId = stringId;
        }

        public int getLabelId() {
            return labelResId;
        }
    }

    public static class RequestsFragment extends Fragment {

        public static final String INPUT_OFFERS = "input_offers";
        public static final String INPUT_SUPPLIERS = "input_suppliers";
        private FragmentOngoingQuoteRequestsBinding b;
        private ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<>();
        private RequestsAdapter adapter;
        private TreeSet<Supplier> suppliers = new TreeSet<>();

        public static RequestsFragment newInstance(Quote quote) {
            // Build each Offer, which consists of a certain Supplier's
            // winning offers, and their corresponding values
            QuoteAnalyser quoteAnalyser = new QuoteAnalyser(quote);
            Bundle args = new Bundle();
            args.putParcelableArrayList(
                    INPUT_OFFERS,
                    new ArrayList<Parcelable>(quoteAnalyser.getBestOffers())
            );

            args.putParcelableArrayList(
                    INPUT_SUPPLIERS,
                    quote.getSuppliers()
            );

            RequestsFragment fragment = new RequestsFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Bundle args = getArguments();
            if (args != null) {
                ArrayList<Supplier> suppliers =
                        args.getParcelableArrayList(INPUT_SUPPLIERS);
                ArrayList<Offer> offers = args.getParcelableArrayList(INPUT_OFFERS);

                inputOffers(suppliers, offers);
            }
        }

        public void inputOffers(List<Supplier> suppliers, List<Offer> offers) {
            purchaseOrders = new ArrayList<>();
            if (suppliers != null && offers != null) {
                Map<Supplier, PurchaseOrder> supOrders = new TreeMap<>();
                for (Supplier supplier : suppliers) {
                    supOrders.put(
                            supplier,
                            new PurchaseOrder().setSupplier(supplier)
                    );
                }

                for (Offer offer : offers) {
                    supOrders.get(offer.getSupplier())
                            .addItem(offer.getProduct(), offer.getPrice());
                }
                purchaseOrders.addAll(supOrders.values());
            }

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            b = FragmentOngoingQuoteRequestsBinding.inflate(
                    inflater,
                    container,
                    false
            );

            b.list.setHasFixedSize(true);
            FragmentActivity context = getActivity();
            b.list.setLayoutManager(new LinearLayoutManager(context));
            adapter = new RequestsAdapter();
            b.list.setAdapter(adapter);

            return b.getRoot();
        }

        public void setSupplierData(TreeSet<Supplier> suppliers) {
            this.suppliers = suppliers;
            adapter.notifyDataSetChanged();
        }

        private class RequestsAdapter
                extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {
            private final Locale locale = new Locale("pt", "BR");

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(
                        ItemOngoingQuoteRequestBinding.inflate(
                                LayoutInflater.from(getActivity()),
                                parent,
                                false
                        ));
            }

            @Override
            public void onBindViewHolder(ViewHolder vh, int position) {
                PurchaseOrder purchaseOrder = purchaseOrders.get(position);
                vh.b.swSupplier.setChecked(true);

                Supplier tempSup = suppliers.floor(purchaseOrder.getSupplier());
                if (tempSup != null) {
                    vh.b.txtSupplierName.setText(
                            tempSup.getSupplierName()
                    );
                }

                String contactName = purchaseOrder.getSupplier().getContactName();
                if (contactName == null || contactName.length() == 0) {
                    contactName = purchaseOrder.getSupplier().getSupplierName();
                }

                vh.b.txtContactName.setText(contactName);

                vh.b.txtPrice.setText(String.format(
                        locale,
                        getString(R.string.money),
                        purchaseOrder.getPriceSum())
                );
                vh.b.txtQuantity.setText(String.valueOf(purchaseOrder.getItems().size()));
            }

            @Override
            public int getItemCount() {
                return purchaseOrders.size();
            }

            public class ViewHolder extends RecyclerView.ViewHolder {
                private ItemOngoingQuoteRequestBinding b;

                public ViewHolder(ItemOngoingQuoteRequestBinding binding) {
                    super(binding.getRoot());
                    b = binding;
                }
            }
        }
    }

    public static class ListFragment extends Fragment {
        public static final String INPUT_QUOTE = "input_quote";
        public static final String INPUT_IS_EDITABLE = "input_is_editable";
        private FragmentOngoingQuoteProductListBinding b;

        private Quote quote = new Quote();
        private boolean isEditable = false;
        private QuoteProductAdapter adapter;

        public static ListFragment newInstance(Quote quote, boolean isEditable) {
            Bundle args = new Bundle();
            args.putParcelable(INPUT_QUOTE, quote);
            args.putBoolean(INPUT_IS_EDITABLE, isEditable);

            ListFragment fragment = new ListFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Bundle args = getArguments();
            if (args != null) {
                quote = args.getParcelable(ListFragment.INPUT_QUOTE);
                isEditable = args.getBoolean(ListFragment.INPUT_IS_EDITABLE, false);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {

            b = FragmentOngoingQuoteProductListBinding
                    .inflate(inflater, container, false);

            b.list.setHasFixedSize(true);
            b.list.setLayoutManager(new LinearLayoutManager(getActivity()));

            adapter = new QuoteProductAdapter(getActivity(), quote, isEditable);
            b.list.setAdapter(adapter);

            return b.getRoot();
        }
    }

    public static class StatusFragment extends Fragment {

        public static final String INPUT_STATUSES = "input_statuses";
        public static final String INPUT_QUOTE_ID = "input_quote_id";
        private QuoteStatus statuses = new QuoteStatus();
        private FragmentOngoingQuoteStatusBinding b;
        private QuoteStatusAdapter adapter;
        private TreeSet<Supplier> suppliers = new TreeSet<>();
        private long quoteId = 0L;

        public static StatusFragment newInstance(Quote quote) {

            Bundle args = new Bundle();

            // FIXME: 02/07/2017 this is placeholder code while there's no API for getting the statuses
            QuoteStatus statuses = QuoteStatus.fromQuote(quote);
            args.putParcelable(INPUT_STATUSES, statuses);
            // end of placeholder code
            args.putLong(INPUT_QUOTE_ID, quote.getId());

            StatusFragment fragment = new StatusFragment();
            fragment.setArguments(args);
            return fragment;
        }

        public void inputStatus(QuoteStatus status) {
            this.statuses = status;
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Bundle args = getArguments();
            if (args != null) {
                QuoteStatus statuses = args.getParcelable(INPUT_STATUSES);
                if (statuses != null) {
                    this.statuses = statuses;
                }

                quoteId = args.getLong(INPUT_QUOTE_ID);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            b = FragmentOngoingQuoteStatusBinding.inflate(
                    inflater,
                    container,
                    false
            );

            b.list.setHasFixedSize(true);
            b.list.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new QuoteStatusAdapter();
            b.list.setAdapter(adapter);

            b.btnResendQuoteRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Api.api.sendAccessUrls(quoteId).enqueue(
                            new Callback<ApiError>(getContext()) {
                                @Override
                                public void onSuccess(ApiError response) {
                                    showApiError(response);
                                }

                                @Override
                                public void onError(Call<ApiError> call,
                                                    ApiError response) {
                                    showApiError(response);
                                }

                                public void showApiError(ApiError response) {
                                    Toast.makeText(
                                            context,
                                            response.getMessage(),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            });
                }
            });

            return b.getRoot();
        }

        public void setSupplierData(TreeSet<Supplier> suppliers) {
            this.suppliers = suppliers;
            adapter.notifyDataSetChanged();
        }

        private class QuoteStatusAdapter
                extends RecyclerView.Adapter<QuoteStatusAdapter.ViewHolder> {

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(ItemQuoteStatusBinding.inflate(
                        LayoutInflater.from(getActivity()),
                        parent,
                        false
                ));
            }

            @Override
            public void onBindViewHolder(ViewHolder vh, int position) {

                Supplier tempSup = suppliers.floor(statuses.getSupplier(position));
                if (tempSup != null) {
                    vh.b.companyName.setText(tempSup.getSupplierName());
                }
                vh.b.contactName.setText(statuses.getSupplier(position).getContactName());
                vh.b.status.setText(statuses.getHasResponded(position) ?
                        R.string.text_item_ongoing_quote_responded_true
                        : R.string.text_item_ongoing_quote_responded_false
                );
            }

            @Override
            public int getItemCount() {
                return statuses.size();
            }

            public class ViewHolder extends RecyclerView.ViewHolder {
                private ItemQuoteStatusBinding b;

                public ViewHolder(ItemQuoteStatusBinding b) {
                    super(b.getRoot());
                    this.b = b;
                }
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public RequestsFragment requestsFragment;
        public StatusFragment statusFragment;
        public ListFragment listFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment created = (Fragment) super.instantiateItem(container, position);
            switch (Page.values()[position]) {
                case Requests:
                    requestsFragment = (RequestsFragment) created;
                    break;
                case Status:
                    statusFragment = (StatusFragment) created;
                    break;
                case List:
                    listFragment = (ListFragment) created;
                    break;
            }

            return created;
        }

        @Override
        public Fragment getItem(int position) {
            switch (Page.values()[position]) {
                case Requests:
                    return RequestsFragment.newInstance(quote);
                case Status:
                    return StatusFragment.newInstance(quote);
                case List:
                    return ListFragment.newInstance(quote, isEditable);
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
                return OngoingQuoteActivity
                        .this.getString(Page.values()[position].getLabelId());
            }

            return null;
        }
    }

    private class EditQuoteCallback extends Callback<Quote> {
        public EditQuoteCallback() {
            super(OngoingQuoteActivity.this);
        }

        @Override
        public void onSuccess(Quote response) {
            finish();
        }

        @Override
        public void onError(Call<Quote> call, ApiError response) {
            finish();
        }
    }

    private class QuoteProductComparator implements Comparator<QuoteProduct> {
        @Override
        public int compare(QuoteProduct o1, QuoteProduct o2) {
            return (int) (o1.getId() - o2.getId());
        }
    }
}
