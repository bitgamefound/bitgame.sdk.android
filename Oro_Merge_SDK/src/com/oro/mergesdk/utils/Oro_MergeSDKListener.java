package com.oro.mergesdk.utils;

import java.util.Map;

import com.oro.mergesdk.bean.Oro_UserInfo;

public interface Oro_MergeSDKListener {

	/**
	 * 用户操作回调接口
	 * @param flag	操作类型（例如：登录成功、登录失败等）
	 * @param userInfo（用户详细信息）
	 */
	public void onCallBack(int flag, Oro_UserInfo userInfo);

//	public void onCallBack(int flag,Map<String,String> map);
	
}
