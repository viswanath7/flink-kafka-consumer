package com.example.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cassandra.core.CqlOperations;
import org.springframework.cassandra.core.CqlTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@ComponentScan(basePackages = "com.example",
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = { "com.example.configuration.TestConfiguration" })
    })@PropertySource(value = {"classpath:cassandra.properties"})
@EnableCassandraRepositories("com.example.repository")
public class ApplicationConfiguration extends AbstractCassandraConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfiguration.class);

  @Value("${cassandra.keyspace}")
  private String keySpace;

  @Value("${cassandra.contactpoints}")
  private String contactPoints;

  @Value("${cassandra.port}")
  private String port;

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }
  
  @Override
  protected String getKeyspaceName() {
    LOGGER.info("Using key-space: {}", keySpace);
    return keySpace;
  }

  @Override
  @Bean
  public CassandraClusterFactoryBean cluster() {
    final Integer portNumber = Integer.parseInt(port);
    final CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
    cluster.setContactPoints(contactPoints);
    cluster.setPort(portNumber);
    LOGGER.info("Cluster created with contact points [" + contactPoints + "] " + "& portNumber [" + portNumber + "].");
    return cluster;
  }

  @Override
  @Bean
  public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
    return new BasicCassandraMappingContext();
  }

  @Bean
  public CassandraMappingContext mappingContext() {
    return new BasicCassandraMappingContext();
  }

  @Bean
  public CassandraConverter converter() {
    return new MappingCassandraConverter(mappingContext());
  }

  @Bean
  public CqlOperations CqlTemplate() throws Exception {
    return new CqlTemplate(session().getObject());
  }

  @Bean
  public CassandraSessionFactoryBean session() throws Exception {
    LOGGER.info("Creating cassandra session factory ...");
    CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
    session.setCluster(cluster().getObject());
    session.setKeyspaceName(getKeyspaceName());
    session.setConverter(converter());
    session.setSchemaAction(SchemaAction.NONE);
    LOGGER.info("Cassandra session factory has been successfully created!");
    return session;
  }

}
