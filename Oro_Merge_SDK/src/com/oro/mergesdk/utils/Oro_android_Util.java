package com.oro.mergesdk.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.oro.mergesdk.bean.Oro_constants;

import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Base64;

public class Oro_android_Util {
	/**
	 * 获取手机的状态信�?
	 * 
	 * @param context
	 * @return 通过键获取�?：IMEI；IESI；phoneType（手机型号）；MAC
	 */
	public static Map<String, String> getPhoneState(Context context) {
		Map<String, String> map = new HashMap<String, String>();
		TelephonyManager mTm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		map.put(Oro_constants.IMEI, mTm.getDeviceId());
		map.put(Oro_constants.IESI, mTm.getSubscriberId());
		map.put(Oro_constants.PHONETYPE, android.os.Build.MODEL);// 手机型号
		// 获取手机的MAC地址
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		map.put(Oro_constants.MAC, wifiInfo.getMacAddress());
		return map;
	}
	/**
	 * 获取手机的MAC地址
	 * @param context
	 * @return
	 */
	public static String getMAC(Context context) {
		// 获取手机的MAC地址
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String macAddress = wifiInfo.getMacAddress();
		return macAddress;
	}

	/**
	 * 获取手机的IMEI号（手机的唯一标识）
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager mTm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mTm.getDeviceId();
		if(imei == null){
			return getMAC(context);
		}else{
			return imei;
		}
		
	}

	/**
	 * 获取CPU�?��频率（单位KHZ�?
	 * 
	 * @return
	 */
	public static String getMaxCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
					"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace(); 
			result = "N/A";
		}
		return result.trim();
	}

	/**
	 * 实时获取CPU当前频率（单位KHZ�?
	 * 
	 * @return
	 */
	public static String getCurCpuFreq() {
		String result = "N/A";
		try {
			FileReader fr = new FileReader(
					"/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			result = text.trim();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 生成UUID�?
	 * 
	 * @return
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String uuidData = uuid.toString();
		return uuidData;
	}

	/**
	 * 获取系统总内存大�?
	 * 
	 * @param context
	 * @return
	 */
	public static long getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件 String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			String str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大�?
			arrayOfString = str2.split("\\s+");
			initial_memory = Long.valueOf(arrayOfString[1]).intValue();// 获得系统总内存，单位是KB，乘�?024转换为Byte
			localBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return initial_memory / 1024;// Byte转换为KB或�?MB，内存大小规格化
	}

	/**
	 * 获得可用的内�?
	 * 
	 * @param context
	 * @return
	 */
	public static long getFreeMemory(Context context) {
		long MEM_UNUSED;
		// 得到ActivityManager
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 创建ActivityManager.MemoryInfo对象
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);
		// 取得剩余的内存空�?
		MEM_UNUSED = (mi.availMem / 1024) / 1024;
		return MEM_UNUSED;
	}

	/**
	 * 将字符串进行BASE64编码
	 * 
	 * @param str
	 * @return
	 */
	public static String getBASE64(String str) {
		if (str == null) {
			return null;
		} else {
			return Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
		}
	}

	/**
	 * 将BASE64字符串进行解�?
	 * 
	 * @param str
	 * @return
	 */
	public static String getFormatBASE64(String str) {
		if (str == null) {
			return null;
		} else {
			return new String(Base64.decode(str, Base64.DEFAULT));
		}
	}
}
