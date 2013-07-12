package org.common.spring;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class PersistenceJPACommonConfig {

    @Autowired
    private Environment env;

    public PersistenceJPACommonConfig() {
        super();
    }

    // beans

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(restDataSource());
        factoryBean.setPackagesToScan(new String[] { "org.common.persistence", "org.keyval.persistence", "org.tweet.meta.persistence", "org.rss.persistence", "org.stackexchange.persistence", "org.tweet.persistence", "org.gplus.persistence" });

        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter() {
            {
                // setDatabase( Database.H2 ); // TODO: is this necessary
                setDatabasePlatform(env.getProperty("hibernate.dialect"));
                setShowSql(env.getProperty("hibernate.show_sql", Boolean.class));
                setGenerateDdl(env.getProperty("jpa.generateDdl", Boolean.class));
            }
        };
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        factoryBean.setJpaProperties(additionlProperties());

        return factoryBean;
    }

    @Bean
    public DataSource restDataSource() {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.username"));
        dataSource.setPassword(env.getProperty("jdbc.password"));

        // specific
        dataSource.setMaxWait(120000);

        dataSource.setRemoveAbandoned(true);
        dataSource.setLogAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(120);

        return dataSource;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    //
    final Properties additionlProperties() {
        return new Properties() {
            {
                // use this to inject additional properties in the EntityManager
                setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
                setProperty("hibernate.ejb.naming_strategy", org.hibernate.cfg.ImprovedNamingStrategy.class.getName());
                setProperty("hibernate.globally_quoted_identifiers", "true");

                // connection handling
                // <prop key="hibernate.connection.release_mode">after_statement</prop>
                // http://stackoverflow.com/questions/7766023/hibernate-failed-to-execute-query-afte-1-day

                // dbcp - http://tomcat.apache.org/tomcat-7.0-doc/jdbc-pool.html
                setProperty("hibernate.dbcp.maxWait", "120000"); // 2*60*1000 = 2 mins
                setProperty("hibernate.dbcp.ps.maxWait", "120000"); // 2*60*1000 = 2 mins
            }
        };
    }
}
