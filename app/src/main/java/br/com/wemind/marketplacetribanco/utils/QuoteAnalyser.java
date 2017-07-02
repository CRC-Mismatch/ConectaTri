package br.com.wemind.marketplacetribanco.utils;

import java.util.ArrayList;
import java.util.Collections;
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
            QuoteSupplier min = Collections.min(quoteProduct.getQuoteSuppliers());
            // Pack data
            result.add(new Offer()
                    .setProduct(quoteProduct.getProduct())
                    .setSupplier(min.getSupplier())
                    .setPrice(new Price()
                            .setUnitPrice(min.getPriceDouble())
                            .setQuantity(min.getQuantity())
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
