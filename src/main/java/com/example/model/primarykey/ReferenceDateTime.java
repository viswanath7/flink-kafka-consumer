package com.example.model.primarykey;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class ReferenceDateTime implements Serializable {

  @PrimaryKeyColumn(name = "reference", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
  private String reference;

  @PrimaryKeyColumn(name = "year", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
  private Integer year;

  @PrimaryKeyColumn(name = "event_date_time", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
  private Date eventDateTime;

  public String getReference() {
    return reference;
  }

  public void setReference(final String reference) {
    this.reference = reference;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(final Integer year) {
    this.year = year;
  }

  public Date getEventDateTime() {
    return eventDateTime;
  }

  public void setEventDateTime(final Date eventDateTime) {
    this.eventDateTime = eventDateTime;
  }

  public ReferenceDateTime() {}

  public ReferenceDateTime(final String reference, final Integer year, final Date eventDateTime) {
    this();
    this.reference = reference;
    this.year = year;
    this.eventDateTime = eventDateTime;
  }

  public ReferenceDateTime(final String reference, final LocalDateTime eventDateTime) {
    this();
    this.reference = reference;
    this.year = eventDateTime.getYear();
    this.eventDateTime = Date.from(eventDateTime.atZone(ZoneId.of("UTC")).toInstant());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ReferenceDateTime)) {
      return false;
    }
    final ReferenceDateTime that = (ReferenceDateTime) o;
    return Objects.equals(getReference(), that.getReference()) &&
        Objects.equals(getYear(), that.getYear()) &&
        Objects.equals(getEventDateTime(), that.getEventDateTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getReference(), getYear(), getEventDateTime());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("reference", reference)
        .append("year", year)
        .append("eventDateTime", eventDateTime)
        .toString();
  }
}
