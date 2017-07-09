package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.adapters.QuoteSupplierAdapter;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.ListCallback;
import br.com.wemind.marketplacetribanco.api.objects.ApiError;
import br.com.wemind.marketplacetribanco.databinding.ActivityQuoteProductBinding;
import br.com.wemind.marketplacetribanco.databinding.FragmentQuoteProductBinding;
import br.com.wemind.marketplacetribanco.models.Quote;
import br.com.wemind.marketplacetribanco.models.QuoteProduct;
import br.com.wemind.marketplacetribanco.models.Supplier;
import retrofit2.Call;

public class QuoteProductActivity extends BaseCreateActivity {

    public static final String INPUT_IS_EDITABLE = "input_is_editable";
    public static final String RESULT_QUOTE = "result_quote";
    public static final String INPUT_QUOTE = "input_quote";
    public static final String INPUT_INITIAL_POSITION = "input_initial_position";
    public TreeSet<Supplier> suppliers = new TreeSet<>();
    ActivityQuoteProductBinding b;
    private boolean isEditable;
    private Quote quote = new Quote();
    private SectionsPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_quote_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isEditable = getIntent().getBooleanExtra(INPUT_IS_EDITABLE, false);
        quote = getIntent().getParcelableExtra(INPUT_QUOTE);

        adapter = new SectionsPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Api.api.getAllSuppliers().enqueue(new ListCallback<List<Supplier>>(this) {

            @Override
            public void onSuccess(List<Supplier> responseBody) {
                suppliers = new TreeSet<>(responseBody);
                b.pager.setAdapter(adapter);
                b.pager.setCurrentItem(getIntent().getIntExtra(INPUT_INITIAL_POSITION, 0));
            }

            @Override
            public void onError(Call<List<Supplier>> call, ApiError responseErrorBody) {

            }
        });
    }

    @Override
    protected boolean validateForm() {
        return true;
    }

    @Override
    protected Intent getResultIntent() {
        Intent i = new Intent();
        if (isEditable) {
            i.putExtra(RESULT_QUOTE, (Parcelable) quote);
        }
        return i;
    }

    private void acceptSetQuoteProduct(QuoteProductFragment quoteProductFragment, int index) {
        quoteProductFragment.setQuoteProduct(quote.getQuoteProducts().get(index));
    }

    public static class QuoteProductFragment extends Fragment {

        public static final String INPUT_TYPE = "input_type";
        private PositionType type;

        enum PositionType {
            First, Middle, Last
        }

        public static final String INPUT_QUOTE_PRODUCT = "input_quote_product";
        public static final String INPUT_IS_EDITABLE = "input_is_editable";
        public static final String INPUT_SUPPLIERS = "input_suppliers";
        public static final String INPUT_POSITION = "input_position";
        private QuoteProduct quoteProduct = new QuoteProduct();
        private FragmentQuoteProductBinding b;
        private boolean isEditable;
        private QuoteSupplierAdapter adapter;
        private ArrayList<Supplier> suppliers = new ArrayList<>();
        private int position;

        public static QuoteProductFragment newInstance(boolean isEditable,
                                                       TreeSet<Supplier> suppliers,
                                                       int position, PositionType type) {
            Bundle args = new Bundle();
            args.putBoolean(INPUT_IS_EDITABLE, isEditable);
            args.putParcelableArrayList(INPUT_SUPPLIERS, new ArrayList<>(suppliers));
            args.putInt(INPUT_POSITION, position);
            args.putSerializable(INPUT_TYPE, type);

            QuoteProductFragment fragment = new QuoteProductFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            isEditable = getArguments().getBoolean(INPUT_IS_EDITABLE, false);
            quoteProduct = getArguments().getParcelable(INPUT_QUOTE_PRODUCT);
            suppliers = getArguments().getParcelableArrayList(INPUT_SUPPLIERS);
            position = getArguments().getInt(INPUT_POSITION, 0);
            type = (PositionType) getArguments().getSerializable(INPUT_TYPE);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            b = FragmentQuoteProductBinding.inflate(inflater, container, false);
            ((QuoteProductActivity) getContext()).acceptSetQuoteProduct(this, position);
            adapter = new QuoteSupplierAdapter(
                    getActivity(), quoteProduct, isEditable, new TreeSet<>(suppliers));

            switch (type) {
                case First:
                    b.imgChevronPrev.setVisibility(View.INVISIBLE);
                    b.imgChevronNext.setVisibility(View.VISIBLE);
                    break;
                case Middle:
                    b.imgChevronPrev.setVisibility(View.VISIBLE);
                    b.imgChevronNext.setVisibility(View.VISIBLE);
                    break;
                case Last:
                    b.imgChevronPrev.setVisibility(View.VISIBLE);
                    b.imgChevronNext.setVisibility(View.INVISIBLE);
                    break;
            }

            b.list.setLayoutManager(new LinearLayoutManager(getContext()));
            b.productName.setText(quoteProduct.getProduct().getName());
            b.productEan.setText(quoteProduct.getProduct().getEAN());

            b.list.setAdapter(adapter);

            return b.getRoot();
        }

        public void setQuoteProduct(QuoteProduct quoteProduct) {
            this.quoteProduct = quoteProduct;
        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            QuoteProductFragment.PositionType positionType;
            if (position == 0) {
                positionType = QuoteProductFragment.PositionType.First;
            } else if (position != getCount() - 1) {
                positionType = QuoteProductFragment.PositionType.Middle;
            } else {
                positionType = QuoteProductFragment.PositionType.Last;
            }

            return QuoteProductFragment.newInstance(
                    isEditable, suppliers, position, positionType);
        }

        @Override
        public int getCount() {
            return quote.getQuoteProducts().size();
        }
    }
}
