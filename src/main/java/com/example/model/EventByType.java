package com.example.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.example.model.primarykey.EventTypeDateTime;

@Table(value = "events_by_type")
public class EventByType extends Event {

  @PrimaryKey
  private EventTypeDateTime primaryKey;

  @Column(value = "reference")
  private String reference;

  @Column(value = "correlation_id")
  private String correlationId;


  public EventTypeDateTime getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(final EventTypeDateTime primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(final String reference) {
    this.reference = reference;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(final String correlationId) {
    this.correlationId = correlationId;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("eventType", getPrimaryKey().getEventType())
        .append("eventDateTime", getPrimaryKey().getEventDateTime())
        .append("correlationId", getCorrelationId())
        .append("clientIp", getClientIp())
        .append("userAgent", getUserAgent())
        .append("userAgentFiltered", getUserAgentFiltered())
        .append("details", getDetails())
        .append("reference", getReference())
        .toString();
  }
}
