package com.oro.mergesdk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.loopj.android.http.AsyncHttpClient;  
import com.loopj.android.http.AsyncHttpResponseHandler;  
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.oro.mergesdk.R;
import com.oro.mergesdk.bean.Oro_MergePayParameter;
import com.oro.mergesdk.bean.Oro_MergeUserParameter;
import com.oro.mergesdk.bean.Oro_UserInfo;
import com.oro.mergesdk.result.Oro_MergeSDKCodeResult;
import com.oro.mergesdk.result.Oro_MergeSDKResultData;
import com.oro.mergesdk.utils.Oro_MD5Helper;
import com.oro.mergesdk.utils.Oro_Md5;
import com.oro.mergesdk.utils.Oro_MergeJSONHelper;
import com.oro.mergesdk.utils.Oro_MergeSDKListener;
import com.oro.mergesdk.utils.Oro_MergeSDKSPPayFinishListener;
import com.oro.mergesdk.utils.Oro_MergeSDKSubUserInfoWrapper;
import com.oro.mergesdk.utils.Oro_MergeSDKUserWrapper;
import com.oro.mergesdk.utils.Oro_MergeSDKUtil;
import com.oro.mergesdk.utils.Oro_MetaDataUtil;
import com.oro.mergesdk.utils.Oro_ShowLog;
import com.reyun.sdk.TrackingIO;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import cn.oro.gamesdk.activity.Oro_LoginActivity;
import cn.oro.gamesdk.activity.Oro_BaseActivity;
import cn.oro.gamesdk.activity.Oro_LogOutActivity;
import cn.oro.gamesdk.sdkinterface.Oro_SDKListener;
import cn.oro.gamesdk.util.Oro_DebugDialog;
import cn.oro.gamesdk.util.Oro_JSONHelper;
import cn.oro.gamesdk.util.Oro_PermissionHelper;
import cn.oro.gamesdk.util.Oro_ResponseDataToLoginCheckIDCard;
import cn.oro.gamesdk.util.Oro_ResponseDataToPay;
import cn.oro.gamesdk.util.Oro_SdkInterface;
import cn.oro.gamesdk.util.Oro_ResponseDataToExit;

public class Oro_SdkAction extends Oro_SdkActionBase {
 
	Boolean isInit = false;
	//getString(R.string.mess_1);
	private static Oro_SdkAction zytxmergesdk;
	Oro_BaseActivity oba = new Oro_BaseActivity();
////	Oro_ShowLog showlog = new Oro_ShowLog();
	String paraString = "";
	Oro_MD5Helper urlhelper=new Oro_MD5Helper();
	private boolean isNetworkAvailable = false;// 检测网络是否可用，默认为false
	private String Amount;
	
	final int INIT = 100;
	private Handler anyHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int what = msg.what;
			switch (what) {
			case INIT:
				if (mySDKlistener != null) {
					
					if(isInit){
						mySDKlistener.onCallBack(Oro_MergeSDKUserWrapper.ACTION_RET_INIT_SUCCESS,null);
					}
					else{
						mySDKlistener.onCallBack(Oro_MergeSDKUserWrapper.ACTION_RET_INIT_FAIL,null);
					}
					
				} else {
					Toast.makeText(mcontext, "初始化完成后，请立即设置监听！",Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
			return false;
		}
	});
	/**
	 * 单例
	 * */
	public static Oro_SdkAction getInstance() {
		if (zytxmergesdk == null) {
			zytxmergesdk = new Oro_SdkAction();
		}
		return zytxmergesdk;
	}
	
	
	
	
	public void Init(Context context){
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK init");
		mcontext = context;
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose, TAG, "AppInfo="+mcontext.getApplicationInfo()); 
		mstate = Oro_MergeSDKUtil.getUUID();
		mchannelId = Oro_MetaDataUtil.getMetaDataValue("Oro_ChannelId", mcontext);
		mgamecode = Oro_MetaDataUtil.getMetaDataValue("Oro_GameCode", mcontext);
		mgameVersion = Oro_MergeSDKUtil.getVersionCode(mcontext);
		getAppInfo();
		//通知游戏初始化成功
//		anyHandler.sendEmptyMessageAtTime(INIT, 10000);
		mInit();

		//热云初始化
		if(!OroGameSDK_Tracking_AppKey.equals("")){

			Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"TrackingIO init ");
			TrackingIO.initWithKeyAndChannelId(mcontext,OroGameSDK_Tracking_AppKey,OroGameSDK_Tracking_Channelid);
		}
		else{
			Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"TrackingIO not init ");
		}
		Oro_LogOutActivity zytxlogoff=Oro_LogOutActivity.getInstance();
		zytxlogoff.setListener(new Oro_SDKListener() {
			
			@Override
			public void onCallBack(int arg0) {
				// TODO Auto-generated method stub

				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"悬浮窗登出 " + arg0);
				switch (arg0){
				case 1://注销帐号成功
					mySDKlistener
					.onCallBack(
							Oro_MergeSDKUserWrapper.ACTION_RET_LOGOUT_SUCCESS,
							null);
					break;
				default:
					break;
				}
				
			}
		});

		//wgq--end
		
		//实名制监听begin
		Oro_LoginActivity zytxshimingzhi=Oro_LoginActivity.getInstance();
		zytxshimingzhi.setListener(new Oro_SDKListener() {
			
			@Override
			public void onCallBack(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0){
				case 26:
					mySDKlistener.onCallBack(
							Oro_MergeSDKUserWrapper.Anti_Addiction_Adult,
							null);
					break;
				case 27:
					mySDKlistener.onCallBack(
							Oro_MergeSDKUserWrapper.Anti_Addiction_NoAdult,
							null);
					break;
				case 28:
					mySDKlistener.onCallBack(
							Oro_MergeSDKUserWrapper.Anti_Addiction_Normal,
							null);
					break;
				case 29:
					mySDKlistener.onCallBack(
							Oro_MergeSDKUserWrapper.Anti_Addiction_IsAddiction,
							null);
					break;
				case 30:
					mySDKlistener.onCallBack(
							Oro_MergeSDKUserWrapper.Anti_Addiction_Unhealthy,
							null);
					break;
				default:
					break;
				}
				
			}
		});
		//实名制监听end
		

		Oro_SPPay_WebView sppaywebview = Oro_SPPay_WebView.getInstance();
		sppaywebview.setMergeSDKSPPayFinishListener(new Oro_MergeSDKSPPayFinishListener(){

			@Override
			public void onCallBack(String OrderNo) {
				// TODO Auto-generated method stub
				getOrderNoStatus(OrderNo);
			}});
	}
	
	
	/**
	 * 初始化AnySDK
	 * */
	public void mInit() {
		// TODO Auto-generated method stub
		isNetworkAvailable = Oro_PermissionHelper.isNetworkAvailable(mcontext);
		if (!isNetworkAvailable) {

			isInit = false;
			Toast.makeText(mcontext, "请检查网络是否可用",Toast.LENGTH_SHORT).show();

			//通知游戏初始化状态
			anyHandler.sendEmptyMessageAtTime(INIT, 1000);
			return;
		} 
		else {
			Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"mInit "+getAllPhoneInfo());
			mPhoneInfo = Oro_MergeSDKUtil.getBASE64(getAllPhoneInfo())
					.replace('+', '*').replace('/', '-').replace('=', '.')
					.replaceAll("\r|\n", "").trim();
			Map<String,String> map = new TreeMap<String, String>(new Comparator<String>() {
				@Override
				public int compare(String arg0, String arg1) {
					// TODO Auto-generated method stub
					return arg0.compareTo(arg1);
				}
			});
			map.put("channelId", mchannelId);
			map.put("gamecode", mgamecode);
			map.put("imei", Oro_MergeSDKUtil.getIMEI(mcontext));
			final String network = Oro_MergeSDKUtil
					.getBASE64(Oro_MergeSDKUtil.getSimType(mcontext))
					.replace('+', '*').replace('/', '-').replace('=', '.')
					.replaceAll("\r|\n", "").trim();
			map.put("networkoperator", network);// 网络提供商
			map.put("networkingway", Oro_MergeSDKUtil.getNetWorkType(mcontext) + "");// 联网方式
			map.put("gameversion", mgameVersion);
			map.put("deviceinfo", mPhoneInfo);
			map.put("extra", "");
			map.put("state", Oro_MergeSDKUtil.getUUID());
			map.put("sign", Oro_Md5.getMD5SignData(map, APPKEY));
			paraString = urlhelper.MapToString(map);
			Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"mInit|parastring="+paraString);
			final List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String>entry:map.entrySet()) {
				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				Log.i("demo", "list"+list);
			}
			// paraString.trim();
			AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
	
				@Override
				protected String doInBackground(String... arg0) {
					// TODO Auto-generated method stub
					Integer retCode = null;
					String content = null;
					try {
						DefaultHttpClient hc = new DefaultHttpClient();
						HttpPost hp = new HttpPost(OpenAnySDKURL);
						HttpEntity entity = new UrlEncodedFormEntity(list, HTTP.UTF_8);
						Log.i("demo", "entity"+entity);
						hp.setEntity(entity);
						hp.setHeader("Cntent-type", "application/x-www-form-urlencoded");
						hp.setHeader("Accept", "application/json");
						HttpResponse hr = hc.execute(hp);
						
	//					HttpGet hg = new HttpGet(arg0[0]);
	//					HttpResponse hr = hc.execute(hg);
						
						retCode = hr.getStatusLine().getStatusCode();
						content = EntityUtils.toString(hr.getEntity(), "UTF-8");
						Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"mInit|content="+content);
	
					} catch (Exception exce) {
						Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"mInit|exce=" + exce.getMessage());
					}
					return content;
				}
				
				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					Oro_MergeSDKCodeResult codeResult = new Oro_MergeSDKCodeResult();
					try {
						codeResult = Oro_MergeJSONHelper.parseObject(result,
								Oro_MergeSDKCodeResult.class);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int status = codeResult.getStatus();
					Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK minit status " + status);
					if (status == 1) {
						isInit = true;
						mySDKlistener
								.onCallBack(
										Oro_MergeSDKUserWrapper.ACTION_RET_INIT_SUCCESS,
										null);
					}
					if (status == 0) {
						isInit = false;
						mySDKlistener
								.onCallBack(
										Oro_MergeSDKUserWrapper.ACTION_RET_INIT_FAIL,
										null);
					}
					super.onPostExecute(result);
				}
			};
			task.execute(OpenAnySDKURL + "?" + paraString);
			Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"mInit|URL=" + OpenAnySDKURL + "?" + paraString);
		}
	}

	public void Start(Context context) {  
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK Start");
		postSDKAction("Start");
		oba.Start(context);
	}

	public void Create(Context context) {  
		if(oromergesdkinfohelper.isfirstinit()){
			Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK is firstinit");
		}
		else{
			Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK not firstinit");
		}
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK Create");
		postSDKAction("Create");
		oba.Create(context);
	}
	
	public void Restart(Context context) {  
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK Restart");
		postSDKAction("Restart");
		oba.Restart(context);
	}
	
	public void Resume(Context context) {  
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK Resume");
		postSDKAction("Resume");
		oba.Resume(context);
	}
	public void Stop(Context context){
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK Stop");
		postSDKAction("Stop");
		oba.Stop(context);
	}
	
	public void Pause(Context context) {  
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK Pause");
		postSDKAction("Pause");
		oba.Pause(context);
	}
	
	public void Destroy(Context context) {  
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK Destroy");
		postSDKAction("Destroy");
		oba.Destroy(context);
	}

	public void NewIntent(Context context,Intent intent) {
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK NewIntent");
		postSDKAction("NewIntent");
	}

	public void ConfigurationChanged(Context context,Configuration newConfig) {
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK ConfigurationChanged");
		postSDKAction("ConfigurationChanged");
	}

    public void RequestPermissionsResult(Context context,int requestCode, String[] permissions, int[] grantResults) {
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK RequestPermissionsResult");
		postSDKAction("RequestPermissionsResult");
    }
    
    public void ActivityResult(Context context,int requestCode, int resultCode, Intent data){
    	Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK ActivityResult");
//    	postSDKAction("ActivityResult");
    }
	
	// 调用SDK支付
	public void Pay(final Oro_MergePayParameter parameter) {
//		final String accesstoken, final String gameOrderNo,
//		final String userId, final String Amount, final String productId,
//		final String productdata, final String productname,
//		final String mserverCode, final String roleid,
//		final String rolename, final String rolebalance,
//		final String roleLevel, final String roleVip, final Context context
		Amount = parameter.getAmount();
		//母包测试为了现实信息，把调用支付方法挪到 提示框里执行
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK Pay");
		// 将商品信息BASE64
		final String mproductDatas = Oro_MergeSDKUtil.getBASE64(parameter.getProductData())
				.replace('+', '*').replace('/', '-').replace('=', '.')
				.replaceAll("\r|\n", "").trim();

		mPhoneInfo = Oro_MergeSDKUtil.getBASE64(getAllPhoneInfo())
				.replace('+', '*').replace('/', '-').replace('=', '.')
				.replaceAll("\r|\n", "").trim();
		/**
		 * AnySDK获取游戏的订单号，将订单号传给SDK
		 * */
		Map<String, String> map = new TreeMap<String, String>(
				new Comparator<String>() {
					@Override
					public int compare(String arg0, String arg1) {
						// TODO Auto-generated method stub
						return arg0.compareTo(arg1);
					}
				});
		mserverCode = String.valueOf(parameter.getServerCode());
		map.put("gameorderno",parameter.getGameOrderNo());
		map.put("channelid", mchannelId);
		map.put("gamecode", mgamecode);
		map.put("userid", parameter.getUserId());
		map.put("servercode", mserverCode);
		map.put("amount", parameter.getAmount());
		map.put("productid", parameter.getProductId());
		map.put("productdata", mproductDatas);
		map.put("state", Oro_MergeSDKUtil.getUUID());
		map.put("deviceinfo", mPhoneInfo);
		map.put("sign", Oro_Md5.getMD5SignData(map, APPKEY));
		paraString = urlhelper.MapToString(map);
		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				HttpClient hc = new DefaultHttpClient();
				HttpGet hg = new HttpGet(arg0[0]);
				String content = null;
				try {
					HttpResponse hr = hc.execute(hg);
					content = EntityUtils.toString(hr.getEntity(), "UTF-8");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK Pay content " + content);
				return content;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				Oro_MergeSDKCodeResult codeResult = new Oro_MergeSDKCodeResult();
				try {
					codeResult = Oro_MergeJSONHelper.parseObject(result,
							Oro_MergeSDKCodeResult.class);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int status = codeResult.getStatus();
				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK Pay status " + status);
				if (status == 1) {
					// Toast.makeText(mcontext, "anySDK获取订单成功",
					// Toast.LENGTH_SHORT).show();

					morderNo = codeResult.getData().getOrderNo();
					parameter.setMergeOrderNo(morderNo);
					if(!OroGameSDK_Tracking_AppKey.equals("")){
						TrackingIO.setPaymentStart(morderNo, "alipay", "CNY",Float.parseFloat(parameter.getAmount()));
						Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK Pay TrackingIO  PaymentStart" + Float.parseFloat(parameter.getAmount()));
					}
					//检测支付方式，根据接口返回决定调用渠道支付还是其他支付
					CheckSwitchPay(parameter);
					//母包充值模拟实际支付，直接通知服务端发点
					
				}
				if (status == 0) {
					// Toast.makeText(mcontext, "anySDK获取订单失败",
					// Toast.LENGTH_SHORT).show();
				}
				super.onPostExecute(result);
			}
		};
		task.execute(GetAnySdkOrderURL + "?" + paraString);
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK Pay " + GetAnySdkOrderURL + "?" + paraString);

	}
	
	public void GetAnySdkOrderNo(final String accesstoken, final String gameOrderNo,
			final String userId, final String Amount, final String productId,
			final String productdata, final String productname,
			final String mserverCode, final String roleid,
			final String rolename, final String rolebalance,
			final String roleLevel, final String roleVip, final Context context){
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK Pay");
		// 将商品信息BASE64
		final String mproductDatas = Oro_MergeSDKUtil.getBASE64(productdata)
				.replace('+', '*').replace('/', '-').replace('=', '.')
				.replaceAll("\r|\n", "").trim();
		/**
		 * AnySDK获取游戏的订单号，将订单号传给SDK
		 * */
		Map<String, String> map = new TreeMap<String, String>(
				new Comparator<String>() {
					@Override
					public int compare(String arg0, String arg1) {
						// TODO Auto-generated method stub
						return arg0.compareTo(arg1);
					}
				});
		map.put("gameorderno", gameOrderNo);
		map.put("channelid", mchannelId);
		map.put("gamecode", mgamecode);
		map.put("userid", userId);
		map.put("servercode", mserverCode);
		map.put("amount", Amount);
		map.put("productid", productId);
		map.put("productdata", mproductDatas);
		map.put("state", Oro_MergeSDKUtil.getUUID());
		map.put("deviceinfo", mPhoneInfo);
		map.put("sign", Oro_Md5.getMD5SignData(map, APPKEY));
		paraString = urlhelper.MapToString(map);
		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				HttpClient hc = new DefaultHttpClient();
				HttpGet hg = new HttpGet(arg0[0]);
				String content = null;
				try {
					HttpResponse hr = hc.execute(hg);
					content = EntityUtils.toString(hr.getEntity(), "UTF-8");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK Pay content " + content);
				return content;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				Oro_MergeSDKCodeResult codeResult = new Oro_MergeSDKCodeResult();
				try {
					codeResult = Oro_MergeJSONHelper.parseObject(result,
							Oro_MergeSDKCodeResult.class);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int status = codeResult.getStatus();

				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK Pay status=" + status);
				if (status == 1) {
					Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK Pay status =1 morderNo=" + morderNo);
					morderNo = codeResult.getData().getOrderNo();
					
//					//渠道支付
//					ChannelPay(userId, mserverCode, productId,productdata, productname, Amount);
//					
//					//切换成巨金支付
//					OroPay(userId, mserverCode, productId,productdata, productname, Amount);

				}
				else
				{
					Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK Pay else status" + status);
				}
				if (status == 0) {
					// Toast.makeText(mcontext, "anySDK获取订单失败",
					// Toast.LENGTH_SHORT).show();
				}
				super.onPostExecute(result);
			}
		};
		task.execute(GetAnySdkOrderURL + "?" + paraString);
		Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"AnySDK获取订单接口" + GetAnySdkOrderURL + "?" + paraString);
	}
	
	public void login(){
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK login");

		postSDKAction("login");
		if(!isInit){
			//调用登陆前检测sdk是否初始化成功
			Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK login check sdk init is false");
			mySDKlistener
					.onCallBack(
							Oro_MergeSDKUserWrapper.ACTION_RET_INIT_FAIL,
							null);
			return;
		}
		//模拟登录
//		Intent intent = new Intent(mcontext,Oro_MergeLoginActivity.class);
//		mcontext.startActivity(intent);
		//模拟登录
		
		Oro_SdkInterface.doKysdSdkLoginCheckIDCard(mcontext,new Oro_ResponseDataToLoginCheckIDCard() {
			@Override
			public void setOro_ResponseDataToLoginCheckIDCard(String code,int idcardtype) {
				// TODO Auto-generated method stub
				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK login code "+code);
				// 将登录成功所得code,base64,访问获取token接口
				String channeldata = "code=" + code;
				String mchannelData = Oro_MergeSDKUtil.getKysdBase64(channeldata);
				/**
				 * 收集信息，AnySDK获取token
				 * */
				Map<String, String> map = new TreeMap<String, String>(
						new Comparator<String>() {
							@Override
							public int compare(String arg0, String arg1) {
								return arg0.compareTo(arg1);
							}
						});
				map.put("channelid", mchannelId);
				map.put("gamecode", mgamecode);
				map.put("channeldata", mchannelData);
				map.put("state", Oro_MergeSDKUtil.getUUID());
				map.put("sign", Oro_Md5.getMD5SignData(map, APPKEY));
				paraString = urlhelper.MapToString(map);
				final List<NameValuePair> list = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String>entry:map.entrySet()) {
					list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				
				AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
					@Override
					protected String doInBackground(String... arg0) {
						// TODO Auto-generated method stub
						
						HttpClient hc = new DefaultHttpClient();
						HttpGet hg = new HttpGet(arg0[0]);
						String content = null;
						try {
							HttpResponse hr = hc.execute(hg);
							content = EntityUtils.toString(hr.getEntity(), "UTF-8");
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK login content "+content);
						return content;
					}

					protected void onPostExecute(String result) {
						//debug  输出接口和返回值信息 begin
						Oro_DebugDialog dialog=new Oro_DebugDialog();
						//debug  输出接口和返回值信息 end
						Oro_MergeSDKCodeResult codeResult = new Oro_MergeSDKCodeResult();
						// Oro_AnyCodeResultData codeResultData = new
						// Oro_AnyCodeResultData();
						try {
							codeResult = Oro_MergeJSONHelper.parseObject(
									result, Oro_MergeSDKCodeResult.class);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						int status = codeResult.getStatus();
						Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK login status "+status);
						if (status == 1) {
							String maccessToken = codeResult.getData()
									.getAccessToken();
							String mchannelData = codeResult.getData()
									.getChannelData();
							getUserInfo(maccessToken, mchannelId, mgamecode, mchannelData);
						}
						else {
							mySDKlistener.onCallBack(Oro_MergeSDKUserWrapper.ACTION_RET_LOGIN_FAIL,null);
						}
					}
				};
				task.execute(LoginForGetTokenAnySDKURL + "?" + paraString);
				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"gettoken " + LoginForGetTokenAnySDKURL + "?" + paraString);
			}
		});
	}
	
	public void getUserInfo(final String token, final String channelid,final String gamecode,final String channelData){
		
		Map<String, String> map = new TreeMap<String, String>(
				new Comparator<String>() {
					@Override
					public int compare(String arg0, String arg1) {
						// TODO Auto-generated method stub
						return arg0.compareTo(arg1);
					}
				});
//		map.put("userid",userid);
		map.put("channelid", mchannelId);
		map.put("gamecode", mgamecode);
		map.put("channeldata", channelData);
		map.put("accesstoken", token);
		map.put("sign", Oro_Md5.getMD5SignData(map, Oro_MetaDataUtil.getMetaDataValue("OroMergeSDK_AppKey",mcontext )));//"6ff70392179a4f8aab8d488049c80ecb"
		paraString = urlhelper.MapToString(map);
		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				HttpClient hc = new DefaultHttpClient();
				HttpGet hg = new HttpGet(arg0[0]);
				String content = null;
				try {
					HttpResponse hr = hc.execute(hg);
					content = EntityUtils.toString(hr.getEntity(), "UTF-8");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"getUserInfo content "+content);
				return content;
			}
			protected void onPostExecute(String result) {
				Oro_MergeSDKCodeResult codeResult = new Oro_MergeSDKCodeResult();
				// Oro_AnyCodeResultData codeResultData = new
				// Oro_AnyCodeResultData();
				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"getUserInfo result "+result);
				try {
					codeResult = Oro_JSONHelper.parseObject(
							result, Oro_MergeSDKCodeResult.class);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int status = codeResult.getStatus();
				if (status == 1) {
					// Toast.makeText(mcontext, "获取userid成功",
					//获取userinfo成功，得到userid
					String userid = codeResult.getData().getUserId();
					
					//获取到userid之后需要返给游戏的，所以需要获取userid成功的地方调用
					Oro_UserInfo userInfo = new Oro_UserInfo();
					userInfo.setAccessToken(token);
					userInfo.setChannelData(channelData);
					userInfo.setGamecode(mgamecode);
					userInfo.setChannelld(mchannelId);
					userInfo.setUserID(userid);
					muserId=userid;
					Oro_SdkInterface.saveuserinfo(userid);
					Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"getUserInfo userid "+userid);
					//登录成功的时候返回给userid的，所以这个位置要是LoginSuccess
					//callback里面的userinfo就是给游戏返回的信息

					mySDKlistener.onCallBack(Oro_MergeSDKUserWrapper.ACTION_RET_LOGIN_SUCCESS,userInfo);
				}
				if (status == 0) {
					mySDKlistener.onCallBack(Oro_MergeSDKUserWrapper.ACTION_RET_LOGIN_FAIL,null);
				}
				
			};

		};
		task.execute(CHECKUSERINFO_URL + "?" + paraString);
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"getUserInfo " + CHECKUSERINFO_URL + "?" + paraString);
		
}
	
	public void logout(String userid,String roleLevel,String roleName,String serverCode){
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK logout");
		postSDKAction("logout");

		
		Map<String, String> map = new TreeMap<String, String>(
				new Comparator<String>() {
					@Override
					public int compare(String arg0, String arg1) {
						// TODO Auto-generated method stub
						return arg0.compareTo(arg1);
					}
				});
		map.put("channelid", mchannelId);
		map.put("gamecode", mgamecode);
		map.put("servercode", mserverCode);
		map.put("userid", userid);
		map.put("state", Oro_MergeSDKUtil.getUUID());
		map.put("sign", Oro_Md5.getMD5SignData(map, APPKEY));
		paraString = urlhelper.MapToString(map);
		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>(){

			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				HttpClient hc = new DefaultHttpClient();
				HttpGet hg = new HttpGet(arg0[0]);
				String content = null;
				try {
					HttpResponse hr = hc.execute(hg);
					content = EntityUtils.toString(hr.getEntity(), "UTF-8");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"退出AnySDK" + content);
				return content;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				Oro_MergeSDKCodeResult codeResult = new Oro_MergeSDKCodeResult();
				// Oro_AnyCodeResultData codeResultData = new
				// Oro_AnyCodeResultData();
				try {
					codeResult = Oro_MergeJSONHelper.parseObject(result,
							Oro_MergeSDKCodeResult.class);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int status = codeResult.getStatus();
				if (status == 1) {
					// Toast.makeText(mcontext, "调用Any退出成功",
					// Toast.LENGTH_SHORT).show();

					mySDKlistener.onCallBack(Oro_MergeSDKUserWrapper.ACTION_RET_LOGOUT_SUCCESS,null);
				} else {
					// Toast.makeText(mcontext, "调用Any退出失败",
					// Toast.LENGTH_SHORT).show();
				}
				super.onPostExecute(result);
			}
		};
		task.execute(LoginOutAnySDKURL + "?" + paraString);
		Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"AnySDK退出" + LoginOutAnySDKURL + "?" + paraString);
	}

	// 当调用AnySDK成功时，调用SDK退出
	public void loginOut(String muserId, String mserverCode) {
	
		Oro_SdkInterface.doKysdSdkExit(mcontext, mserverCode, muserId,new Oro_ResponseDataToExit() {
					@Override
					public void setResponseDataToExit(String arg0) {
						// TODO Auto-generated method stub
						// kyfloat.removeKysdFloatingView();
						mySDKlistener.onCallBack(Oro_MergeSDKUserWrapper.ACTION_RET_LOGOUT_SUCCESS,null);
					}
				});
	}
	
	public void Exit(Context context)
	{
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"Exit");
//		mySDKlistener.onCallBack(Oro_MergeSDKUserWrapper.ACTION_RET_EXIT_NOEXIT,null);
		//Oro_ResponseDataToExit
		new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

        		Oro_SdkInterface.doKysdSdkExit(mcontext, mserverCode, "", new Oro_ResponseDataToExit() {
        			
        			@Override
        			public void setResponseDataToExit(String arg0) {
        				// TODO Auto-generated method stub

        				mySDKlistener.onCallBack(Oro_MergeSDKUserWrapper.ACTION_RET_EXIT_PAGE,null);
        			}
        		});
            }
        });
	}
	
	public void submitExtendData(Oro_MergeUserParameter parameter){
		if(parameter.getSubType() == Oro_MergeSDKSubUserInfoWrapper.ROLE_CREATE)
		{
			//角色创建处理
//			TalkingDataAppCpa.onRegister(parameter.getUserID());
			Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"merge提交用户信息 角色创建");
		}
		else if(parameter.getSubType() == Oro_MergeSDKSubUserInfoWrapper.ROLE_LOGIN)
		{
			//角色登陆处理

			if(oromergesdkinfohelper.isrolefirstlogin(parameter.getUserID())){
				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK is rolefirstlogin");

				if(!OroGameSDK_Tracking_AppKey.equals("")){
					TrackingIO.setRegisterWithAccountID(parameter.getUserID());
				}
			}
			else{
				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK not rolefirstlogin");

				if(!OroGameSDK_Tracking_AppKey.equals("")){
					TrackingIO.setLoginSuccessBusiness(parameter.getUserID());
				}
			}
			Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"merge提交用户信息 角色登陆");
		}
		else if(parameter.getSubType() == Oro_MergeSDKSubUserInfoWrapper.ROLE_LEVEL_UP)
		{
			//角色升级处理
			Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"merge提交用户信息 角色升级");
		}
		else if(parameter.getSubType() == Oro_MergeSDKSubUserInfoWrapper.ROLE_SELECT_SERVER)
		{
			//选择服务器
			mserverCode = String.valueOf(parameter.getServerCode());
			Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"merge提交用户信息 选择服务器");
		}
		else if(parameter.getSubType() == Oro_MergeSDKSubUserInfoWrapper.ROLE_LOGOUT)
		{
			//角色登出
			Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"merge提交用户信息 角色登出");
		}
		
		if(parameter.getSubType() == Oro_MergeSDKSubUserInfoWrapper.ROLE_LOGIN)
		{//角色登录调用服务端接口记录数据
			mPhoneInfo = Oro_MergeSDKUtil.getBASE64(getAllPhoneInfo())
					.replace('+', '*').replace('/', '-').replace('=', '.')
					.replaceAll("\r|\n", "").trim();
			Map<String, String> map = new TreeMap<String, String>(
					new Comparator<String>() {
						@Override
						public int compare(String arg0, String arg1) {
							// TODO Auto-generated method stub
							return arg0.compareTo(arg1);
						}
					});
			map.put("channelid", mchannelId);
			map.put("gamecode", mgamecode);
			map.put("servercode",String.valueOf(parameter.getServerCode()));
			map.put("userid",parameter.getUserID());
			map.put("state", Oro_MergeSDKUtil.getUUID());
			map.put("deviceinfo", mPhoneInfo );//mPhoneInfo   Oro_MergeSDKUtil.getIMEI(mcontext)   deviceinfo
			map.put("imei", Oro_MergeSDKUtil.getIMEI(mcontext));
			map.put("rolelevel", Oro_MergeSDKUtil.getKysdBase64(String.valueOf(parameter.getRoleLevel())));
			map.put("rolename", Oro_MergeSDKUtil.getKysdBase64(parameter.getRoleName()));
			map.put("sign", Oro_Md5.getMD5SignData(map, APPKEY));
			paraString = urlhelper.MapToString(map);
			AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>(){

				@Override
				protected String doInBackground(String... arg0) {
					// TODO Auto-generated method stub
					HttpClient hc = new DefaultHttpClient();
					HttpGet hg = new HttpGet(arg0[0]);
					String content = null;
					try {
						HttpResponse hr = hc.execute(hg);
						content = EntityUtils.toString(hr.getEntity(), "UTF-8");
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"merge提交用户信息" + content);
					return content;
				}

				@Override
				protected void onPostExecute(String result) {
					//请求成功
					Oro_MergeSDKCodeResult codeResult = new Oro_MergeSDKCodeResult();
					try {
						codeResult = Oro_MergeJSONHelper.parseObject(result,
								Oro_MergeSDKCodeResult.class);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int status = codeResult.getStatus();
					if (status == 1) {
						//提交自己服务器成功，调用其他渠道提交用户数据

					}
					super.onPostExecute(result);
				}
			};
			task.execute(SubmitGameUserInfoURL + "?" + paraString);
			Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"merge提交用户信息" + SubmitGameUserInfoURL + "?" + paraString);
		}
	}
	
	private void postSDKAction(final String sAction)
	{
//		Map<String, String> map = new TreeMap<String, String>();
//		map.put("GameCode", mgamecode);
//		map.put("ChannelID", mchannelId);
//		map.put("UserID", muserId);
//		map.put("DeviceID", DeviceId);
//		map.put("Action", sAction);
//		map.put("Sign", Oro_MD5Helper.getMD5SignData(map, "APPKEY"));
//		
//		AsyncHttpClient client = new AsyncHttpClient();
//		client.get(SDKLifeCycleURl,Oro_MD5Helper.MapToParams(map),new JsonHttpResponseHandler(){
//			@Override
//            public void onSuccess(int statusCode, Header[] headers,String responseString) {
//
//				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"SDK "+sAction+"|"+responseString);
//                super.onSuccess(statusCode, headers, responseString);
//            }
//		});
	}
	
	/**
	 * 设置用户监听
	 * */
	public void setListener(Oro_MergeSDKListener zytxMergeSDKListener) {
		mySDKlistener = zytxMergeSDKListener;
	}

	private void CheckSwitchPay(final Oro_MergePayParameter parameter)
	{
		//检测切换支付接口
		/**
		 * AnySDK获取游戏的订单号，将订单号传给SDK
		 * */
		Map<String, String> cspmap = new TreeMap<String, String>(
				new Comparator<String>() {
					@Override
					public int compare(String arg0, String arg1) {
						// TODO Auto-generated method stub
						return arg0.compareTo(arg1);
					}
				});
		cspmap.put("channelid", mchannelId);
		cspmap.put("gamecode", mgamecode);
		cspmap.put("packagename", mPackageName);
		cspmap.put("rolelevel", parameter.getRoleLevel()+"");
		cspmap.put("userid", parameter.getUserId());
		cspmap.put("orderno", morderNo);
		paraString = urlhelper.MapToString(cspmap);
		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				HttpClient hc = new DefaultHttpClient();
				HttpGet hg = new HttpGet(arg0[0]);
				String content = null;
				try {
					HttpResponse hr = hc.execute(hg);
					content = EntityUtils.toString(hr.getEntity(), "UTF-8");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK Pay content " + content);
				return content;
			}

			@Override
			protected void onPostExecute(String result) {

				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK sp result=" + result);
				if (result.equals("1")) {
						//切换成巨金支付
						OroPay(parameter);
				}
				else
				{
					//渠道支付
					ChannelPay(parameter);
				}
				super.onPostExecute(result);
			}
		};
		Oro_ShowLog.ShowLog(Oro_ShowLog.Info, TAG, "MergeSDKCSP接口" + CheckSwitchPayURL + "?" + paraString);
		task.execute(CheckSwitchPayURL + "?" + paraString);
	}
	
	//private void OroPay(final String userId,String mserverCode, final String productId,String productdata,String productname,final String Amount)
	private void OroPay(final Oro_MergePayParameter parameter)
	{
		//切换支付
		
		//morderNo 全局的sdk订单号
//		ShowPayInfo(parameter);
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK OroPay");
		//母包测试界面
        Intent intent = new Intent();  
        Bundle bundle = new Bundle();
        bundle.putString("oromergesdk_orderno", morderNo);
        bundle.putString("getAmount", parameter.getAmount());
        bundle.putString("getGameOrderNo", parameter.getGameOrderNo());
        bundle.putString("getProductData", parameter.getProductData());
        bundle.putString("getProductDescribe", parameter.getProductDescribe());
        bundle.putString("getProductId", parameter.getProductId());
        bundle.putString("getProductName", parameter.getProductName());
        bundle.putInt("getRechargeRatio", parameter.getRechargeRatio());
        bundle.putInt("getRoleBalance", parameter.getRoleBalance());
        bundle.putString("getRoleId", parameter.getRoleId());
        bundle.putInt("getRoleLevel", parameter.getRoleLevel());
        bundle.putString("getRoleName", parameter.getRoleName());
        bundle.putInt("getRoleVip", parameter.getRoleVip());
        bundle.putInt("getServerCode", parameter.getServerCode());
        bundle.putString("getServerName", parameter.getServerName());
        bundle.putString("getUserId", parameter.getUserId());
        intent.putExtras(bundle);
        intent.setClass(mcontext, Oro_SPPay_WebView.class);  
//        intent.putExtras(bundle);
//        intent.putExtra("Oro_MergePayParameter", sdkwebviewparameter);
        mcontext.startActivity(intent);
	}

	private void ChannelPay(final Oro_MergePayParameter parameter)
	{
		//渠道支付方法
		
		//支付成功调用TalkingData统计
		//TalkingDataAppCpa.onPay(parameter.getUserId(),parameter.getProductId(),Integer.parseInt(parameter.getAmount()), "CNY");
		
		//母包测试界面
		//ShowPayInfo(accesstoken,gameOrderNo,userId,Amount,productId,productdata,productname,mserverCode,roleid,rolename,rolebalance,roleLevel,roleVip,context);
		
		//自有包，渠道支付调用巨金支付方法

		Oro_SdkInterface.doKysdSdkPay(mcontext, mgamecode,
				morderNo, parameter.getUserId(), mserverCode, parameter.getProductId(),
				parameter.getProductData(),parameter.getProductDescribe(),parameter.getAmount(),
				new Oro_ResponseDataToPay() {
					@Override
					public void setOro_ResponseDataToPay(int arg0) {
						// TODO Auto-generated method stub
						int code = arg0;
						Log.i("kysd", "获取订单结果值" + code);
						if (code == 0) {
							// 支付成功
							mySDKlistener
									.onCallBack(
											Oro_MergeSDKUserWrapper.PAYRESULT_SUCCESS,
											null);

					    	 //支付成功调用TalkingData统计
							int TalkingDataAmount = Integer.parseInt(parameter.getAmount())*100;//单位是分
					    	 //TalkingDataAppCpa.onPay(parameter.getUserId(),parameter.getProductId(),TalkingDataAmount, "CNY");

							if(!OroGameSDK_Tracking_AppKey.equals("")){
								TrackingIO.setPayment(morderNo, "alipay", "CNY",Float.parseFloat(parameter.getAmount()));
							}
						} else if (code == 1) {
							// 支付失败
							mySDKlistener
									.onCallBack(
											Oro_MergeSDKUserWrapper.PAYRESULT_FAIL,
											null);
						} else if (code == 2) {
							// 支付取消
							mySDKlistener
									.onCallBack(
											Oro_MergeSDKUserWrapper.PAYRESULT_CANCEL,
											null);
						}
					}
				});
		
	}
	private void getOrderNoStatus(final String sOrderNo){

		//检测切换支付接口
		/**
		 * AnySDK获取游戏的订单号，将订单号传给SDK
		 * */
		Map<String, String> map = new TreeMap<String, String>(
				new Comparator<String>() {
					@Override
					public int compare(String arg0, String arg1) {
						// TODO Auto-generated method stub
						return arg0.compareTo(arg1);
					}
				});
		map.put("orderno", sOrderNo);
		paraString = urlhelper.MapToString(map);
		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				HttpClient hc = new DefaultHttpClient();
				HttpGet hg = new HttpGet(arg0[0]);
				String content = null;
				try {
					HttpResponse hr = hc.execute(hg);
					content = EntityUtils.toString(hr.getEntity(), "UTF-8");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"onoStatus content " + content);
				return content;
			}

			@Override
			protected void onPostExecute(String result) {

				Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"onoStatus result " + result + " " + Amount);
				if (result.equals("2")) {
					mySDKlistener
					.onCallBack(
							Oro_MergeSDKUserWrapper.PAYRESULT_SUCCESS,
							null);


					if(!OroGameSDK_Tracking_AppKey.equals("")){
						TrackingIO.setPayment(sOrderNo, "alipay", "CNY",Float.parseFloat(Amount));
						Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"MergeSDK Pay TrackingIO  Payment" + Float.parseFloat(Amount));
					}
				}
				super.onPostExecute(result);
			}
		};
		Oro_ShowLog.ShowLog(Oro_ShowLog.Info, TAG, "onoStatus接口" + CheckOrderNoStatusURL + "?" + paraString);
		task.execute(CheckOrderNoStatusURL + "?" + paraString);
	}
	
}