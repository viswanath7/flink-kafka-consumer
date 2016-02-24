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
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

@TestExecutionListeners({ CassandraUnitTestExecutionListener.class })
@CassandraDataSet(value = { "users_activity_db.cql" })
@EmbeddedCassandra
@RunWith(SpringJUnit4ClassRunner.class)
public class MyIntegrationTest {

  private static final Logger LOG = LoggerFactory.getLogger(MyIntegrationTest.class);

  @Test
  public void should_have_started_and_execute_cql_script() throws Exception {

    Cluster cluster = Cluster.builder()
        .addContactPoints("127.0.0.1")
        .withPort(9142)
        .build();
    Session session = cluster.connect("cassandra_unit_keyspace");

    ResultSet result = session.execute("SELECT * FROM events_by_correlation_id WHERE correlation_id = 'cfd66ccc-d857-4e90-b1e5-df98a3d40cd6' ");
    assertThat(result.iterator().next().getString("correlation_id"), is("cfd66ccc-d857-4e90-b1e5-df98a3d40cd6"));
  }

}
