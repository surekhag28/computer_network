package edu.mqtt.au;

import java.util.Map;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
* Simple publisher client publishing calculated statistics to broker
*
* @author  Surekha Gaikwad(u6724013)
* @version 1.0
*/
public class Publisher implements  MqttCallback {
	
	static final String BROKER_URL = "tcp://comp3310.ddns.net";
	static final String USERNAME = "students";
	static final String PASSWORD = "33106331";
	static final String CLIENT_ID = "3310-u6724013";
	String topic = "studentreport/u6724013";
	
	private MqttClient mqttClient;
	private MqttConnectOptions connOpt;

	boolean publisher = false;
	
	/**
	   * This method is used to connect to broker
	   */
	public void connectBroker() {
		 connOpt = new MqttConnectOptions();
		 connOpt.setCleanSession(true);
		 connOpt.setAutomaticReconnect(true);
		 connOpt.setUserName(USERNAME);
		 connOpt.setPassword(PASSWORD.toCharArray());
		 
		 // Connect to Broker
		 try {
			 mqttClient = new MqttClient(BROKER_URL, CLIENT_ID, new MemoryPersistence());
			 mqttClient.setCallback(this);
			 mqttClient.connect(connOpt);
		}catch (MqttException e) {
			e.printStackTrace();
			System.exit(-1);	
		 }
		
		System.out.println("Connected to " + BROKER_URL);
		System.out.println("Publishing data to broker: ");
	}
	
	/**
	   * This method is used to publish messages to broker to specified topics
	   * @param publishData HashMap containing calculated statistics
	   */
 	public void publishMessage(Map<String,String> publishData) {
		
		connectBroker();
		publishData.put("/language","java");
		publishData.put("/network","ethernet");
		
		for (Map.Entry<String, String> entry : publishData.entrySet()) {
			MqttMessage message = new MqttMessage();
	        message.setPayload(entry.getValue().getBytes());
	        message.setRetained(true);
			message.setQos(2);
		
			try {
				mqttClient.publish(topic+entry.getKey(), message);
				System.out.println("Published message " + message+" to topic " + topic+entry.getKey());
				publisher = true;
			} catch (MqttPersistenceException e1) {
				e1.printStackTrace();
			} catch (MqttException e1) {
				e1.printStackTrace();
			}
		}
		
		// disconnect
		try {
			if (publisher) {
				mqttClient.disconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	 }

	@Override
	public void connectionLost(Throwable arg0) {
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		
	}
}
