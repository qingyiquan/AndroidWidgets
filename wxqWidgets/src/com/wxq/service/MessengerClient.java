package com.wxq.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessengerClient {
	
	Context mContext;
	ClientListener userConnection = new BaseClientListener();

	public MessengerClient(Context mContext,ClientListener connection) {
		super();
		this.mContext = mContext;
		
		if (this.userConnection != null) {
			this.userConnection = connection;
		}
	}

	Handler handler = new Handler(Looper.getMainLooper()){

		@Override
		public void handleMessage(Message msg) {
			// 处理服务器的消息
			handleMessageFromService(msg);
			super.handleMessage(msg);
		}
		
	};
	Messenger clientMessenger = new Messenger(handler);
	boolean binded = false;
	Messenger serviceMessenger;
	
	ServiceConnection serviceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// 取消-不一定被调用
			Log.d("wxq", "client:异常解绑!");
			serviceMessenger = null;
			binded = false;
			userConnection.onServiceDisconnected(name);
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//开始
			Log.d("wxq", "client:绑定成功!");
			binded = true;
			serviceMessenger = new Messenger(service);	
			registerToService();
			userConnection.onServiceConnected(name, service);
		}
	};
	
	public void handleMessageFromService(Message msg) {
		userConnection.handleMessageFromService(msg);
	}
	
	public boolean isBinded() {
		return binded;
	}
	
	public void bindService(String serviceAction) {
		if (!binded && mContext != null) {
			mContext.bindService(new Intent(serviceAction), serviceConnection, Context.BIND_AUTO_CREATE);
		}
	}
	
	public void unBindService() {
		if (binded && mContext != null) {
			unRigisterToService();
			
			mContext.unbindService(serviceConnection);
			serviceMessenger = null;
			binded = false;
		}
	}
	
	public void sendToService(Message message) {
		if (serviceMessenger!=null) {
			try {
				serviceMessenger.send(message);
			} catch (RemoteException e) {
				// 打印异常
				e.printStackTrace();
			}
		}
		else {
			Log.d("wxq", "can not send message:unbind state");
		}
	}
	
	private void registerToService() {
		Message registerMessage = Message.obtain(null, ServiceConsts.REGISTER);
		registerMessage.replyTo = clientMessenger;
		sendToService(registerMessage);
	}
	
	private void unRigisterToService() {
		if (binded) {
			Message unRigisterMessage = Message.obtain(null, ServiceConsts.UNREGISTER);
			unRigisterMessage.replyTo = clientMessenger;
			sendToService(unRigisterMessage);
		}
	}
}
