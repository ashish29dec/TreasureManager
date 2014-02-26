/**
 * 
 */
package com.foodtreasure.account.treasuremanager.menu;

import java.util.ArrayList;

import com.foodtreasure.account.treasuremanager.R;
import com.foodtreasure.account.treasuremanager.db.DatabaseUtils;
import com.foodtreasure.account.treasuremanager.db.Menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author agupta
 *
 */
public class ModifyMenuActivity extends Activity implements OnItemSelectedListener {

	private static final String TAG = "ModifyMenuActivity";
	
	private DatabaseUtils dbUtils;
	
	ArrayList<String> dishes;
	
	private Spinner dishView;
	private Spinner quantityView;
	private EditText priceView;
	
	private int selectedDishId;
	private int selectedQuantityId;
	
	private ArrayList<Menu> selectedDishMenu;
	private Menu editedDish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.modify_menu);
		
		// Getting DatabaseUtils objects
		dbUtils = DatabaseUtils.getInstance();
		
		// Populate the dishes
		// Get the dish names
		dishes = dbUtils.getAllDishes();
		dishView = (Spinner) findViewById(R.id.spinner_dish_name);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, dishes);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dishView.setAdapter(adapter);
		dishView.setOnItemSelectedListener(this);
		
		
		quantityView = (Spinner) findViewById(R.id.spinner_quantity);
		quantityView.setAdapter(null);
		quantityView.setOnItemSelectedListener(this);
		
		priceView = (EditText) findViewById(R.id.textview_price);
		
		Button saveBtn = (Button) findViewById(R.id.btn_save);
		saveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Check if all the fields are populated
				String price = priceView.getText().toString();
				if (price == null || price.length() == 0) {
					displayToast("Price can not be empty");
					return;
				}
				
				editedDish.setPrice(price);
				int rowsAffected = dbUtils.replaceDishInMenu(editedDish);
				if (rowsAffected == 0) {
					displayToast("No dish modified");
				} else {
					displayToast(rowsAffected + " dish modified");
					finish();
				}
			}
		});
		
		Button delBtn = (Button) findViewById(R.id.btn_delete);
		delBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Display dialog asking for confirmation
				displayConfirmationDialog();
			}
		});
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == dishView) {
			priceView.setText("");
			
			String dish = (String) parent.getItemAtPosition(position);
			selectedDishId = dbUtils.getDishIdByName(dish);
			selectedDishMenu = dbUtils.getMenuByDishId(selectedDishId);
			int num = selectedDishMenu.size();
			String[] quantity = new String[num];
			for (int i = 0; i < num; i++) {
				quantity[i] = selectedDishMenu.get(i).getQuantity();
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, quantity);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			quantityView.setAdapter(adapter);
		} else if (parent == quantityView) {
			editedDish = selectedDishMenu.get(position);
			priceView.setText(editedDish.getPrice());
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	private void displayToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
	
	private void displayConfirmationDialog() {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(R.string.str_are_you_sure);
		View v = LayoutInflater.from(this).inflate(R.layout.dish_delete_confirmation, null);
		
		TextView dishView = (TextView) v.findViewById(R.id.id_dish_name);
		dishView.setText(editedDish.getDishName());
		
		TextView quantityView = (TextView) v.findViewById(R.id.id_quantity_value);
		quantityView.setText(editedDish.getQuantity());
		
		TextView priceView = (TextView) v.findViewById(R.id.id_price_value);
		priceView.setText(editedDish.getPrice());
		
		adb.setView(v);
		adb.setCancelable(true);
		adb.setPositiveButton(R.string.str_delete, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				int numRowsAffected = dbUtils.deleteDishInMenu(editedDish);
				if (numRowsAffected == 0) {
					displayToast("No dishes deleted from Menu");
				} else {
					displayToast(numRowsAffected + " dishes deleted from Menu");
					finish();
				}
			}
		});
		
		adb.setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		adb.create().show();
	}
}
