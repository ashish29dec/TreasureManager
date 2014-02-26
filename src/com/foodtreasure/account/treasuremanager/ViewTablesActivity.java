/**
 * 
 */
package com.foodtreasure.account.treasuremanager;

import java.util.ArrayList;

import com.foodtreasure.account.treasuremanager.db.DatabaseUtils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author agupta
 *
 */
public class ViewTablesActivity extends Activity {

	private DatabaseUtils dbUtils;
	private ArrayList<String> table_name_list;
	private TableAdapter listAdapter;
	
	private Context context;
	
	private OnItemSelectedListener listener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			Cursor c = dbUtils.getTableContents(table_name_list.get(position));
			if (listAdapter == null) {
				listAdapter = new TableAdapter(context, c);
				attachAdapterToList();
			} else {
				listAdapter.updateAdapter(c);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.context = this;
	
		dbUtils = DatabaseUtils.getInstance();
		
		setContentView(R.layout.view_table_contents);
		
		table_name_list = dbUtils.getTablesInDatabase();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, table_name_list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner table_name = (Spinner) findViewById(R.id.id_table_name);
		table_name.setAdapter(adapter);
		table_name.setOnItemSelectedListener(listener);
	}
	
	public void attachAdapterToList() {
		ListView lv = (ListView) findViewById(R.id.id_table_contents);
		lv.setAdapter(listAdapter);
	}
	
	class TableAdapter extends BaseAdapter {
		
		private Cursor c;
		private Context context;
		String[] col_names;

		public TableAdapter() {
			// TODO Auto-generated constructor stub
		}
		
		public TableAdapter(Context context, Cursor c) {
			this();
			this.c = c;
			this.context = context;
			fetchColNames();
		}
		
		@Override
		public int getCount() {
			return c.getCount();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.view_table_contents_item, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.id_table_content_item);
			StringBuffer text = new StringBuffer();
			c.moveToPosition(position);
			for (int i = 0; i < col_names.length; i++) {
				text.append(col_names[i]);
				text.append("= ");
				text.append(fetchData(i));
				text.append("\n");
			}
			tv.setText(text.toString());
			return convertView;
		}
		
		public void updateAdapter(Cursor c) {
			this.c = c;
			fetchColNames();
			notifyDataSetChanged();
		}
		
		public void fetchColNames() {
			col_names = c.getColumnNames();
		}
		
		public String fetchData(int index) {
			switch(c.getType(index)) {
			case Cursor.FIELD_TYPE_FLOAT:
				return String.valueOf(c.getDouble(index));
			case Cursor.FIELD_TYPE_INTEGER:
				return String.valueOf(c.getLong(index));
			default:
				return c.getString(index);
			}
		}
	}
}
