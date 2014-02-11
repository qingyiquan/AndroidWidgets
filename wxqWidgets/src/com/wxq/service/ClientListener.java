package com.wxq.service;

import android.content.ServiceConnection;
import android.os.Message;

public interface ClientListener extends ServiceConnection {
	
	public void handleMessageFromService(Message msg);
}
