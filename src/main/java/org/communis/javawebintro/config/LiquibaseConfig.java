package org.communis.javawebintro.config;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Liquibase configuration class
 */
@Configuration
public class LiquibaseConfig {

    /**
     * {@link SpringLiquibase} builder method for given datasource and properties
     */
    private static SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties properties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setContexts(properties.getContexts());
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setDropFirst(properties.isDropFirst());
        liquibase.setShouldRun(properties.isEnabled());
        liquibase.setLabels(properties.getLabels());
        liquibase.setChangeLogParameters(properties.getParameters());
        liquibase.setRollbackFile(properties.getRollbackFile());
        return liquibase;
    }

    /**
     * Liquibase bean for primary datasource
     */
    @Bean(name = "liquibase")
    @Primary
    public SpringLiquibase primaryLiquibase(@Qualifier("dataSource") DataSource dataSource) {
        return springLiquibase(dataSource, defaultLiquibaseProperties());
    }

    /**
     * Liquibase bean for first datasource
     */
    @Bean(name = "liquibase2")
    public SpringLiquibase firstLiquibase(@Qualifier("firstDatasource") DataSource dataSource) {
        return springLiquibase(dataSource, firstDataSourceLiquibaseProperties());
    }

    /**
     * Liquibase bean for second datasource
     */
    @Bean(name = "liquibase3")
    public SpringLiquibase secondLiquibase(@Qualifier("secondDatasource") DataSource dataSource) {
        return springLiquibase(dataSource, secondDataSourceLiquibaseProperties());
    }

    /**
     * Primary Liquibase properties bean, filled from application.yml
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.liquibase")
    public LiquibaseProperties defaultLiquibaseProperties() {
        return new LiquibaseProperties();
    }
    /**
     * First datasource Liquibase properties bean, filled from application.yml
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.first.liquibase")
    public LiquibaseProperties firstDataSourceLiquibaseProperties() {
        return new LiquibaseProperties();
    }
    /**
     * Second Liquibase properties bean, filled from application.yml
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.second.liquibase")
    public LiquibaseProperties secondDataSourceLiquibaseProperties() {
        return new LiquibaseProperties();
    }
}
