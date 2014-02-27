package com.foodtreasure.account.treasuremanager;

import com.foodtreasure.account.treasuremanager.db.DatabaseUtils;
import com.foodtreasure.account.treasuremanager.menu.AddDishToMenuActivity;
import com.foodtreasure.account.treasuremanager.menu.ModifyMenuActivity;
import com.foodtreasure.account.treasuremanager.menu.ViewMenuActivity;
import com.foodtreasure.account.treasuremanager.order.CreateOrderActivity;
import com.foodtreasure.account.treasuremanager.order.ViewBalancesActivity;
import com.foodtreasure.account.treasuremanager.order.ViewOrderActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	// TAG for logging
	private static final String TAG = "MainActivity";
	
	// Database utils object to work with DB
	private DatabaseUtils dbUtils;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "OnCreate");
        
        Log.i(TAG, "Setting Content View from the xml");
        setContentView(R.layout.activity_main);
        
        // Create menu
        Button createMenuBtn = (Button) findViewById(R.id.id_create_menu);
        createMenuBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), AddDishToMenuActivity.class));
			}
		});
        
        // Create Order/Take Order
        Button takeOrderBtn = (Button) findViewById(R.id.id_take_order);
        takeOrderBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), CreateOrderActivity.class));
			}
		});
        
        // View Order
        Button viewOrderBtn = (Button) findViewById(R.id.id_view_order);
        viewOrderBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ViewOrderActivity.class));
			}
		});
        
        Button viewBalancesBtn = (Button) findViewById(R.id.id_view_balances);
        viewBalancesBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ViewBalancesActivity.class));
			}
		});
        
        // View menu
        Button viewMenuBtn = (Button) findViewById(R.id.id_view_menu);
        viewMenuBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ViewMenuActivity.class));
			}
		});
        
        // Modify menu
        Button modifyMenuBtn = (Button) findViewById(R.id.id_modify_menu);
        modifyMenuBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ModifyMenuActivity.class));
			}
		});
        
        // View Payments
        Button viewPaymentsBtn = (Button) findViewById(R.id.id_view_payments);
        viewPaymentsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ViewPaymentsActivity.class));
			}
		});
        
        
        // View Tables
        Button viewTablesBtn = (Button) findViewById(R.id.id_view_tables);
        viewTablesBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ViewTablesActivity.class));
			}
		});
        
        // Loading the DB
        Log.i(TAG, "Creating instance of DatabaseUtils");
        dbUtils = DatabaseUtils.getInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	if (dbUtils != null) {
    		dbUtils.closeDB();
    	}
    }
}
