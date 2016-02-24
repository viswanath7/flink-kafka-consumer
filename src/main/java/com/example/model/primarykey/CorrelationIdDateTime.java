package com.example.model.primarykey;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import com.example.model.Event;

@PrimaryKeyClass
public class CorrelationIdDateTime implements Serializable {

  @PrimaryKeyColumn(name = "correlation_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
  private String correlationId;

  @PrimaryKeyColumn(name = "event_date_time", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
  private Date eventDateTime;

  public CorrelationIdDateTime() {}

  public CorrelationIdDateTime(final String correlationId, final Date eventDateTime) {
    this();
    this.correlationId = correlationId;
    this.eventDateTime = eventDateTime;
  }

  public CorrelationIdDateTime(final String correlationId, final LocalDateTime eventDateTime) {
    this();
    this.correlationId = correlationId;
    this.eventDateTime = Date.from(eventDateTime.atZone(Event.UTC).toInstant());
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(final String correlationId) {
    this.correlationId = correlationId;
  }

  public Date getEventDateTime() {
    return eventDateTime;
  }

  public void setEventDateTime(final Date eventDateTime) {
    this.eventDateTime = eventDateTime;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CorrelationIdDateTime)) {
      return false;
    }
    final CorrelationIdDateTime that = (CorrelationIdDateTime) o;
    return Objects.equals(getCorrelationId(), that.getCorrelationId()) &&
        Objects.equals(getEventDateTime(), that.getEventDateTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCorrelationId(), getEventDateTime());
  }

}
