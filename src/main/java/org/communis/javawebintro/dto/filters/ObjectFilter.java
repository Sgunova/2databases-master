package org.communis.javawebintro.dto.filters;

import java.lang.reflect.Field;

public class ObjectFilter {

    static final String DATE_FORMAT = "dd.MM.yyyy";

    private String search;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean isFilterExist() throws IllegalAccessException {
        for (Field f : getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if (f.get(this) != null && !f.get(this).toString().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
