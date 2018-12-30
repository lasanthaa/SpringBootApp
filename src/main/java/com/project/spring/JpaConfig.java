package com.project.spring;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:persistance.properties")
@ComponentScan("com.project.entity")
@ComponentScan("com.project.repository")
@EnableJpaRepositories(basePackages = "com.project.repository")
public class JpaConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource datasource = new DriverManagerDataSource();
		datasource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		datasource.setUrl(env.getProperty("jdbc.url"));
		datasource.setUsername(env.getProperty("jdbc.username"));
		datasource.setPassword(env.getProperty("jdbc.password"));
		
		return datasource;
	}
	
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		
		final LocalSessionFactoryBean sessionFactory= new LocalSessionFactoryBean();
		 
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan("com.project.entity");
		sessionFactory.setHibernateProperties(hibernateProperties());
		
		return sessionFactory;
		
	}
	
	@Bean
	@Autowired
	public HibernateTransactionManager transacationManager(SessionFactory sessionFactory) {
			final HibernateTransactionManager txmanager = new HibernateTransactionManager();
			txmanager.setSessionFactory(sessionFactory);		
			
			return txmanager;
	}
	
	@Bean
	public Properties hibernateProperties() {
		final Properties hibernateprop = new Properties();
		hibernateprop.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		
		return hibernateprop;
	}
	
	
}
