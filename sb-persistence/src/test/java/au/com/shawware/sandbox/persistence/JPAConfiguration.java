/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.persistence;

import java.sql.SQLException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import au.com.shawware.sandbox.model.Node;

/**
 * Configure a {@link NodeRepository} and its JPA using JavaConfig.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "au.com.shawware.sandbox.persistence",
    includeFilters = @ComponentScan.Filter(
        value = {NodeRepository.class},
        type = FilterType.ASSIGNABLE_TYPE
    )
)
@SuppressWarnings("nls")
public class JPAConfiguration
{
    /**
     * Defines the data source to use.
     * 
     * @return the data source bean
     * 
     * @throws SQLException error creating the bean
     */
    @Bean
    @SuppressWarnings("static-method")
    public DataSource dataSource()
        throws SQLException
    {
        final EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setName("node-test");
        builder.setType(EmbeddedDatabaseType.HSQL);
        return builder.build();
    }

    /**
     * Defines the entity manager factory to use.
     * 
     * @return the entity manager factory bean
     * 
     * @throws SQLException error creating the bean
     */
    @Bean
    public EntityManagerFactory entityManagerFactory()
         throws SQLException
    {
        final HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
//        adapter.setDatabase(Database.HSQL);

        final LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(adapter);
        factory.setPackagesToScan(Node.class.getPackage().getName());
        factory.setDataSource(dataSource());

        final Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.show_sql", "true"); // redundant?
        jpaProperties.setProperty("hibernate.format_sql", "true");
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create");
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        jpaProperties.setProperty("hibernate.connection.pool_size", "0");
//        jpaProperties.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
//        jpaProperties.setProperty("hibernate.hibernate.transaction.factory_class", "org.hibernate.transaction.JDBCTransactionFactory");
        jpaProperties.setProperty("hibernate.connection.url", "jdbc:hsqldb:file:target/data/test;shutdown=true");
        jpaProperties.setProperty("hibernate.connection.username", "sa");
        jpaProperties.setProperty("hibernate.connection.password", "");
        jpaProperties.setProperty("hibernate.connection.autocommit", "true");
        jpaProperties.setProperty("hibernate.jdbc.batch_size", "0");
        jpaProperties.setProperty("hibernate.ejb.entitymanager_factory_name", "sandbox");
        factory.setJpaProperties(jpaProperties);

        // The following method call is important. Things break without it.
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    /**
     * Defines the entity manager to use.
     * 
     * @param entityManagerFactory the factory to use to create the bean
     * 
     * @return the entity manager bean
     */
    @Bean
    @SuppressWarnings("static-method")
    public EntityManager entityManager(final EntityManagerFactory entityManagerFactory)
    {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Defines the transaction manager factory to use.
     * 
     * @return the transaction manager factory bean
     * 
     * @throws SQLException error creating the bean
     */
    @Bean
    public PlatformTransactionManager transactionManager()
        throws SQLException
    {
        final JpaTransactionManager mgr = new JpaTransactionManager();
        mgr.setEntityManagerFactory(entityManagerFactory());
        return mgr;
    }

    /**
     * Defines the exception translator to use.
     * 
     * @return the exception translator factory bean
     */
    @Bean
    @SuppressWarnings("static-method")
    public HibernateExceptionTranslator hibernateExceptionTranslator()
    {
        return new HibernateExceptionTranslator();
    }
}
