package org.communis.javawebintro.utils;

import java.text.ParseException;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.communis.javawebintro.first.entity.FirstDataSourceEntity;
import org.communis.javawebintro.first.service.FirstDatasourceEntityService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FirstDataSourceEntityFormatter implements Formatter<FirstDataSourceEntity> {

    private final FirstDatasourceEntityService service;

    public FirstDataSourceEntityFormatter(FirstDatasourceEntityService service) {
        this.service = service;
    }

    @Override
    public FirstDataSourceEntity parse(String text, Locale locale) throws ParseException {
        try {
            final Long id = Long.valueOf(text);
            return service.findById(id);
        } catch (NumberFormatException e) {
            log.error("Null id passed", e);
        }
        return null;
    }

    @Override
    public String print(final FirstDataSourceEntity object, Locale locale) {
        return (object != null ? object.toString() : "");
    }
}
