package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.models.Listing;
import br.com.wemind.marketplacetribanco.models.ListingProduct;
import br.com.wemind.marketplacetribanco.models.Product;
import br.com.wemind.marketplacetribanco.models.Supplier;

public class QuoteCreationFlowController extends AppCompatActivity {

    public static final int REQUEST_NOTHING = -1;
    public static final int REQUEST_SELECT_LISTING = 1;
    public static final int REQUEST_SELECT_PRODUCT = 2;
    public static final int REQUEST_SELECT_SUPPLIER = 3;
    public static final int REQUEST_FINISH_CREATION = 4;

    public static final String INPUT_IS_MANUAL = "input_is_manual";
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
        // Check if this is a manual quote
        isManualQuote = getIntent().getBooleanExtra(INPUT_IS_MANUAL, false);

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

        } else{
            finish();
        }
    }

    private void selectListing() {
        Intent i = new Intent(this, ListingsSelectActivity.class);
        startActivityForResult(i, REQUEST_SELECT_LISTING);
        currentStep = REQUEST_SELECT_LISTING;
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

    private void selectProduct(TreeSet<Product> products) {
        Bundle inputBundle = new Bundle();
        inputBundle.putParcelableArrayList(
                ProductsSelectActivity.INPUT_PRODUCTS, new ArrayList<>(products));

        Intent i = new Intent(this, ProductsSelectActivity.class);
        i.putExtra(ProductsSelectActivity.INPUT_BUNDLE, inputBundle);

        startActivityForResult(i, REQUEST_SELECT_PRODUCT);

        currentStep = REQUEST_SELECT_PRODUCT;
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

    private void selectSupplier(ArrayList<Supplier> suppliers) {
        Bundle inputBundle = new Bundle();
        inputBundle.putParcelableArrayList(
                SuppliersSelectActivity.INPUT_SUPPLIERS, suppliers);

        Intent i = new Intent(this, SuppliersSelectActivity.class);
        i.putExtra(SuppliersSelectActivity.INPUT_BUNDLE, inputBundle);
        startActivityForResult(i, REQUEST_SELECT_SUPPLIER);

        currentStep = REQUEST_SELECT_SUPPLIER;
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
        // TODO: 10/06/2017


        Intent i = new Intent(this, QuoteCreateActivity.class);
        startActivityForResult(i, REQUEST_FINISH_CREATION);
    }
}
