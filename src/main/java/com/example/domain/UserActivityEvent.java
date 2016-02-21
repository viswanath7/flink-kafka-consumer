package com.example.domain;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class UserActivityEvent implements Serializable {

  private Long id;

  private String eventType;

  private LocalDateTime date;

  private String correlationId;

  private String clientIp;

  private String userAgent;

  private String userAgentFiltered;

  private String details;

  private String reference;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(final LocalDateTime localDateTime) {
    this.date = localDateTime;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(final String correlationId) {
    this.correlationId = correlationId;
  }

  public String getClientIp() {
    return clientIp;
  }

  public void setClientIp(final String clientIp) {
    this.clientIp = clientIp;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(final String userAgent) {
    this.userAgent = userAgent;
  }

  public String getUserAgentFiltered() {
    return userAgentFiltered;
  }

  public void setUserAgentFiltered(final String userAgentFiltered) {
    this.userAgentFiltered = userAgentFiltered;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(final String details) {
    this.details = details;
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

  @JsonIgnore
  public boolean isValid() {
    return eventType != null && !StringUtils.isBlank(correlationId) && date != null;
  }


  public UserActivityEvent(final Long id, final String eventType, final LocalDateTime date,
                           final String correlationId, final String clientIp, final String userAgent,
                           final String userAgentFiltered, final String details, final String reference) {
    this();
    setId(id);
    setEventType(eventType);
    setDate(date);
    setCorrelationId(correlationId);
    setClientIp(clientIp);
    setUserAgent(userAgent);
    setUserAgentFiltered(userAgentFiltered);
    setDetails(details);
    setReference(reference);
  }

  public UserActivityEvent() {}

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserActivityEvent)) {
      return false;
    }
    final UserActivityEvent event = (UserActivityEvent) o;
    return Objects.equals(getId(), event.getId()) &&
        getEventType() == event.getEventType() &&
        Objects.equals(getDate(), event.getDate()) &&
        Objects.equals(getCorrelationId(), event.getCorrelationId()) &&
        Objects.equals(getClientIp(), event.getClientIp()) &&
        Objects.equals(getUserAgent(), event.getUserAgent()) &&
        Objects.equals(getUserAgentFiltered(), event.getUserAgentFiltered()) &&
        Objects.equals(getDetails(), event.getDetails()) &&
        Objects.equals(getReference(), event.getReference());
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(getId(), getEventType(), getDate(), getCorrelationId(), getClientIp(),
            getUserAgent(), getUserAgentFiltered(), getDetails(), getReference());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("eventType", eventType)
        .append("date", date)
        .append("correlationId", correlationId)
        .append("clientIp", clientIp)
        .append("userAgent", userAgent)
        .append("userAgentFiltered", userAgentFiltered)
        .append("details", details)
        .append("reference", reference)
        .toString();
  }
}
