package br.com.wemind.marketplacetribanco.api.objects;

import com.google.gson.annotations.SerializedName;

public class SearchQuery {
    @SerializedName("query")
    private String query = "";

    public SearchQuery() {}

    public SearchQuery(CharSequence query) {
        setQuery(query);
    }

    public String getQuery() {
        return query;
    }

    public SearchQuery setQuery(CharSequence query) {
        this.query = query.toString();
        return this;
    }
}
