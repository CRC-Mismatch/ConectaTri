package br.com.wemind.marketplacetribanco.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.api.Api;
import br.com.wemind.marketplacetribanco.models.Listing;
import br.com.wemind.marketplacetribanco.models.ListingProduct;
import br.com.wemind.marketplacetribanco.models.Product;
import br.com.wemind.marketplacetribanco.models.Quote;
import br.com.wemind.marketplacetribanco.models.QuoteProduct;
import br.com.wemind.marketplacetribanco.models.QuoteSupplier;
import br.com.wemind.marketplacetribanco.models.Supplier;
import retrofit2.Call;
import retrofit2.Response;

public class QuoteCreationFlowController extends AppCompatActivity {

    public static final int REQUEST_NOTHING = -1;
    public static final int REQUEST_SELECT_LISTING = 1;
    public static final int REQUEST_SELECT_PRODUCT = 2;
    public static final int REQUEST_SELECT_SUPPLIER = 3;
    public static final int REQUEST_FINISH_CREATION = 4;

    public static final String INPUT_IS_MANUAL = "input_is_manual";
    public static final String INPUT_BUNDLE = "input_bundle";
    public static final String INPUT_SELECTED_PRODUCTS = "input_products";
    public static final String INPUT_SELECTED_SUPPLIERS = "input_suppliers";

    private ArrayList<Listing> listings = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Product> selectedProducts = new ArrayList<>();
    private ArrayList<Supplier> suppliers = new ArrayList<>();
    private boolean isManualQuote = false;
    private int currentStep = REQUEST_NOTHING;
    private ArrayList<Supplier> selectedSuppliers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read input intent
        //      Check if this is a manual quote
        isManualQuote = getIntent().getBooleanExtra(INPUT_IS_MANUAL, false);

        //      Get pre-selected objects, in the case of editing an existing quote
        Bundle inputBundle = getIntent().getBundleExtra(INPUT_BUNDLE);
        if (inputBundle != null) {
            ArrayList<Product> inputProducts =
                    inputBundle.getParcelableArrayList(INPUT_SELECTED_PRODUCTS);
            if (inputProducts != null) {
                selectedProducts = inputProducts;
            }

            ArrayList<Supplier> inputSuppliers =
                    inputBundle.getParcelableArrayList(INPUT_SELECTED_SUPPLIERS);
            if (inputSuppliers != null) {
                selectedSuppliers = inputSuppliers;
            }
        }

        // Initial state
        selectListing();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            previousStep(currentStep);
            return;
        }

        if (resultCode == RESULT_OK) {
            // Finite State Machine-like flow control is done below
            if (requestCode == REQUEST_SELECT_LISTING) {
                listingReturned(data);

            } else if (requestCode == REQUEST_SELECT_PRODUCT) {
                productReturned(data);

            } else if (requestCode == REQUEST_SELECT_SUPPLIER) {
                supplierReturned(data);

            } else if (currentStep == REQUEST_FINISH_CREATION) {
                finishCreationReturned(data);
            }
        }
    }

    private void previousStep(int currentStep) {
        if (currentStep == REQUEST_SELECT_PRODUCT) {
            selectListing();

        } else if (currentStep == REQUEST_SELECT_SUPPLIER) {
            selectProduct(new TreeSet<>(products));

        } else if (currentStep == REQUEST_FINISH_CREATION) {
            selectSupplier(suppliers);

        } else {
            finish();
        }
    }

    private void listingReturned(Intent data) {
        if (data != null) {
            Bundle b = data.getBundleExtra(ListingsSelectActivity.RESULT_BUNDLE);
            if (b != null) {
                listings = b.getParcelableArrayList(
                        ListingsSelectActivity.SELECTED_LIST);

                TreeSet<Supplier> suppliers = new TreeSet<>();
                for (Listing li : listings) {
                    suppliers.addAll(li.getSuppliers());
                }

                this.suppliers = new ArrayList<>(suppliers);
            }
        }

        TreeSet<ListingProduct> listingProducts = new TreeSet<>();
        TreeSet<Product> products = new TreeSet<>();
        if (listings.size() > 0) {
            // If the user selected at least one listing, get the products from
            // each listing
            for (Listing li : listings) {
                listingProducts.addAll(li.getProducts());
            }

            for (ListingProduct lp : listingProducts) {
                products.add(lp.getProduct());
            }
        }

        this.products = new ArrayList<>(products);

        // Request that the user review the product list
        selectProduct(products);
    }

    private void productReturned(Intent data) {
        if (data != null) {
            Bundle b = data.getBundleExtra(ProductsSelectActivity.RESULT_BUNDLE);
            if (b != null) {
                selectedProducts = b.getParcelableArrayList(
                        ProductsSelectActivity.SELECTED_LIST);
            }
        }

        // Request that the user review the suppliers to be added to this quote
        selectSupplier(suppliers);
    }

    private void supplierReturned(Intent data) {
        if (data != null) {
            Bundle b = data.getBundleExtra(ProductsSelectActivity.RESULT_BUNDLE);
            if (b != null) {
                selectedSuppliers = b.getParcelableArrayList(
                        SuppliersSelectActivity.SELECTED_LIST);
            }
        }

        // Request that the user finish creating this quote by
        // giving it a name and expiration date
        Intent i = new Intent(this, QuoteCreateActivity.class);
        startActivityForResult(i, REQUEST_FINISH_CREATION);
        currentStep = REQUEST_FINISH_CREATION;
    }

    private void finishCreationReturned(Intent data) {
        Quote quote = null;

        Bundle b = data.getBundleExtra(QuoteCreateActivity.RESULT_BUNDLE);
        if (b != null) {
            quote = b.getParcelable(QuoteCreateActivity.RESULT_QUOTE);
            if (quote != null) {
                List<QuoteSupplier> quoteSuppliers = new ArrayList<>();
                for (Supplier sup :
                        selectedSuppliers) {
                    quoteSuppliers.add(new QuoteSupplier()
                            .setSupplier(sup)
                    );
                }

                List<QuoteProduct> quoteProducts = new ArrayList<>();
                for (Product p : selectedProducts) {
                    quoteProducts.add(new QuoteProduct()
                            .setProduct(p)
                            .setQuoteSuppliers(quoteSuppliers)
                    );
                }
                quote.setQuoteProducts(quoteProducts);
            }
        }

        if (quote == null) {
            Toast.makeText(this, "Erro: nullQuote", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!isManualQuote) {
            quote.setType(Quote.TYPE_REMOTE);
            Api.api.addQuote(quote).enqueue(new CreateQuoteCallback(this));
            finish();

        } else {
            quote.setType(Quote.TYPE_MANUAL);
            Intent i = new Intent(this, QuoteProductsListActivity.class);
            i.putExtra(QuoteProductsListActivity.QUOTE, (Parcelable) quote);
            i.putExtra(QuoteProductsListActivity.INPUT_IS_EDITABLE, isManualQuote);

            startActivity(i);
            finish();
        }
    }

    private void selectListing() {
        Intent i = new Intent(this, ListingsSelectActivity.class);
        startActivityForResult(i, REQUEST_SELECT_LISTING);
        currentStep = REQUEST_SELECT_LISTING;
    }

    private void selectProduct(TreeSet<Product> products) {
        Bundle inputBundle = new Bundle();
        inputBundle.putParcelableArrayList(
                ProductsSelectActivity.INPUT_PRODUCTS, new ArrayList<>(products));

        if (selectedProducts.size() != 0) {
            // Send pre-selected items
            inputBundle.putParcelableArrayList(
                    ProductsSelectActivity.INPUT_SELECTED, selectedProducts);
        }

        Intent i = new Intent(this, ProductsSelectActivity.class);
        i.putExtra(ProductsSelectActivity.INPUT_BUNDLE, inputBundle);

        startActivityForResult(i, REQUEST_SELECT_PRODUCT);

        currentStep = REQUEST_SELECT_PRODUCT;
    }

    private void selectSupplier(ArrayList<Supplier> suppliers) {
        Bundle inputBundle = new Bundle();
        inputBundle.putParcelableArrayList(
                SuppliersSelectActivity.INPUT_SUPPLIERS, suppliers);

        if (selectedSuppliers.size() != 0) {
            // Send pre-selected items
            inputBundle.putParcelableArrayList(
                    SuppliersSelectActivity.INPUT_SELECTED, selectedSuppliers);
        }


        Intent i = new Intent(this, SuppliersSelectActivity.class);
        i.putExtra(SuppliersSelectActivity.INPUT_BUNDLE, inputBundle);
        startActivityForResult(i, REQUEST_SELECT_SUPPLIER);

        currentStep = REQUEST_SELECT_SUPPLIER;
    }

    private class CreateQuoteCallback extends br.com.wemind.marketplacetribanco.api.Callback<Quote> {
        public CreateQuoteCallback(Context context) {
            super(context);
        }

        @Override
        public void onSuccess(Quote response) {

        }

        @Override
        public void onError(Call<Quote> call, Response<Quote> response) {

        }
    }
}
