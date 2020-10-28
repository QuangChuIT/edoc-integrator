package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.pagination.PaginationCriteria;
import com.google.gson.Gson;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class AppUtil {

    /**
     * Checks if is collection empty.
     *
     * @param collection the collection
     * @return true, if is collection empty
     */
    private static boolean isCollectionEmpty(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Checks if is object empty.
     *
     * @param object the object
     * @return true, if is object empty
     */
    public static boolean isObjectEmpty(Object object) {
        if (object == null) return true;
        else if (object instanceof String) {
            if (((String) object).trim().length() == 0) {
                return true;
            }
        } else if (object instanceof Collection) {
            return isCollectionEmpty((Collection<?>) object);
        }
        return false;
    }

    /**
     * Gets the bean to json string.
     *
     * @param beanClass the bean class
     * @return the bean to json string
     */
    public static String getBeanToJsonString(Object beanClass) {
        return new Gson().toJson(beanClass);
    }

    /**
     * Gets the bean to json string.
     *
     * @param beanClasses the bean classes
     * @return the bean to json string
     */
    public static String getBeanToJsonString(Object... beanClasses) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object beanClass : beanClasses) {
            stringBuilder.append(getBeanToJsonString(beanClass));
            stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }

    /**
     * Concatenate.
     *
     * @param listOfItems the list of items
     * @param separator   the separator
     * @return the string
     */
    public String concatenate(List<String> listOfItems, String separator) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = listOfItems.iterator();

        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()) {
                sb.append(separator);
            }
        }

        return sb.toString();
    }

    /**
     * Builds the paginated query.
     *
     * @param baseQuery          the base query
     * @param paginationCriteria the pagination criteria
     * @return the string
     */
    public static String buildPaginatedQuery(String baseQuery, PaginationCriteria paginationCriteria) {
        String finalQuery = null;
        if (!AppUtil.isObjectEmpty(paginationCriteria)) {
            finalQuery = baseQuery
                    .replaceAll("#WHERE_CLAUSE#", ((AppUtil.isObjectEmpty(paginationCriteria.getFilterByClause())) ? "" : " AND ") + paginationCriteria.getFilterByClause())
                    .replaceAll("#ORDER_CLASUE#", paginationCriteria.getOrderByClause());
        }
        return (null == finalQuery) ? baseQuery : finalQuery;
    }

}
