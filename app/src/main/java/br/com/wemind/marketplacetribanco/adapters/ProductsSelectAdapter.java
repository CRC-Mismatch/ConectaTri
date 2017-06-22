package br.com.wemind.marketplacetribanco.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filterable;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.api.objects.SearchQuery;
import br.com.wemind.marketplacetribanco.databinding.ItemProductsSelectHeaderBinding;
import br.com.wemind.marketplacetribanco.databinding.ItemSimpleProductSelectBinding;
import br.com.wemind.marketplacetribanco.models.Product;
import retrofit2.Response;

public class ProductsSelectAdapter
        extends RecyclerView.Adapter<ProductsSelectAdapter.ViewHolder>
        implements Filterable {

    private static final int VIEW_TYPE_DEFAULT = 0;
    private static final int VIEW_TYPE_HEADER = 1;
    private final Context context;
    private TreeSet<Product> selectedData;
    private ArrayList<Product> data;
    private ArrayList<Product> filteredData;
    private Filter filter = new Filter();
    private Set<DefaultViewHolder> items = new HashSet<>();
    private HeaderViewHolder headerHolder;

    public ProductsSelectAdapter(Context context, List<Product> data,
                                 Set<Product> selectedData) {
        this.context = context;

        data = data == null ? new ArrayList<Product>() : data;
        this.data = new ArrayList<>(data);
        filteredData = new ArrayList<>(data);

        selectedData = selectedData == null ? new TreeSet<Product>() : selectedData;
        this.selectedData = new TreeSet<>(selectedData);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            HeaderViewHolder vh = new HeaderViewHolder(
                    ItemProductsSelectHeaderBinding.inflate(
                            LayoutInflater.from(context), parent, false)
            );
            headerHolder = vh;
            return vh;

        } else {
            DefaultViewHolder vh = new DefaultViewHolder(
                    ItemSimpleProductSelectBinding.inflate(
                            LayoutInflater.from(context), parent, false)
            );

            items.add(vh);
            return vh;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_DEFAULT) {
            ((DefaultViewHolder) holder).setup(filteredData.get(position - 1));

        } else if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            ((HeaderViewHolder) holder).setup(null);
        }
    }

    @Override
    public int getItemCount() {
        // + 1 so header ViewHolder is counted in
        return filteredData.size() + 1;
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

    private void setSelectAll(boolean selectAll) {
        for (DefaultViewHolder vh : items) {
            vh.b.checkbox.setChecked(selectAll);
        }

        if (selectAll) {
            selectedData.addAll(data);
            
        } else {
            selectedData.removeAll(data);
        }
    }

    public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void setup(T input);
    }

    public class Filter extends android.widget.Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Product> result = constraint.equals("") ?
                    new ArrayList<>(data)
                    : Api.syncSearchProduct(constraint);

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

    private class DefaultViewHolder extends ViewHolder<Product> {

        private ItemSimpleProductSelectBinding b;

        public DefaultViewHolder(ItemSimpleProductSelectBinding binding) {
            super(binding.getRoot());
            b = binding;
        }

        @Override
        public void setup(final Product product) {

            b.product.setText(product.getName());

            if (selectedData.contains(product)) {
                b.checkbox.setChecked(true);
            } else {
                b.checkbox.setChecked(false);
            }
            b.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (b.checkbox.isChecked()) {
                        selectedData.remove(product);
                        b.checkbox.setChecked(false);

                        headerHolder.setChecked(false);

                    } else {
                        selectedData.add(product);
                        b.checkbox.setChecked(true);
                    }
                }
            });
        }
    }

    private class HeaderViewHolder extends ViewHolder<Void> {
        private ItemProductsSelectHeaderBinding b;

        public HeaderViewHolder(ItemProductsSelectHeaderBinding binding) {
            super(binding.getRoot());
            b = binding;
        }

        @Override
        public void setup(Void input) {
            b.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (b.checkbox.isChecked()) {
                        setSelectAll(true);
                    } else {
                        setSelectAll(false);
                    }
                }
            });
        }

        public void setChecked(boolean checked) {
            b.checkbox.setChecked(checked);
        }
    }
}
