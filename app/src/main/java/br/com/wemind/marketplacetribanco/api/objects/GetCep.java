package br.com.wemind.marketplacetribanco.api.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fuinha on 05/07/2017.
 */

public class GetCep extends Status {

    @SerializedName("logradouro")
    private String logradouro;

    @SerializedName("localidade")
    private String localidade;

    @SerializedName("bairro")
    private String bairro;

    public String getLogradouro() {
        return logradouro;
    }

    public GetCep setLogradouro(String logradouro) {
        this.logradouro = logradouro;
        return this;
    }

    public String getLocalidade() {
        return localidade;
    }

    public GetCep setLocalidade(String localidade) {
        this.localidade = localidade;
        return this;
    }

    public String getBairro() {
        return bairro;
    }

    public GetCep setBairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public String getUf() {
        return uf;
    }

    public GetCep setUf(String uf) {
        this.uf = uf;
        return this;
    }

    @SerializedName("uf")

    private String uf;

}
