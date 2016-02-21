package com.example.repository;


import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.EventByReference;

@Repository
public interface EventsByReferenceRepository extends CassandraRepository<EventByReference> {

  String FIND_EVENTS_FOR_REFERENCE_AND_YEAR = "SELECT * FROM events_by_reference_and_year WHERE reference = ?0 AND year = ?1";

  @Query(FIND_EVENTS_FOR_REFERENCE_AND_YEAR)
  Iterable<EventByReference> findByReferenceAndYear(final String reference, final Integer year);

}
