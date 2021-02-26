package com.bkav.edoc.web.payload;

import java.io.Serializable;

public class ImportExcelError implements Serializable {
    private int col;
    private int row;
    private String messageKey;

    public ImportExcelError(int col, int row, String messageKey) {
        this.col = col;
        this.row = row;
        this.messageKey = messageKey;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
}
