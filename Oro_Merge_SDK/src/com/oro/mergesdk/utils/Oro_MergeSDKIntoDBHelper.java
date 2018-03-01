package com.oro.mergesdk.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

/**
 * 操作数据库辅助类
 */
public class Oro_MergeSDKIntoDBHelper {
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "oro_mergesdk.db";
	public static final String MergeSDK_INIT_TABLE_NAME = "mergesdk_init_table";
	public static final String UserFirstLogin_TABLE_NAME = "userfirstlogin_table";
	public static final String IsFirstInit = "isfirstinit";
	public static final String IsRoleFirstLogin = "isrolefirstlogin";
	public static final String UserMarking = "usermarking";
	
	
	private static Oro_MergeSDKIntoDBHelper instance;
	private SQLiteDatabase db;
	private DBOpenHelper dbOpenHelper;
	private String TAG="MergeSDK";
	private Oro_MergeSDKIntoDBHelper(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
		establishDb();
	}

	public static Oro_MergeSDKIntoDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new Oro_MergeSDKIntoDBHelper(context);
		}
		return instance;
	}
	/**
	 * 打开数据库
	 */
	private void establishDb() {
		if (this.db == null) {
			this.db = this.dbOpenHelper.getWritableDatabase();
		}
	}

	/**
	 * 查询是否存在SDK信息
	 * @return 存在，返回true；不存在，返回false。
	 */
	public Boolean HasMergeSDKInit()
	{
		Cursor cursor =	db.query(MergeSDK_INIT_TABLE_NAME, null, null, null, null, null, null);
		if(cursor.getCount() > 0){
			return true;
		}else{
			AddMergeSDKInit();
			return false;
		}
	}
	
	private long AddMergeSDKInit()
	{
		//Boolean isrolefirstlogin
		ContentValues values = new ContentValues();
		values.put(IsFirstInit, 0);
		return db.insert(MergeSDK_INIT_TABLE_NAME, null, values);
	}
	
	
	public Boolean isrolefirstlogin(String usermarking)
	{
		String sql = "SELECT isrolefirstlogin FROM "+UserFirstLogin_TABLE_NAME+" WHERE "+UserMarking+"='"+usermarking+"'";
		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		if(count>0){
//			String isrolefirstlogin = "";
//			cursor.moveToFirst();
//			isrolefirstlogin = cursor.getString(cursor.getColumnIndex(IsRoleFirstLogin));
//			if(isrolefirstlogin.equals("1")){
//				return true;
//			}
//			else{
//				return false;
//			}
			return false;
		}
		else{
			//无数据为首次调用，状态=帐号未登录过

			ContentValues values = new ContentValues();
			values.put(UserMarking, usermarking);
			values.put(IsRoleFirstLogin, 0);
			db.insert(UserFirstLogin_TABLE_NAME, null, values);
			return true;
		}
	}
	public Boolean isfirstinit()
	{
		String sql = "SELECT isfirstinit FROM "+MergeSDK_INIT_TABLE_NAME+" WHERE id=1";
		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		if(count>0){
			String isfirstinit = "";
			cursor.moveToFirst();
			isfirstinit = cursor.getString(cursor.getColumnIndex(IsFirstInit));
			if(isfirstinit.equals("1")){
				return true;
			}
			else{
				return false;
			}
		}
		else{

			AddMergeSDKInit();
			return true;
		}
	}
	
	
	/**
	 * 关闭数据库
	 */
	public void cleanup() {
		if (this.db != null) {
			this.db.close();
			this.db = null;
		}
	}

	/**
	 * 数据库辅助类
	 */
	private static class DBOpenHelper extends SQLiteOpenHelper {

		public DBOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// db.execSQL("create table " + USER_TABLE_NAME + " (" + User._ID
			// + " integer primary key," + User.USERNAME + " text, "
			// + User.PASSWORD + " text, " + User.ISSAVED + " INTEGER)");
			db.execSQL("create table IF NOT EXISTS " + MergeSDK_INIT_TABLE_NAME + " (id integer primary key,"+IsFirstInit+" varchar)");
			db.execSQL("create table IF NOT EXISTS " + UserFirstLogin_TABLE_NAME + " (id integer primary key,"+UserMarking+" varchar, "+IsRoleFirstLogin+" varchar)");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + MergeSDK_INIT_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + UserFirstLogin_TABLE_NAME);
			onCreate(db);
		}

	}

}