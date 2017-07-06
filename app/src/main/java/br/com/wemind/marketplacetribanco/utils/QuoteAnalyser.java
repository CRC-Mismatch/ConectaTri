package br.com.wemind.marketplacetribanco.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.com.wemind.marketplacetribanco.models.Offer;
import br.com.wemind.marketplacetribanco.models.Price;
import br.com.wemind.marketplacetribanco.models.Quote;
import br.com.wemind.marketplacetribanco.models.QuoteProduct;
import br.com.wemind.marketplacetribanco.models.QuoteSupplier;
import br.com.wemind.marketplacetribanco.models.Supplier;

public class QuoteAnalyser {
    private Quote quote;
    private Map<Long, Supplier> suppliers;

    public QuoteAnalyser(Quote quote) {
        this.quote = quote;
    }

    public List<Offer> getBestOffers() {
        List<Offer> result = new ArrayList<>();

        for (QuoteProduct quoteProduct : quote.getQuoteProducts()) {
            // Get QuoteSupplier with lowest price per unit
            List<QuoteSupplier> sorted =
                    new ArrayList<>(quoteProduct.getQuoteSuppliers());
            Collections.sort(sorted,
                    new Comparator<QuoteSupplier>() {
                        @Override
                        public int compare(QuoteSupplier o1, QuoteSupplier o2) {
                            return o1.compareTo(o2);
                        }
            });

            if (sorted.size() > 1 && sorted.get(0).compareTo(sorted.get(1)) == 0) {
                // If there's more than one best offer (meaning they have equal price per unit
                // Do not add this product to the list
                continue;
            }

            // Else, get the top item
            QuoteSupplier bestPricePerUnit = sorted.get(0);

            // Pack data
            result.add(new Offer()
                    .setProduct(quoteProduct.getProduct())
                    .setSupplier(bestPricePerUnit.getSupplier())
                    .setPrice(new Price()
                            .setUnitPrice(bestPricePerUnit.getPriceDouble())
                            .setQuantity(bestPricePerUnit.getQuantity())
                    )
            );
        }
        return result;
    }

    public void invalidateData() {
        suppliers = null;
    }

    public Map<Long, Supplier> getSuppliers() {
        if (suppliers == null) {
            Map<Long, Supplier> newMap = new TreeMap<>();
            for (Supplier sup : quote.getSuppliers()) {
                newMap.put(sup.getId(), sup);
            }

            suppliers = newMap;
        }
        return suppliers;
    }
}
