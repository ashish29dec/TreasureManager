/**
 * 
 */
package com.foodtreasure.account.treasuremanager.menu;

import java.util.ArrayList;

import com.foodtreasure.account.treasuremanager.R;
import com.foodtreasure.account.treasuremanager.db.DatabaseUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author agupta
 *
 */
public class AddDishToMenuActivity extends Activity {
	
	private static final String TAG = "AddDishToMenuActivity";
	
	private DatabaseUtils dbUtils;
	
	private ArrayList<String> dishArray;
	private ArrayList<String> quantityArray;
	
	private AutoCompleteTextView dishNameView;
	private AutoCompleteTextView quantityView;
	private EditText priceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.add_dish_to_menu);
		
		dishNameView = (AutoCompleteTextView) findViewById(R.id.textview_dish_name);
		quantityView = (AutoCompleteTextView) findViewById(R.id.textview_quantity);
		priceView = (EditText) findViewById(R.id.textview_price);
		
		populateActivity();
		
		Button saveBtn = (Button) findViewById(R.id.btn_save);
		saveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Check if all fields are filled
				String enteredDishName = dishNameView.getText().toString();
				Log.i(TAG, "Entered Dish Name: " + enteredDishName);
				if (enteredDishName == null || enteredDishName.length() == 0) {
					displayToast("Cannot leave Dish Name blank");
					return;
				}
				
				String enteredQuantity = quantityView.getText().toString();
				Log.i(TAG, "Entered Quantity: " + enteredQuantity);
				if (enteredQuantity == null || enteredQuantity.length() == 0) {
					displayToast("Cannot leave Quantity blank");
					return;
				}
				
				String enteredPrice = priceView.getText().toString();
				Log.i(TAG, "Entered Price: " + enteredPrice);
				if (enteredPrice == null || enteredPrice.length() == 0) {
					displayToast("Cannot leave Price blank");
					return;
				}
				
				// Save the data in the DB
				long rowId;
				Log.i(TAG, "Saving Data in DB");
				if (!dishArray.contains(enteredDishName)) {
					// Save the new dish name in the DB
					Log.i(TAG, "Saving newly created dish " + enteredDishName + " into DB");
					rowId = dbUtils.saveDishName(enteredDishName);
					if (rowId < 0) {
						Log.i(TAG, "Error while saving Dish Name in DB. RowId: " + rowId);
						displayToast("Error saving Dish Name in DB");
						return;
					}
				}
				
				if (!quantityArray.contains(enteredQuantity)) {
					// Save the new quantity in the DB
					Log.i(TAG, "Saving newly created quantity " + enteredQuantity + " into DB");
					rowId = dbUtils.saveQuantity(enteredQuantity);
					if (rowId < 0) {
						Log.i(TAG, "Error while saving Quantity in DB. RowId: " + rowId);
						displayToast("Error saving Quantity in DB");
						return;
					}
				}
				
				// Save new item in Menu
				Log.i(TAG, "Saving new item in Menu");
				rowId = dbUtils.addInMenu(enteredDishName, enteredQuantity, enteredPrice);
				if (rowId < 0) {
					Log.i(TAG, "Error while adding in Menu. RowId: " + rowId);
					displayToast("Error saving in Menu. Try again");
					return;
				}
				
				// Clear all the fields
				dishNameView.setText("");
				quantityView.setText("");
				priceView.setText("");

				// Re-populate the fields
				populateActivity();
			}
		});
	}
	
	/**
	 * Fetches data from the DB and populates the corresponding widgets
	 */
	private void populateActivity() {
		Log.i(TAG, "Getting DatabaseUtils instance");
		dbUtils = DatabaseUtils.getInstance();
		
		Log.i(TAG, "Loading dishes from DB");
		dishArray = dbUtils.getAllDishes();
		if (dishArray.size() > 0) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, dishArray);
			dishNameView.setAdapter(adapter);
		}

		Log.i(TAG, "Loading quantities from DB");
		quantityArray = dbUtils.getAllQuantities();
		if (quantityArray.size() > 0) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, quantityArray);
			quantityView.setAdapter(adapter);
		}
	}
	
	/**
	 * Display Toast with message passed to it
	 * @param message
	 */
	private void displayToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

}
