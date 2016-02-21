package com.example.model.builder;

import java.time.LocalDateTime;

import com.example.model.EventByCorrelationId;

public class EventBuilder {

  private String eventType;
  private LocalDateTime date;
  private String correlationId;
  private String clientIp;
  private String userAgent;
  private String userAgentFiltered;
  private String details;
  private String reference;

  public EventBuilder withEventType(final String eventType) {
    this.eventType = eventType;
    return this;
  }

  public EventBuilder withDate(final LocalDateTime date) {
    this.date = date;
    return this;
  }

  public EventBuilder withCorrelationId(final String correlationId) {
    this.correlationId = correlationId;
    return this;
  }

  public EventBuilder withClientIp(final String clientIp) {
    this.clientIp = clientIp;
    return this;
  }

  public EventBuilder withUserAgent(final String userAgent) {
    this.userAgent = userAgent;
    return this;
  }

  public EventBuilder withUserAgentFiltered(final String userAgentFiltered) {
    this.userAgentFiltered = userAgentFiltered;
    return this;
  }

  public EventBuilder withDetails(final String details) {
    this.details = details;
    return this;
  }

  public EventBuilder withReference(final String reference) {
    this.reference = reference;
    return this;
  }

  public EventByCorrelationId createEvent() {
    return new EventByCorrelationId(eventType, date, correlationId, clientIp, userAgent,
        userAgentFiltered, details, reference);
  }
}