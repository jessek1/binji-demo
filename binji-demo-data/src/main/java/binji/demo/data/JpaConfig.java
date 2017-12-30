package binji.demo.data;

import java.util.Properties;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration of dependencies for Jpa
 * 
 * @author jesse keane
 *
 */
@Configuration
@EnableJpaRepositories(basePackages = "binji.demo.data.repositories")
@EnableTransactionManagement
@PropertySource(value = { "classpath:binji-demo.properties" })
public class JpaConfig implements EnvironmentAware {
	private Environment environment;
	
	@Override
	public void setEnvironment(Environment environment) {		
		this.environment = environment;				
	}


	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
		dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
		return dataSource;
	}

	@Bean
	@DependsOn("flyway")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource());
		factory.setJpaVendorAdapter(jpaVendorAdapter());
		factory.setPackagesToScan("binji.demo.data.entities");
		//factory.setJpaProperties(jpaProperties());
		return factory;
	}
	
	public Properties jpaProperties() {
	    Properties properties = new Properties();

	   
	    properties.put(
	        "javax.persistence.schema-generation.create-database-schemas", "true");
	    properties.put("javax.persistence.schema-generation.scripts.action",
	        "create");
	    properties.put("javax.persistence.schema-generation.scripts.create-target",
	        "src/main/resources/schema.sql");
	    //properties.put("javax.persistence.database-product-name", "HSQL");

	    return properties;
	  }

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return txManager;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter
				.setShowSql(Boolean.parseBoolean(environment.getRequiredProperty("hibernate.show_sql")));
		hibernateJpaVendorAdapter
				.setGenerateDdl(Boolean.parseBoolean(environment.getRequiredProperty("hibernate.generate_ddl")));
		hibernateJpaVendorAdapter.setDatabase(Database.POSTGRESQL);
		
		return hibernateJpaVendorAdapter;
	}

	@Bean
	public Flyway flyway() {
		Boolean isFlywayAutoMigrationEnabled = Boolean
				.parseBoolean(environment.getRequiredProperty("flyway.auto.migration.enabled"));

		Flyway flyway = new Flyway();
		//flyway.setBaselineOnMigrate(true);
		flyway.setLocations("binji.demo.data.migrations");
		flyway.setDataSource(dataSource());
		flyway.setCleanDisabled(true);
		if (isFlywayAutoMigrationEnabled) {
			flyway.migrate();
		}
		return flyway;

	}
}
