package com.example.model;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.example.model.primarykey.CorrelationIdDateTime;

@Table(value = "events_by_correlation_id")
public class EventByCorrelationId extends Event {

  @Column(value = "event_type")
  protected String eventType;
  @PrimaryKey
  private CorrelationIdDateTime primaryKey;
  @Column(value = "reference")
  private String reference;

  public EventByCorrelationId(final String eventType, final LocalDateTime date, final String correlationId,
                              final String clientIp, final String userAgent,
                              final String userAgentFiltered, final String details, final String reference) {
    this();
    setEventType(eventType);
    getPrimaryKey().setEventDateTime(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()));
    getPrimaryKey().setCorrelationId(correlationId);
    setClientIp(clientIp);
    setUserAgent(userAgent);
    setUserAgentFiltered(userAgentFiltered);
    setDetails(details);
    setReference(reference);
  }

  public EventByCorrelationId() {
  }

  public CorrelationIdDateTime getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(final CorrelationIdDateTime primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(final String reference) {
    this.reference = reference;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(final String eventType) {
    this.eventType = eventType;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("eventType", getEventType())
        .append("eventDateTime", getPrimaryKey().getEventDateTime())
        .append("correlationId", getPrimaryKey().getCorrelationId())
        .append("clientIp", getClientIp())
        .append("userAgent", getUserAgent())
        .append("userAgentFiltered", getUserAgentFiltered())
        .append("details", getDetails())
        .append("reference", getReference())
        .toString();
  }

}
