package com.example.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.converter.MessagingToDomainObjectConverter;
import com.example.domain.UserActivityEvent;
import com.example.mapper.JSONDeserialiser;
import com.example.model.EventByCorrelationId;
import com.example.model.EventByReference;
import com.example.repository.EventsByCorrelationIdRepository;
import com.example.repository.EventsByReferenceRepository;

@Service
public class UserActivityEventsPersister {

  private static final Logger LOG = LoggerFactory.getLogger(UserActivityEventsPersister.class);

  @Autowired
  private JSONDeserialiser jsonDeserialiser;

  @Autowired
  private MessagingToDomainObjectConverter messagingToDomainObjectConverter;

  @Autowired
  private EventsByCorrelationIdRepository eventsByCorrelationIdRepository;

  @Autowired
  private EventsByReferenceRepository eventsByReferenceRepository;

  public JSONDeserialiser getJsonDeserialiser() {
    return jsonDeserialiser;
  }

  public void setJsonDeserialiser(final JSONDeserialiser jsonDeserialiser) {
    this.jsonDeserialiser = jsonDeserialiser;
  }

  public MessagingToDomainObjectConverter getMessagingToDomainObjectConverter() {
    return messagingToDomainObjectConverter;
  }

  public void setMessagingToDomainObjectConverter(final MessagingToDomainObjectConverter messagingToDomainObjectConverter) {
    this.messagingToDomainObjectConverter = messagingToDomainObjectConverter;
  }

  public EventsByCorrelationIdRepository getEventsByCorrelationIdRepository() {
    return eventsByCorrelationIdRepository;
  }

  public void setEventsByCorrelationIdRepository(final EventsByCorrelationIdRepository eventsByCorrelationIdRepository) {
    this.eventsByCorrelationIdRepository = eventsByCorrelationIdRepository;
  }

  public EventsByReferenceRepository getEventsByReferenceRepository() {
    return eventsByReferenceRepository;
  }

  public void setEventsByReferenceRepository(final EventsByReferenceRepository eventsByReferenceRepository) {
    this.eventsByReferenceRepository = eventsByReferenceRepository;
  }

  public void persist(final String json) {
    LOG.debug("Saving event: {}", json);
    if(StringUtils.isBlank(json)) return;
    try {
      final UserActivityEvent userActivityEvent = jsonDeserialiser.convert(json, UserActivityEvent.class);
      saveEventByCorrelationId(userActivityEvent);
      saveEventsByReference(userActivityEvent);
      LOG.debug("Successfully saved event: {} ", json);
    } catch (final IOException ex) {
      LOG.error("Error encountered while trying to save the event: {}", json, ex);
    }
  }

  //TODO: Make it asynchronous
  public void saveEventByCorrelationId(final UserActivityEvent userActivityEvent) {
    final EventByCorrelationId eventByCorrelationId = messagingToDomainObjectConverter.toEventByCorrelationId(userActivityEvent);
    eventsByCorrelationIdRepository.save(eventByCorrelationId);
  }

  //TODO: Make it asynchronous
  public void saveEventsByReference(final UserActivityEvent userActivityEvent) {
    final EventByReference eventByReference = messagingToDomainObjectConverter.toEventByReference(userActivityEvent);
    eventsByReferenceRepository.save(eventByReference);
  }

}
