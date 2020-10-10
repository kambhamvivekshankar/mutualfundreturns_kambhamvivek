package com.reputation.mutualfundreturns_kambhamvivek.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    Nav[] data;

    public Nav[] getData() {
        return data;
    }

    public void setData(Nav[] data) {
        this.data = data;
    }
}
