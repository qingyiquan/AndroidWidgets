package com.wxq.samples;

import com.wxq.widgets.DialogMenu.DialogMenuListener;
import com.wxq.widgets.DialogMenu;
import com.wxq.widgets.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button swipeViewListBtn;
	Button dialogMenuBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_list);
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
							break;
						case R.id.menu_2:
							Toast.makeText(MainActivity.this, "点击了第2项", Toast.LENGTH_LONG).show();
							break;
						default:
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
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
