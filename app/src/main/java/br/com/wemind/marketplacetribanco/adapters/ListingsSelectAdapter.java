package br.com.wemind.marketplacetribanco.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.models.Listing;

public class ListingsSelectAdapter extends RecyclerView.Adapter<ListingsSelectAdapter.ViewHolder>
        implements Filterable {

    private Context context;
    private ArrayList<Listing> data;
    private ArrayList<Listing> filteredData;
    private Set<Listing> selectedData;
    private Filter filter = new Filter();

    public ListingsSelectAdapter(Context context, List<Listing> data) {
        this.context = context;
        this.data = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
        this.selectedData = new TreeSet<>();
    }

    @Override
    public int getItemViewType(int position) {
        return filteredData.get(position).getType();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        int layoutId;
        if (viewType == Listing.TYPE_SEASONAL) {
            layoutId = R.layout.item_selectable_listing_seasonal;

        } else if (viewType == Listing.TYPE_WEEKLY) {
            layoutId = R.layout.item_selectable_listing_weekly;
        } else {
            // Default to common type
            layoutId = R.layout.item_selectable_listing_common;
        }
        v = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Listing listing = filteredData.get(position);
        holder.listingName.setText(listing.getName());
        holder.itemCount.setText(String.valueOf(listing.getListingProducts().size()));

        holder.checkBox.setChecked(selectedData.contains(listing));

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.setChecked(!v.isSelected());
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedData.add(listing);
                    holder.v.setSelected(true);
                } else {
                    selectedData.remove(listing);
                    holder.v.setSelected(false);
                }
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

    public ArrayList<Listing> getSelectedList() {
        return new ArrayList<>(selectedData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View v;
        private final CheckBox checkBox;
        private final TextView listingName;
        private final TextView itemCount;

        public ViewHolder(View v) {
            super(v);

            this.v = v;
            listingName = (TextView) v.findViewById(R.id.txt_listing_name);
            itemCount = (TextView) v.findViewById(R.id.txt_item_count);
            checkBox = (CheckBox) v.findViewById(R.id.check_box);
        }
    }

    public class Filter extends android.widget.Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Listing> filtered = constraint.equals("") ?
                    new ArrayList<>(data)
                    : Api.syncSearchListing(constraint);

            // Pack and return results
            FilterResults result = new FilterResults();
            result.count = filtered.size();
            result.values = filtered;
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Listing>) results.values;
            notifyDataSetChanged();
        }
    }
}

