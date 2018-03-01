package com.oro.mergesdk;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.oro.mergesdk.bean.Oro_MergePayParameter;
import com.oro.mergesdk.result.Oro_OrderResult;
import com.oro.mergesdk.utils.Oro_JSONHelper;
import com.oro.mergesdk.utils.Oro_MD5Helper;
import com.oro.mergesdk.utils.Oro_MResource;
import com.oro.mergesdk.utils.Oro_Md5;
import com.oro.mergesdk.utils.Oro_MergeSDKSPPayFinishListener;
import com.oro.mergesdk.utils.Oro_ShowLog;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import cn.oro.gamesdk.sdkinterface.Oro_SDKListener;

public class Oro_SPPay_WebView extends Oro_SdkActionBase{
	private ImageView iv_Back;
	private WebView wv;
	private Oro_MergePayParameter parameter;
	private String oromergesdk_orderno;
	Oro_MD5Helper urlhelper = new Oro_MD5Helper();
	Bundle myBundle;
	private static Oro_SPPay_WebView sppaywebview;
	/**
	 * 单例
	 * */
	public static Oro_SPPay_WebView getInstance() {
		if (sppaywebview == null) {
			sppaywebview = new Oro_SPPay_WebView();
		}
		return sppaywebview;
	}
	Oro_MergeSDKSPPayFinishListener mergesdksppayfinishListener = null;
	/**
	 * 设置用户监听
	 * */
	public void setMergeSDKSPPayFinishListener(Oro_MergeSDKSPPayFinishListener OroMergeSDKSPPayFinish) {
		mergesdksppayfinishListener = OroMergeSDKSPPayFinish;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(Oro_MResource.getIdByName(getApplication(), "layout","oro_sp_wv"));
//		parameter.setAccessToken(getIntent().getStringExtra(""));

        myBundle = getIntent().getExtras();
        parameter = new Oro_MergePayParameter();
		oromergesdk_orderno = myBundle.getString("oromergesdk_orderno");
        parameter.setAmount(myBundle.getString("getAmount"));
        parameter.setGameOrderNo(myBundle.getString("getGameOrderNo"));
        parameter.setProductData(myBundle.getString("getProductData"));
        parameter.setProductDescribe(myBundle.getString("getProductDescribe"));
        parameter.setProductId(myBundle.getString("getProductId"));
        parameter.setProductName(myBundle.getString("getProductName"));
        parameter.setRechargeRatio(myBundle.getInt("getRechargeRatio"));
        parameter.setRoleBalance(myBundle.getInt("getRoleBalance"));
        parameter.setRoleId(myBundle.getString("getRoleId"));
        parameter.setRoleLevel(myBundle.getInt("getRoleLevel"));
        parameter.setRoleName(myBundle.getString("getRoleName"));
        parameter.setRoleVip(myBundle.getInt("getRoleVip"));
        parameter.setServerCode(myBundle.getInt("getServerCode"));
        parameter.setServerName(myBundle.getString("getServerName"));
        parameter.setUserId(myBundle.getString("getUserId"));
		iv_Back = (ImageView) findViewById(Oro_MResource.getIdByName(getApplication(), "id", "oro_sppay_iv_back"));

		iv_Back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sppaywebview.getInstance().mergesdksppayfinishListener.onCallBack(oromergesdk_orderno);
				finish();
			}
		});
        
        CreaterSDKOrder(getIntent());
	}
	
    public void CreaterSDKOrder(final Intent intent) {
		Oro_ShowLog.ShowLog(Oro_ShowLog.Info, TAG, "开始支付======1=======");
		Map<String, String> map = new TreeMap<String, String>(
				new Comparator<String>() {
					public int compare(String obj1, String obj2) {
						// 升序排序
						return obj1.compareTo(obj2);
					}
				});
		map.put("state", UUID.randomUUID().toString());
		map.put("ordersource", "1");// 充值来源
		map.put("paymethodcode", String.valueOf(2));// 充值方式
		map.put("gameorderno", oromergesdk_orderno);// 游戏的订单号
		map.put("userid",parameter.getUserId());// 平台用户id   parameter.getUserId()  15EE1C7F5920B46A9E6705B2229F2321
		map.put("gamecode", mgamecode);// 游戏code
		map.put("servercode", String.valueOf(parameter.getServerCode()));// 区服code
		map.put("productid", parameter.getProductId());// 商品id
		map.put("productdata", mProductdata);
		map.put("opsource", opsource);
		map.put("amount", parameter.getAmount());// 充值总金额
		Double amd = Double.valueOf(parameter.getAmount());
		map.put("sign", Oro_Md5.getMD5SignData(map, msdkkey));
		String paraString = urlhelper.MapToString(map);
		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				Oro_ShowLog.ShowLog(Oro_ShowLog.Info, TAG, "arg0 " + arg0[0]);
				// TODO Auto-generated method stub
				// 获取JSON数据
				Oro_ShowLog.ShowLog(Oro_ShowLog.Info, TAG, "开始支付======1=======");
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
				return content;
			}

			@Override
			protected void onPostExecute(String result) {
				Oro_ShowLog.ShowLog(Oro_ShowLog.Info, TAG, "result=" + result);

				Oro_OrderResult orderResult;
				try {
					orderResult = Oro_JSONHelper.parseObject(result, Oro_OrderResult.class);
					int status = orderResult.getStatus();
					if (status == 0) {// 返回失败
						String msgCode = orderResult.getData().getMsgCode();
						Oro_ShowLog.ShowLog(Oro_ShowLog.Info, TAG, "======msgCode========="+msgCode);
						String guidString = orderResult.getData().getGuid();
						if (msgCode.equals("UserId_NotExist")) {
							Toast.makeText(getApplication(), "您的角色不存在",
									Toast.LENGTH_SHORT).show();
						}
						if (msgCode.equals("Amount_Error")) {
							Toast.makeText(getApplication(), "总金额超出范围",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(Oro_SPPay_WebView.this,
									msgCode + "+" + guidString, Toast.LENGTH_LONG)
									.show();
						}
						Oro_ShowLog.ShowLog(Oro_ShowLog.Info, TAG, msgCode + "+" + guidString);

					} else if (status == 1) {// 返回成功
						String notifyUrl = orderResult.getData().getNotifyUrl();// 回调URL
						String orderNo = orderResult.getData().getOrderNo();// 订单号
//						String amount = conversionValuation(String.valueOf(parameter.getAmount()));// 总金额
						Oro_ShowLog.ShowLog(Oro_ShowLog.Info, TAG, "orderNo=" + orderNo);
						showwv(orderNo);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		task.execute(PAYORDER_URL + "?" + paraString);
		Oro_ShowLog.ShowLog(Oro_ShowLog.Info, TAG, "订单接口" + PAYORDER_URL + "?" + paraString);
	}
	
	private void showwv(String sdkorderno){

		wv = (WebView) findViewById(Oro_MResource.getIdByName(
				getApplicationContext(), "id", "oro_sppay_wv"));
		String url = SPPayUrl + "?anyorderno=" + oromergesdk_orderno + "&orderno="+sdkorderno;
		Oro_ShowLog.ShowLog(Oro_ShowLog.Info,TAG,"sppay url = "+ url);
		Log.v("sdk", "webview = "+url);
		WebSettings setting = wv.getSettings();  
		setting.setJavaScriptEnabled(true);//支持js  
		wv.setScrollBarStyle(0);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上  
		//如果不设置WebViewClient，请求会跳转系统浏览器  
		wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains("alipays://platformapi")) {
					if (checkAliPayInstalled(Oro_SPPay_WebView.this)) {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(url));
						startActivity(intent);
					}

				} else if (url.startsWith("weixin://wap/pay?")) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					startActivity(intent);

					return true;
				}else
				{
					view.loadUrl(url);
				}
				return true;
			}
		});
        

		wv.loadUrl(url);
	}
	
	//判断是否安装支付宝app  
    public static boolean checkAliPayInstalled(Context context) {  
  
        Uri uri = Uri.parse("alipays://platformapi/startApp");  
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());  
        return componentName != null;  
    } 
}
