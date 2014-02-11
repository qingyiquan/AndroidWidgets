package com.wxq.testservice;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
		
	Button startBtn;
	Button stopBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startBtn = (Button) findViewById(R.id.btn_start);
		stopBtn = (Button) findViewById(R.id.btn_stop);
		
		startBtn.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//启动
				Intent intent = new Intent(MainActivity.this, CountService.class);
				startService(intent);
			}
		});
		
		stopBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 结束服务
				Intent intent = new Intent(MainActivity.this, CountService.class);
				if (stopService(intent)) {
					Log.d("wxq", "手动终止服务成功!");
				} else {
					Log.d("wxq", "手动终止服务失败!");
				}
			}
		});
	}
}
