package br.com.wemind.marketplacetribanco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeSet;

import br.com.wemind.marketplacetribanco.models.Listing;
import br.com.wemind.marketplacetribanco.models.ListingProduct;
import br.com.wemind.marketplacetribanco.models.Product;
import br.com.wemind.marketplacetribanco.models.Supplier;

public class QuoteCreationFlowController extends AppCompatActivity {

    public static final int REQUEST_SELECT_LISTING = 1;
    public static final int REQUEST_SELECT_PRODUCT = 2;
    public static final int REQUEST_SELECT_SUPPLIER = 3;
    public static final String INPUT_IS_MANUAL = "input_is_manual";
    private ArrayList<Listing> listings = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Supplier> suppliers = new ArrayList<>();
    private boolean isManualQuote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read input intent
        // Check if this is a manual quote
        isManualQuote = getIntent().getBooleanExtra(INPUT_IS_MANUAL, false);

        // Initial state
        Intent i = new Intent(this, ListingsSelectActivity.class);
        startActivityForResult(i, REQUEST_SELECT_LISTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "RESULT CANCELLED", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Finite State Machine-like flow control is done below
        if (requestCode == REQUEST_SELECT_LISTING) {
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
            if (listings.size() <= 0) {
                // If list is empty, get all products from database so the user
                // can handpick each one, effectively creating a temporary product
                // listing
                // TODO
                Toast.makeText(this, "Not implemented yet!", Toast.LENGTH_SHORT).show();
                throw new RuntimeException("Not implemented yet!");
            } else {
                // If the user selected at least one listing, get the products from
                // each listing
                for (Listing li : listings) {
                    listingProducts.addAll(li.getProducts());
                }

                for (ListingProduct lp : listingProducts) {
                    products.add(lp.getProduct());
                }
            }
            // Request that the user review the product list
            Bundle inputBundle = new Bundle();
            inputBundle.putParcelableArrayList(
                    ProductsSelectActivity.INPUT_PRODUCTS, new ArrayList<>(products));

            Intent i = new Intent(this, ProductsSelectActivity.class);
            i.putExtra(ProductsSelectActivity.INPUT_BUNDLE, inputBundle);

            startActivityForResult(i, REQUEST_SELECT_PRODUCT);

        } else if (requestCode == REQUEST_SELECT_PRODUCT) {
            if (data != null) {
                Bundle b = data.getBundleExtra(ProductsSelectActivity.RESULT_BUNDLE);
                if (b != null) {
                    products = b.getParcelableArrayList(
                            ProductsSelectActivity.SELECTED_LIST);
                }
            }

            // Request that the user review the suppliers to be added to this quote
            Bundle inputBundle = new Bundle();
            inputBundle.putParcelableArrayList(
                    SuppliersSelectActivity.INPUT_SUPPLIERS, suppliers);

            Intent i = new Intent(this, SuppliersSelectActivity.class);
            i.putExtra(SuppliersSelectActivity.INPUT_BUNDLE, inputBundle);
            startActivityForResult(i, REQUEST_SELECT_SUPPLIER);

        } else if (requestCode == REQUEST_SELECT_SUPPLIER) {
            if (data != null) {
                Bundle b = data.getBundleExtra(ProductsSelectActivity.RESULT_BUNDLE);
                if (b != null) {
                    suppliers = b.getParcelableArrayList(
                            SuppliersSelectActivity.SELECTED_LIST);
                }
            }

            // Request that the user finish creating this quote by
            // giving it a name and expiration date
            // TODO
            Toast.makeText(this,
                    "l " + listings.size()
                            + " p " + products.size()
                            + " s " + suppliers.size(),
                    Toast.LENGTH_LONG
            ).show();


            Intent i = new Intent(this, QuoteCreateActivity.class);
            startActivity(i);
        }
    }
}
