package com.oro.mergesdk.bean;

import android.content.Context;

public class Oro_MergePayParameter {

	private String mergeOrderNo;
	private String accesstoken;
	private String gameOrderNo;
	private String userId;
	private String Amount;
	private String productId;
	private String productdata;
	private String productname;
	private String productdescribe;
	private int rechargeratio;
	private int serverCode;
	private String serverName;
	private String roleid;
	private String rolename;
	private int rolebalance;
	private int roleLevel;
	private int roleVip;
	private Context context;
	
	/**
	 * MergeOrderNo
	 * @return
	 */
	public String getMergeOrderNo() {
		return mergeOrderNo;
	}
	/**
	 * MergeOrderNo
	 * @param accessToken 登录成功时会得到token
	 */
	public void setMergeOrderNo(String mergeOrderNo) {
		this.mergeOrderNo = mergeOrderNo;
	}
	
	/**
	 * 获取token
	 * @return
	 */
	public String getAccessToken() {
		return accesstoken;
	}
	/**
	 * 设置token
	 * @param accessToken 登录成功时会得到token
	 */
	public void setAccessToken(String accessToken) {
		this.accesstoken = accessToken;
	}
	/**
	 * 获取CP订单号
	 * @return
	 */
	public String getGameOrderNo() {
		return gameOrderNo;
	}
	/**
	 * 设置CP订单号
	 * @param gameOrderNo 玩家点击付款时CP生成的订单号
	 */
	public void setGameOrderNo(String gameOrderNo) {
		this.gameOrderNo = gameOrderNo;
	}
	/**
	 * 获取用户id
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置用户id
	 * @param userId 登录成功后会得到userid
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取订单金额
	 * @return
	 */
	public String getAmount() {
		return Amount;
	}
	/**
	 * 设置订单金额
	 * @param Amount 订单金额正整数最小1
	 */
	public void setAmount(String Amount) {
		this.Amount = Amount;
	}
	/**
	 * 获取商品id
	 * @return
	 */
	public String getProductId() {
		return productId;
	}
	/**
	 * 设置商品id
	 * @param productId 每一个计费当的商品编号
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**
	 * 获取扩展参数
	 * @return
	 */
	public String getProductData() {
		return productdata;
	}
	/**
	 * 设置扩展参数
	 * @param productdata 透传参数，会在调用发点接口是回传给cp，注:不要使用特殊符号和中文
	 */
	public void setProductData(String productdata) {
		this.productdata = productdata;
	}
	/**
	 * 获取商品名称
	 * @return
	 */
	public String getProductName() {
		return productname;
	}
	/**
	 * 设置商品名称
	 * @param productname 商品名称 例如:元宝，钻石等
	 */
	public void setProductName(String productname) {
		this.productname = productname;
	}
	/**
	 * 获取商品描述
	 * @return
	 */
	public String getProductDescribe() {
		return productdescribe;
	}
	/**
	 * 设置商品描述
	 * @param productdescribe 商品描述 例如:60元宝，120钻石等
	 */
	public void setProductDescribe(String productdescribe) {
		this.productdescribe = productdescribe;
	}
	/**
	 * 获取兑换比例
	 * @return
	 */
	public int getRechargeRatio() {
		return rechargeratio;
	}
	/**
	 * 设置兑换比例
	 * @param productdescribe 充值比例 例如:6元=60元宝 rechargeratio=10，6元=120钻石 rechargeratio=5
	 */
	public void setRechargeRatio(int rechargeratio) {
		this.rechargeratio = rechargeratio;
	}
	
	/**
	 * 获取服务器编号
	 * @return
	 */
	public int getServerCode() {
		return serverCode;
	}
	/**
	 * 设置服务器编号
	 * @param serverCode 服务器标示要求纯数字，不能包含英文或者中文或符号
	 */
	public void setServerCode(int serverCode) {
		this.serverCode = serverCode;
	}
	/**
	 * 获取服务器名称
	 * @return
	 */
	public String getServerName() {
		return serverName;
	}
	/**
	 * 设置服务器名称
	 * @param serverName 显示给玩家的服务器名称
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	/**
	 * 获取角色id
	 * @return
	 */
	public String getRoleId() {
		return roleid;
	}
	/**
	 * 设置角色id
	 * @param roleid 触发充值的角色在CP方的唯一标示
	 */
	public void setRoleId(String roleid) {
		this.roleid = roleid;
	}
	/**
	 * 获取角色名
	 * @return
	 */
	public String getRoleName() {
		return rolename;
	}
	/**
	 * 设置角色id
	 * @param roleid 触发充值的角色名
	 */
	public void setRoleName(String rolename) {
		this.rolename = rolename;
	}

	/**
	 * 获取角色剩余点数
	 * @return
	 */
	public int getRoleBalance() {
		return rolebalance;
	}
	/**
	 * 设置角色剩余点数
	 * @param rolebalance 剩余元宝或者钻石等的数量 数字值
	 */
	public void setRoleBalance(int rolebalance) {
		this.rolebalance = rolebalance;
	}
	/**
	 * 获取角色等级
	 * @return
	 */
	public int getRoleLevel() {
		return roleLevel;
	}
	/**
	 * 设置角色人物等级
	 * @param roleLevel
	 */
	public void setRoleLevel(int roleLevel) {
		this.roleLevel = roleLevel;
	}
	/**
	 * 获取角色vip等级
	 * @return
	 */
	public int getRoleVip() {
		return roleVip;
	}
	/**
	 * 设置角色vip等级
	 * @param roleVip 无等级为0
	 */
	public void setRoleVip(int roleVip) {
		this.roleVip = roleVip;
	}
	/**
	 * 获取当前context
	 * @return
	 */
	public Context getcontext() {
		return context;
	}
	/**
	 * 设置当前的context
	 * @param context
	 */
	public void setcontext(Context context) {
		this.context = context;
	}
}
