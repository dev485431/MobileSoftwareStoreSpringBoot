package com.dataart.softwarestore.service;

public enum QueryResultsOrder {

    ASCENDING("asc"), DESCENDING("desc");

    private final String value;

    QueryResultsOrder(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
