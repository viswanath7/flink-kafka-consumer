package com.example.model.primarykey;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import com.example.model.Event;

@PrimaryKeyClass
public class EventTypeDateTime implements Serializable {

  @PrimaryKeyColumn(name = "event_type", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
  private String eventType;

  @PrimaryKeyColumn(name = "event_date_time", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
  private Date eventDateTime;

  public EventTypeDateTime() {}

  public EventTypeDateTime(final String eventType, final Date eventDateTime) {
    this();
    setEventType(eventType);
    setEventDateTime(eventDateTime);
  }

  public EventTypeDateTime(final String eventType, final LocalDateTime eventDateTime) {
    this();
    setEventType(eventType);
    setEventDateTime(Date.from(eventDateTime.atZone(Event.UTC).toInstant()));
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(final String eventType) {
    this.eventType = eventType;
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
    if (!(o instanceof EventTypeDateTime)) {
      return false;
    }
    final EventTypeDateTime that = (EventTypeDateTime) o;
    return Objects.equals(getEventType(), that.getEventType()) &&
        Objects.equals(getEventDateTime(), that.getEventDateTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getEventType(), getEventDateTime());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("eventType", eventType)
        .append("eventDateTime", eventDateTime)
        .toString();
  }

}
