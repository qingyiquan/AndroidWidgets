package com.wxq.testclient;

import com.wxq.service.*;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.app.Activity;
import android.content.ComponentName;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Button bindBtn;
	Button unbindBtn;
	Button startCountBtn;
	Button stopCountBtn;

	ClientListener serviceConnection = new ClientListener() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// 取消-不一定被调用
			bindBtn.setEnabled(true);
			unbindBtn.setEnabled(false);
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//开始	
			bindBtn.setEnabled(false);
			unbindBtn.setEnabled(true);
		}
		
		@Override
		public void handleMessageFromService(Message msg) {
			// 处理服务器的消息
			switch (msg.what) {
			case 0:
				Log.d("wxq", "客户端收到数字:"+msg.arg1);
				break;

			default:
				break;
			}
		}
	};
	
	MessengerClient client = new MessengerClient(this, serviceConnection);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bindBtn = (Button) findViewById(R.id.btn_bind);
		unbindBtn = (Button) findViewById(R.id.btn_unbind);
		startCountBtn = (Button) findViewById(R.id.btn_start_count);
		stopCountBtn = (Button) findViewById(R.id.btn_stop_count);
		
		unbindBtn.setEnabled(false);
		
		bindBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//bind
				bindBtn.setEnabled(false);
				unbindBtn.setEnabled(true);
				client.bindService("com.wxq.testservice.CountService");
			}
		});
		
		unbindBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 
				bindBtn.setEnabled(true);
				unbindBtn.setEnabled(false);
				unbindService();
			}
		});
		
		startCountBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 开始线程
				startCount();
			}
		});
		
		stopCountBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 停止线程
				stopCount();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		unbindService();
		super.onDestroy();
	}
	
	public void unbindService() {
		client.unBindService();
	}
	public void startCount() {
		Message msg = Message.obtain(null, 0);
		client.sendToService(msg);
	}
	
	public void stopCount() {
		Message msg = Message.obtain(null, 1);
		client.sendToService(msg);
	}
}
