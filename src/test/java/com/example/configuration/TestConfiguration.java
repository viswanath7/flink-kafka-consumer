package com.example.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@PropertySource(value = { "classpath:cassandra-test.properties" })
@ComponentScan(basePackages = "com.example",
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = { "com.example.configuration.ApplicationConfiguration" })
    })
@EnableCassandraRepositories(basePackages = "com.example.repository")
public class TestConfiguration extends AbstractCassandraConfiguration {
  private static final Logger LOG = LoggerFactory.getLogger(TestConfiguration.class);

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
    LOG.info("Using key space: '{}' for test configuration.\n", keySpace);
    return keySpace;
  }

  @Override
  @Bean
  public CassandraClusterFactoryBean cluster() {
    LOG.info("Creating test cluster created with contact points [" + contactPoints + "] " + "& port [" + port + "].");
    final CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
    cluster.setContactPoints(contactPoints);
    cluster.setPort(Integer.parseInt(port));
    return cluster;
  }

  @Override
  @Bean
  public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
    return new BasicCassandraMappingContext();
  }
}