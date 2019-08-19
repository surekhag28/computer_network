package edu.mqtt.au;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
* Generates report in order to publish to broker
*
* @author  Surekha Gaikwad(u6724013)
* @version 1.0
*/
public class GenerateReport {
	
	Statistics report = new Statistics();
	
	Map<String,List<String>> counterMap = new HashMap<String,List<String>>();
	Map<String,String> publishReport = new HashMap<String,String>();
	
	/**
	   * This method is used to publish messages to broker to specified topics
	   * @param messageList List containing messages in the format <topic>-<counter_value>-<time>
	   * @return List<String> List containing only counter values
	   */
	public List<String> getCounterList(List<String> messageList) {
		
		List<String> counterList = new ArrayList<String>();
		Iterator<String> iterator = messageList.iterator();
	    while(iterator.hasNext()) {
	    	String[] counter = iterator.next().split("-");
	    	counterList.add(counter[0]);
	      }
		return counterList;
	}
	
	/**
	   * This method is used to get list of counter values for given topic name
	   * @param topic topic name 
	   * @return List<String> List containing counter values for all topics
	   */
	public List<String> getPayloadContent(String topic, List<String> messages) {
			
			List<String> payloadList = new ArrayList<String>();
			
			Iterator<String> iterator = messages.iterator();
		    while (iterator.hasNext()) {
		        String[] message = iterator.next().split("-");
		        if(message[0].equals(topic)) {
		        	payloadList.add(message[1]+"-"+message[2]);
		        }
		    }
			return payloadList;
		}
	
	/**
	   * This method is used to get list of counter values for given topic name
	   * @param messages List containing messages in the format <topic>-<counter_value>-<time>
	   * @param publisherFlag flag specifying whether slow or fast counter
	   * @return Map<String, String> Map containing calculated rates against each of the topic
	   */
		public Map<String, String> getMessages(List<String> messages,int publisherFlag) {
			
			System.out.println(messages);
			if(publisherFlag == 1) {
				counterMap.put("counter/slow/q0",getPayloadContent("counter/slow/q0",messages));
				counterMap.put("counter/slow/q1",getPayloadContent("counter/slow/q1",messages));
				counterMap.put("counter/slow/q2",getPayloadContent("counter/slow/q2",messages));
			} else if (publisherFlag == 2) {
				counterMap.put("counter/fast/q0",getPayloadContent("counter/fast/q0",messages));
				counterMap.put("counter/fast/q1",getPayloadContent("counter/fast/q1",messages));
				counterMap.put("counter/fast/q2",getPayloadContent("counter/fast/q2",messages));
			}
			
			generateMessageLoss(counterMap,publisherFlag);
			generateDuplicateMessage(counterMap,publisherFlag);
			generateOutOfOrderMessage(counterMap,publisherFlag);
			generateMessageGap(counterMap,publisherFlag);
		
			return publishReport;
	}
	
		/**
		   * This method is used to update hashmap with calculated loss rate for both slow and fast counter depending on flag
		   * @param counterMap List containing messages in the format <topic>-<counter_value>-<time>
		   * @param publisherFlag flag specifying whether slow or fast counter
		   */	
		public void generateMessageLoss(Map<String,List<String>> counterMap, int publisherFlag) {
			if(publisherFlag == 1) {
				publishReport.put("/slow/q0/loss",String.valueOf(report.getLossRate(getCounterList(counterMap.get("counter/slow/q0")))));
				publishReport.put("/slow/q1/loss",String.valueOf(report.getLossRate(getCounterList(counterMap.get("counter/slow/q1")))));
				publishReport.put("/slow/q2/loss",String.valueOf(report.getLossRate(getCounterList(counterMap.get("counter/slow/q2")))));
			
			}else if (publisherFlag == 2) {
				publishReport.put("/fast/q0/loss",String.valueOf(report.getLossRate(getCounterList(counterMap.get("counter/fast/q0")))));
				publishReport.put("/fast/q1/loss",String.valueOf(report.getLossRate(getCounterList(counterMap.get("counter/fast/q1")))));
				publishReport.put("/fast/q2/loss",String.valueOf(report.getLossRate(getCounterList(counterMap.get("counter/fast/q2")))));
			}
		}
		
		/**
		   * This method is used to update hashmap with calculated duplicate rate for both slow and fast counter depending on flag
		   * @param counterMap List containing messages in the format <topic>-<counter_value>-<time>
		   * @param publisherFlag flag specifying whether slow or fast counter
		   */	
		public void generateDuplicateMessage(Map<String,List<String>> counterMap, int publisherFlag) {
			if(publisherFlag == 1) {
				publishReport.put("/slow/q0/dupe",String.valueOf(report.getDuplicateRate(getCounterList(counterMap.get("counter/slow/q0")))));
				publishReport.put("/slow/q1/dupe",String.valueOf(report.getDuplicateRate(getCounterList(counterMap.get("counter/slow/q1")))));
				publishReport.put("/slow/q2/dupe",String.valueOf(report.getDuplicateRate(getCounterList(counterMap.get("counter/slow/q2")))));
				
			}else if (publisherFlag == 2) {
				publishReport.put("/fast/q0/dupe",String.valueOf(report.getDuplicateRate(getCounterList(counterMap.get("counter/fast/q0")))));
				publishReport.put("/fast/q1/dupe",String.valueOf(report.getDuplicateRate(getCounterList(counterMap.get("counter/fast/q1")))));
				publishReport.put("/fast/q2/dupe",String.valueOf(report.getDuplicateRate(getCounterList(counterMap.get("counter/fast/q2")))));
			}
		}
		
		/**
		   * This method is used to update hashmap with calculated out of order rate for both slow and fast counter depending on flag
		   * @param counterMap List containing messages in the format <topic>-<counter_value>-<time>
		   * @param publisherFlag flag specifying whether slow or fast counter
		   */	
		public void generateOutOfOrderMessage(Map<String,List<String>> counterMap, int publisherFlag) {
			if(publisherFlag == 1) {
				publishReport.put("/slow/q0/ooo",String.valueOf(report.getOutOfOrderRate(getCounterList(counterMap.get("counter/slow/q0")))));
				publishReport.put("/slow/q1/ooo",String.valueOf(report.getOutOfOrderRate(getCounterList(counterMap.get("counter/slow/q1")))));
				publishReport.put("/slow/q2/ooo",String.valueOf(report.getOutOfOrderRate(getCounterList(counterMap.get("counter/slow/q2")))));
			}else if (publisherFlag == 2) {
				publishReport.put("/fast/q0/ooo",String.valueOf(report.getOutOfOrderRate(getCounterList(counterMap.get("counter/fast/q0")))));
				publishReport.put("/fast/q1/ooo",String.valueOf(report.getOutOfOrderRate(getCounterList(counterMap.get("counter/fast/q1")))));
				publishReport.put("/fast/q2/ooo",String.valueOf(report.getOutOfOrderRate(getCounterList(counterMap.get("counter/fast/q2")))));
			}
		}
		
		/**
		   * This method is used to update hashmap with calculated inter-message gap and gap variattion 
		   * for both slow and fast counter depending on flag
		   * @param counterMap List containing messages in the format <topic>-<counter_value>-<time>
		   * @param publisherFlag flag specifying whether slow or fast counter
		   */	
		public void generateMessageGap(Map<String,List<String>> counterMap, int publisherFlag) {
			if(publisherFlag == 1) {
				publishReport.put("/slow/q0/gap",String.valueOf(report.getMessageGap(counterMap.get("counter/slow/q0")).get(0)));
				publishReport.put("/slow/q1/gap",String.valueOf(report.getMessageGap(counterMap.get("counter/slow/q1")).get(0)));
				publishReport.put("/slow/q2/gap",String.valueOf(report.getMessageGap(counterMap.get("counter/slow/q2")).get(0)));
				
				publishReport.put("/slow/q0/gvar",String.valueOf(report.getMessageGap(counterMap.get("counter/slow/q0")).get(1)));
				publishReport.put("/slow/q1/gvar",String.valueOf(report.getMessageGap(counterMap.get("counter/slow/q1")).get(1)));
				publishReport.put("/slow/q2/gvar",String.valueOf(report.getMessageGap(counterMap.get("counter/slow/q2")).get(1)));
			}else if (publisherFlag == 2) {
				publishReport.put("/fast/q0/gap",String.valueOf(report.getMessageGap(counterMap.get("counter/fast/q0")).get(0)));
				publishReport.put("/fast/q1/gap",String.valueOf(report.getMessageGap(counterMap.get("counter/fast/q1")).get(0)));
				publishReport.put("/fast/q2/gap",String.valueOf(report.getMessageGap(counterMap.get("counter/fast/q2")).get(0)));
				
				publishReport.put("/fast/q0/gvar",String.valueOf(report.getMessageGap(counterMap.get("counter/fast/q0")).get(1)));
				publishReport.put("/fast/q1/gvar",String.valueOf(report.getMessageGap(counterMap.get("counter/fast/q1")).get(1)));
				publishReport.put("/fast/q2/gvar",String.valueOf(report.getMessageGap(counterMap.get("counter/fast/q2")).get(1)));
			}
		}
}
