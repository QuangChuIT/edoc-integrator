package com.bkav.edoc.converter;

import java.sql.SQLException;
import java.util.List;

public interface Converter<T> {

    List<T> getFromDatabase() throws SQLException;

    void convert(List<T> list) throws SQLException;

}
