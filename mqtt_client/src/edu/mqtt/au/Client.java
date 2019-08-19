package edu.mqtt.au;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Client implements MqttCallback {

	static final String BROKER_URL = "tcp://comp3310.ddns.net";
	static final String USERNAME = "students";
	static final String PASSWORD = "33106331";
	static final String CLIENT_ID = "3310-u6724013";
	static boolean subscriber = false;
	
	private MqttClient mqttClient;
	private static MqttConnectOptions connOpt;
	
	public void pubSub() {
        String subscribeTopicName = "/language";
        String publishTopicName = "/language";
        String payload;
        try {
        	connOpt = new MqttConnectOptions();
   		 	connOpt.setCleanSession(true);
   		 	connOpt.setAutomaticReconnect(true);
   		 	connOpt.setUserName(USERNAME);
   		 	connOpt.setPassword(PASSWORD.toCharArray());
           
   		 	mqttClient = new MqttClient(BROKER_URL, CLIENT_ID, new MemoryPersistence());
   		 	mqttClient.connect(connOpt);
		 
            mqttClient.subscribe(subscribeTopicName);
            System.out.println(mqttClient.getClientId() + " subscribed to topic: "+ subscribeTopicName);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    System.out.println("Connection lost to MQTT Broker");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("-------------------------------------------------");
                    System.out.println("| Received ");
                    System.out.println("| Topic: "+ topic);
                    System.out.println("| Message: "+ new String(message.getPayload()));
                    System.out.println("| QoS: "+ message.getQos());
                    System.out.println("-------------------------------------------------");

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println("Delivery Complete");
                }
            });
            MqttMessage message = new MqttMessage();
            for (int i = 1; i < 3; i++) {
                payload = "java";
                message.setPayload(payload
                        .getBytes());
                System.out.println("Set Payload: "+ payload);
                System.out.println(mqttClient.getClientId() + " published to topic: "+ publishTopicName);
                mqttClient.publish(publishTopicName, message);
            }
            
            mqttClient.disconnect();
        } catch (MqttException me) {
            System.out.println("reason: {}"+ me.getReasonCode());
            System.out.println("msg: {}"+ me.getMessage());
            System.out.println("loc: {} "+ me.getLocalizedMessage());
            System.out.println("cause: {}"+ me.getCause());
            System.out.println("excep: {}"+ me);
            me.printStackTrace();
        }
	}
    
	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
    	Client client = new Client();
    	client.pubSub();
    }
}