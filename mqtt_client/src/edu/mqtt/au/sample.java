package edu.mqtt.au;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class sample {

	public static void main(String[] args) {

		List<String> list_qos = new ArrayList<String>();
		Set<String> uniques = new HashSet<>();
		
		// 1 2 4 3 5 3 6 3 7 8 9
		
		list_qos.add("1"); list_qos.add("2"); list_qos.add("4"); list_qos.add("3"); list_qos.add("5"); list_qos.add("3");
		list_qos.add("6");list_qos.add("3");list_qos.add("7");list_qos.add("8");list_qos.add("7");list_qos.add("9");
		list_qos.add("7");
		
		int j = 0 ;
		int count = 0;
		List<String> list = new ArrayList(); // to add duplicate elements and to check for out of order messages.
		
		for(int i=0 ; i<list_qos.size() ;i++) {
			j=i+1;
			if(j==list_qos.size())
				break;
			int diff = new Integer(list_qos.get(j)) - new Integer(list_qos.get(i));
			if(diff<0) {
				count++;
				list.add(list_qos.get(j));
			}
			if(count>1) {
				for(int k=0;k<list.size();k++) {
					if(list_qos.get(j).equals(list.get(k)))
						continue;
				}
			}
		}
	    uniques.addAll(list);
	    System.out.println(uniques);
		
	}
}