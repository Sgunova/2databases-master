package org.communis.javawebintro.config.datasource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Base datasource configuration class
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "org.communis.javawebintro.repository",
        transactionManagerRef = "transactionManager",
        entityManagerFactoryRef = "entityManagerFactory"
)
public class DefaultDatasourceConfig {

    @Autowired
    private Environment env;

    /**
     * Configure main datasource from config
     *
     * @return
     */
    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource firstDatasourceEntityDatasource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Configure entity manager for main datasource
     *
     * @return
     */
    @Bean(name = "entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean firstDatasourceEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("dataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                //pakages to scan for @Entity
                .packages("org.communis.javawebintro.entity")
                //persistence unit identifier
                .persistenceUnit("default")
                .build();
    }

    /**
     * Transaction manager for main datasorce
     */
    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager firstDatasourceTransactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }
}

