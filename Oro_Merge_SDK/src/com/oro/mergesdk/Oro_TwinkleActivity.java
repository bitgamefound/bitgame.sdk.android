package com.oro.mergesdk;

import com.oro.mergesdk.utils.Oro_generic;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Oro_TwinkleActivity extends Oro_SdkActionBase{
	ImageView logoImage;
	private String TAG = "merge_sdk";
	int[] resIds;
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        // 取消标题  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        // 取消状态栏  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //加载界面
        setContentView(Oro_generic.getIdByName(Oro_TwinkleActivity.this, "layout", "oro_tminkle_activity"));
        logoImage = (ImageView) this.findViewById(Oro_generic.getIdByName(Oro_TwinkleActivity.this, "id", "zytx_twinkle_iv"));
        //读取配置文件内闪屏图列表/res/values/zytx_array zytx_twinkle_images
        TypedArray ar = getResources().obtainTypedArray(Oro_generic.getIdByName(Oro_TwinkleActivity.this, "array", "zytx_twinkle_images")); 
        
        if(ar.length() == 0){
        	//列表为空直接结束
    		TwinkleShowEnd();
    	}else{
    		//列表有内容记录 创建int数组来保存资源id
    		resIds = new int[ar.length()];     
    		for (int i = 0; i < ar.length(); i++){
    			resIds[i] = ar.getResourceId(i, 0);
    		}
    		ar.recycle();
        //创建线程 来更新imageview
        new Thread(new Runnable()
    		{   
    		    public void run()
    		    {
    		    	Message msg = new Message();
		    		
    		    	for(int i=0;i<resIds.length;i++)
    		    	{
    		    		//传入资源id值
		    			msg = handler.obtainMessage();  
		    			msg.what = resIds[i];
    		    		handler.sendMessage(msg);//告诉主线程执行任务
        		        try {
        		        	//更新之后延迟1.5秒之后再循环
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

    		    	}
	    			//循环结束通知子类 闪屏结束状态
	    			msg = handler.obtainMessage(); 
    		    	msg.what=-1;
    		    	msg.obj=null;
    		    	handler.sendMessage(msg);
    		    } 
    		}).start();
        }
    }  
	Handler handler = new Handler()
	  {
		public void handleMessage(Message msg) {
        	if(msg.what >0){
        		//读取资源id更新图片
        		logoImage.setImageDrawable(getResources().getDrawable(msg.what));
        		//开始执行动画 读取res/anim/zytx_twinkle 渐变动画效果
        		Animation animation = AnimationUtils.loadAnimation(Oro_TwinkleActivity.this, Oro_generic.getIdByName(Oro_TwinkleActivity.this, "anim", "zytx_twinkle"));
        		logoImage.startAnimation(animation);  
            }
        	else if(msg.what == -1)
        	{
        		//循环结束通知子类 闪屏调用完毕
        		TwinkleShowEnd();
        	}
		};
	  };
  
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;  
    }
    public void TwinkleShowEnd(){}
}
