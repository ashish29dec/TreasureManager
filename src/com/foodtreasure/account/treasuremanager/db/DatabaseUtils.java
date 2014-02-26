/**
 * 
 */
package com.foodtreasure.account.treasuremanager.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.foodtreasure.account.treasuremanager.DBHelper;
import com.foodtreasure.account.treasuremanager.TreasureApplication;

/**
 * @author agupta
 *
 */
public class DatabaseUtils {

	// TAG for Logging
	private static final String TAG = "DatabaseUtils";
	
	private static DatabaseUtils instance;
	
	// Database Helper object
	private DBHelper dbHelper;
	
	/**
	 * Singleton
	 * @param context
	 * @return
	 */
	public static DatabaseUtils getInstance() {
		if (instance == null) {
			Log.i(TAG, "DatabaseUtils object already created");
			instance = new DatabaseUtils(TreasureApplication.context);
		}
		return instance;
	}
	
	/**
	 * Instance
	 * @param context
	 */
	private DatabaseUtils(Context context) {
		// Creating DBHelper object
		Log.i(TAG, "Creating DBHelper");
		dbHelper = new DBHelper(context);
	}
	
	//********** Getter methods **************
	/**
	 * Returns the list of dishes
	 * @return
	 */
	public synchronized ArrayList<String> getAllDishes() {
		return dbHelper.getAllDishes();
	}
	
	/**
	 * Returns the number of dishes
	 * @return
	 */
	public synchronized int getDishesCount() {
		return getAllDishes().size();
	}

	/**
	 * Returns the list of dishes
	 * @return
	 */
	public synchronized ArrayList<String> getAllQuantities() {
		return dbHelper.getAllQuantities();
	}
	
	/**
	 * Returns the number of dishes
	 * @return
	 */
	public synchronized int getQuantitiesCount() {
		return getAllQuantities().size();
	}
	
	/**
	 * Returns the menu
	 * @return
	 */
	public synchronized ArrayList<ArrayList<String>> getMenu() {
		return dbHelper.getMenu();
	}
	
	/**
	 * Gets id for the dish name passed as argument, otherwise returns -1
	 * @param dish
	 * @return
	 */
	public synchronized int getDishIdByName(String dish) {
		return dbHelper.getDishIdByName(dish);
	}
	
	/**
	 * Gets dish name for id passed as argument, otherwise null
	 * @param id
	 * @return
	 */
	public synchronized String getDishNameById(int id) {
		return dbHelper.getDishNameById(id);
	}
	
	/**
	 * Gets the quantity for id passed as argument, otherwise returns null
	 * @param id
	 * @return
	 */
	public synchronized String getQuantityNameById(int id) {
		return dbHelper.getQuantityNameById(id);
	}
	
	/**
	 * Retrieve the menu for a particular dish from DB
	 * @param id
	 * @return
	 */
	public synchronized ArrayList<Menu> getMenuByDishId(int id) {
		return dbHelper.getMenuByDishId(id);
	}
	
	/**
	 * Returns the list of customers
	 * @return
	 */
	public synchronized ArrayList<Customer> getAllCustomers() {
		return dbHelper.getAllCustomers();
	}
	
	/**
	 * Fetches the orders for passed date
	 * @param millis
	 * @param endOrderDate 
	 * @return
	 */
	public synchronized ArrayList<ViewOrder> getOrdersForDate(long startMillis, long endMillis) {
		return dbHelper.getOrdersForDate(startMillis, endMillis);
	}
	
	/**
	 * Returns table names in the database
	 * @return
	 */
	public synchronized ArrayList<String> getTablesInDatabase() {
		return dbHelper.getTablesInDatabase();
	}
	
	/**
	 * Returns cursor pointing to contents of the table
	 * @return
	 */
	public synchronized Cursor getTableContents(String table_name) {
		return dbHelper.getTableContents(table_name);
	}
	
	/**
	 * Replaces the dish in question with new data passed as parameter
	 * @param replacedDish
	 * @return the number of rows affected, 0 otherwise
	 */
	public synchronized int replaceDishInMenu(Menu replacedDish) {
		return dbHelper.replaceDishInMenu(replacedDish);
	}
	
	/**
	 * Deletes the passed dish from the Menu
	 * @param dishToDelete
	 * @return
	 */
	public synchronized int deleteDishInMenu(Menu dishToDelete) {
		return dbHelper.deleteDishInMenu(dishToDelete);
	}
	
	/**
	 * Saving newly created dish name in the table
	 * @param name
	 * @return
	 */
	public synchronized long saveDishName(String name) {
		return dbHelper.storeDishName(name);
	}

	/**
	 * Saving newly created dish name in the table
	 * @param name
	 * @return
	 */
	public synchronized long saveQuantity(String quantity) {
		return dbHelper.storeQuantity(quantity);
	}
	
	/**
	 * Saving newly created dish name in the table
	 * @param name
	 * @return
	 */
	public synchronized long addInMenu(String dish, String quantity, String price) {
		return dbHelper.storeInMenu(dish, quantity, price);
	}
	
	/**
	 * Save order
	 * @param order
	 * @return
	 */
	public synchronized boolean saveOrderInDB(PlaceOrder order) {
		return dbHelper.saveOrderInDB(order);
	}
	
	/**
	 * Closes any open DB
	 */
	public synchronized void closeDB() {
		dbHelper.closeDB();
	}

	/**
	 * Saves the payment made
	 * @param customer
	 * @param millis 
	 * @param payment
	 * @return
	 */
	public synchronized Customer savePaymentForCustomerInSingleTransaction(Customer customer, long millis, String payment) {
		return dbHelper.savePaymentForCustomerInSingleTransaction(customer, millis, payment);
	}
}
