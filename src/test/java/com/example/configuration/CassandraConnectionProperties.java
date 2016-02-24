package com.example.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CassandraConnectionProperties {

  @Value("cassandra.keyspace")
  private String keySpace;

  @Value("cassandra.contactpoints")
  private String contactPoints;

  @Value("cassandra.port")
  private String port;

  public String getKeySpace() {
    return keySpace;
  }

  public void setKeySpace(final String keySpace) {
    this.keySpace = keySpace;
  }

  public String getContactPoints() {
    return contactPoints;
  }

  public void setContactPoints(final String contactPoints) {
    this.contactPoints = contactPoints;
  }

  public String getPort() {
    return port;
  }

  public void setPort(final String port) {
    this.port = port;
  }
}
