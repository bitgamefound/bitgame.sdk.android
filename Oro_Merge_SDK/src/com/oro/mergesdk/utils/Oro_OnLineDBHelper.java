package com.oro.mergesdk.utils;

import com.oro.mergesdk.utils.Oro_OnLineDB.OnLineInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 操作数据库辅助类
 */
public class Oro_OnLineDBHelper {
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "zytx_useronline.db";
	public static final String online_TABLE_NAME = "online_table";
	private static Oro_OnLineDBHelper instance;
	private SQLiteDatabase db;
	private DBOpenHelper dbOpenHelper;

	private Oro_OnLineDBHelper(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
		establishDb();
	}

	public static Oro_OnLineDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new Oro_OnLineDBHelper(context);
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
	 * 查询是否存在登录账号
	 * @return 存在，返回true；不存在，返回false。
	 */
	public Boolean HasOnlineUsers()
	{
		Cursor cursor =	db.query(online_TABLE_NAME, null, null, null, null, null, null);
		if(cursor.getCount() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * 添加用户，如果存在，则根据用户名更新在线时间
	 * @param userName 用户名
	 * @param userid 用户ID
	 * @param onlinetime 在线时间
	 * @param datetime 当前时间
	 * @return 行号，大于0成功，小于0失败
	 */
	
	public long AddUserOnline(String userName,String userId,int onlineTime)
	{
		if(this.HasOnlineUser(userName))
		{
			String whereClause = OnLineInfo.USERNAME +"=?"; 
			String[] whereArgs = new String[]{userName};
			ContentValues values = new ContentValues();
			values.put(OnLineInfo.ONLINETIME,onlineTime);//key为字段名，value为值
			values.put(OnLineInfo.DATETIME,System.currentTimeMillis());//key为字段名，value为值
			int ia=db.update(online_TABLE_NAME, values, whereClause, whereArgs);
			return ia;
		}
		else
		{
			ContentValues values = new ContentValues();
			values.put(OnLineInfo.USERNAME, userName);
			values.put(OnLineInfo.USERID, userId);
			values.put(OnLineInfo.ONLINETIME, onlineTime);
			values.put(OnLineInfo.DATETIME, System.currentTimeMillis());
			long ib=db.insert(online_TABLE_NAME, null, values);
			return ib;
		}
	}
	
	/**
	 * 查询是否存在此用户
	 * @param userName 用户名
	 * @return 存在，返回true；不存在，返回false。
	 */
	public Boolean HasOnlineUser(String userName)
	{
		String selection = OnLineInfo.USERNAME + "=?";
		String[] selectionArgs = new String[]{userName};
		Cursor cursor =	db.query(online_TABLE_NAME, null, selection, selectionArgs, null, null, null);
		if(cursor.getCount() > 0)
			return true;
		return false;
	}
		
	/**
	 * 获取最后一个登录用户
	 * @return 用户名、密码、是否保存密码(0表示记住密码，1表示不记住密码)、是否游客(0表示正式用户；1表示游客用户)
	 */
	public String[] GetUserOnlineinfo(String userName)
	{
		String sql = "SELECT * FROM " + online_TABLE_NAME + " WHERE " + OnLineInfo.USERNAME + " = '"+userName+"'";

		Cursor cursor =	db.rawQuery(sql,null);
		String[] userInfo = new String[2];
		if(cursor.getCount() == 1)
		{
			cursor.moveToFirst();
			userInfo[0] = cursor.getString(cursor.getColumnIndex(OnLineInfo.ONLINETIME));
			userInfo[1] = cursor.getString(cursor.getColumnIndex(OnLineInfo.DATETIME));
		}
		else {
			userInfo= null;
		}
		return userInfo;
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
			db.execSQL("create table " + online_TABLE_NAME 
					+ " (ID integer primary key," 
					+ OnLineInfo.USERNAME + " text, "
					+ OnLineInfo.USERID + " long, " 
					+ OnLineInfo.ONLINETIME + " INTEGER, " 
					+ OnLineInfo.DATETIME + " long)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + online_TABLE_NAME);
			onCreate(db);
		}

	}

}