package br.com.wemind.marketplacetribanco.models;

/**
 * Created by kmkraiker on 27/05/2017.
 */

public class ProductType implements Listable {
    //private int id;
    private String type;

    public String getType() {
        return type;
    }

    public ProductType setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String getLabel() {
        return type;
    }
}
