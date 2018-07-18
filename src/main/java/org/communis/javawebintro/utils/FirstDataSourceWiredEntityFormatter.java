package org.communis.javawebintro.utils;

import java.text.ParseException;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.communis.javawebintro.first.entity.FirstDataSourceWiredEntity;
import org.communis.javawebintro.first.service.FirstDatasourceWiredEntityService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FirstDataSourceWiredEntityFormatter implements Formatter<FirstDataSourceWiredEntity> {

    private final FirstDatasourceWiredEntityService service;

    public FirstDataSourceWiredEntityFormatter(
            FirstDatasourceWiredEntityService service) {
        this.service = service;
    }

    @Override
    public FirstDataSourceWiredEntity parse(String text, Locale locale) throws ParseException {
        final Long id;
        try {
            id = Long.valueOf(text);
            return service.findById(id);
        } catch (NumberFormatException e) {
            log.error("No id passed", e);
        }
        return null;
    }

    @Override
    public String print(FirstDataSourceWiredEntity object, Locale locale) {
        return (object != null ? object.toString() : "");
    }
}
