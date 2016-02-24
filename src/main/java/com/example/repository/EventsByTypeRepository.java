package com.example.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.EventByType;

@Repository
public interface EventsByTypeRepository extends CassandraRepository<EventByType> {

  String FIND_EVENTS_BY_TYPE = "SELECT * FROM events_by_type WHERE event_type = ?0";

  @Query(FIND_EVENTS_BY_TYPE)
  Iterable<EventByType> findByType(final String eventType);

}
