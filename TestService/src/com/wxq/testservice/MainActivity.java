package com.wxq.testservice;

import com.wxq.widgets.DialogMenu;
import com.wxq.widgets.DialogMenu.DialogMenuListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
		
	Button startBtn;
	Button stopBtn;
	
	Button swipeViewListBtn;
	Button dialogMenuBtn;

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
		
		swipeViewListBtn = (Button) findViewById(R.id.btn_swipe_view_list);
		swipeViewListBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 打开滑动列表
				startActivity(new Intent(MainActivity.this, SwipeViewListActivity.class));
			}
		});
		
		dialogMenuBtn = (Button) findViewById(R.id.btn_dialog_menu);
		dialogMenuBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 生成并创建菜单
				DialogMenuListener listener = new DialogMenuListener() {
					
					@Override
					public void onItemClick(DialogMenu dm, View v) {
						// 点击某个菜单项
						switch (v.getId()) {
						case R.id.menu_1:
							Toast.makeText(MainActivity.this, "点击了第1项", Toast.LENGTH_LONG).show();
							dm.dismiss();
							break;
						case R.id.menu_2:
							Toast.makeText(MainActivity.this, "点击了第2项", Toast.LENGTH_LONG).show();
							dm.dismiss();
							break;
						default:
							dm.dismiss();
							break;
						}
					}
					
					@Override
					public void onCreate(View parent) {
						// 在显示之前修改样式等
						
					}
				};
				DialogMenu dialogMenu = new DialogMenu(
						MainActivity.this, 
						null, 
						listener, 
						new int []{R.id.menu_1,R.id.menu_2}, 
						new int []{R.string.menu_1,R.string.menu_2});
				dialogMenu.show();
			}
		});
	}
}
