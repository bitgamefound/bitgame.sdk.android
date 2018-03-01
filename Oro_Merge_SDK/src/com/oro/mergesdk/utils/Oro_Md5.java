package com.oro.mergesdk.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Oro_Md5 {

	public final static String getMD5SignData(Map<String, String> map, String value) {
		String TAG = "";
		map.put("key", value);
		StringBuilder signDate = new StringBuilder();
		Set<String> keySet = map.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			signDate.append(key + "=" + map.get(key) + "&");
		}
		signDate.deleteCharAt(signDate.length() - 1);
		String sign = signDate.toString().toLowerCase();

		Oro_ShowLog showlog = new Oro_ShowLog();
		showlog.ShowLog(Oro_ShowLog.Verbose,TAG,"getMD5SignData|"+sign);
		
		sign = Oro_MD5Helper.getMD5(sign).trim();
		showlog.ShowLog(Oro_ShowLog.Verbose,TAG,"getMD5SignData|"+sign);
		map.remove("key");
		return sign;
	}
}
