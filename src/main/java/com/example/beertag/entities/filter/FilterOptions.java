package com.example.beertag.entities.filter;

import java.util.Optional;

public class FilterOptions {

    private Optional<String> name;
    private Optional<Double> minAbv;
    private Optional<Double> maxAbv;
    private Optional<Integer> styleId;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;


    public FilterOptions(String name,
                         Double minAbv,
                         Double maxAbv,
                         Integer styleId,
                         String sortBy,
                         String sortOrder) {
        this.name = Optional.ofNullable(name);
        this.minAbv = Optional.ofNullable(minAbv);
        this.maxAbv = Optional.ofNullable(maxAbv);
        this.styleId = Optional.ofNullable(styleId);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<Double> getMinAbv() {
        return minAbv;
    }

    public void setMinAbv(Optional<Double> minAbv) {
        this.minAbv = minAbv;
    }

    public Optional<Double> getMaxAbv() {
        return maxAbv;
    }

    public void setMaxAbv(Optional<Double> maxAbv) {
        this.maxAbv = maxAbv;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public void setSortBy(Optional<String> sortBy) {
        this.sortBy = sortBy;
    }

    public Optional<Integer> getStyleId() {
        return styleId;
    }

    public void setStyleId(Optional<Integer> styleId) {
        this.styleId = styleId;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Optional<String> sortOrder) {
        this.sortOrder = sortOrder;
    }
}
