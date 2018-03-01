package com.oro.mergesdk.result;

/**
 * 登录后，服务端返回的实体Data
 * 
 * @author Administrator
 * 
 */
public class Oro_MergeSDKResultData {
	private String code;
	private String accessToken;
	private String orderNo;

	private String state;
	private String tmpUserName;

	private int codeExpire;
	private String msgCode;
	private String userId;
	private String channelData;
	
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getChannelData() {
		return channelData;
	}
	public void setChannelData(String channelData) {
		this.channelData = channelData;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTmpUserName() {
		return tmpUserName;
	}
	public void setTmpUserName(String tmpUserName) {
		this.tmpUserName = tmpUserName;
	}
	public int getCodeExpire() {
		return codeExpire;
	}
	public void setCodeExpire(int codeExpire) {
		this.codeExpire = codeExpire;
	}
	public String getMsgCode() {
		return msgCode;
	}
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}


	
}
