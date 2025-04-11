package br.com.fenix.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

//@Configuration
//@EnableTransactionManagement
public class JpaConfig {
/*
 * Exemplo de como configurar um data source 
 */
	
//	    @Autowired
//	    private Environment env;
//
//	    @Bean
//	    public DataSource dataSource() {
//	        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//	        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
//	        dataSource.setUrl(env.getProperty("spring.datasource.url"));
//	        dataSource.setUsername(env.getProperty("spring.datasource.username"));
//	       ;
//		return dataSource;
//	    }
//	  @Bean
//	   public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//	       //...
//	   }
//
//	   @Bean
//	   public PlatformTransactionManager transactionManager() {
//	      JpaTransactionManager transactionManager = new JpaTransactionManager();
//	      transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//	      return transactionManager;
//	   }
//	
}
