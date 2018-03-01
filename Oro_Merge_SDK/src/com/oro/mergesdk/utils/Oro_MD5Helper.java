package com.oro.mergesdk.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.loopj.android.http.RequestParams;

public class Oro_MD5Helper {
	static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
		'9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * ����ַ��MD5��
	 * 
	 * @param s
	 * @return
	 */
	public final static String getMD5(String s) {
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
	
			md.update(strTemp);
			return digest(md);
		} catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
			return null;
		}
	}
	
	/**
	 * ����ժҪ
	 * 
	 * @param mDigest
	 * @return
	 */
	private static String digest(MessageDigest mDigest) {
		byte[] md = mDigest.digest();
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}
	
	/**
	 * A hashing method that changes a string (like a URL) into a hash suitable for using as a
	 * disk filename.
	 */
	public static String hashKeyForDisk(String key) {
	    String cacheKey;
	    try {
	        final MessageDigest mDigest = MessageDigest.getInstance("MD5");
	        mDigest.update(key.getBytes());
	        cacheKey = bytesToHexString(mDigest.digest());
	    } catch (NoSuchAlgorithmException e) {
	        cacheKey = String.valueOf(key.hashCode());
	    }
	    return cacheKey;
	}
	
	private static String bytesToHexString(byte[] bytes) {
	    // http://stackoverflow.com/questions/332079
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < bytes.length; i++) {
	        String hex = Integer.toHexString(0xFF & bytes[i]);
	        if (hex.length() == 1) {
	            sb.append('0');
	        }
	        sb.append(hex);
	    }
	    return sb.toString();
	}
	
	public final static String getMD5SignData(Map<String, String> map, String value) {
		String TAG = "zytx";
		Oro_ShowLog showlog = new Oro_ShowLog();
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
		showlog.ShowLog(Oro_ShowLog.Verbose,TAG,"getMD5SignData|"+sign);
		sign = getMD5(sign).trim();
		showlog.ShowLog(Oro_ShowLog.Verbose,TAG,"getMD5SignData|"+sign);
		map.remove("key");
		return sign;
	}
	
	
	public String MapToString(Map<String,String> map){
		StringBuilder signDate = new StringBuilder();
		Set<String> keySet = map.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			signDate.append(key + "=" + map.get(key) + "&");
		}
		signDate.deleteCharAt(signDate.length() - 1);
		return signDate.toString().trim();
	}
	

	public static RequestParams MapToParams(Map<String,String> map){
		RequestParams params = null;
		Set<String> keySet = map.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {			
			params.put(iter.next(), map.get(iter.next()));
		}
		return params;
	}
	

//	public String MapToString(RequestParams parmas){
//		
//		StringBuilder signDate = new StringBuilder();
//		Set<String> keySet = map.keySet();
//		Iterator<String> iter = keySet.iterator();
//		while (iter.hasNext()) {
//			String key = iter.next();
//			signDate.append(key + "=" + map.get(key) + "&");
//		}
//		signDate.deleteCharAt(signDate.length() - 1);
//		return signDate.toString().trim();
//	}
}
