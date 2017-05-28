package br.com.wemind.marketplacetribanco.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.activities.ListingCreateActivity;
import br.com.wemind.marketplacetribanco.activities.ListingsListActivity;
import br.com.wemind.marketplacetribanco.models.Listing;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ViewHolder>
        implements Filterable {

    private Context context;
    private ArrayList<Listing> data;
    private ArrayList<Listing> filteredData;
    private Filter filter = new Filter();

    public ListingsAdapter(Context context, List<Listing> data) {
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
        if (viewType == Listing.TYPE_SEASONAL) {
            v = LayoutInflater.from(context)
                    .inflate(R.layout.item_listing_seasonal, parent, false);

        } else if (viewType == Listing.TYPE_WEEKLY) {
            v = LayoutInflater.from(context)
                    .inflate(R.layout.item_listing_weekly, parent, false);
        } else {
            // Default to common type
            v = LayoutInflater.from(context)
                    .inflate(R.layout.item_listing_common, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Listing listing = filteredData.get(position);
        holder.listingName.setText(listing.getName());
        holder.itemCount.setText(String.valueOf(listing.getProducts().size()));

        // FIXME: bind event handlers
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete " + listing.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle toEdit = new Bundle();
                toEdit.putParcelable(ListingCreateActivity.INPUT_LISTING, listing);

                Intent edit = new Intent(context, ListingCreateActivity.class);
                edit.putExtra(ListingCreateActivity.INPUT_BUNDLE, toEdit);
                ((Activity) context)
                        .startActivityForResult(edit, ListingsListActivity.EDIT_LISTING);
            }
        });
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
            ArrayList<Listing> filtered = new ArrayList<>();
            for (Listing listing : data) {
                if (listing.getName().contains(constraint)) {
                    filtered.add(listing);
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
            filteredData = (ArrayList<Listing>) results.values;
        }
    }
}