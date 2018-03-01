package com.oro.mergesdk;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.WindowManager;
import cn.oro.gamesdk.util.Oro_DBHelper;
import cn.oro.gamesdk.util.Oro_DBHelper_Msg;
import com.oro.mergesdk.utils.Oro_ShowLog;
import cn.oro.gamesdk.view.KysdFloatingView;

import com.oro.mergesdk.common.Oro_AnyPhoneState;
import com.oro.mergesdk.common.Oro_Constants;
import com.oro.mergesdk.utils.Oro_MergeJSONHelper;
import com.oro.mergesdk.utils.Oro_MergeSDKIntoDBHelper;
import com.oro.mergesdk.utils.Oro_MergeSDKListener;
import com.oro.mergesdk.utils.Oro_MergeSDKUtil;
import com.oro.mergesdk.utils.Oro_MetaDataUtil;
import com.oro.mergesdk.utils.Oro_PhoneState;
import com.oro.mergesdk.utils.Oro_android_Util;

public class Oro_SdkActionBase extends Activity	 {
//	
	private static String anysdk_url="http://anysdk.oroera.com";
	private static String oauth2_url="http://oauth2.oroera.com";
	private static String sdk_account_url="http://sdk.account.oroera.com";
	private static String sdk_pay_url="http://sdkpay.oroera.com";
	
	//AnySDK打开日志接口
	public static String OpenAnySDKURL = anysdk_url+"/Login/OpenAnySdkV3";
	//AnySDK登出接口
	public static String  LoginOutAnySDKURL = anysdk_url+"/Login/LoginOut";
	//AnySDK获取订单接口
	public static String GetAnySdkOrderURL = anysdk_url+"/Order/GetAnySdkOrderV2";
	//AnySDK 收集游戏账号信息接口
	public static String SubmitGameUserInfoURL=anysdk_url+"/Login/SubmitGameUserInfoV2";
	//AnySDK获取Token接口
	public static String  LoginForGetTokenAnySDKURL = anysdk_url+"/Login/LoginForGetToken";
	//提交游戏错误日志接口
	public static String SubmitGameExceptionURL=anysdk_url+"/Exception/SubmitGameException";
	// 登录接口
	public static String LOGIN_URL = oauth2_url+"/OAuth2/Authoriza";
	//检测用户信息接口
	public static String CHECKUSERINFO_URL = anysdk_url+"/Login/AppLoginForGetUserInfo";
	// 防沉迷信息接口
	public static String GetIdCode_URL = sdk_account_url+"/IDCardBind/GetIdCodeInfo";
	//记录生命周期数据接口
	public static String SDKLifeCycleURl="";
	//切换支付开关接口
	public static String CheckSwitchPayURL = anysdk_url+"/thirdpay/getisopen";
	//payurl
	public static String SPPayUrl=sdk_pay_url+"/PayWAP/index";
	// 订单接口
	public static String PAYORDER_URL = sdk_pay_url+"/order/GetThirdPayOrder";//Oro_payMain_Activity
	//查询订单状态接口
	public static String CheckOrderNoStatusURL = anysdk_url+"/Order/GetPayStatusByOrderNo";
	
	public static Context mcontext;
	public static String mgamecode="";
	public static String mserverCode="";
	public static String mchannelId="";
	public static String muserId="";
	public static String DeviceId="";
	public static String APPKEY="";
	public static String mstate = "";
	public static String mgameVersion="";
	public static String morderNo="";
	Oro_MergeJSONHelper mJsonHelper;
	public static String TAG = "MergeSDK";
	public static String opsource = "Android";
	public static String mphonestate;
	public static String mmergeskdkey="";
	public static String msdkkey="";
	public static String msdksecret="";
	public static String mProductdata="";
	public static Oro_MergeSDKListener mySDKlistener;
	public static Timer mTimer = null;
	public static TimerTask mTimerTask = null;
	public static String mPhoneInfo = "";
	public static String mPackageName = "";
	public static int mVersionCode = 0;
	public static String mVersionName = "";
	public Oro_MergeSDKIntoDBHelper oromergesdkinfohelper;

	public static String OroGameSDK_Tracking_AppKey = "";
	public static String OroGameSDK_Tracking_Channelid = "";
	
	public void initDatas() {
//		mphonestate = Oro_MergeJSONHelper.toJSON(getPhoneState());
		mphonestate = Oro_MergeSDKUtil.getBASE64(mphonestate)
				.replace('+', '*').replace('/', '-').replace('=', '.')
				.replaceAll("\r|\n", "").trim();
		if (mProductdata!=null&&!mProductdata.equals("")) {
			
			mProductdata = Oro_MergeSDKUtil.getBASE64(mProductdata)
					.replace('+', '*').replace('/', '-').replace('=', '.')
					.replaceAll("\r|\n", "").trim();
		}
	}

	public void getAppInfo() {
 		try {
 			mPackageName = mcontext.getPackageName();
 			mVersionName = mcontext.getPackageManager().getPackageInfo(mPackageName, 0).versionName;
 			mVersionCode = mcontext.getPackageManager().getPackageInfo(mPackageName, 0).versionCode;
 			msdkkey = Oro_MetaDataUtil.getMetaDataValue("OroGameSDK_AppKey", mcontext);
 			OroGameSDK_Tracking_AppKey = Oro_MetaDataUtil.getMetaDataValue("OroGameSDK_Tracking_AppKey", mcontext);
 			OroGameSDK_Tracking_Channelid = Oro_MetaDataUtil.getMetaDataValue("OroGameSDK_Tracking_Channelid", mcontext);
 		} catch (Exception e) {
 		}
 		oromergesdkinfohelper = Oro_MergeSDKIntoDBHelper.getInstance(mcontext);
 	}
	
	//获取手机基本信息
		public String getAllPhoneInfo(){
			APPKEY = Oro_MetaDataUtil.getMetaDataValue("OroMergeSDK_AppKey", mcontext);
			Oro_AnyPhoneState phoneState = new Oro_AnyPhoneState();
			Map<String, String> phoneInfo = Oro_MergeSDKUtil.getPhoneState(mcontext);
			phoneState.setImei(phoneInfo.get(Oro_Constants.IMEI));
			phoneState.setIesi(phoneInfo.get(Oro_Constants.IESI));
			phoneState.setMac(phoneInfo.get(Oro_Constants.MAC));
			phoneState.setPhoneType(phoneInfo.get(Oro_Constants.PHONETYPE));
			phoneState.setSystem_version(Oro_MergeSDKUtil.getSystemVersion());
			phoneState.setRam_max(phoneInfo.get(Oro_MergeSDKUtil.getMaxCpuFreq()));
			phoneState.setCpu_avaliable(Oro_MergeSDKUtil.getCurCpuFreq());
			phoneState.setVersion(mVersionName+"_"+mVersionCode);
			phoneState.setPackage(mPackageName);
			//将phoneState转换成JSON
			String mphoneInfo = mJsonHelper.toJSON(phoneState);
			return mphoneInfo;
		}
		private Oro_PhoneState getPhoneState() {
			APPKEY = Oro_MetaDataUtil.getMetaDataValue("OroMergeSDK_AppKey", mcontext);
			TelephonyManager mTm = (TelephonyManager) Oro_SdkActionBase.this
					.getSystemService(mcontext.TELEPHONY_SERVICE);
			Oro_PhoneState phoneState = new Oro_PhoneState();
			phoneState.setImei(mTm.getDeviceId());
			phoneState.setIesi(mTm.getSubscriberId());

			// 获取手机的MAC地址
			WifiManager wifiManager = (WifiManager) Oro_SdkActionBase.this
					.getSystemService(mcontext.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();

			phoneState.setMac(wifiInfo.getMacAddress());
			phoneState.setPhoneType(android.os.Build.MODEL);
			String ramMax = Formatter.formatFileSize(mcontext,
					Oro_android_Util.getTotalMemory(mcontext));
			
			phoneState.setRam_max(ramMax);
			 
			// 屏幕的宽高度
			WindowManager wm = this.getWindowManager();
			int width = wm.getDefaultDisplay().getWidth();
			int height = wm.getDefaultDisplay().getHeight();
			phoneState.setScreenWidth(width);
			phoneState.setScreenHight(height);
			phoneState.setCpu_max(Oro_android_Util.getMaxCpuFreq());

			return phoneState;
		}
		@Override
		protected void onCreate(Bundle arg0) {
			//控制横屏竖屏
			String screenConfig= Oro_MetaDataUtil.getMetaDataValue("Oro_screenConfig", this);
			Oro_ShowLog.ShowLog(Oro_ShowLog.Info, TAG, "screenConfig="+screenConfig);
			if(screenConfig.contains("LANDSCAPE")){
				Oro_ShowLog.ShowLog(Oro_ShowLog.Info, TAG, "LANDSCAPE");
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
			}
			else if(screenConfig.contains("PORTRAIT")){
				Oro_ShowLog.ShowLog(Oro_ShowLog.Info, TAG, "PORTRAIT");
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
			}
			super.onCreate(arg0);
		}
}
