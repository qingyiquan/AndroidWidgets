package com.wxq.testservice;

import com.wxq.service.*;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

public class CountService extends  MessengerService{
	
	int counter = 0;
	Runnable countRunnable = new Runnable() {
		
		@Override
		public void run() {
			//加1并且通知所有的监听器
			try {
				while(true)
				{
					Thread.sleep(1000);
					counter++;	
					// 依次调用
					Log.d("wxq", "num="+counter);
					if (counter%5==0) {
						Message message = Message.obtain(null, 0,counter,0);
						updateToClients(message);
					}
				}
			} catch (InterruptedException e) {
				// 提示
				Log.d("wxq", "计数线程被打断!");
			}
			finally{
				Log.d("wxq", "完成并退出计数线程!");
			}
		}
	};
	Thread countThread = new Thread(countRunnable);

	@Override
	public void handleMessageFromClient(Message msg) {
		super.handleMessageFromClient(msg);
		// 处理事件
		
		switch (msg.what) {
		case 0://
			Log.d("wxq", "服务器端接收到启动线程消息："+msg.what);
			startCount();
			break;

		case 1:
			Log.d("wxq", "服务器端接收到终止线程消息："+msg.what);
			stopCount();
			break;

		default:
			break;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//
		startCount();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		stopCount();
		super.onDestroy();
	}
	
	public void startCount() {
		if (! countThread.isAlive()) {
			Log.d("wxq", "开启计数线程");
			counter = 0;
			countThread.start();
		}
		else {
			Log.d("wxq", "计数线程已经开始，无需再次启动");
		}
	}
	
	public void stopCount() {
		if (countThread.isAlive()) {
			countThread.interrupt();
			countThread = new Thread(countRunnable);
		}
	}
}
