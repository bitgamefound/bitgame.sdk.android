package com.oro.mergesdk;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.oro.mergesdk.bean.Oro_UserInfo;
import com.oro.mergesdk.bean.Oro_constants;
import com.oro.mergesdk.result.Oro_CheckIDCardResult;
import com.oro.mergesdk.result.Oro_CheckIDCardResultData;
import com.oro.mergesdk.result.Oro_MergeSDKCodeResult;
import com.oro.mergesdk.utils.Oro_MD5Helper;
import com.oro.mergesdk.utils.Oro_MResource;
import com.oro.mergesdk.utils.Oro_Md5;
import com.oro.mergesdk.utils.Oro_MergeJSONHelper;
import com.oro.mergesdk.utils.Oro_MergeSDKUserWrapper;
import com.oro.mergesdk.utils.Oro_MergeSDKUtil;
import com.oro.mergesdk.utils.Oro_MetaDataUtil;
import com.oro.mergesdk.utils.Oro_OnLineDBHelper;
import com.oro.mergesdk.utils.Oro_PermissionHelper;
import com.oro.mergesdk.utils.Oro_ShowLog;
import com.oro.mergesdk.utils.Oro_StringTools;
import com.oro.mergesdk.utils.Oro_Verify;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Oro_MergeLoginActivity extends Oro_SdkActionBase {

	RadioGroup rg;
	Oro_MergeSDKUtil android_util = new Oro_MergeSDKUtil();
	Oro_MD5Helper urlHelper = new Oro_MD5Helper();
	String accounttype="";

	Map<String, String> userinfomap = new TreeMap<String, String>();

	//1 未绑定
	//2 已成年
	//3 未成年
	private int idcardtype = 1;
	
	private String code = null;
	Oro_OnLineDBHelper oldb = null;
	String[] onlineinfo = new String[2];
	int onlinetime = 0;
	long onlinedatetime = 0;
	String onlineusername = "";
	String zytx_username_content="sdklogintest01";
	String zytx_password_content="qweqaz12345";
	//母包测试参数
	//母包测试参数
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(Oro_MResource.getIdByName(getApplication(), "layout", "oro_merge_login_activity"));
		rg = (RadioGroup) findViewById(Oro_MResource.getIdByName(
				getApplication(), "id", "merge_sdk_testaccount_rg"));
		
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
			//在这个函数里面用来改变选择的radioButton的数值，以及与其值相关的 //任何操作，详见下文
				if(checkedId==Oro_MResource.getIdByName(mcontext, "id", "merge_sdk_testaccount01_rb")){
					//选择了第一个帐号
					zytx_username_content="sdklogintest01";
					zytx_password_content="qweqaz12345";
					Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"login checked account 1");
				}else{
					//选择了第二个帐号
					zytx_username_content="sdklogintest02";
					zytx_password_content="qweqaz12345";
					Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"login checked account 2");
				}
			}
		});
		
	}
	
	public void Login_fail(View view){
		//测试用方法登陆失败

		mySDKlistener.onCallBack(Oro_MergeSDKUserWrapper.ACTION_RET_LOGIN_FAIL,null);
		finish();
	}
	
	public void Zytx_Login(View view) {
		//添加渠道支付代码begin
		//添加渠道支付代码end
		//母包登陆测试代码begin
		initDatas();
		boolean isNetworkAvailable = Oro_PermissionHelper.isNetworkAvailable(mcontext);
		if (!isNetworkAvailable) {
			Toast.makeText(mcontext, "请检查网络是否可用",Toast.LENGTH_SHORT).show();
			return;
		} else {
			// 获取用户输入信息，判断是否为空值
//			zytx_username_content = zytx_username.getText().toString().trim();
//			zytx_password_content = zytx_password.getText().toString().trim();

//			if (Zytx_StringTools.StringisNull(zytx_username_content)
//					|| Zytx_StringTools.StringisNull(zytx_password_content)) {
//				Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			if (!Zytx_Verify.CheckAccount(zytx_username_content)
//					&& !Zytx_Verify.CheckTell(zytx_username_content)) {
//				Toast.makeText(this, "账号格式错误", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			if (!Zytx_Verify.CheckPassword(zytx_password_content)) {
//				Toast.makeText(this, "密码格式错误", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			if (Zytx_Verify.CheckAccount(zytx_username_content)) {
//				accounttype = "1";
//			}
//			if (Zytx_Verify.CheckTell(zytx_username_content)) {
//				accounttype = "2";
//			}

			accounttype = "1";
			Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"accounttype="+accounttype);
			
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.mmm");
			String starsubInfoTime = format.format(date);

			Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"登录访问开始时间====="+starsubInfoTime);
			// 泛型，键值对，将信息打包封装到URL当中
			Map<String, String> map = new TreeMap<String, String>(
					new Comparator<String>() {
						public int compare(String obj1, String obj2) {
							return obj1.compareTo(obj2);
						}
					});
			map.put("account", zytx_username_content);
			map.put("md5Password", Oro_MD5Helper.getMD5(zytx_password_content));
			map.put("validatecode", "0000");
			map.put("gamecode", mgamecode);
			map.put("opsource", opsource);
			map.put("responseType", Oro_constants.responseType);
			map.put("state", android_util.getUUID());
			map.put("accounttype", accounttype);
			map.put("redirectUrl", Oro_constants.redirectUrl);
			map.put("scope", Oro_constants.scope);
			map.put("phonestate", mphonestate);
			map.put("imei", android_util.getIMEI(mcontext));
			map.put("sign", Oro_Md5.getMD5SignData(map, msdkkey));
			String paraString = urlHelper.MapToString(map);
			Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"Login paraString="+paraString);
			// 获取参数，调用登录接口
			AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
				protected String doInBackground(String... arg0) {
					Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"Login arg0="+ arg0[0]);
					HttpClient hc = new DefaultHttpClient();
					HttpGet hg = new HttpGet(arg0[0]);
					String content = null;
					try {
						HttpResponse hr = hc.execute(hg);
						content = EntityUtils.toString(hr.getEntity(), "UTF-8");
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"Login content="+ content);
					Date date = new Date();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.mmm");
					String starsubInfoTime = format.format(date);
					Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"登录访问结束时间====="+starsubInfoTime);
					return content;
				}

				// 结束
				protected void onPostExecute(String result) {
					Oro_MergeSDKCodeResult codeResult = new Oro_MergeSDKCodeResult();
					try {
						codeResult = Oro_MergeJSONHelper.parseObject(result,
								Oro_MergeSDKCodeResult.class);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					// 判断是否登录成功
					int status = codeResult.getStatus();
					if (status == 1) {

						Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"Login getTokenSUccess Token="+codeResult.getData().getAccessToken()+"|ChannelDate="+codeResult.getData().getChannelData());
//						登录成功，隐藏软键盘
						//((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(Zytx_MergeLoginActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
						code = codeResult.getData().getCode();
						// 登录成功，通知调用者取到了返回码
						Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"Login code="+ code);
						
						getToke(code);

//						Map<String,String> map = new TreeMap<String, String>();
//						map.put("Token", codeResult.getData().getAccessToken());
//						map.put("ChannelData", codeResult.getData().getChannelData());
//						map.put("GameCode", mgamecode);
//						map.put("ChannelId", mchannelId);
//						
//						getUserInfo(codeResult.getData().getAccessToken(),mchannelId,mgamecode,codeResult.getData().getChannelData());

//						mySDKlistener.onCallBack(Zytx_MergeSDKUserWrapper.ACTION_RET_LOGIN_SUCCESS,map);
						
					}
					String msgCode = codeResult.getData().getMsgCode();
					if (msgCode.equals("Account_Not_Exist")) {
						Toast.makeText(getApplication(), "账号不存在，请去注册！",Toast.LENGTH_SHORT).show();
						return;
					}
					if (msgCode.equals("Account_Password_Error")) {
						Toast.makeText(getApplication(), "账号密码错误！",Toast.LENGTH_SHORT).show();
						return;
					}
					if (msgCode.equals("Network_Error")) {
						Toast.makeText(getApplication(), "网络错误，请检查您的网络设置",Toast.LENGTH_SHORT).show();
						return;
					}
					super.onPostExecute(result);
				}
			};
			task.execute(LOGIN_URL + "?" + paraString);
			Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"Login url=" + LOGIN_URL + "?" + paraString);
		}
		//母包登陆测试代码 end
	}
	
	//获取用户userid	
	
	public void getUserInfo(final String token, final String channelid,final String gamecode,final String channelData){
			
			Map<String, String> map = new TreeMap<String, String>(
					new Comparator<String>() {
						@Override
						public int compare(String arg0, String arg1) {
							// TODO Auto-generated method stub
							return arg0.compareTo(arg1);
						}
					});
//			map.put("userid",userid);
			map.put("channelid", mchannelId);
			map.put("gamecode", mgamecode);
			map.put("channeldata", channelData);
			map.put("accesstoken", token);
			map.put("sign", Oro_Md5.getMD5SignData(map, Oro_MetaDataUtil.getMetaDataValue("OroMergeSDK_AppKey",mcontext )));//"6ff70392179a4f8aab8d488049c80ecb"
			String paraString = urlHelper.MapToString(map);
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
					Log.i("anysdk", "Zytx_AnySDKUSser|getUserInfo，，，AnySDK收集游戏账号信息=====" + content);
					return content;
				}
				
				protected void onPostExecute(String result) {

					Oro_MergeSDKCodeResult codeResult = new Oro_MergeSDKCodeResult();
					// zytx_AnyCodeResultData codeResultData = new
					// zytx_AnyCodeResultData();
					Log.i("anysdk", "Zytx_AnySDKUSser|onPostExecute111 " + result);
					try {
						codeResult = Oro_MergeJSONHelper.parseObject(
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

						Log.i("anysdk", "Zytx_AnySDKUSser|onPostExecute-222"+userid);
						//manySDKUserIDlistener.onCallBack("2222222222222222");
						//登录成功的时候返回给userid的，所以这个位置要是LoginSuccess
						//callback里面的userinfo就是给游戏返回的信息
						mySDKlistener.onCallBack(Oro_MergeSDKUserWrapper.ACTION_RET_LOGIN_SUCCESS,userInfo);
						finish();
						//登陆成功检测防沉迷
						
//						checkidcard(zytx_username_content);
					}
					if (status == 0) {
						 Toast.makeText(mcontext,
						 "获取userinfo失败"+codeResult.getData().getUserId(),
						 Toast.LENGTH_SHORT).show();
						 finish();
					}
					
				};

			};
			task.execute(CHECKUSERINFO_URL + "?" + paraString);
			Log.i("anysdk", "Zytx_AnySDKUSser|getUserInfo" + CHECKUSERINFO_URL + "?" + paraString);
			
	}
	
//	private void checkidcard(String account){
//		//根据开关判读是否检测防沉迷
//		if(!isAntiAddiction){
////			不检测
//			idcardtype = 2;//2=已成年
//			//通知游戏已成年
//			mySDKlistener.onCallBack(26,null);
//		}
//		else{
//			onlineusername = account;
//			// 准备参数数据
//			if(account.equals("QuickReg")){
//				//快速注册用户，默认需要防沉迷
//				idcardtype=1;
//				//通知游戏未成年
//				mySDKlistener.onCallBack(27 ,null);
//			}
//			else{
//				Map<String, String> map = new TreeMap<String, String>(
//						new Comparator<String>() {
//							public int compare(String obj1, String obj2) {
//								return obj1.compareTo(obj2);
//							}
//						});
//				map.put("account", account);
//				map.put("gamecode", mgamecode);
//				String paraString = urlHelper.MapToString(map);
//				AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
//					protected String doInBackground(String... arg0) {
//						Zytx_ShowLog.ShowLog(Zytx_ShowLog.Verbose,TAG,"login " + arg0[0]);
//						HttpClient hc = new DefaultHttpClient();
//						HttpGet hg = new HttpGet(arg0[0]);
//						String content = null;
//						try {
//							HttpResponse hr = hc.execute(hg);
//							content = EntityUtils.toString(hr.getEntity(), "UTF-8");
//						} catch (ClientProtocolException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//
//						Zytx_ShowLog.ShowLog(Zytx_ShowLog.Verbose,TAG,"检测防沉迷=" + content);
//						Date date = new Date();
//						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.mmm");
//						String starsubInfoTime = format.format(date);
//						Zytx_ShowLog.ShowLog(Zytx_ShowLog.Verbose,TAG,"登录访问结束时间="+starsubInfoTime);
//						return content;
//					}
//		
//					protected void onPostExecute(String result) {
//						Zytx_ShowLog.ShowLog(Zytx_ShowLog.Verbose,TAG,"检测防沉迷=" + result);
//						Zytx_CheckIDCardResult CheckIDCardcodeResult = new Zytx_CheckIDCardResult();
//						Zytx_CheckIDCardResultData CheckIDCardcodeResultData = new Zytx_CheckIDCardResultData();
//						try {
//							CheckIDCardcodeResult = Zytx_MergeJSONHelper.parseObject(result,
//									Zytx_CheckIDCardResult.class);
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//						
//						int status = CheckIDCardcodeResult.getStatus();
//						if (status == 1) {
//							idcardtype = CheckIDCardcodeResult.getCode();
//						}
//						else{
//							//接口请求失败，默认需要防沉迷
//							idcardtype = 1;
//						}
//
//						Zytx_ShowLog.ShowLog(Zytx_ShowLog.Verbose,TAG,"status=" + status + " idcardtype=" + idcardtype);
//						
//						if(idcardtype!=2)//未实名启动计时器3小时候提醒绑定信息
//						{//1未实名，2已成年，3未成年
//							//未成年执行定时器记录在线时间
//
//							Zytx_ShowLog.ShowLog(Zytx_ShowLog.Verbose,TAG,"执行定时器检测在线时间 idcardtype=" + idcardtype);
////					    	handler.removeCallbacks(runnable); //执行前关闭定时器，否则可能出现重复执行
////					    	handler.postDelayed(runnable, 1000);
//							if (mTimer == null) { 
//								if(mTimerTask == null){
//									startTimer();
//								}
//							}
//
//    						//通知游戏未成年
//    						mySDKlistener.onCallBack(27,null);
//						}
//						else
//						{
//    						//通知游戏已成年
//    						mySDKlistener.onCallBack(26,null);
//    						stopTimer();
//						}
//						
//						Zytx_MergeLoginActivity.this.finish();
//						super.onPostExecute(result);
//					}
//				};
//				task.execute(GetIdCode_URL + "?" + paraString);
//
//				Zytx_ShowLog.ShowLog(Zytx_ShowLog.Verbose,TAG,"GetIdCode_URL " + GetIdCode_URL + "?" + paraString);
//			}
//		}
//		Zytx_ShowLog.ShowLog(Zytx_ShowLog.Verbose,TAG,"防沉迷验证 code="+code+" idcardtype="+idcardtype);
////		mZytx_ResponseDataToLoginCheckIDCard.setZytx_ResponseDataToLoginCheckIDCard(code, idcardtype);
//		
//		Zytx_MergeLoginActivity.this.finish();
//	}
	
	Handler handler=new Handler();  
	Runnable runnable=new Runnable() {  
	    @Override  
	    public void run() {  
	        // TODO Auto-generated method stub  
	        
	    	
	        handler.postDelayed(this, 1000);
	    }  
	}; 
	

	private void startTimer(){  
        if (mTimer == null) {  
            mTimer = new Timer();  
        }  
  
        if (mTimerTask == null) {  
            mTimerTask = new TimerTask() {  
                @Override  
                public void run() {  
                	//读取当前用户在线时间
        	    	onlineinfo = oldb.GetUserOnlineinfo(onlineusername);
        	    	if(onlineinfo!=null)
        	    	{
        	    		onlinetime = Integer.parseInt(onlineinfo[0]);
        	    		onlinedatetime = Long.parseLong(onlineinfo[1]);
        	    		
        	    		int Fatigue = Integer.parseInt(Oro_MetaDataUtil.getMetaDataValue("Oro_AntiAddiction_Fatigue", getApplicationContext()));
        		    	int Unhealthy = Integer.parseInt(Oro_MetaDataUtil.getMetaDataValue("Oro_AntiAddiction_Unhealthy", getApplicationContext()));
        		    	if(onlinetime>=Fatigue){
        		    		//在线时间到达3小时疲劳时间弹提示框 游戏收益50%
        		    		if(onlinetime%10 == 0)
        		    		{//每10分钟提示一次
	        					if(onlinetime>=Unhealthy){
	        						Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"沉迷游戏 "+onlinetime+">="+Unhealthy);
	        						//通知游戏不健康游戏时间
	        						mySDKlistener.onCallBack(30,null);
	        						//到达5小时不健康游戏清空数据重新计算并弹提示框 游戏收益0%
	        						oldb.AddUserOnline(onlineusername, "", 0);
	//        				    	handler.removeCallbacks(runnable); //关闭定时器，否则可能出现重复执行
	        						stopTimer();
	        						
	        					}
	        					else{
	        						Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"不健康游戏 "+onlinetime+">="+Fatigue);
	        						//通知游戏疲劳游戏时间
	        						mySDKlistener.onCallBack(29,null);
	        					}
//	        					if(!bindidcardactivityisshow && idcardtype==1){
//	        						
//	        			    		Intent intent = new Intent();
//	        						intent.setClass(getApplication(), Zytx_BindIDCardActivity.class);
//	        						intent.putExtra("username",onlineusername);
//	        						intent.putExtra("usertype",idcardtype);
//	        						intent.putExtra("onlinetime",onlinetime);
//	        						intent.putExtra("source","timer");
//	        						startActivity(intent);
//	        					}
        		    		}
        		    	}
        		    	
        		    	Date date1 = new Date(onlinedatetime);
        		    	Date date2 = new Date(System.currentTimeMillis());
        		    	
        		    	if(date1.getDay() != date2.getDay())
        		    	{
        		    		//通知游戏正常游戏时间
    						mySDKlistener.onCallBack(28,null);
        		    		oldb.AddUserOnline(onlineusername, "", 0);

//        			    	Log.v("zytx", "执行计时器 date1!=date2 在线时间"+onlinetime);
        		    	}
        		    	else{
        		    		onlinetime = onlinetime+1;
        		    		oldb.AddUserOnline(onlineusername, "", onlinetime);
//        			    	Log.v("zytx", "执行计时器 date1==date2 在线时间"+onlinetime);
        		    	}
        	    	}
        	    	else{
        	    		//通知游戏正常游戏时间
						mySDKlistener.onCallBack(28,null);
        	    		oldb.AddUserOnline(onlineusername, "", 0);
//        		    	Log.v("zytx", "执行计时器 onlineinfo==null 在线时间"+onlinetime);
        	    				
        	    	}

        			Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,TAG,"在线时间"+onlinetime);
                }  
            };  
        }  
  
        if(mTimer != null && mTimerTask != null )  {
        	try
			 {
				 String istest = Oro_MetaDataUtil.getMetaDataValue("Oro_AntiAddiction_IsTest", getApplicationContext());
				 if(istest!= null && istest.equals("1"))
				 {
					 mTimer.schedule(mTimerTask, 1000, 1000);  //测试使用 定时器1秒启动一次
				 }
				 else
				 {
					 mTimer.schedule(mTimerTask, 60*1000, 60*1000);  //正式启动 定时器1分钟启动一次
				 }
			 }
			 catch(Exception e)
			 {
				 mTimer.schedule(mTimerTask, 60*1000, 60*1000);  //正式启动 定时器1分钟启动一次
			 }
        }
    }  
  
    private void stopTimer(){  
          
        if (mTimer != null) {  
            mTimer.cancel();  
            mTimer = null;  
        }  
  
        if (mTimerTask != null) {  
            mTimerTask.cancel();  
            mTimerTask = null;  
        }
    }
    
    private void getToke(String code)
    {
    	String channeldata = "code=" + code;
		String mchannelData = Oro_MergeSDKUtil.getKysdBase64(channeldata);
		Log.i("demo", "登录成功啦，哈哈" + mchannelData);
		/**
		 * 收集信息，AnySDK获取token
		 * */
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
		map.put("channeldata", mchannelData);
		map.put("state", Oro_MergeSDKUtil.getUUID());
		map.put("sign", Oro_Md5.getMD5SignData(
				map, APPKEY));
		String paraString = urlHelper.MapToString(map);
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
				Log.i("anysdk", "Zytx_AnySDKUSser|login---获取token结果" + content);
				return content;
			}

			protected void onPostExecute(String result) {
				Oro_MergeSDKCodeResult codeResult = new Oro_MergeSDKCodeResult();
				// zytx_AnyCodeResultData codeResultData = new
				// zytx_AnyCodeResultData();
				try {
					codeResult = Oro_MergeJSONHelper.parseObject(
							result, Oro_MergeSDKCodeResult.class);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				int status = codeResult.getStatus();
				Log.i("anysdk", "Zytx_AnySDKUSser|login---status=" + status);
				if (status == 1) {
					// Toast.makeText(mcontext, "获取token成功",
					// Toast.LENGTH_SHORT).show();
					String maccessToken = codeResult.getData()
							.getAccessToken();
					String mchannelData = codeResult.getData()
							.getChannelData();
					Oro_UserInfo userInfo = new Oro_UserInfo();
					userInfo.setAccessToken(maccessToken);
					userInfo.setChannelData(mchannelData);
					userInfo.setGamecode(mgamecode);
					userInfo.setChannelld(mchannelId);
					getUserInfo(maccessToken,mchannelId,mgamecode,mchannelData);
//					mySDKlistener.onCallBack(
//									Zytx_MergeSDKUserWrapper.ACTION_RET_LOGIN_SUCCESS,userInfo);
				}
				if (status == 0) {
					// Toast.makeText(mcontext,
					// "获取token失败"+codeResult.getData().getMsgCode(),
					// Toast.LENGTH_SHORT).show();
				}
			};
		};
		task.execute(LoginForGetTokenAnySDKURL + "?"
				+ paraString);
		Log.i("demo", "获取token接口" + LoginForGetTokenAnySDKURL
				+ "?" + paraString);
    }
}
