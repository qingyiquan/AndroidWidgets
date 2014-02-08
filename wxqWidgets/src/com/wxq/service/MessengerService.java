package com.wxq.service;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessengerService extends Service {

	//registered client messengers
	ArrayList<Messenger> clientMessengers = new ArrayList<Messenger>();
	//handle message
	Handler handler = new Handler(Looper.getMainLooper()){

		@Override
		public void handleMessage(Message msg) {
			// 处理事件
			
			switch (msg.what) {
			
			case ServiceConsts.REGISTER://注册
				Log.d("wxq", "服务器接收到注册消息");
				clientMessengers.add(msg.replyTo);
				break;
				
			case ServiceConsts.UNREGISTER:
				Log.d("wxq", "服务器接收到注销信息");
				clientMessengers.remove(msg.replyTo);
				break;
				
			default:
				super.handleMessage(msg);
				break;
			}
			//subclass handle message
			handleMessageFromClient(msg);
		}
		
	};
	// messenger for service use
	Messenger messenger = new Messenger(handler);
	
	@Override
	public IBinder onBind(Intent intent) {
		// use handler deal with message
		return messenger.getBinder();
	}
	
	@Override
	public void onDestroy() {
		// clear registered  client
		clientMessengers.clear();
		super.onDestroy();
		Log.d("wxq", "MessengerSerice destroy");
	}
	
	public void handleMessageFromClient(Message msg) {
		
	}
	
	public void updateToClients(Message message) {
		for (Messenger tempMessenger : clientMessengers) {
			try {
				tempMessenger.send(message);
			} catch (RemoteException e) {
				// remove this client
				clientMessengers.remove(tempMessenger);
			}
		}
	}
}
