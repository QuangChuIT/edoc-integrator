package com.bkav.edoc.service.database.entity.pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class DatatableColumnSpecs {
    private int index;

    /**
     * The data.
     */
    private String data;

    /**
     * The name.
     */
    private String name;

    /**
     * The searchable.
     */
    private boolean searchable;

    /**
     * The orderAble.
     */
    private boolean orderAble;

    /**
     * The search.
     */
    private String search;

    /**
     * The regex.
     */
    private boolean regex;

    /**
     * The sort dir.
     */
    private String sortDir;

    private static List<String> columns;

    public DatatableColumnSpecs(HttpServletRequest request, int i) {
        this.setIndex(i);
        prepareColumnSpecs(request, i);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public boolean isOrderAble() {
        return orderAble;
    }

    public void setOrderAble(boolean orderAble) {
        this.orderAble = orderAble;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean isRegex() {
        return regex;
    }

    public void setRegex(boolean regex) {
        this.regex = regex;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = (null != sortDir) ? sortDir.toUpperCase() : null;
    }

    /**
     * Prepare column specs.
     *
     * @param request the request
     * @param i       the i
     */
    private void prepareColumnSpecs(HttpServletRequest request, int i) {

        this.setOrderAble(Boolean.parseBoolean(request.getParameter("columns[" + i + "][orderable]")));
        this.setRegex(Boolean.parseBoolean(request.getParameter("columns[" + i + "][search][regex]")));
        this.setSearch(request.getParameter("columns[" + i + "][search][value]"));
        this.setSearchable(Boolean.parseBoolean(request.getParameter("columns[" + i + "][searchable]")));
        int sortableCol = Integer.parseInt(request.getParameter("order[0][column]"));
        this.setData(request.getParameter("columns[" + i + "][data]"));
        this.setName(request.getParameter("columns[" + i + "][name]"));
        /*this.setData(columns.get(i));
        this.setName(columns.get(i));*/
        String sortDir = request.getParameter("order[0][dir]");

        if (i == sortableCol) {
            this.setSortDir(sortDir);
        }
    }

    static {
        columns = new ArrayList<>();
        columns.add("ed.subject");
        columns.add("ed.fromOrganDomain");
        columns.add("ed.toOrganDomain");
        columns.add("ed.docCode");
        columns.add("ed.documentTypeName");
        columns.add("ed.createDate");
    }
}
