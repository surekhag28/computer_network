package edu.mqtt.au;
/*
 * package mqtt_client;
 * 
 * import java.sql.Timestamp; import java.util.ArrayList; import java.util.List;
 * import java.util.Map; import
 * org.eclipse.paho.client.mqttv3.IMqttDeliveryToken; import
 * org.eclipse.paho.client.mqttv3.MqttCallback; import
 * org.eclipse.paho.client.mqttv3.MqttClient; import
 * org.eclipse.paho.client.mqttv3.MqttConnectOptions; import
 * org.eclipse.paho.client.mqttv3.MqttException; import
 * org.eclipse.paho.client.mqttv3.MqttMessage; import
 * org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
 * 
 * public class temp implements MqttCallback{
 * 
 * 
 * 
 * static final String BROKER_URL = "tcp://comp3310.ddns.net"; static final
 * String USERNAME = "students"; static final String PASSWORD = "33106331";
 * static final String CLIENT_ID = "3310-u6724013";
 * 
 * private MqttClient mqttClient; private MqttConnectOptions connOpt;
 * List<String> messages = new ArrayList<String>(); ; Map<String,String>
 * publishData ; boolean publishFlag = false;
 * 
 * public void connectBroker() {
 * 
 * connOpt = new MqttConnectOptions(); connOpt.setCleanSession(true);
 * connOpt.setAutomaticReconnect(true); connOpt.setUserName(USERNAME);
 * connOpt.setPassword(PASSWORD.toCharArray());
 * 
 * // Connect to Broker try { mqttClient = new MqttClient(BROKER_URL, CLIENT_ID,
 * new MemoryPersistence()); mqttClient.setCallback(this);
 * mqttClient.connect(connOpt); }catch (MqttException e) { e.printStackTrace();
 * System.exit(-1); }
 * 
 * System.out.println("Connected to " + BROKER_URL);
 * System.out.println("Subscribing client to topic: ");
 * 
 * 
 * DisplayReport report = new DisplayReport();
 * 
 * subscribeMessage(mqttClient,1); publishData = report.getMessages(messages,1);
 * 
 * //subscribeMessage(mqttClient,2); //publishData =
 * report.getMessages(messages,2);
 * 
 * System.out.println("=================================");
 * 
 * Publisher publisher = new Publisher(); publishFlag =
 * publisher.publishMessage(mqttClient, publishData);
 * 
 * // disconnect try { if (publishFlag) { mqttClient.disconnect(); } }catch
 * (Exception e) { e.printStackTrace(); } }
 * 
 * public void subscribeMessage(MqttClient mqttClient,int publisherFlag) {
 * if(publisherFlag == 1) { try { mqttClient.subscribe("counter/slow/q0", 0);
 * mqttClient.subscribe("counter/slow/q1", 1);
 * mqttClient.subscribe("counter/slow/q2", 2);
 * //mqttClient.subscribe("$SYS/broker/clients/total",0);
 * //mqttClient.subscribe("$SYS/broker/messages/received",0);
 * //mqttClient.subscribe("$SYS/broker/heap/maximum size",0);
 * 
 * } catch (MqttException e1) { e1.printStackTrace(); } try { // wait to ensure
 * subscribed messages are delivered //Thread.sleep(300000); // 5 mins
 * Thread.sleep(1000); // 1 sec }catch(Exception e) { e.printStackTrace(); }
 * 
 * }else if (publisherFlag == 2) { try { mqttClient.subscribe("counter/fast/q0",
 * 0); mqttClient.subscribe("counter/fast/q1", 1);
 * mqttClient.subscribe("counter/fast/q2", 2);
 * 
 * } catch (MqttException e1) { e1.printStackTrace(); } try { // wait to ensure
 * subscribed messages are delivered //Thread.sleep(300000); // 5 mins
 * Thread.sleep(1000); // 1 sec }catch(Exception e) { e.printStackTrace(); } } }
 * 
 * @Override public void connectionLost(Throwable rootCause) {
 * System.out.println("Connection lost because: " + rootCause); System.exit(1);
 * }
 * 
 * @Override public void deliveryComplete(IMqttDeliveryToken arg0) {
 * 
 * }
 * 
 * public void messageArrived(String topic, MqttMessage message) throws
 * Exception {
 * 
 * String time = new Timestamp(System.currentTimeMillis()).toString();
 * System.out.println("\nReceived a Message!" + "\n\tTime:    " + time +
 * "\n\tTopic:   " + topic + "\n\tMessage: " + new String(message.getPayload())
 * + "\n\tQoS:     " + message.getQos() + "\n");
 * 
 * 
 * String time = new
 * Timestamp(System.currentTimeMillis()).toString().substring(11); String
 * recvMessage = topic + "-" + new String(message.getPayload()) + "-" + time ;
 * System.out.println(recvMessage); messages.add(recvMessage); }
 * 
 * public static void main(String[] args) { Subscriber client = new
 * Subscriber(); client.connectBroker(); } }
 */