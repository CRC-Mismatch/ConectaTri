package br.com.wemind.marketplacetribanco.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.databinding.ItemSimpleProductSelectBinding;
import br.com.wemind.marketplacetribanco.models.Product;

public class ProductsSelectAdapter
        extends RecyclerView.Adapter<ProductsSelectAdapter.ViewHolder>
        implements Filterable {

    private final Context context;
    private TreeSet<Product> selectedData;
    private ArrayList<Product> data;
    private ArrayList<Product> filteredData;
    private Filter filter = new Filter();

    public ProductsSelectAdapter(Context context, List<Product> data,
                                 Set<Product> selectedData) {
        this.context = context;
        this.data = new ArrayList<>(data);
        this.selectedData = new TreeSet<>(selectedData);
        filteredData = new ArrayList<>(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemSimpleProductSelectBinding.inflate(
                LayoutInflater.from(context), parent, false
        ));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Product product = data.get(position);

        holder.b.product.setText(product.getName());

        if (selectedData.contains(product)) {
            holder.b.checkbox.setSelected(true);
        } else {
            holder.b.checkbox.setSelected(false);
        }
        holder.b.getRoot().setOnClickListener(new View.OnClickListener() {
            CheckBox checkbox = holder.b.checkbox;

            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()) {
                    selectedData.remove(product);
                    checkbox.setChecked(false);

                } else {
                    selectedData.add(product);
                    checkbox.setChecked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    public Filter getFilter() {
        return filter;
    }

    public ArrayList<Product> getSelectedList() {
        return new ArrayList<>(selectedData);
    }

    public void setData(ArrayList<Product> data) {
        this.data = data;
        filteredData = new ArrayList<>(data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemSimpleProductSelectBinding b;

        public ViewHolder(ItemSimpleProductSelectBinding binding) {
            super(binding.getRoot());
            b = binding;
        }
    }

    public class Filter extends android.widget.Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Product> result = new ArrayList<>();
            for (Product p : data) {
                if (p.getName().contains(constraint)) {
                    result.add(p);
                }
            }

            // Pack and return
            FilterResults fr = new FilterResults();
            fr.count = result.size();
            fr.values = result;
            return fr;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Product>) results.values;
            notifyDataSetChanged();
        }
    }
}
