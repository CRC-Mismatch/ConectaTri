package br.com.wemind.marketplacetribanco.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import java.util.ArrayList;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.activities.OngoingQuoteActivity;
import br.com.wemind.marketplacetribanco.activities.QuoteProductActivity;
import br.com.wemind.marketplacetribanco.databinding.ItemSimpleProductBinding;
import br.com.wemind.marketplacetribanco.models.Quote;
import br.com.wemind.marketplacetribanco.models.QuoteProduct;

public class QuoteProductAdapter
        extends RecyclerView.Adapter<QuoteProductAdapter.ViewHolder>
        implements Filterable {

    private Context context;
    private Quote quote;
    private QuoteProductAdapter.Filter filter = new QuoteProductAdapter.Filter();
    private ArrayList<QuoteProduct> filteredData;
    private boolean isEditable;

    public QuoteProductAdapter(Context context, Quote quote, boolean isEditable) {
        this.context = context;
        setData(quote);
        this.isEditable = isEditable;
    }

    public void setData(Quote quote) {
        this.quote = quote;
        this.filteredData = new ArrayList<>(quote.getQuoteProducts());
    }

    @Override
    public QuoteProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        return new QuoteProductAdapter.ViewHolder(
                (ItemSimpleProductBinding) DataBindingUtil.inflate(
                        LayoutInflater.from(context),
                        R.layout.item_simple_product,
                        parent,
                        false
                ));
    }

    @Override
    public void onBindViewHolder(QuoteProductAdapter.ViewHolder vh, int position) {
        final QuoteProduct product = filteredData.get(position);
        vh.b.product.setText(product.getProduct().getName());
        vh.b.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, QuoteProductActivity.class);
                i.putExtra(QuoteProductActivity.QUOTE_PRODUCT, product);
                i.putExtra(QuoteProductActivity.INPUT_IS_EDITABLE, isEditable);
                ((OngoingQuoteActivity) context).startActivityForResult(i,
                        OngoingQuoteActivity.REQUEST_EDIT_QUOTE_PRODUCT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    @Override
    public QuoteProductAdapter.Filter getFilter() {
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSimpleProductBinding b;

        public ViewHolder(ItemSimpleProductBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

    public class Filter extends android.widget.Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO: inspect this
            // Naive filtering
            ArrayList<QuoteProduct> filtered = new ArrayList<>();
            for (QuoteProduct product : quote.getQuoteProducts()) {
                if (product.getProduct().getName().contains(constraint)) {
                    filtered.add(product);
                }
            }

            // Pack and return results
            FilterResults result = new FilterResults();
            result.count = filtered.size();
            result.values = filtered;
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<QuoteProduct>) results.values;
            notifyDataSetChanged();
        }
    }
}
