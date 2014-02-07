package com.wxq.samples;

import com.wxq.widgets.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	Button swipeViewListBtn;
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
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
