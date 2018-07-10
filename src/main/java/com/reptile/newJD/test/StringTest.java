package com.reptile.newJD.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringTest {
	public static void main(String[] args) {
		
		String body = "[{\"level\":3,\"categoryId\":\"20680\"},{\"level\":3,\"categoryId\":\"20698\"},{\"level\":3,\"categoryId\":\"20683\"},{\"level\":3,\"categoryId\":\"20686\"},{\"level\":3,\"categoryId\":\"20688\"},{\"level\":3,\"categoryId\":\"20691\"},{\"level\":3,\"categoryId\":\"20693\"},{\"level\":3,\"categoryId\":\"20695\"}]|[{\"level\":3,\"categoryId\":\"20655\"},{\"level\":3,\"categoryId\":\"20652\"},{\"level\":3,\"categoryId\":\"20665\"},{\"level\":3,\"categoryId\":\"20649\"},{\"level\":3,\"categoryId\":\"20663\"},{\"level\":3,\"categoryId\":\"20646\"},{\"level\":3,\"categoryId\":\"20660\"},{\"level\":3,\"categoryId\":\"20645\"},{\"level\":3,\"categoryId\":\"20642\"},{\"level\":3,\"categoryId\":\"20658\"},{\"level\":3,\"categoryId\":\"20643\"},{\"level\":3,\"categoryId\":\"20641\"}]|[{\"level\":3,\"categoryId\":\"20744\"},{\"level\":3,\"categoryId\":\"20739\"},{\"level\":3,\"categoryId\":\"20741\"},{\"level\":3,\"categoryId\":\"20731\"},{\"level\":3,\"categoryId\":\"20736\"},{\"level\":3,\"categoryId\":\"20743\"},{\"level\":3,\"categoryId\":\"20761\"},{\"level\":3,\"categoryId\":\"20746\"},{\"level\":3,\"categoryId\":\"20749\"},{\"level\":3,\"categoryId\":\"20748\"},{\"level\":3,\"categoryId\":\"20733\"},{\"level\":3,\"categoryId\":\"20737\"},{\"level\":3,\"categoryId\":\"20754\"}]|[{\"level\":3,\"categoryId\":\"20594\"},{\"level\":3,\"categoryId\":\"20579\"},{\"level\":3,\"categoryId\":\"20576\"},{\"level\":3,\"categoryId\":\"20583\"},{\"level\":3,\"categoryId\":\"20581\"},{\"level\":3,\"categoryId\":\"20597\"},{\"level\":3,\"categoryId\":\"20587\"},{\"level\":3,\"categoryId\":\"20568\"},{\"level\":3,\"categoryId\":\"20603\"},{\"level\":3,\"categoryId\":\"20600\"},{\"level\":3,\"categoryId\":\"20571\"},{\"level\":3,\"categoryId\":\"20591\"},{\"level\":3,\"categoryId\":\"20573\"},{\"level\":3,\"categoryId\":\"20575\"}]|[{\"level\":3,\"categoryId\":\"20712\"},{\"level\":3,\"categoryId\":\"20713\"},{\"level\":3,\"categoryId\":\"20707\"},{\"level\":3,\"categoryId\":\"20705\"},{\"level\":3,\"categoryId\":\"20710\"}]|[{\"level\":3,\"categoryId\":\"22820\"},{\"level\":3,\"categoryId\":\"20617\"},{\"level\":3,\"categoryId\":\"22765\"},{\"level\":3,\"categoryId\":\"20619\"},{\"level\":3,\"categoryId\":\"20614\"},{\"level\":3,\"categoryId\":\"20611\"}]|[{\"level\":3,\"categoryId\":\"20908\"},{\"level\":3,\"categoryId\":\"20924\"},{\"level\":3,\"categoryId\":\"20910\"},{\"level\":3,\"categoryId\":\"20926\"},{\"level\":3,\"categoryId\":\"20921\"},{\"level\":3,\"categoryId\":\"20920\"},{\"level\":3,\"categoryId\":\"20923\"},{\"level\":3,\"categoryId\":\"20906\"},{\"level\":3,\"categoryId\":\"20916\"},{\"level\":3,\"categoryId\":\"20918\"},{\"level\":3,\"categoryId\":\"20913\"},{\"level\":3,\"categoryId\":\"20914\"}]|[{\"level\":3,\"categoryId\":\"20775\"},{\"level\":3,\"categoryId\":\"20768\"},{\"level\":3,\"categoryId\":\"20781\"},{\"level\":3,\"categoryId\":\"20780\"},{\"level\":3,\"categoryId\":\"20783\"}]|[{\"level\":3,\"categoryId\":\"20632\"},{\"level\":3,\"categoryId\":\"20635\"},{\"level\":3,\"categoryId\":\"20634\"},{\"level\":3,\"categoryId\":\"20631\"}]|[{\"level\":3,\"categoryId\":\"20400\"},{\"level\":3,\"categoryId\":\"20809\"}]|[{\"level\":3,\"categoryId\":\"20789\"},{\"level\":3,\"categoryId\":\"20787\"},{\"level\":3,\"categoryId\":\"20796\"},{\"level\":3,\"categoryId\":\"20790\"},{\"level\":3,\"categoryId\":\"20531\"},{\"level\":3,\"categoryId\":\"20532\"},{\"level\":3,\"categoryId\":\"20530\"},{\"level\":3,\"categoryId\":\"20528\"},{\"level\":3,\"categoryId\":\"20537\"},{\"level\":3,\"categoryId\":\"20534\"},{\"level\":3,\"categoryId\":\"20543\"},{\"level\":3,\"categoryId\":\"23021\"},{\"level\":3,\"categoryId\":\"20524\"},{\"level\":3,\"categoryId\":\"20525\"},{\"level\":3,\"categoryId\":\"20522\"},{\"level\":3,\"categoryId\":\"20540\"},{\"level\":3,\"categoryId\":\"20527\"},{\"level\":3,\"categoryId\":\"20520\"}]|[{\"level\":3,\"categoryId\":\"20938\"},{\"level\":3,\"categoryId\":\"20939\"},{\"level\":3,\"categoryId\":\"20937\"},{\"level\":3,\"categoryId\":\"20943\"},{\"level\":3,\"categoryId\":\"20941\"},{\"level\":3,\"categoryId\":\"20378\"},{\"level\":3,\"categoryId\":\"20379\"}]|[{\"level\":3,\"categoryId\":\"23274\"}]|[{\"level\":3,\"categoryId\":\"23271\"}]|[{\"level\":3,\"categoryId\":\"23272\"}]|[{\"level\":3,\"categoryId\":\"23270\"}]|[{\"level\":3,\"categoryId\":\"23269\"}]|[{\"level\":3,\"categoryId\":\"23275\"},{\"level\":3,\"categoryId\":\"23273\"}]|";
		String[] split = body.split("\\|");
		List<String> list = new ArrayList<String>();
		for (String string : split) {
			list.add(string);
		}
		
		List<String> list2 =  shuffleToEight(list);
		
		for (String string : list2) {
			System.out.println(string);
		}
	}
	
	/**
	 * 返回前8个打乱顺序的catids
	 * @return list
	 */
	public static List<String> shuffleToEight(List<String> list){
		Collections.shuffle(list);
		List<String> list2 = new ArrayList<String>();
		
		int limit = list.size();
		if(limit > 8){
			limit = 8;
		}
		for (int i = 0; i < limit; i++) {
			list2.add(list.get(i));
		}
		return list2;
	}
}
