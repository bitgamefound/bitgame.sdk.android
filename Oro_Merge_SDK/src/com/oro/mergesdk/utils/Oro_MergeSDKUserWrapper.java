package com.oro.mergesdk.utils;

public class Oro_MergeSDKUserWrapper {
	public static final int ACTION_RET_INIT_SUCCESS = 1;// 初始化SDK成功回调
	public static final int ACTION_RET_INIT_FAIL = 2;// 初始化SDK失败回调

	public static final int ACTION_RET_LOGIN_SUCCESS = 3;// 登录成功回调
	public static final int ACTION_RET_LOGIN_TIMEOUT = 4;// 登录超时回调
	public static final int ACTION_RET_LOGIN_NO_NEED = 5;// 无需登录回调
	public static final int ACTION_RET_LOGIN_CANCEL = 6;// 登录取消回调
	public static final int ACTION_RET_LOGIN_FAIL = 7;// 登录失败回调

	public static final int ACTION_RET_LOGOUT_SUCCESS = 8;// 登出成功回调
	public static final int ACTION_RET_LOGOUT_FAIL = 9;// 登出失败回调

	public static final int ACTION_RET_PLATFORM_ENTER = 10;// 平台中心进入回调
	public static final int ACTION_RET_PLATFORM_BACK = 11;// 平台中心退出回调

	public static final int ACTION_RET_PAUSE_PAGE = 12;// 暂停界面回调

	public static final int ACTION_RET_EXIT_PAGE = 13;// 退出游戏成功回调
	public static final int ACTION_RET_EXIT_FAIL = 131;// 退出游戏失败
	public static final int ACTION_RET_EXIT_NOEXIT = 132;// SDK没有推出界面启动游戏退出界面回调

	public static final int ACTION_RET_ANTIADDICTIONQUERY = 14;// 防沉迷查询回调

	public static final int ACTION_RET_REALNAMEREGISTER = 15;// 实名注册回调

	public static final int ACTION_RET_ACCOUNTSWITCH_SUCCESS = 16;// 切换账号成功回调

	public static final int ACTION_RET_ACCOUNTSWITCH_FAIL = 17;// 切换账号失败回调

	public static final int PAYRESULT_SUCCESS = 18;// 支付成功回调
	public static final int PAYRESULT_FAIL = 19;// 支付失败回调
	public static final int PAYRESULT_CANCEL = 20;// 支付取消回
	public static final int PAYRESULT_NETWORK_ERROR = 21;// 支付超时回调（网络错误）
	public static final int PAYRESULT_PRODUCTIONINFOR_INCOMPLETE = 22;// 支付超时回调（支付信息不完整）
	public static final int TWINKLE_SHOW_END = 23;//闪屏执行完毕
	/**
	 * 新增加:正在进行中回调 支付过程中若SDK没有回调结果，就认为支付正在进行中 游戏开发商可让玩家去判断是否需要等待，若不等待则进行下一次的支付
	 */
	public static final int PAYRESULT_INIT_FAIL = 23;
	public static final int BIND_BDPUSH_SUCESS = 24;//绑定百度消息推送成功回调
	public static final int BIND_BDPUSH_FAIL = 25;//绑定百度消息推送失败回调
	
	public static final int Anti_Addiction_Adult = 26;//防沉迷已成年 
	public static final int Anti_Addiction_NoAdult  = 27;//防沉迷未成年 
	public static final int Anti_Addiction_Normal = 28;//防沉正常游戏
	public static final int Anti_Addiction_IsAddiction  = 29;//防沉3小时沉迷游戏
	public static final int Anti_Addiction_Unhealthy   = 30;//防沉5小时不健康游戏 

	public static final int SPPay_WebView_Finish   = 31;//切换支付界面关闭
}
