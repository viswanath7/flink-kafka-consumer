package com.example.mapper;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public final class JSONDeserialiser {

  private static final Logger LOG = LoggerFactory.getLogger(JSONDeserialiser.class);

  private ObjectMapper mapper;

  public JSONDeserialiser() {
    LOG.info("Registering modules so that JSR-310 types are (de)serialized as numbers in Java 8");
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
  }

  public ObjectMapper getMapper() {
    return mapper;
  }

  public void setMapper(final ObjectMapper mapper) {
    this.mapper = mapper;
    mapper.registerModule(new JavaTimeModule());
  }

  /**
   * Given a JSON string and its class, returns an object representing the string.
   *
   * @param json
   * @param clazz
   * @param <T>
   * @return
   * @throws IOException
   */
  public <T> T convert(final String json, Class<T> clazz) throws IOException {
    LOG.debug("Converting json string: \n {} \n to an object", json);
    return  mapper.readValue(json, clazz);
  }

}
