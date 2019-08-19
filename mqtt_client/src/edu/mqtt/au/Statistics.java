package edu.mqtt.au;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
* Calculates all the statistics - loss_rate,dupe_rate,ooo_rate,gap,gvar
*
* @author  Surekha Gaikwad(u6724013)
* @version 1.0
*/
public class Statistics {

	Map<String,List<String>> counterMap = new HashMap<String,List<String>>();
	
	
	/**
	   * This method is used to calculate loss rate for each of the slow and fast counters for all qos levels
	   * @param list_qos List containing counter value
	   * @return float This returns calculated loss rate
	   */
	public float getLossRate(List<String> list_qos) {

		int loss_count = 0;
		float loss_rate = 0;
		// int close_count = Collections.frequency(list_qos, "close");
		int close_count = 0;

		String toRemove = "close.*";
		Pattern pattern = Pattern.compile(toRemove);
		for (int i = 0; i < list_qos.size(); i++) {
			Matcher matcher = pattern.matcher(list_qos.get(i));
			while (matcher.find()) {
				close_count++;
				list_qos.remove(i);
			}
		}

		Collections.sort(list_qos);
		List<String> uniqueList = list_qos.stream().distinct().collect(Collectors.toList());

		for (int i = 0; i < uniqueList.size(); i++) {
			if (i > 0) {
				int diff = new Integer(uniqueList.get(i)) - new Integer(uniqueList.get(i - 1));
				if (diff != 1) {
					loss_count++;
				}
			}
		}

		int total_messages = list_qos.size() + close_count;
		System.out.println("Total messages:- " + total_messages);
		System.out.println("Loss count " + loss_count);

		if (list_qos.size() != 0)
			loss_rate = (float) (((float) loss_count / total_messages) * 100);
		BigDecimal rate = new BigDecimal(loss_rate).setScale(2, RoundingMode.HALF_UP);
		return rate.floatValue();

	}

	/**
	   * This method is used to calculate duplicate rate for each of the slow and fast counters for all qos levels
	   * @param list_qos List containing counter value
	   * @return float This returns calculated duplicate rate
	   */
	public float getDuplicateRate(List<String> list_qos) {

		// do we need to consider close message or not?
		int close_count = 0, duplicate_count = 0;
		float duplicate_rate = 0;

		Set<String> uniques = new HashSet<>();

		for (String t : list_qos) {
			if (!uniques.add(t)) {
				duplicate_count++;
			}
		}

		int total_messages = list_qos.size() + close_count;

		if (list_qos.size() != 0)
			duplicate_rate = (float) (((float) duplicate_count / total_messages) * 100);
		BigDecimal rate = new BigDecimal(duplicate_rate).setScale(2, RoundingMode.HALF_UP);
		return rate.floatValue();
	}

	/**
	   * This method is used to calculate out of order rate for each of the slow and fast counters for all qos levels
	   * @param list_qos List containing counter value
	   * @return float This returns calculated out of order rate
	   */
	public float getOutOfOrderRate(List<String> list_qos) {

		int j, count = 0;
		int close_count = 0;
		float out_rate = 0;

		List<String> list = new ArrayList<String>(); // to add duplicate elements and to check for out of order messages.
		Set<String> uniques = new HashSet<String>();

		String toRemove = "close.*";
		Pattern pattern = Pattern.compile(toRemove);
		for (int i = 0; i < list_qos.size(); i++) {
			Matcher matcher = pattern.matcher(list_qos.get(i));
			while (matcher.find()) {
				list_qos.remove(i);
			}
		}

		for (int i = 0; i < list_qos.size(); i++) {
			j = i + 1;
			if (j == list_qos.size())
				break;
			int diff = new Integer(list_qos.get(j)) - new Integer(list_qos.get(i));
			if (diff < 0) {
				count++;
				list.add(list_qos.get(j));
			}
			if (count > 1) {
				for (int k = 0; k < list.size(); k++) {
					if (list_qos.get(j).equals(list.get(k)))
						continue;
				}
			}
		}
		uniques.addAll(list);
		int total_messages = list_qos.size() + close_count;

		if (list_qos.size() != 0)
			out_rate = (float) (((float) (uniques.size()) / total_messages) * 100);
		BigDecimal rate = new BigDecimal(out_rate).setScale(2, RoundingMode.HALF_UP);
		return rate.floatValue();
	}

	/**
	   * This method is used to calculate inter-message gap and gap variation for each of the slow and fast counters for all qos levels
	   * @param list_qos List containing counter value
	   * @return List<Double> This returns calculated inter-message gap and gap varaition
	   */
	public List<Double> getMessageGap(List<String> list_qos) { // did it correctly ignore the gap for ooo and duplicates
																// in between

		double sum = 0.0;
		double std = 0.0;
		List<Double> messageGap = new ArrayList<Double>();
		List<Double> variation = new ArrayList<Double>();

		String toRemove = "close.*";
		Pattern pattern = Pattern.compile(toRemove);
		for (int i = 0; i < list_qos.size(); i++) {
			Matcher matcher = pattern.matcher(list_qos.get(i));
			while (matcher.find()) {
				list_qos.remove(i);
			}
		}

		int j = 0;

		for (int i = 0; i < list_qos.size(); i++) {
			j = i + 1;

			if (j == list_qos.size())
				break;
			int diff = new Integer(list_qos.get(j).split("-")[0]) - new Integer(list_qos.get(i).split("-")[0]);
			if (diff == 1) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
				Date curDate = null, nextDate = null;
				try {
					curDate = dateFormat.parse(list_qos.get(i).split("-")[1]);
					nextDate = dateFormat.parse(list_qos.get(j).split("-")[1]);
				} catch (ParseException e) {
					System.out.println("Invalid date format:-  " + e.getMessage());
				}
				double gapTime = nextDate.getTime() - curDate.getTime();
				messageGap.add(gapTime);
			}
		}

		Iterator<Double> iterator = messageGap.iterator();
		while (iterator.hasNext()) {
			sum += iterator.next();
		}
		double mean = sum / messageGap.size();
		for (double num : messageGap) {
			std += Math.pow(num - mean, 2);
		}
		if (messageGap.isEmpty()) {
			variation.add(0.0);
			variation.add(0.0);
			return variation;
		} else {
			variation.add(new BigDecimal(mean).setScale(2, RoundingMode.HALF_UP).doubleValue());
			variation.add(new BigDecimal(std).setScale(2, RoundingMode.HALF_UP).doubleValue());
			return variation;
		}
	}
}
