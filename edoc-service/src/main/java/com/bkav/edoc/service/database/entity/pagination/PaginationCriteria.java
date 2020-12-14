package com.bkav.edoc.service.database.entity.pagination;

import java.util.Map;

public class PaginationCriteria {
    /* The page number. */
    private Integer pageNumber;

    /**
     * The page size.
     */
    private Integer pageSize;

    /**
     * The total records.
     */
    private Integer totalRecords;

    /**
     * The sort by.
     */
    private SortBy sortBy;

    /**
     * The filter by.
     */
    private FilterBy filterBy;

    /**
     * the search keyword
     */
    private String search;

    /**
     * Gets the page number.
     *
     * @return the pageNumber
     */
    public Integer getPageNumber() {
        return (null == pageNumber) ? 0 : pageNumber;
    }

    /**
     * Sets the page number.
     *
     * @param pageNumber the pageNumber to set
     */
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Gets the page size.
     *
     * @return the pageSize
     */
    public Integer getPageSize() {
        return (null == pageSize) ? 10 : pageSize;
    }

    /**
     * Sets the page size.
     *
     * @param pageSize the pageSize to set
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Gets the total records.
     *
     * @return the totalRecords
     */
    public Integer getTotalRecords() {
        return totalRecords;
    }

    /**
     * Sets the total records.
     *
     * @param totalRecords the totalRecords to set
     */
    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    /**
     * Gets the sort by.
     *
     * @return the sortBy
     */
    public SortBy getSortBy() {
        return sortBy;
    }

    /**
     * Sets the sort by.
     *
     * @param sortBy the sortBy to set
     */
    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * Gets the filter by.
     *
     * @return the filterBy
     */
    public FilterBy getFilterBy() {
        return filterBy;
    }

    /**
     * Sets the filter by.
     *
     * @param filterBy the filterBy to set
     */
    public void setFilterBy(FilterBy filterBy) {
        this.filterBy = filterBy;
    }

    /**
     * Checks if is filter by empty.
     *
     * @return true, if is filter by empty
     */
    public boolean isFilterByEmpty() {
        return null == filterBy || null == filterBy.getMapOfFilters() || filterBy.getMapOfFilters().size() == 0;
    }

    /**
     * Get the search
     *
     * @return string
     */
    public String getSearch() {
        return search;
    }

    /**
     * Sets search
     *
     * @param search the search value
     */
    public void setSearch(String search) {
        this.search = search;
    }

    /**
     * Checks if is sort by empty.
     *
     * @return true, if is sort by empty
     */
    public boolean isSortByEmpty() {
        return null == sortBy || null == sortBy.getSortBys() || sortBy.getSortBys().size() == 0;
    }

    /**
     * Gets the filter by clause.
     *
     * @return the filter by clause
     */
    public String getFilterByClause() {

        StringBuilder stringBuilder = null;

        if (!isFilterByEmpty()) {

            for (Map.Entry<String, String> pair : filterBy.getMapOfFilters().entrySet()) {

                if (null == stringBuilder) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(BRKT_OPN);

                    stringBuilder.append(SPACE)
                            .append(BRKT_OPN)
                            .append(pair.getKey())
                            .append(LIKE_PREFIX)
                            .append(pair.getValue())
                            .append(LIKE_SUFFIX)
                            .append(BRKT_CLS);

                } else {

                    stringBuilder.append(filterBy.isGlobalSearch() ? OR : AND)
                            .append(BRKT_OPN)
                            .append(pair.getKey())
                            .append(LIKE_PREFIX)
                            .append(pair.getValue())
                            .append(LIKE_SUFFIX)
                            .append(BRKT_CLS);

                }
            }
            if (stringBuilder != null) {
                stringBuilder.append(BRKT_CLS);
            }
        }

        return (null == stringBuilder) ? BLANK : stringBuilder.toString();
    }

    /**
     * Gets the order by clause.
     *
     * @return the order by clause
     */
    public String getOrderByClause() {

        StringBuilder stringBuilder = null;

        if (!isSortByEmpty()) {

            for (Map.Entry<String, SortOrder> pair : sortBy.getSortBys().entrySet()) {
                if (null == stringBuilder) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(ORDER_BY).append(pair.getKey()).append(SPACE).append(pair.getValue());
                } else {
                    stringBuilder.append(COMMA).append(pair.getKey()).append(SPACE).append(pair.getValue());
                }
            }
        }

        return (null == stringBuilder) ? BLANK : stringBuilder.toString();
    }

    /**
     * Gets the order by clause.
     *
     * @return the order by clause
     */
    public String getOrderBy() {

        StringBuilder stringBuilder = null;

        if (!isSortByEmpty()) {

            for (Map.Entry<String, SortOrder> pair : sortBy.getSortBys().entrySet()) {
                if (null == stringBuilder) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(pair.getKey()).append(SPACE).append(pair.getValue());
                } else {
                    stringBuilder.append(COMMA).append(pair.getKey()).append(SPACE).append(pair.getValue());
                }
            }
        }

        return (null == stringBuilder) ? BLANK : stringBuilder.toString();
    }

    /**
     * The Constant BLANK.
     */
    private static final String BLANK = "";

    /**
     * The Constant SPACE.
     */
    private static final String SPACE = " ";

    /**
     * The Constant LIKE_PREFIX.
     */
    private static final String LIKE_PREFIX = " LIKE '%";

    /**
     * The Constant LIKE_SUFFIX.
     */
    private static final String LIKE_SUFFIX = "%' ";

    /**
     * The Constant AND.
     */
    private static final String AND = " AND ";

    /**
     * The Constant OR.
     */
    private static final String OR = " OR ";

    /**
     * The Constant ORDER_BY.
     */
    private static final String ORDER_BY = " ORDER BY ";

    private static final String BRKT_OPN = " ( ";

    private static final String BRKT_CLS = " ) ";

    /**
     * The Constant COMMA.
     */
    private static final String COMMA = " , ";

    /**
     * The Constant PAGE_NO.
     */
    public static final String PAGE_NO = "start";

    /**
     * The Constant PAGE_SIZE.
     */
    public static final String PAGE_SIZE = "length";

    /**
     * The Constant DRAW.
     */
    public static final String DRAW = "draw";

}
