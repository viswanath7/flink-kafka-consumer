package com.example.repository;


import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.EventByCorrelationId;

@Repository
public interface EventsByCorrelationIdRepository extends CassandraRepository<EventByCorrelationId> {

  String FIND_EVENTS_FOR_CORRELATION_IDENTIFIER = "SELECT * FROM events_by_correlationId WHERE correlationId = ?0";

  @Query(FIND_EVENTS_FOR_CORRELATION_IDENTIFIER)
  Iterable<EventByCorrelationId> findByCorrelationId(final String correlationId);

}
