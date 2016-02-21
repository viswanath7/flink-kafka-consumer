package com.example.messaging;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.configuration.ApplicationConfiguration;
import com.example.service.UserActivityEventsPersister;

/**
 * Cassandra database sink to persist json message as rows of tables in cassandra
 */
public class CassandraDataSink implements SinkFunction<String> {

  private static final Logger LOG = LoggerFactory.getLogger(CassandraDataSink.class);

  private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

  /**
   * Function for standard sink behaviour. This function is called for every record.
   *
   * @param jsonEntity The input json entity that shall be stored in various cassandra table
   */
  @Override
  public void invoke(final String jsonEntity) throws Exception {
    LOG.debug("Persisting JSON entity: \n{}\n  via cassandra database sink ...", jsonEntity);
    UserActivityEventsPersister userActivityEventsPersister = applicationContext.getBean(UserActivityEventsPersister.class);
    userActivityEventsPersister.persist(jsonEntity);
  }
}
