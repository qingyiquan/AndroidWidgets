package com.wxq.samples;

import com.wxq.widgets.R;
import com.wxq.widgets.SwipeView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.list);
		String [] data = new String []{
			"item1","item2","item3","item4","item5","item6","item7","item8"
			,"item9","item10","item11","item12","item13"
		};
		ListAdapter adapter = new SwipeListViewAdaper(this, R.layout.listview_item, R.id.frontView, data);
		listView.setAdapter(adapter);
	}
	
	class SwipeListViewAdaper extends ArrayAdapter<String>
	{
		public SwipeListViewAdaper(Context context, int resource,
				int textViewResourceId, String[] objects) {
			super(context, resource, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			SwipeView reuseView = (SwipeView) super.getView(position, convertView, parent);
			reuseView.closeFront();
			return reuseView;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
