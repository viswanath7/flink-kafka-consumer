package com.example.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.domain.UserActivityEvent;

/**
 *
 */
public class JSONDeserialiserTest {
  //TODO: Convert to spring test class

  private static final Logger LOG = LoggerFactory.getLogger(JSONDeserialiserTest.class);

  public static final String KAFKA_EVENT = "src/test/resources/kafka_event.json";

  private String json;
  private JSONDeserialiser jsonDeserialiser;

  @Before
  public void setUp() throws Exception {
    json = new String (Files.readAllBytes(Paths.get(KAFKA_EVENT)), StandardCharsets.UTF_8);
    jsonDeserialiser = new JSONDeserialiser();
  }

  @After
  public void tearDown() throws Exception {
    json = null;
    jsonDeserialiser = null;
  }

  @Test
  public void should_convert_json_to_object() throws Exception {
    LOG.debug("Checking if json string is properly converted to an object ...");
    final UserActivityEvent userActivityEvent = jsonDeserialiser.convert(json, UserActivityEvent.class);
    assertThat(userActivityEvent, notNullValue() );
    assertThat(userActivityEvent.getCorrelationId(), equalToIgnoringCase("2c08b8f1-afbb-455a-a1bd-31f15115d424") );
    assertThat(userActivityEvent.getDate(), equalTo(LocalDateTime.of(2016,2,29,23,45,50,40)) );
  }
}