package com.iot.app.kafka.util;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.app.kafka.vo.IoTData2;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

/**
 * Class to convert IoTData java object to JSON String
 * 
 * @author abaghel
 *
 */
public class IoTDataEncoder2 implements Encoder<IoTData2> {
	
	private static final Logger logger = Logger.getLogger(IoTDataEncoder2.class);	
	private static ObjectMapper objectMapper = new ObjectMapper();		
	public IoTDataEncoder2(VerifiableProperties verifiableProperties) {

    }
	public byte[] toBytes(IoTData2 iotEvent) {
		try {
			String msg = objectMapper.writeValueAsString(iotEvent);
			logger.info(msg);
			return msg.getBytes();
		} catch (JsonProcessingException e) {
			logger.error("Error in Serialization", e);
		}
		return null;
	}
}
