package com.example.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;


@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({CassandraUnitTestExecutionListener.class })
@EmbeddedCassandra(timeout = 10000)
@CassandraDataSet(value = { "users_activity_db.cql" }, keyspace = "cassandra_unit_keyspace")
public class CQLScriptIntegrationTest {

  public static final String CONTACT_POINT = "127.0.0.1";
  public static final int PORT = 9142;
  public static final String DEFAULT_CASSANDRA_UNIT_KEYSPACE = "cassandra_unit_keyspace";
  public static final String QUERY_EVENTS_BY_CORRELATION_ID = "SELECT * FROM events_by_correlation_id " +
      "WHERE correlation_id = 'cfd66ccc-d857-4e90-b1e5-df98a3d40cd6' ";
  public static final String QUERY_EVENTS_BY_REFERENCE = "SELECT * FROM events_by_reference_and_year " +
      "WHERE reference = 'wenstfhjwwhucm9zme04szfyq0tdykpbtxlmwlbnywm[' AND year = 2016 AND event_date_time > '2016-01-20 10:26:06' ";
  private static final Logger LOG = LoggerFactory.getLogger(CQLScriptIntegrationTest.class);

  @Test
  public void should_correctly_execute_cql_script_to_create_column_families() throws Exception {

    LOG.debug("Creating a session to connect to the embedded cassandra database ...");
    Cluster cluster = Cluster.builder().addContactPoints(CONTACT_POINT).withPort(PORT).build();
    Session session = cluster.connect(DEFAULT_CASSANDRA_UNIT_KEYSPACE);

    LOG.debug("Verifying the pre-executed script through execution of select queries ...");
    assertThat(session.execute(QUERY_EVENTS_BY_CORRELATION_ID).iterator().next().getString("correlation_id"), is("cfd66ccc-d857-4e90-b1e5-df98a3d40cd6"));
    assertThat(session.execute(QUERY_EVENTS_BY_REFERENCE).iterator().next().getString("event_type"), is("USER_ACCESSED_PROFILE"));

  }

}
