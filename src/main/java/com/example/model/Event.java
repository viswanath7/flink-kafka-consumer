package com.example.model;


import java.io.Serializable;
import java.time.ZoneId;

import org.springframework.data.cassandra.mapping.Column;

public abstract class Event implements Serializable {

  public static final ZoneId UTC = ZoneId.of("UTC");

  @Column(value = "client_ip")
  protected String clientIp;

  @Column(value = "user_agent")
  protected String userAgent;

  @Column(value = "user_agent_filtered")
  protected String userAgentFiltered;

  @Column(value = "details")
  protected String details;

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

}
