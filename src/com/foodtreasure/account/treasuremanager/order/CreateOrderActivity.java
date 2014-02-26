/**
 * 
 */
package com.foodtreasure.account.treasuremanager.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import com.foodtreasure.account.treasuremanager.R;
import com.foodtreasure.account.treasuremanager.db.Customer;
import com.foodtreasure.account.treasuremanager.db.DatabaseUtils;
import com.foodtreasure.account.treasuremanager.db.Menu;
import com.foodtreasure.account.treasuremanager.db.PlaceOrder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

/**
 * @author agupta
 *
 */
public class CreateOrderActivity extends Activity {

	private static final String TAG = "CreateOrderActivity";
	
	private static final String MAP_KEY_NAME = "name";
	private static final String MAP_KEY_PHONE = "phone";

	private ArrayList<View> sectionsInOrder;

	private ViewGroup dishContainer;
	private TextView totalPrice;
	private AutoCompleteTextView customerName;
	private AutoCompleteTextView customerPhone;
	private DatePicker date;

	private DatabaseUtils dbUtils;
	private ArrayList<String> dishesInMenu;
	private ArrayList<Customer> customers;
	
//	private SimpleDateFormat sdf;
//	
	private SimpleAdapter adapter;

	private long orderTimestamp;
	
	
	private OnItemClickListener clickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.i(TAG, "position: " + position);
			Log.i(TAG, "id: " + id);
			HashMap<String, String> map = (HashMap<String, String>) parent.getAdapter().getItem(position);
			customerName.setText(map.get(MAP_KEY_NAME));
			customerPhone.setText(map.get(MAP_KEY_PHONE));
		}
	};
	
	private OnDateChangedListener dateListener = new OnDateChangedListener() {
		
		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Calendar cal = Calendar.getInstance(TimeZone.getDefault());
			cal.set(year, monthOfYear, dayOfMonth);
			orderTimestamp = cal.getTimeInMillis();
			Log.i(TAG, "onDateChanged: orderTimeStamp: " + orderTimestamp);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_order);

		sectionsInOrder = new ArrayList<View>();

		dishContainer = (LinearLayout) findViewById(R.id.id_dish_container);

		dbUtils = DatabaseUtils.getInstance();
		dishesInMenu = dbUtils.getAllDishes();
		customers = dbUtils.getAllCustomers();

		Button addDishBtn = (Button) findViewById(R.id.id_add_dish);
		addDishBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addDishSectionToOrder((Button) v);
			}
		});

		totalPrice = (TextView) findViewById(R.id.id_total_price);

//		sdf = new SimpleDateFormat("dd MMM yyyy");
//
		date = (DatePicker) findViewById(R.id.id_date_value);
		Calendar c = Calendar.getInstance(TimeZone.getDefault());
		date.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), dateListener);
		date.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		orderTimestamp = c.getTimeInMillis();
		Log.i(TAG, "onCreate: orderTimeStamp: " + orderTimestamp);
//		date.setText(sdf.format(c.getTime()));

		int numCustomers = customers.size();
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		for (int index = 0; index < numCustomers; index++) {
			Customer customer = customers.get(index);
			HashMap<String, String> customerMap = new HashMap<String, String>();
			customerMap.put(MAP_KEY_NAME, customer.getName());
			customerMap.put(MAP_KEY_PHONE, customer.getPhone());
			list.add(customerMap);
		}
		String[] from = new String[] {MAP_KEY_NAME, MAP_KEY_PHONE};
		int[] to = new int[] {R.id.id_name, R.id.id_phone};
		adapter = new SimpleAdapter(getApplicationContext(), list, R.layout.customer_info, from, to);
		customerName = (AutoCompleteTextView) findViewById(R.id.id_person_name);
		customerName.setAdapter(adapter);
		customerName.setOnItemClickListener(clickListener);
		
		customerPhone = (AutoCompleteTextView) findViewById(R.id.id_phone_number);
		customerPhone.setAdapter(adapter);
		customerPhone.setOnItemClickListener(clickListener);

		Button saveOrderBtn = (Button) findViewById(R.id.id_save_order);
		saveOrderBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = customerName.getText().toString();
				if (name == null || name.length() == 0) {
					displayToast("Name cannot be empty");
					return;
				}

				String phone = customerPhone.getText().toString();
				if (phone == null || phone.length() == 0) {
					displayToast("Phone cannot be empty");
					return;
				}
				
				String balance = totalPrice.getText().toString();
				
				// Get the dishes that customer wants to add in the order
				ArrayList<PlaceOrder.Dishes> dishesToOrder = new ArrayList<PlaceOrder.Dishes>();
				int numChildren = dishContainer.getChildCount();
				for (int i = 0; i < numChildren; i++) {
					View dishView = dishContainer.getChildAt(i);
					Menu dish = ((OrderChangeListener) dishView.getTag()).getSelectedDish();
					Log.i(TAG, "Dish Ordered: " + dish.getDishName());
					Log.i(TAG, "Quantity Ordered: " + dish.getQuantity());
					int num_items = Integer.parseInt(((EditText) dishView.findViewById(R.id.id_items_value)).getText().toString());
					dishesToOrder.add(new PlaceOrder.Dishes(dish, num_items));
				}
				
				// Create PlaceOrder object with relevant data to add it in the DB
				PlaceOrder orderToPlace = new PlaceOrder();
				orderToPlace.setCustomerName(name);
				orderToPlace.setCustomerPhone(phone);
				orderToPlace.setPriceOfOrder(balance);
				orderToPlace.setOrderDate(orderTimestamp);
				orderToPlace.addDishesToOrder(dishesToOrder);

				boolean order_created = dbUtils.saveOrderInDB(orderToPlace);
				
				if (order_created) {
					displayToast("Order saved successfully");
					finish();
				} else {
					displayToast("There was an error in saving the order. Try again");
					return;
				}
			}
		});
	}

	private void addDishSectionToOrder(Button button) {
		View sectionToAdd = LayoutInflater.from(getApplicationContext()).inflate(R.layout.add_dish_to_order, null);
		sectionsInOrder.add(sectionToAdd);

		OrderChangeListener listener = new OrderChangeListener();

		Spinner dishes = (Spinner) sectionToAdd.findViewById(R.id.id_dish_name);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, dishesInMenu);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dishes.setAdapter(adapter);
		dishes.setOnItemSelectedListener(listener);

		Spinner quantities = (Spinner) sectionToAdd.findViewById(R.id.id_quantity_value);
		quantities.setOnItemSelectedListener(listener);

		EditText numItems = (EditText) sectionToAdd.findViewById(R.id.id_items_value);
		numItems.addTextChangedListener(listener);

		Button btnDelete = (Button) sectionToAdd.findViewById(R.id.id_delete);
		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dishContainer.removeView((View) v.getParent());
				calculateTotalPrice();
			}
		});
		dishContainer.addView(sectionToAdd);
		listener.setDishSectionIndexInParent(dishContainer.indexOfChild(sectionToAdd));

		Log.i(TAG, "addDishSectionToOrder: tag added: " + listener);
		sectionToAdd.setTag(listener);
	}

	private void calculateTotalPrice() {
		int numDishesInOrder = dishContainer.getChildCount();
		double total = 0.0d;
		for (int i = 0; i < numDishesInOrder; i++) {
			TextView priceView = (TextView) ((ViewGroup) dishContainer.getChildAt(i)).findViewById(R.id.id_price_value);
			total += Double.valueOf(priceView.getText().toString());
			Log.i(TAG, "Price for dish: " + Double.valueOf(priceView.getText().toString()));
		}

		totalPrice.setText(String.valueOf(total));
	}

	private void displayToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

	public class OrderChangeListener implements TextWatcher, OnItemSelectedListener {

		private int dishSectionIndexInParent;
		private Menu dish;
		private ArrayList<Menu> menuByDish;

		public OrderChangeListener() {

		}

		public void setDishSectionIndexInParent(int index) {
			dishSectionIndexInParent = index;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {


		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			int numItems = 0;
			if (s != null && s.length() > 0) {
				try {
					numItems = Integer.parseInt(s.toString());
				} catch (Exception e) {
					numItems = 0;
				}
			}
			View v = (RelativeLayout) dishContainer.getChildAt(dishSectionIndexInParent);
			TextView price = (TextView) v.findViewById(R.id.id_price_value);
			price.setText(String.valueOf(Float.parseFloat(dish.getPrice()) * numItems));
			calculateTotalPrice();
		}

		@Override
		public void afterTextChanged(Editable s) {


		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {

			String tag = (String) parent.getTag();
			if (tag.equals("dish")) {
				Log.i(TAG, "Dish selected: " + (String) parent.getItemAtPosition(position));
				menuByDish = dbUtils.getMenuByDishId(dbUtils.getDishIdByName((String) parent.getItemAtPosition(position)));
				int num = menuByDish.size();
				String[] quantity = new String[num];
				for (int i = 0; i < num; i++) {
					quantity[i] = menuByDish.get(i).getQuantity();
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, quantity);
				// Specify the layout to use when the list of choices appears
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				ViewGroup topParent = (ViewGroup) parent.getParent().getParent();
				Spinner quantityView = (Spinner) topParent.findViewWithTag("quantity");
				quantityView.setAdapter(adapter);
			} else if (tag.equals("quantity")) {
				dish = menuByDish.get(position);
				ViewGroup topParent = (ViewGroup) parent.getParent().getParent();
				TextView priceView = (TextView) topParent.findViewById(R.id.id_price_value);
				EditText num = (EditText) topParent.findViewById(R.id.id_items_value);
				priceView.setText(String.valueOf(Float.parseFloat(dish.getPrice()) * Integer.parseInt(num.getText().toString())));
				calculateTotalPrice();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {


		}

		public Menu getSelectedDish() {
			return dish;
		}

	}
}
