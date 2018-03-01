package com.oro.mergesdk.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.oro.mergesdk.common.Oro_Constants;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Xml;

public class Oro_MergeSDKUtil {
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
		map.put(Oro_Constants.IMEI, mTm.getDeviceId());
		map.put(Oro_Constants.IESI, mTm.getSubscriberId());
		map.put(Oro_Constants.PHONETYPE, android.os.Build.MODEL);// 手机型号
		// 获取手机的MAC地址
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		map.put(Oro_Constants.MAC, wifiInfo.getMacAddress());
		return map;
	}

	/**
	 * 获取手机的MAC地址
	 * 
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
	 * 获取手机的IMEI号（手机的唯�?��识）
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager mTm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mTm.getDeviceId();
		if (imei == null) {
			return getMAC(context);
		} else {
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
	 * 
	 * @param input
	 * @return Kysd格式的Base64
	 */
	public static String getKysdBase64(String input) {
		if (input == null) {
			return null;
		} else {
			return getBASE64(input).replace('+', '*').replace('/', '-')
					.replace('=', '.').replaceAll("\r|\n", "").trim();
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

	/**
	 * 
	 * @param input
	 *            Kysd格式的Base64
	 * @return 解码后的字符�?
	 */
	public static String getFormatKysdBase64(String input) {
		if (input == null) {
			return null;
		} else {
			return getFormatBASE64(input.replace('*', '+').replace('-', '/')
					.replace('.', '=').replaceAll("\r|\n", "").trim());
		}
	}

	/**
	 * 获取手机卡所属运营商(移动,联�?,电信,其它)
	 * 
	 * @param context
	 * @return 运营商名�?
	 */
	public static String getSimType(Context context) {
		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String[] simType = { "中国移动", "中国联�?", "中国电信", "其它" };
		String operator = telManager.getSimOperator();

		if (operator != null) {

			if (operator.equals("46000") || operator.equals("46002")
					|| operator.equals("46007")) {
				// 中国移动
				return simType[0];

			} else if (operator.equals("46001")) {
				// 中国联�?
				return simType[1];
			} else if (operator.equals("46003")) {
				// 中国电信
				return simType[2];
			}

		}
		return simType[3];
	}

	/**
	 * 获取系统版本号（例如：Android4.4.2�?
	 * 
	 * @return 系统版本�?
	 */
	public static String getSystemVersion() {
		return "Android" + Build.VERSION.RELEASE;
	}

	/**
	 * 获得该应用版本号
	 * 
	 * @param context
	 *            上下文对�?
	 * @return 本应用版本号
	 * @throws NameNotFoundException
	 */
	public static String getVersionCode(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			String versionName = packInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取网络状�?，wifi,wap,2g,3g.
	 * 
	 * @param context
	 *            上下�?
	 * @return int 网络状�? {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G}, *
	 *         {@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}*
	 *         <p>
	 *         {@link #NETWORKTYPE_WIFI}
	 */

	public static int getNetWorkType(Context context) {
		/** 没有网络 */
		final int NETWORKTYPE_INVALID = 0;
		/** wap网络 */
		final int NETWORKTYPE_WAP = 1;
		/** 2G网络 */
		final int NETWORKTYPE_2G = 2;
		/** 3G�?G以上网络，或统称为快速网�?*/
		final int NETWORKTYPE_3G = 3;
		/** wifi网络 */
		final int NETWORKTYPE_WIFI = 4;
		int mNetWorkType = NETWORKTYPE_3G;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			String type = networkInfo.getTypeName();

			if (type.equalsIgnoreCase("WIFI")) {
				mNetWorkType = NETWORKTYPE_WIFI;
			} else if (type.equalsIgnoreCase("MOBILE")) {
				String proxyHost = android.net.Proxy.getDefaultHost();

				mNetWorkType = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G
						: NETWORKTYPE_2G)
						: NETWORKTYPE_WAP;
			}
		} else {
			mNetWorkType = NETWORKTYPE_INVALID;
		}

		return mNetWorkType;
	}

	private static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return true; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return true; // ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return true; // ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return true; // ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return true; // ~ 400-7000 kbps
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return true; // ~ 1-2 Mbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return true; // ~ 5 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return true; // ~ 10-20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false; // ~25 kbps
		case TelephonyManager.NETWORK_TYPE_LTE:
			return true; // ~ 10+ Mbps
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		default:
			return false;
		}
	}

	/**
	 * 使用Android PULL解析器解析assets目录下的KysdAnySDKConfig.xml文件(根据传入的key解析出value)
	 * 
	 * @param key
	 *            �?
	 * @return 返回key对应的value 不存在则返回空字符串
	 */
	public static String parseSDKConfigXML(Context context, String key) {
		String value = "";
		InputStream in = null;
		try {
			XmlPullParser pullParser = Xml.newPullParser();
			in = context.getAssets().open("KysdAnySDKConfig.xml");
			pullParser.setInput(in, "utf-8");
			int eventType = pullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String qName = pullParser.getName();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:
					// if("appKey".equalsIgnoreCase(qName)){
					// appKey = pullParser.nextText();
					//
					// }else if("appId".equalsIgnoreCase(qName)){
					// appId = pullParser.nextText();
					// }
					if (key.equalsIgnoreCase(qName)) {
						value = pullParser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				eventType = pullParser.next();
			}
			in.close();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
}
