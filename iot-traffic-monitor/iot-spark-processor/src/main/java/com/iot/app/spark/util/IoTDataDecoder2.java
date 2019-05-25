package com.iot.app.spark.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.app.spark.vo.IoTData2;

import kafka.serializer.Decoder;
import kafka.utils.VerifiableProperties;

/**
 * Class to deserialize JSON string to IoTData java object
 * 
 * @author abaghel
 *
 */
public class IoTDataDecoder2 implements Decoder<IoTData2> {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public IoTDataDecoder2(VerifiableProperties verifiableProperties) {

    }
	public IoTData2 fromBytes(byte[] bytes) {
		try {
			return objectMapper.readValue(bytes, IoTData2.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
