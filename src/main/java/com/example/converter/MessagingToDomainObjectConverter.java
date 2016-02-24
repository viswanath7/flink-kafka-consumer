package com.example.converter;

import org.springframework.stereotype.Service;

import com.example.domain.UserActivityEvent;
import com.example.model.EventByCorrelationId;
import com.example.model.EventByReference;
import com.example.model.EventByType;
import com.example.model.primarykey.CorrelationIdDateTime;
import com.example.model.primarykey.EventTypeDateTime;
import com.example.model.primarykey.ReferenceDateTime;

@Service
public class MessagingToDomainObjectConverter {

  public EventByCorrelationId toEventByCorrelationId(final UserActivityEvent message) {
    EventByCorrelationId eventByCorrelationId = new EventByCorrelationId();
    eventByCorrelationId.setPrimaryKey(new CorrelationIdDateTime(message.getCorrelationId(), message.getDate()));
    eventByCorrelationId.setReference(message.getReference());
    eventByCorrelationId.setClientIp(message.getClientIp());
    eventByCorrelationId.setDetails(message.getDetails());
    eventByCorrelationId.setEventType(message.getEventType());
    eventByCorrelationId.setUserAgent(message.getUserAgent());
    eventByCorrelationId.setUserAgentFiltered(message.getUserAgentFiltered());
    return eventByCorrelationId;
  }

  public EventByReference toEventByReference(final UserActivityEvent message) {
    EventByReference eventByReference = new EventByReference();
    eventByReference.setPrimaryKey(new ReferenceDateTime(message.getReference(), message.getDate()));
    eventByReference.setCorrelationId(message.getCorrelationId());
    eventByReference.setClientIp(message.getClientIp());
    eventByReference.setDetails(message.getDetails());
    eventByReference.setEventType(message.getEventType());
    eventByReference.setUserAgent(message.getUserAgent());
    eventByReference.setUserAgentFiltered(message.getUserAgentFiltered());
    return eventByReference;
  }

  public EventByType toEventByType(final UserActivityEvent message) {
    EventByType eventByType = new EventByType();
    eventByType.setPrimaryKey(new EventTypeDateTime(message.getEventType(), message.getDate()));
    eventByType.setCorrelationId(message.getCorrelationId());
    eventByType.setClientIp(message.getClientIp());
    eventByType.setDetails(message.getDetails());
    eventByType.setReference(message.getReference());
    eventByType.setUserAgent(message.getUserAgent());
    eventByType.setUserAgentFiltered(message.getUserAgentFiltered());
    return eventByType;
  }

}
