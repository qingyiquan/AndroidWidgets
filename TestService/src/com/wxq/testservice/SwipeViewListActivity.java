package com.wxq.testservice;

import com.wxq.widgets.SwipeView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SwipeViewListActivity extends Activity {

	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipeview_list);
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
			// 重置
			SwipeView reuseView = (SwipeView) super.getView(position, convertView, parent);
			Integer tag = (Integer) reuseView.getTag(R.id.list_item_position_key);
			if (tag == null || position != tag.intValue()) {
				reuseView.setTag(R.id.list_item_position_key, Integer.valueOf(position));
				reuseView.closeFront();
			}
			return reuseView;
		}
	}
}
