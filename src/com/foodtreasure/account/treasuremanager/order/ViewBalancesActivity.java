package com.foodtreasure.account.treasuremanager.order;

import java.util.ArrayList;

import com.foodtreasure.account.treasuremanager.R;
import com.foodtreasure.account.treasuremanager.db.Customer;
import com.foodtreasure.account.treasuremanager.db.DatabaseUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewBalancesActivity extends Activity {

	private DatabaseUtils dbUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_customer_balance);
		
		dbUtils = DatabaseUtils.getInstance();
		ArrayList<Customer> customerArray = dbUtils.getAllCustomers();
		
		ListView lv = (ListView) findViewById(R.id.id_customer_list);
		CustomerAdapter adapter = new CustomerAdapter(customerArray);
		lv.setAdapter(adapter);	
	}
	
	private class ViewHolder {
		TextView name;
		TextView phone;
		TextView balance;
	}

	class CustomerAdapter extends BaseAdapter {
		
		private ArrayList<Customer> customerArray;
		
		CustomerAdapter(ArrayList<Customer> customerArray) {
			this.customerArray = customerArray;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return customerArray.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return customerArray.get(position);
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
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_customer_balance_list_item, parent, false);
				
				holder.name = (TextView) convertView.findViewById(R.id.id_list_item_name);
				holder.phone = (TextView) convertView.findViewById(R.id.id_list_item_phone);
				holder.balance = (TextView) convertView.findViewById(R.id.id_list_item_balance);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.name.setText(customerArray.get(position).getName());
			holder.phone.setText(customerArray.get(position).getPhone());
			holder.balance.setText("$" + customerArray.get(position).getBalance());
			
			return convertView;
		}
		
	}
}
