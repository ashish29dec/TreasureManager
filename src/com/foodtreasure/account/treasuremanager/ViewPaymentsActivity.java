/**
 * 
 */
package com.foodtreasure.account.treasuremanager;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.foodtreasure.account.treasuremanager.db.DatabaseUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author agupta
 *
 */
public class ViewPaymentsActivity extends Activity {

	private DatabaseUtils dbUtils;
	
	private NumberFormat format;
	
	private Spinner monthSpinner;
	private TextView paymentsView;
	
	private OnItemSelectedListener listener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Calendar cal = Calendar.getInstance(TimeZone.getDefault());
			int year = cal.get(Calendar.YEAR);
			cal.set(Calendar.MONTH, arg2);
			int days_in_month = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.US);
			calendar.set(year, arg2, 1, 0, 0, 0);
			long startTime = calendar.getTimeInMillis();
			
			calendar.set(year, arg2, days_in_month, 23, 59, 59);
			long endTime = calendar.getTimeInMillis();
			
			paymentsView.setText(format.format(dbUtils.getTotalPaymentsReceived(startTime, endTime)));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		dbUtils = DatabaseUtils.getInstance();
		
		setContentView(R.layout.view_payments);
		
		paymentsView = (TextView) findViewById(R.id.id_payments);
		double initial_payment = 0.0d;
		format = NumberFormat.getCurrencyInstance(Locale.US);
		paymentsView.setText(format.format(initial_payment));
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, new DateFormatSymbols(Locale.US).getMonths());
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		monthSpinner = (Spinner) findViewById(R.id.id_month_spinner);
		monthSpinner.setAdapter(adapter);
		monthSpinner.setOnItemSelectedListener(listener);
	}
}
