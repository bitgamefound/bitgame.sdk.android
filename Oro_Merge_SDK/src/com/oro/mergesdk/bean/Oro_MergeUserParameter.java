package com.oro.mergesdk.bean;

import android.content.Context;

/**
 * 上传用户信息参数实体类
 * @author fuwei
 *
 */
public class Oro_MergeUserParameter {
	private String UserID;//帐号id
	private String roleId;//角色id
	private String roleName;//角色名称
	private int roleLevel;//角色等级
	private int servercode;//服务器id
	private String servername;//服务器名称
	private int balance=0;//帐号余额
	private int vip;//vip等级
	private String rolepower="";//角色战力
	private String partyid="0";//工会id
	private String partyName="工会";//工会名称
	private String rolecreateTime;//角色创建时间
	private String roleLevelMTime="0";//角色当前等级在线时间
	int subtype;//提交类型
	Context context;//当前context
	
	public String getUserID() {
		return UserID;
	}
	/**
	 * userid
	 * @param UserID
	 */
	public void setUserID(String UserID) {
		this.UserID = UserID;
	}
	
	public String getRoleId() {
		return roleId;
	}
	/**
	 * 角色标示
	 * @param roleId
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public String getRoleName() {
		return roleName;
	}
	/**
	 * 角色名称
	 * @param roleName
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public int getRoleLevel() {
		return roleLevel;
	}
	/**
	 * 角色等级
	 * @param roleLevel
	 */
	public void setRoleLevel(int roleLevel) {
		this.roleLevel = roleLevel;
	}
	
	public int getServerCode() {
		return servercode;
	}
	/**
	 * 服务器编号 数字 不能包含英文中文或符号
	 * @param servercode
	 */
	public void setServerCode(int servercode) {
		this.servercode = servercode;
	}
	
	public String getServerName() {
		return servername;
	}
	/**
	 * 服务器名
	 * @param servername
	 */
	public void setServerName(String servername) {
		this.servername = servername;
	}
	
	public int getbalance() {
		return balance;
	}
	/**
	 * 账户余额
	 * @param balance 非必填
	 */
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public int getvip() {
		return vip;
	}
	/**
	 * 角色vip等级
	 * @param vip
	 */
	public void setVip(int vip) {
		this.vip = vip;
	}
	
	public String getRolePower() {
		return rolepower;
	}
	/**
	 * 角色战力 非必填
	 * @param rolepower
	 */
	public void setRolePower(String rolepower) {
		this.rolepower = rolepower;
	}
	
	public String getPartyId() {
		return partyid;
	}
	/**
	 * 工会编号 非必填
	 * @param partyid
	 */
	public void setPartyId(String partyid) {
		this.partyid = partyid;
	}
	
	public String getPartyName() {
		return partyName;
	}
	/**
	 * 工会名称 非必填
	 * @param partyName
	 */
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	
	public String getRoleCreateTime() {
		return rolecreateTime;
	}
	/**
	 * 角色创建时间
	 * @param rolecreateTime
	 */
	public void setRoleCreateTime(String rolecreateTime) {
		this.rolecreateTime = rolecreateTime;
	}
	
	public String getRoleLevelMTime() {
		return roleLevelMTime;
	}
	/**
	 * 当前等级在线时间 非必填
	 * @param roleLevelMTime
	 */
	public void setRoleLevelMTime(String roleLevelMTime) {
		this.roleLevelMTime = roleLevelMTime;
	}
	
	public int getSubType() {
		return subtype;
	}
	/**
	 * 
	 * @param subtype 
	 * 1 创建角色
	 * 2 登录角色
	 * 3 角色升级
	 * 4 选择服务器
	 * 5 角色退出
	 */
	public void setSubType(int subtype) {
		this.subtype = subtype;
	}
	
	public Context getContext() {
		return context;
	}
	/**
	 * 当前context
	 * @param context
	 */
	public void setContext(Context context) {
		this.context = context;
	}
}
