package com.mapoh.ppg.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapoh.ppg.dto.ContractScheduledRequest;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * @author mabohv
 * @date 2025/2/10 12:33
 */


public class ContractScheduledRequestDeserializer implements Deserializer<ContractScheduledRequest> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public ContractScheduledRequest deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, ContractScheduledRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing contract request", e);
        }
    }

    @Override
    public void close() {

    }
}