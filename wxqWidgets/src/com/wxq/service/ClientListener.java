package com.wxq.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;

public interface ClientListener extends ServiceConnection {
	
	public void handleMessageFromService(Message msg);
	
	public static class ClientListenerFactory{
		public static ClientListener defautInstance;
		
		public static ClientListener getDefaultInstance() {
			if (defautInstance == null) {
				defautInstance = new ClientListener() {
					
					@Override
					public void onServiceDisconnected(ComponentName name) {

					}
					
					@Override
					public void onServiceConnected(ComponentName name, IBinder service) {

					}
					
					@Override
					public void handleMessageFromService(Message msg) {

					}
				};
			}
			return defautInstance;
		}
	}
}
