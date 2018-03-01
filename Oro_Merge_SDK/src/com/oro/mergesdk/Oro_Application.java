package com.oro.mergesdk;

import com.oro.mergesdk.utils.Oro_ShowLog;
import android.app.Application;
import android.content.Context;

public class Oro_Application extends Application{

	private static Oro_Application mergeapplication;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		getInstance();
		//059C30BC9ED749239D5AB2A5D15F3C87 深圳瞻宇 TalkingData appid
		//TalkingDataAppCpa.init(this, "059C30BC9ED749239D5AB2A5D15F3C87", "GooglePlay");
		Oro_ShowLog.ShowLog(Oro_ShowLog.Verbose,"MergeSDK","MergeSDK Oro_Application onCreate");
	}
	public static Oro_Application getInstance() {
		if (mergeapplication == null) {
			mergeapplication = new Oro_Application();
		}
		return mergeapplication;
	}
}
