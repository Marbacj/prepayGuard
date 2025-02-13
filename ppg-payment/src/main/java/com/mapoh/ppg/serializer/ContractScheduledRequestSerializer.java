package com.mapoh.ppg.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapoh.ppg.dto.ContractScheduledRequest;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * @author mabohv
 * @date 2025/2/10 12:19
 */

public class ContractScheduledRequestSerializer implements Serializer<ContractScheduledRequest> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String topic, ContractScheduledRequest data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing contract request", e);
        }
    }

    @Override
    public void close() {

    }
}