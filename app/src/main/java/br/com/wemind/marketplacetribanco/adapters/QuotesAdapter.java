package br.com.wemind.marketplacetribanco.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.activities.OngoingQuoteActivity;
import br.com.wemind.marketplacetribanco.activities.QuoteCreateActivity;
import br.com.wemind.marketplacetribanco.activities.QuoteProductsListActivity;
import br.com.wemind.marketplacetribanco.activities.QuotesListActivity;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.models.Quote;
import br.com.wemind.marketplacetribanco.utils.Alerts;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.ViewHolder>
        implements Filterable {

    private Context context;
    private ArrayList<Quote> data = new ArrayList<>();
    private ArrayList<Quote> filteredData = new ArrayList<>();
    private Filter filter = new Filter();

    public QuotesAdapter(Context context, List<Quote> data) {
        this.context = context;
        this.data = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
    }

    @Override
    public int getItemViewType(int position) {
        return filteredData.get(position).getType();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == Quote.TYPE_REMOTE) {
            v = LayoutInflater.from(context)
                    .inflate(R.layout.item_quote_remote, parent, false);

        } else {
            v = LayoutInflater.from(context)
                    .inflate(R.layout.item_quote_manual, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Quote quote = filteredData.get(position);
        holder.listingName.setText(quote.getName());
        holder.itemCount.setText(String.valueOf(quote.getQuoteProducts().size()));

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, OngoingQuoteActivity.class);
                i.putExtra(OngoingQuoteActivity.INPUT_QUOTE, (Parcelable) quote);
                if (quote.getType() == Quote.TYPE_MANUAL) {
                    i.putExtra(OngoingQuoteActivity.INPUT_IS_EDITABLE, true);
                }

                context.startActivity(i);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alerts.getDeleteConfirmationAlert(quote.getName(), context,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                enqueueDeleteQuote(quote.getId());
                            }
                        },
                        null
                ).show();
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putParcelable(QuoteCreateActivity.INPUT_QUOTE, quote);

                Intent i = new Intent(context, QuoteCreateActivity.class);
                i.putExtra(QuoteCreateActivity.INPUT_BUNDLE, b);

                ((Activity) context)
                        .startActivityForResult(i, QuotesListActivity.EDIT_QUOTE);
            }
        });
    }

    private void enqueueDeleteQuote(long id) {
        Api.api.deleteQuote(id).enqueue(
                ((QuotesListActivity) context)
                        .new DeleteQuoteCallback(context));
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View v;
        private final TextView listingName;
        private final TextView itemCount;
        private final ImageButton btnEdit;
        private final ImageButton btnDelete;

        public ViewHolder(View v) {
            super(v);

            this.v = v;
            listingName = (TextView) v.findViewById(R.id.txt_listing_name);
            itemCount = (TextView) v.findViewById(R.id.txt_item_count);
            btnEdit = (ImageButton) v.findViewById(R.id.btn_edit);
            btnDelete = (ImageButton) v.findViewById(R.id.btn_delete);
        }
    }

    public class Filter extends android.widget.Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO: inspect this
            // Naive filtering
            ArrayList<Quote> filtered = new ArrayList<>();
            for (Quote quote : data) {
                if (quote.getName().contains(constraint)) {
                    filtered.add(quote);
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
            filteredData = (ArrayList<Quote>) results.values;
        }
    }
}
