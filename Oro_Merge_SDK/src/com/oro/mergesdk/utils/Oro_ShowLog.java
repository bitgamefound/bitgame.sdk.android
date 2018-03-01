package com.oro.mergesdk.utils;

import android.util.Log;

public class Oro_ShowLog {
	public static final int Verbose = 1;
	public static final int Debug = 2;
	public static final int Info = 3;
	public static final int Warn = 4;
	public static final int Error = 5;
	public static final int Assert = 6;
	
	public static void ShowLog(int logType,String tag,String msg){
		switch (logType) {
		case Verbose:
			Log.v(tag, msg);
			break;
		case Debug:
			Log.d(tag, msg);
			break;
		case Info:
			Log.i(tag, msg);
			break;
		case Warn:
			Log.w(tag, msg);
			break;
		case Error:
			Log.e(tag, msg);
			break;
		default:
			break;
		}
	}
}
