package com.example.model;


import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.example.model.primarykey.ReferenceDateTime;

@Table(value = "events_by_reference_and_year")
public class EventByReference extends Event {

  @PrimaryKey
  private ReferenceDateTime primaryKey;

  @Column(value = "correlation_id")
  private String correlationId;

  public ReferenceDateTime getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(final ReferenceDateTime primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(final String correlationId) {
    this.correlationId = correlationId;
  }

}
