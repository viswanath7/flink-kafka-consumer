package com.example.repository;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.StreamSupport;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.cql.CqlIdentifier;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.example.configuration.TestConfiguration;
import com.example.converter.MessagingToDomainObjectConverter;
import com.example.domain.UserActivityEvent;
import com.example.mapper.JSONDeserialiser;
import com.example.model.EventByCorrelationId;
import com.example.model.EventByReference;
import com.example.model.EventByType;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class CassandraRepositoriesIntegrationTest {
  public static final String KAFKA_EVENT = "src/test/resources/kafka_event.json";
  public static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS test_keyspace " + "WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };";
  public static final String KEYSPACE_ACTIVATE_QUERY = "USE test_keyspace;";
  public static final String EVENTS_BY_CORRELATION_ID_COLUMN_FAMILY = "events_by_correlation_id";
  public static final String EVENTS_BY_TYPE_COLUMN_FAMILY = "events_by_type";
  public static final String EVENTS_BY_REFERENCE_AND_YEAR_COLUMN_FAMILY = "events_by_reference_and_year";
  public static final String EVENT_REFERENCE = "qtvszw9qbjnswfprsxnfelk2yvdvqt09";
  public static final String EVENT_CORRELATION_ID = "2c08b8f1-afbb-455a-a1bd-31f15115d424";
  public static final String EVENT_TYPE = "LOGIN_INTERCEPTOR_CREATE_SESSION";
  private static final Logger LOG = LoggerFactory.getLogger(CassandraRepositoriesIntegrationTest.class);

  @Autowired
  private CassandraAdminOperations adminTemplate;

  @Autowired
  private CassandraOperations cassandraTemplate;

  @Autowired
  private JSONDeserialiser jsonDeserialiser;

  @Autowired
  private MessagingToDomainObjectConverter messagingToDomainObjectConverter;

  @Autowired
  private EventsByCorrelationIdRepository eventsByCorrelationIdRepository;

  @Autowired
  private EventsByReferenceRepository eventsByReferenceRepository;

  @Autowired
  private EventsByTypeRepository eventsByTypeRepository;

  private String json;

  @BeforeClass
  public static void startCassandraEmbedded() throws InterruptedException, TTransportException, ConfigurationException, IOException {
    LOG.debug("Staring embedded cassandra database for integration test ...");
    EmbeddedCassandraServerHelper.startEmbeddedCassandra();
    final Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").withPort(9142).build();
    LOG.info("Server Started at 127.0.0.1:9142... ");
    final Session session = cluster.connect();
    session.execute(KEYSPACE_CREATION_QUERY);
    session.execute(KEYSPACE_ACTIVATE_QUERY);
    LOG.info("KeySpace created and activated.");
    Thread.sleep(5000);
  }

  @AfterClass
  public static void stopCassandraEmbedded() {
    LOG.debug("Stopping embedded cassandra database used for integration tests ...");
    EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
  }

  @Before
  public void readContent() throws InterruptedException, TTransportException, ConfigurationException, IOException {
    LOG.debug("Reading content for column family: {} from the data file: {} ...", EVENTS_BY_CORRELATION_ID_COLUMN_FAMILY, KAFKA_EVENT);
    json = new String (Files.readAllBytes(Paths.get(KAFKA_EVENT)), StandardCharsets.UTF_8);
  }

  @Test
  public void should_save_and_retrieve_event_by_correlation_id() {

    LOG.debug("Testing save operation for 'EventByCorrelationId' followed by a retrieve");

    LOG.debug("Creating column-family: {} ...", EVENTS_BY_CORRELATION_ID_COLUMN_FAMILY);
    adminTemplate.createTable(true, CqlIdentifier.cqlId(EVENTS_BY_CORRELATION_ID_COLUMN_FAMILY), EventByCorrelationId.class, new HashMap<String, Object>());

    final EventByCorrelationId eventByCorrelationId = messagingToDomainObjectConverter.toEventByCorrelationId(getUserActivityEvent(json));
    assertThat( eventByCorrelationId, notNullValue());
    assertThat( eventByCorrelationId.getReference(), equalToIgnoringCase(EVENT_REFERENCE) );

    eventsByCorrelationIdRepository.save(eventByCorrelationId);

    final Select select = QueryBuilder.select().from(EVENTS_BY_CORRELATION_ID_COLUMN_FAMILY)
        .where(QueryBuilder.eq("correlation_id", EVENT_CORRELATION_ID))
        .and(QueryBuilder.gt("event_date_time", convert(LocalDateTime.of(2012, 12, 31, 23, 59)) )).limit(10);

    final EventByCorrelationId event = cassandraTemplate.selectOne(select, EventByCorrelationId.class);

    assertThat(event, notNullValue());
    assertThat(event.getPrimaryKey().getCorrelationId(), equalToIgnoringCase(EVENT_CORRELATION_ID));
    assertThat(event.getReference(), equalToIgnoringCase(EVENT_REFERENCE));

    LOG.debug("Dropping column family: {} ...", EVENTS_BY_CORRELATION_ID_COLUMN_FAMILY);
    adminTemplate.dropTable(CqlIdentifier.cqlId(EVENTS_BY_CORRELATION_ID_COLUMN_FAMILY));
  }

  @Test
  public void should_save_and_retrieve_event_by_reference_and_year() {

    LOG.debug("Testing save operation for 'EventByReference' followed by a retrieve");

    LOG.debug("Creating column-family: {} ...", EVENTS_BY_REFERENCE_AND_YEAR_COLUMN_FAMILY);
    adminTemplate.createTable(true, CqlIdentifier.cqlId(EVENTS_BY_REFERENCE_AND_YEAR_COLUMN_FAMILY), EventByReference.class, new HashMap<String, Object>());

    final EventByReference eventByReference = messagingToDomainObjectConverter.toEventByReference(getUserActivityEvent(json));
    assertThat( eventByReference, notNullValue());
    assertThat( eventByReference.getPrimaryKey().getReference(), equalToIgnoringCase(EVENT_REFERENCE) );

    eventsByReferenceRepository.save(eventByReference);

    final Iterable<EventByReference> iterable = eventsByReferenceRepository.findByReferenceAndYear(EVENT_REFERENCE, 2016);

    assertTrue(StreamSupport.stream(iterable.spliterator(), false)
        .allMatch(event-> event.getPrimaryKey().getReference().equalsIgnoreCase(EVENT_REFERENCE) &&
        event.getPrimaryKey().getYear() == 2016));

    LOG.debug("Dropping column family: {} ...", EVENTS_BY_REFERENCE_AND_YEAR_COLUMN_FAMILY);
    adminTemplate.dropTable(CqlIdentifier.cqlId(EVENTS_BY_REFERENCE_AND_YEAR_COLUMN_FAMILY));
  }

  @Test
  public void should_save_and_retrieve_event_by_type() {
    LOG.debug("Testing save operation for 'EventByType' followed by a retrieve");

    LOG.debug("Creating column-family: {} ...", EVENTS_BY_TYPE_COLUMN_FAMILY);
    adminTemplate.createTable(true, CqlIdentifier.cqlId(EVENTS_BY_TYPE_COLUMN_FAMILY), EventByType.class, new HashMap<String, Object>());

    final EventByType eventByType = messagingToDomainObjectConverter.toEventByType(getUserActivityEvent(json));
    assertThat( eventByType, notNullValue());
    assertThat( eventByType.getPrimaryKey().getEventType(), equalToIgnoringCase(EVENT_TYPE));
    assertThat( eventByType.getReference(), equalToIgnoringCase(EVENT_REFERENCE) );

    eventsByTypeRepository.save(eventByType);

    final Iterable<EventByType> iterable = eventsByTypeRepository.findByType(EVENT_TYPE);

    assertTrue(StreamSupport.stream(iterable.spliterator(), false)
        .allMatch(event-> event.getPrimaryKey().getEventType().equalsIgnoreCase(EVENT_TYPE)));

    LOG.debug("Dropping column family: {} ...", EVENTS_BY_TYPE_COLUMN_FAMILY);
    adminTemplate.dropTable(CqlIdentifier.cqlId(EVENTS_BY_TYPE_COLUMN_FAMILY));
  }

  private UserActivityEvent getUserActivityEvent(final String json) {
    if(StringUtils.isBlank(json)) return null;
    try {
      return jsonDeserialiser.convert(json, UserActivityEvent.class);
    } catch (final IOException ioEx) {
      LOG.error("Error encountered while creating a user activity event from json: {}", json, ioEx);
      return null;
    }
  }

  private Date convert(final LocalDateTime localDateTime) {
    return (localDateTime == null)? null : Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

}
