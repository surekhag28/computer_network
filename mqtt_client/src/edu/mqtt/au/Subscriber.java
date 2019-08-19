package edu.mqtt.au;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


/**
* Simple subscriber client subscribing to topics and receiving messages from broker
*
* @author  Surekha Gaikwad(u6724013)
* @version 1.0
*/

public class Subscriber implements  MqttCallback{

	static final String BROKER_URL = "tcp://comp3310.ddns.net";
	static final String USERNAME = "students";
	static final String PASSWORD = "33106331";
	static final String CLIENT_ID = "3310-u6724013";
	
	private MqttClient mqttClient;
	private MqttConnectOptions connOpt;
	List<String> messages = new ArrayList<String>(); ;
	Map<String,String> publishData ;
	boolean subscriber = false;
	 
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
        System.out.println("Subscribing client to topic: ");
		
        GenerateReport report = new GenerateReport();
        subscribeMessage(mqttClient,1);
        
        
        
        subscribeMessage(mqttClient,2);
        //publishData = report.getMessages(messages,2);
        
        //System.out.println("=================================");
        
        
		// disconnect
		try {
			if (subscriber) {
				mqttClient.disconnect();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		publishData = report.getMessages(messages,1);
		Publisher publisher = new Publisher();
        publisher.publishMessage(publishData);
        
	 }

	/**
	   * This method is used to subscribe messages to topics
	   * @param mqttClient 
	   * @param publisherFlag 
	   */
	 public void subscribeMessage(MqttClient mqttClient,int publisherFlag) {
		 subscriber = false;
		 if(publisherFlag == 1) { // subscrie for slow counter
			 try {
					mqttClient.subscribe("counter/slow/q0", 0);
					mqttClient.subscribe("counter/slow/q1", 1);
					mqttClient.subscribe("counter/slow/q2", 2);
					//mqttClient.subscribe("$SYS/broker/clients/total",0);
					//mqttClient.subscribe("$SYS/broker/messages/received",0);
					//mqttClient.subscribe("$SYS/broker/heap/maximum size",0);
					subscriber = true;
				} catch (MqttException e1) {
					e1.printStackTrace();	
			}
			 try {
				 // wait to ensure subscribed messages are delivered
				 	Thread.sleep(300000); // 5 mins
					//Thread.sleep(1000); // 1 sec
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
			 
		 }else if (publisherFlag == 2) { // subscribe for fast counter
			 try {
					mqttClient.subscribe("counter/fast/q0", 0);
					mqttClient.subscribe("counter/fast/q1", 1);
					mqttClient.subscribe("counter/fast/q2", 2);
					
					subscriber = true;
					
				} catch (MqttException e1) {
					e1.printStackTrace();	
			}
			 try {
				 // wait to ensure subscribed messages are delivered
				 	Thread.sleep(300000); // 5 mins
					//Thread.sleep(1000); // 1 sec
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
		 }
	 }

	@Override
	public void connectionLost(Throwable rootCause) {
		System.out.println("Connection lost because: " + rootCause);
        System.exit(1);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		String time = new Timestamp(System.currentTimeMillis()).toString().substring(11);
		String recvMessage = topic + "-" + new String(message.getPayload()) + "-" + time ;
		System.out.println(recvMessage);
        messages.add(recvMessage);
    }

	public static void main(String[] args) {
		Subscriber client = new Subscriber();
		client.connectBroker();
	}
}
