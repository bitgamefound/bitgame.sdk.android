package com.oro.mergesdk.bean;

import com.oro.mergesdk.utils.Oro_MetaDataUtil;

import android.content.Context;

public class Oro_constants {
	public static Context mcontext;
	public static final String responseType ="code";
	public static final String redirectUrl = "oob";
	public static final int accounttype = 1;
	public static final String scope= "basic";
	/**
	 * ��ֵ��ʽ����Ϸֱ�䣩
	 */
//	public static final String RECHARGETYPE = "GAME";
	/**
	 * ��ֵ��Կ
	 */
//	public static final String RECHARGEKEY = "testSDKSignKey";
	/**
	 * DES������Կ
	 */
//	public static final String DESEY = "KYSDOB01";
	/**
	 * �ֻ��IMEI
	 */
	public static final String IMEI = "imei";
	/**
	 * �ֻ��IESI
	 */
	public static final String IESI = "iesi";
	/**
	 * �ֻ��ͺ�
	 */
	public static final String PHONETYPE = "phoneType";
	/**
	 * �ֻ��MAC��ַ
	 */
	public static final String MAC = "mac";
	/**
	 * �洢�ο��ǳƵ�SP�ļ���
	 */
//	public static final String VISITORACCOUTN = "visitoraccoutn";
	/**
	 * �����˻���ʱ��Ӧ��Key
	 */
	public static final String USERNAME = "userName";
	/**
	 * �����˻�����ʱ��Ӧ��Key
	 */
	public static final String PWD = "pwd";

	public static final String APPKEY=Oro_MetaDataUtil.getMetaDataValue("zytx_AppKey", mcontext);
	
	public static final String API_PAY_key = "";
	public static final String PARTNER = "2088121018050521";
	public static final String SELLER = "zhifubao@fingersky.cn";
	public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAORX8U8B9x5sffMCTOl0EW7/D5pKE1KbWY/cvoC1Nnbhu7YWYsSqr03ZWPhIViUxJEml24jun9WV67V5XKKNC0a1F8Qyyqcvn6J6rlpqeyiC0SVfw2KZnws8ZQp6kdN+dfbTu29XqL5/Sgq1Sjwram6z9vxNjJWwdydWUh18VgdNAgMBAAECgYA2jxi2QwbzuwKBDNyprzT3K3Rj7i0dwMaujBTsld/Ume0K/eBNk2bdmAAdGtOSgWJlQn7pAfMoynCOZNgQctjtcWSySjofGjurDHtW5PZ1GhAuy+xGSIiHa9TJ8qdk/972GBhdrnoXmIbHlYafNYiLgjnBu8MvF4BCYWMZN3p+oQJBAP8qRQENGPUKgTNDAI9fuHziVTuhMSYgdu0AY0TrCDBzwDl16IGaGcvnXcYywIacjZOAtzc4QlpuT2bAX0s0wlUCQQDlFzT1FMCwzT/hewXiq7Yd4O9+5hv9IonXXA64d07CERdHMp1/ZdxsIgQEW4/pzHCnREscfn2Rt4dDGhhkodkZAkEA3aZ5NJWNBWbvnnjoZqLunkT5Zwf5qrXuuKdqk/ZaMcSKyR88LkCPgglj8wQpK4WYkfXwsV4EfYtvG8Glxa151QJBAL2hAAz+KKIww3f5RV3jlNQWpSf9gZ1/QWoqoWTAHUk2rHYax5P01QgNABY7VFzLBVTvit0DNx2vNR+uMhLahcECQQDktxJ04dekAeZbqxFlIPtK/iFDLZbJSK0/6i8egFzzpiuQMJhPUxiQEvno9RhUHp1yQuO1pbWTmtrVO5Z8Tt8B";
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCXVhFqMdUk4ONHfeH2vK2FLpsP6dgwCBSfKOKXdRgy0lf4iwFj7PNOvjv17xSxsEAnGzGD2LjwSQqrd+2tNkG6vJZwg9ser8KVY5zr491kMGmu8jpObsaQ/sZ6KCZpCGkj/qJ1J/S1Q7OMrPPzSkwgzr2COmT6R4mPU4d/p5WQwIDAQAB";
	public static final String KYSDSDK_ALIPAYAKEY_MD5_SIGN_STRING = "";

}
