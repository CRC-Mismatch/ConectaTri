package br.com.wemind.marketplacetribanco.dummy;

import java.util.ArrayList;
import java.util.List;

import br.com.wemind.marketplacetribanco.models.ProductType;

/**
 * Created by kmkraiker on 27/05/2017.
 */

public class DummyProductTypes {
    public static final List<ProductType> list = new ArrayList<ProductType>();

    static {
        for (int i = 0; i < 25; i++) {
            ProductType dummy = new ProductType();
            dummy.setType("Tipo " + i);
            list.add(dummy);
        }
    }
}
