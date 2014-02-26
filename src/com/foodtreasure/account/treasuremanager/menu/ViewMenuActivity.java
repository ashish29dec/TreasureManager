/**
 * 
 */
package com.foodtreasure.account.treasuremanager.menu;

import java.util.ArrayList;

import com.foodtreasure.account.treasuremanager.R;
import com.foodtreasure.account.treasuremanager.db.DatabaseUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author agupta
 *
 */
public class ViewMenuActivity extends Activity {

	private DatabaseUtils dbUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_menu);
		
		dbUtils = DatabaseUtils.getInstance();
		ArrayList<ArrayList<String>> menuArray = dbUtils.getMenu();
		
		ListView lv = (ListView) findViewById(R.id.id_menu_list);
		MenuAdapter adapter = new MenuAdapter(menuArray);
		lv.setAdapter(adapter);
	}
	
	private class ViewHolder {
		TextView dishName;
		TextView quantity;
		TextView price;
	}
	
	class MenuAdapter extends BaseAdapter {
		
		private ArrayList<ArrayList<String>> menuArray;
		
		MenuAdapter(ArrayList<ArrayList<String>> menuArray) {
			this.menuArray = menuArray;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return menuArray.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return menuArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = new ViewHolder();
			
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_menu_list_item, parent, false);
				
				holder.dishName = (TextView) convertView.findViewById(R.id.id_list_item_dish_name);
				holder.quantity = (TextView) convertView.findViewById(R.id.id_list_item_quantity);
				holder.price = (TextView) convertView.findViewById(R.id.id_list_item_price);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.dishName.setText(menuArray.get(position).get(0));
			holder.quantity.setText(menuArray.get(position).get(1));
			holder.price.setText(getString(R.string.str_dollar) + menuArray.get(position).get(2));
			
			return convertView;
		}
		
	}
}
