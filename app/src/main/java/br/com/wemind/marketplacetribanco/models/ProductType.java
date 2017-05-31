package br.com.wemind.marketplacetribanco.models;

/**
 * Created by kmkraiker on 27/05/2017.
 */

public class ProductType implements Listable {
    private long id;
    private String type;

    public long getId() {
        return id;
    }

    public ProductType setId(long id) {
        this.id = id;
        return this;
    }

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
