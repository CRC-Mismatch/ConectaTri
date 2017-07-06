package br.com.wemind.marketplacetribanco.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.ViewGroup;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.R;
import br.com.wemind.marketplacetribanco.databinding.ItemQuoteSupplierBinding;
import br.com.wemind.marketplacetribanco.models.QuoteProduct;
import br.com.wemind.marketplacetribanco.models.QuoteSupplier;
import br.com.wemind.marketplacetribanco.models.Supplier;
import br.com.wemind.marketplacetribanco.utils.Formatting;

public class QuoteSupplierAdapter extends RecyclerView.Adapter<QuoteSupplierAdapter.ViewHolder> {

    private final boolean isEditable;
    private Context context;
    private List<QuoteSupplier> data;
    private TreeSet<Supplier> suppliers = new TreeSet<>();

    public QuoteSupplierAdapter(Context context, QuoteProduct product,
                                boolean isEditable, TreeSet<Supplier> suppliers) {
        this.context = context;
        this.data = product.getQuoteSuppliers();
        Collections.sort(this.data, new Comparator<QuoteSupplier>() {
            @Override
            public int compare(QuoteSupplier o1, QuoteSupplier o2) {
                return o1.compareTo(o2);
            }
        });
        this.isEditable = isEditable;
        if (suppliers == null) {
            suppliers = new TreeSet<>();
        }
        this.suppliers = suppliers;
    }

    @Override
    public QuoteSupplierAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        return new QuoteSupplierAdapter.ViewHolder(
                ItemQuoteSupplierBinding.inflate(
                        ((Activity) context).getLayoutInflater(), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(QuoteSupplierAdapter.ViewHolder vh, int position) {
        final QuoteSupplier quoteSupplier = data.get(position);
        final Locale brLocale = new Locale("pt", "BR");
        vh.b.price.setText(String.format(
                brLocale,
                context.getString(R.string.money),
                quoteSupplier.getPriceDouble()
        ));
        vh.b.qty.setText(String.valueOf(quoteSupplier.getQuantity()));
        Supplier supplier = suppliers.floor(quoteSupplier.getSupplier());
        if (supplier != null) {
            vh.b.supplierName.setText(supplier.getSupplierName());
        }

        vh.b.contactName.setText(quoteSupplier.getSupplier().getContactName());

        vh.b.price.setEnabled(isEditable);
        vh.b.qty.setEnabled(isEditable);

        if (isEditable) {
            vh.b.price.addTextChangedListener(new TextWatcher() {
                private NumberFormat brDouble =
                        NumberFormat.getNumberInstance(brLocale);
                private boolean selfChange = false;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (selfChange) {
                        return;
                    }

                    selfChange = true;
                    s.replace(0, s.length(),
                            context.getString(R.string.money_prefix)
                                    + " "
                                    + Formatting.maskBrazilianCurrency(s.toString())
                    );

                    Selection.setSelection(s, s.length(), s.length());
                    selfChange = false;

                    String value = s.toString().replaceAll("[^0-9,.]", "");
                    if (!value.equals("")) {
                        try {
                            String price = ((Double) brDouble.parse(value)
                                    .doubleValue())
                                    .toString();

                            quoteSupplier.setPrice(
                                    price
                            );
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        quoteSupplier.setPrice("0.00");
                    }
                }
            });

            vh.b.qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().equals("")) {
                        quoteSupplier.setQuantity(Integer.valueOf(s.toString()));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setSupplierData(TreeSet<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuoteSupplierBinding b;

        public ViewHolder(ItemQuoteSupplierBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
