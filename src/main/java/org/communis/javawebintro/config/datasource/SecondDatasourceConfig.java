package org.communis.javawebintro.config.datasource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration class for second embedded datasource
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"org.communis.javawebintro.second.repository"},
        transactionManagerRef = "secondTransactionManager",
        entityManagerFactoryRef = "secondEntityManagerFactory"
)
public class SecondDatasourceConfig {
    @Autowired
    private Environment env;

    /**
     * Configure datasource from configuration
     *
     * @return
     */
    @Bean(name = "secondDatasource")
    @LiquibaseDataSource
    @ConfigurationProperties(prefix = "spring.datasource.second")
    public DataSource secondDatasourceEntityDatasource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Configure entity manager
     *
     * @return
     */
    @Bean(name = "secondEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondDatasourceEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("secondDatasource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                // packages to scan for second datasource
                .packages("org.communis.javawebintro.second.entity")
                // second datasource entity manager label
                .persistenceUnit("second")
                .build();
    }

    /**
     * Transaction manager bean for second datasource
     */
    @Bean(name = "secondTransactionManager")
    public PlatformTransactionManager secondDatasourceTransactionManager(
            @Qualifier("secondEntityManagerFactory") EntityManagerFactory factory
    ) {
        return new JpaTransactionManager(factory);
    }

}
