package com.vas.desafioseniorsociotocedor.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConfig {
	@Value(value = "${spring.kafka.url}")
	private String kafkaUrl;
	@Value(value = "${spring.kafka.port}")
	private String kafkaPort;
	@Value(value = "${spring.kafka.groupid.campanha}")
	private String groupId;
	@Value("${spring.kafka.session.timeout.ms}")
	private Integer sessionTimeoutTime;
	@Value("${spring.kafka.max.poll.interval.ms}")
	private Integer maxPollIntervalTime;

	@Autowired
	private RetryConfig retryConfig;

	private Map<String, Object> defaultProperties() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl + ":" + kafkaPort);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutTime);
		config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalTime);
		config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
		config.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
				RoundRobinAssignor.class.getName());
		config.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
		config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return config;
	}

	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = this.defaultProperties();
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		return props;
	}

	@Bean
	public Map<String, Object> manualCommitConsumerConfigs() {
		Map<String, Object> props = this.defaultProperties();
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

		return props;
	}

	@Bean
	public ConsumerFactory<String, Object> consumerFactoryDefault() {
		return new DefaultKafkaConsumerFactory<>(manualCommitConsumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
		factory.setConsumerFactory(consumerFactoryDefault());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, String> campanhasConsumerFactory() {
		return new DefaultKafkaConsumerFactory<>(
				consumerConfigs(), new StringDeserializer(),
				new JsonDeserializer<>(String.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> campanhasContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(campanhasConsumerFactory());
		factory.setConcurrency(10);
		factory.getContainerProperties().setAckOnError(false);
		factory.getContainerProperties().setAckMode(AckMode.RECORD);
		factory.getConsumerFactory().getConfigurationProperties();
		factory.setRetryTemplate(retryConfig.retryTemplate());
		factory.setRecoveryCallback(context -> retryConfig.recoveryCallback(context));
		return factory;
	}
}
