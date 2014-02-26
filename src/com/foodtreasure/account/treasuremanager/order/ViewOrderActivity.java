/**
 * 
 */
package com.foodtreasure.account.treasuremanager.order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;

import com.foodtreasure.account.treasuremanager.R;
import com.foodtreasure.account.treasuremanager.db.Customer;
import com.foodtreasure.account.treasuremanager.db.DatabaseUtils;
import com.foodtreasure.account.treasuremanager.db.ViewOrder;
import com.foodtreasure.account.treasuremanager.db.ViewOrder.Dishes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;

/**
 * @author agupta
 *
 */
public class ViewOrderActivity extends Activity {

	private final static String TAG = ViewOrderActivity.class.getName();
	
	// Views in view_order.xml
	private LinearLayout customerInfoContainer;
	private TextView noOrderTextView;
	private DatePicker orderDatePicker;
	
	private DatabaseUtils dbUtils;
	
	private long startOrderDate;
	private long endOrderDate;
	
	private OnDateChangedListener dateChangeListener = new OnDateChangedListener() {
		
		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Log.i(TAG, "onDateChanged");
			Calendar c = Calendar.getInstance(TimeZone.getDefault());
			c.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
			startOrderDate = c.getTimeInMillis();
			
			c.set(year, monthOfYear, dayOfMonth, 23, 59, 59);
			endOrderDate = c.getTimeInMillis();
			
			Log.i(TAG, "onDateChanged: startOrderDate: " + startOrderDate);
			Log.i(TAG, "onDateChanged: endOrderDate: " + endOrderDate);
			// Fetch the orders for this date
			fetchAndPopulate(startOrderDate, endOrderDate);
		}
	};
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.id_accept_pay) {
				Log.i(TAG, "Accept Payment button clicked");
				// Get the ViewOrder object for this customer
				ViewOrder customer_order = (ViewOrder) v.getTag();
				// Display the Dialog to accept payment
				displayAcceptPaymentDialog(customer_order);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_order);
		
		dbUtils = DatabaseUtils.getInstance();
		
		customerInfoContainer = (LinearLayout) findViewById(R.id.id_customer_info_container);
		
		noOrderTextView = (TextView) findViewById(R.id.id_no_order);
		
		orderDatePicker = (DatePicker) findViewById(R.id.id_date);
		Calendar c = Calendar.getInstance(TimeZone.getDefault());
		orderDatePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), dateChangeListener);
		orderDatePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		Log.i(TAG, "onCreate: requestedOrderDate: " + c.getTimeInMillis());
	}

	/**
	 * Fetches the orders for the date requested and populates the screen
	 * @param requestedOrderDate
	 * @param endOrderDate2 
	 */
	private void fetchAndPopulate(long startOrderDate, long endOrderDate) {
		// Clear all the views from the customer container
		customerInfoContainer.removeAllViews();
		ArrayList<ViewOrder> orders = dbUtils.getOrdersForDate(startOrderDate, endOrderDate);
		if (orders == null || orders.size() == 0) {
			noOrderTextView.setVisibility(View.VISIBLE);
			customerInfoContainer.setVisibility(View.GONE);
		} else {
			noOrderTextView.setVisibility(View.GONE);
			customerInfoContainer.setVisibility(View.VISIBLE);
			
			// Populate the customer info layout
			int numCustomers = orders.size();
			for (int customer_index = 0; customer_index < numCustomers; customer_index++) {
				ViewOrder customer_order = orders.get(customer_index);
				View customer_info_view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customer_info_view_order, null);
				// Populate Customer Name
				TextView customer_name = (TextView) customer_info_view.findViewById(R.id.id_customer_name);
				customer_name.setText(customer_order.getCustomer().getName());
				// Populate Customer Phone
				TextView customer_phone = (TextView) customer_info_view.findViewById(R.id.id_customer_phone);
				customer_phone.setText(customer_order.getCustomer().getPhone());
				// Populate Balance
				TextView customer_balance = (TextView) customer_info_view.findViewById(R.id.id_balance);
				customer_balance.setText(getString(R.string.str_dollar) + String.valueOf(customer_order.getCustomer().getBalance()));
				// Handle the Accept Payment Button
				Button accept_payment_btn = (Button) customer_info_view.findViewById(R.id.id_accept_pay);
				accept_payment_btn.setTag(customer_order);
				accept_payment_btn.setOnClickListener(clickListener);
				// Get the Order Info container
				ViewGroup order_info_container_view = (ViewGroup) customer_info_view.findViewById(R.id.id_order_info_container);
				
				// Populate the Order info layout
				Iterator<ArrayList<Dishes>> order_iterator = customer_order.getOrders().values().iterator();
				int order_number = 1;
				while (order_iterator.hasNext()) {
					ArrayList<Dishes> order = order_iterator.next();
					// Inflate the order_info layout
					View order_info_view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.order_info_view_order, null);
					// Populate Order Number
					TextView order_number_view = (TextView) order_info_view.findViewById(R.id.id_order_number);
					order_number_view.setText(getString(R.string.str_order) + " " + order_number++);
					// Get Order container
					ViewGroup order_container_view = (ViewGroup) order_info_view.findViewById(R.id.id_order_container);
					
					// Populate the dishes in the order
					int num_dishes_in_order = order.size();
					for (int dishes_index = 0; dishes_index < num_dishes_in_order; dishes_index++) {
						Dishes dish = order.get(dishes_index);
						// Inflate order_detail layout file
						View order_detail_view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.order_detail_view_order, null);
						// Populate the dish name
						TextView dish_name = (TextView) order_detail_view.findViewById(R.id.id_dish);
						dish_name.setText(dish.getDish().getDishName());
						// Populate the quantity
						TextView dish_quantity = (TextView) order_detail_view.findViewById(R.id.id_quantity);
						dish_quantity.setText(dish.getDish().getQuantity());
						// Populate the dish number
						TextView num_dish = (TextView) order_detail_view.findViewById(R.id.id_num);
						num_dish.setText(String.valueOf(dish.getNumberOfDish()));
						
						// Add this view to order_container_view
						order_container_view.addView(order_detail_view);
					}
					
					// Add order_info_view to order_info_container_view
					order_info_container_view.addView(order_info_view);
				}
				
				// Add customer_info_view to customerInfoContainer
				customerInfoContainer.addView(customer_info_view);
			}
		}
	}

	private void displayAcceptPaymentDialog(final ViewOrder customer_order) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.str_accept_payment);
		View dialogView = LayoutInflater.from(this).inflate(R.layout.accept_payment_dialog, null);
		// Set Customer Name and Balance
		TextView name = (TextView) dialogView.findViewById(R.id.id_name);
		name.setText(customer_order.getCustomer().getName());
		TextView balance = (TextView) dialogView.findViewById(R.id.id_balance);
		balance.setText(getString(R.string.str_dollar) + customer_order.getCustomer().getBalance());
		// Get the EditText object
		final EditText payment_text = (EditText) dialogView.findViewById(R.id.id_payment);
		builder.setView(dialogView);
		builder.setCancelable(true);
		builder.setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton(R.string.str_accept, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Save the payment and update the balance in the db
				// Get the payment made
				String payment = payment_text.getText().toString();
				float payment_fl = Float.parseFloat(payment);
				if (payment == null || payment.length() == 0 || payment_fl == 0.0) {
					displayToast("Please enter a valid payment");
					return;
				} else {
					// Save the payment history in payment table and also adjust the balance for this customer.
					// Both these steps must happen in single transaction
					Calendar cal = Calendar.getInstance(TimeZone.getDefault());
					Customer updated_customer = dbUtils.savePaymentForCustomerInSingleTransaction(customer_order.getCustomer(), cal.getTimeInMillis(), payment);
					if (updated_customer != null) {
						// Display another dialog that shows remaining balance on customer and dismiss this one
						displaySuccessDialog(updated_customer);
						dialog.dismiss();
					} else {
						displayToast("Error in saving the payment. Please try again.");
					}
				}
			}
		});
		builder.create().show();
	}

	protected void displaySuccessDialog(Customer updated_customer) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.str_payment_saved);
		StringBuffer message = new StringBuffer();
		message.append(getString(R.string.str_new_balance));
		message.append(updated_customer.getBalance());
		builder.setMessage(message);
		builder.setCancelable(true);
		builder.setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finish();
			}
		});
		builder.create().show();
	}

	protected void displayToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_LONG).show();
	}
}
