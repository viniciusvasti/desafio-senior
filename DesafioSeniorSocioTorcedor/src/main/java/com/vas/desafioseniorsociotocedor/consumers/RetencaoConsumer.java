package com.vas.desafioseniorsociotocedor.consumers;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TimeZone;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.vas.desafioseniorsociotocedor.models.RetryableMessage;
import com.vas.desafioseniorsociotocedor.utils.PartitionsMessagesQueue;

@Component
public class RetencaoConsumer implements AcknowledgingMessageListener<String, Object>, ConsumerSeekAware {

	private static final Logger LOG = LoggerFactory.getLogger(RetencaoConsumer.class);
	private static final String DADOS_KAFKA = "Kafka Message Acknowledged- Topico: {} - Offset: {} - IdAnalise: {} - MessageTime: {}";
	private PartitionsMessagesQueue partitionsMessagesQueue = new PartitionsMessagesQueue();

	@Value("${spring.kafka.retry.tempo.retencao}")
	private Integer tempoRetencaoMensagem;

	@Autowired
	public KafkaTemplate<String, Object> vendaProducer;

	private ConsumerSeekCallback consumerSeekCallback;

	@Override
	@KafkaListener(topics = "${spring.kafka.topics.retencao}", containerFactory = "kafkaListenerContainerFactory", id = "${spring.kafka.topics.retencao}", clientIdPrefix = "${spring.kafka.topics.retencao}")
	public void onMessage(ConsumerRecord<String, Object> data, Acknowledgment acknowledgment) {
		LOG.info("############################ RetencaoMensagemConsumer, {}", data.value());

		partitionsMessagesQueue.putMessage(data);

		LocalDateTime messageTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(data.timestamp()),
				TimeZone.getDefault().toZoneId());
		if (Duration.between(messageTime, LocalDateTime.now())
				.toMinutes() >= tempoRetencaoMensagem) {
			LOG.info(DADOS_KAFKA, data.topic(), data.offset(), data.key(), messageTime);
			RetryableMessage message = (RetryableMessage) data.value();
			vendaProducer.send(message.getTopico(), data.key(), data.value());
			acknowledgment.acknowledge();
			partitionsMessagesQueue.removeMessage(data);
		} else {
			Long nextOffset = partitionsMessagesQueue
					.getFirstOffsetMessageOnPartition(data.partition()).offset();
			consumerSeekCallback.seek(data.topic(), data.partition(), nextOffset);
		}
	}

	@Override
	public void registerSeekCallback(ConsumerSeekCallback callback) {
		this.consumerSeekCallback = callback;
	}

	@Override
	public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
		//METODO IMPLEMENTADO PELA INTERFACE - NAO E NECESSARIO PROCESSAMENTO
	}

	@Override
	public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
		//METODO IMPLEMENTADO PELA INTERFACE - NAO E NECESSARIO PROCESSAMENTO
	}
}
