package com.sam.springbootmall.util;

import java.util.List;

public class Page<t> {
    private Integer limit;
    private Integer offset;
    private Integer total;
    private List<t> results;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<t> getResults() {
        return results;
    }

    public void setResults(List<t> results) {
        this.results = results;
    }
}
