package com.vas.desafioseniorsociotocedor.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class PartitionsMessagesQueue {

	// Tabela Hash contendo partições, sendo cada valor, um fila de mensagens
	private Map<Integer, SortedMap<Long, ConsumerRecord<String, Object>>> partitionMessagesQueues = new HashMap<>();

	public void putMessage(ConsumerRecord<String, Object> message) {
		// se ainda não foi criada uma fila para a partição, criamos uma
		SortedMap<Long, ConsumerRecord<String, Object>> partitionMessagesQueue = partitionMessagesQueues
				.get(message.partition());
		if (partitionMessagesQueue == null) {
			partitionMessagesQueues.put(message.partition(), new TreeMap<>());
		}
		// adicionamos a mensagem a fila de mensagens da partição, utilizando offset
		// como chave
		partitionMessagesQueues.get(message.partition()).put(message.offset(), message);
	}

	public void removeMessage(ConsumerRecord<String, Object> message) {
		partitionMessagesQueues.get(message.partition()).remove(message.offset());
	}

	public ConsumerRecord<String, Object> getFirstOffsetMessageOnPartition(Integer partition) {
		return partitionMessagesQueues.get(partition)
				.get(partitionMessagesQueues.get(partition).firstKey());
	}

}
