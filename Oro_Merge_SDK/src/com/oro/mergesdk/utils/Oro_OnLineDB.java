package com.oro.mergesdk.utils;


import android.provider.BaseColumns;

public final class Oro_OnLineDB {

	public static final class OnLineInfo implements BaseColumns {
		/**
		 * 用户名
		 */
		public static final String USERNAME = "username";
		/**
		 * 用户密码
		 */
		public static final String USERID = "userid";
		/**
		 * 是否保存密码
		 */
		public static final String ONLINETIME = "onlinetime";
		/**
		 * 账户类型  0为正式账户；1为游客账户
		 */
		public static final String DATETIME = "datetime";
		public static String getUsername() {
			return USERNAME;
		}
		public static String getUserid() {
			return USERID;
		}
		public static String getOnlineTime() {
			return ONLINETIME;
		}
		public static String getDatetime() {
			return DATETIME;
		}
		
		
	}

}
