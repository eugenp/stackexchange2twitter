package org.common.spring;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.tweet.spring.util.SpringProfileUtil;

import com.google.api.client.util.Preconditions;

@Configuration
@EnableTransactionManagement
@Profile(SpringProfileUtil.PERSISTENCE)
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
        factoryBean.setDataSource(mainDataSource());
        factoryBean.setPackagesToScan(new String[] { "org.common.persistence", "org.keyval.persistence", "org.tweet.meta.persistence", "org.rss.persistence", "org.stackexchange.persistence", "org.tweet.persistence", "org.gplus.persistence" });

        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter() {
            {
                // setDatabase( Database.H2 ); // TODO: is this necessary
                setDatabasePlatform(Preconditions.checkNotNull(env.getProperty("hibernate.dialect")));
                setShowSql(Preconditions.checkNotNull(env.getProperty("hibernate.show_sql", Boolean.class)));
                setGenerateDdl(Preconditions.checkNotNull(env.getProperty("jpa.generateDdl", Boolean.class)));
            }
        };
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        factoryBean.setJpaProperties(additionlProperties());

        return factoryBean;
    }

    @Bean
    public DataSource mainDataSource() {
        return tomcatJdbcDataSource();
        // return tomcatDbcpDataSource();
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

    // data sources

    private DataSource tomcatDbcpDataSource() {
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

    private DataSource tomcatJdbcDataSource() {
        final org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        final PoolProperties p = new PoolProperties();

        p.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        p.setUrl(env.getProperty("jdbc.url"));
        p.setUsername(env.getProperty("jdbc.username"));
        p.setPassword(env.getProperty("jdbc.password"));

        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(30);
        p.setMaxWait(20000);
        p.setRemoveAbandonedTimeout(90);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        dataSource.setPoolProperties(p);

        return dataSource;
    }

}
