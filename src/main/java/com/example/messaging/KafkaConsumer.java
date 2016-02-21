package com.example.messaging;

import java.util.Properties;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer082;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

/**
 *  Kafka consumer:  It expects the following parameters to be set
 *  - "bootstrap.servers" (comma separated list of kafka brokers)
 *  - "zookeeper.connect" (comma separated list of zookeeper servers)
 *  - "group.id" the id of the consumer group
 *  - "topic" the name of the topic to read data from.
 *
 * One can pass these required parameters using
 * "--bootstrap.servers host:port,host1:port1 --zookeeper.connect host:port --topic testTopic"
 *
 * This is a valid input example:
 * 		--topic test --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --group.id myGroup
 */
public class KafkaConsumer {

	private static final StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();


  //private static JSONDeserialiser jsonMapper = new JSONDeserialiser();

  public static void main(String[] args) throws Exception {

		// parse user parameters
		ParameterTool parameterTool = ParameterTool.fromArgs(args);

		DataStream<String> constructedMessageStream = executionEnvironment.addSource(getDataSource(parameterTool));

		constructedMessageStream.rebalance().addSink(new CassandraDataSink());

    //.timeWindowAll(Time.of(1, TimeUnit.SECONDS))




    // repartition data so that all machines see the messages
				// for example when number of kafka partitions < number of flink operators
				//.map(value -> jsonMapper.convert(value, UserActivityEvent.class))
				// map function that transforms input elements of type string and returns an output element of type string
				//.print();
		// write the contents of the stream to the TaskManager's standard output stream


		executionEnvironment.execute();
		// Trigger the execution of the program
	}

	/**
	 * Returns a kafka consumer to read from Kafka 0.8.2.x brokers
	 *
	 * @param parameterTool
	 * @return
	 */
	private static FlinkKafkaConsumer082<String> getDataSource(final ParameterTool parameterTool) {
		return new FlinkKafkaConsumer082<>(getTopicName(parameterTool), getKafkaMessageDeserialiser(),
				getKafkaConsumerProperties(parameterTool));
	}

	private static Properties getKafkaConsumerProperties(final ParameterTool parameterTool) {
		return parameterTool.getProperties();
	}

	/**
	 * Gets deserialiser that's used to convert between Kafka's byte messages and Flink's objects
	 *
	 * @return
	 */
	private static SimpleStringSchema getKafkaMessageDeserialiser() {
		return new SimpleStringSchema();
	}

	/**
	 * Returns the name of a topic from which the subscriber should consume messages
	 *
	 * @param parameterTool
	 * @return
	 */
	private static String getTopicName(final ParameterTool parameterTool) {
		return parameterTool.getRequired("topic");
	}


}
