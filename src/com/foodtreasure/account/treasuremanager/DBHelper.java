/**
 * 
 */
package com.foodtreasure.account.treasuremanager;

import java.util.ArrayList;
import java.util.HashMap;

import com.foodtreasure.account.treasuremanager.db.Customer;
import com.foodtreasure.account.treasuremanager.db.Menu;
import com.foodtreasure.account.treasuremanager.db.PlaceOrder;
import com.foodtreasure.account.treasuremanager.db.ViewOrder;
import com.foodtreasure.account.treasuremanager.db.ViewOrder.Dishes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.view.MenuCompat;
import android.util.Log;
import android.webkit.WebChromeClient.CustomViewCallback;

/**
 * @author agupta
 *
 */
public class DBHelper extends SQLiteOpenHelper {
	
	// TAG for logging
	private static final String TAG = "DBHelper";
	
	// Database Version
	private static final int DB_VERSION = 1;
	
	// Database name
	private static final String DB_NAME = "Treasure.db";
	
	// Table names
	private static final String TABLE_DISH_NAME = "dishes";
	private static final String TABLE_QUANTITY = "quantity";
	private static final String TABLE_MENU = "menu";
	private static final String TABLE_CUSTOMER = "customer";
	private static final String TABLE_ORDER = "orders";
	private static final String TABLE_ORDER_DETAILS = "order_details";
	private static final String TABLE_PAYMENT = "payment";
	
	// Create table queries
	private static final String QUERY_CREATE_DISHES_TABLE = "CREATE TABLE " + TABLE_DISH_NAME + " (" 
			+ DishColoumnNames.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DishColoumnNames.NAME + " TEXT"
			+ ");";
	private static final String QUERY_CREATE_QUANTITY_TABLE = "CREATE TABLE " + TABLE_QUANTITY + " (" 
			+ QuantityColoumnNames.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ QuantityColoumnNames.QUANTITY + " TEXT"
			+ ");";
	private static final String QUERY_CREATE_MENU_TABLE = "CREATE TABLE " + TABLE_MENU + " (" 
			+ MenuColoumnNames.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ MenuColoumnNames.DISH_ID + " INTEGER, "
			+ MenuColoumnNames.QUANTITY_ID + " INTEGER, "
			+ MenuColoumnNames.PRICE + " TEXT NOT NULL"
			+ ");";
	private static final String QUERY_CREATE_CUSTOMER_TABLE = "CREATE TABLE " + TABLE_CUSTOMER + " (" 
			+ CustomerColoumnNames.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ CustomerColoumnNames.NAME + " TEXT, "
			+ CustomerColoumnNames.PHONE + " TEXT, "
			+ CustomerColoumnNames.BALANCE + " TEXT DEFAULT 0.00"
			+ ");";
	
	private static final String QUERY_CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_ORDER + " (" 
			+ OrderColoumnNames.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ OrderColoumnNames.CUSTOMER_ID + " INTEGER, "
			+ OrderColoumnNames.DATE + " INTEGER"
			+ ");";	
	
	private static final String QUERY_CREATE_ORDER_DETAILS_TABLE = "CREATE TABLE " + TABLE_ORDER_DETAILS + " (" 
			+ OrderDetailsColoumnNames.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ OrderDetailsColoumnNames.ORDER_ID + " INTEGER, "
			+ OrderDetailsColoumnNames.MENU_ID + " INTEGER, "
			+ OrderDetailsColoumnNames.NUM_ITEMS + " INTEGER"
			+ ");";	

	private static final String QUERY_CREATE_PAYMENT_TABLE = "CREATE TABLE " + TABLE_PAYMENT + " (" 
			+ PaymentColoumnNames.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ PaymentColoumnNames.CUSTOMER_ID + " INTEGER, "
			+ PaymentColoumnNames.DATE + " INTEGER, "
			+ PaymentColoumnNames.PAYMENT + " TEXT"
			+ ");";	

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
//		// Calling getWritableDatabase to create the DB
//		Log.i(TAG, "<init>: Calling getWritableDatabase to create DB");
//		getWritableDatabase();
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "onCreate");
		try {
			// Creating DISHES table
			Log.i(TAG, "onCreate: Creating \"dishes\" table");
			db.execSQL(QUERY_CREATE_DISHES_TABLE);
			
			// Creating QUANTITY table
			Log.i(TAG, "onCreate: Creating \"quantity\" table");
			db.execSQL(QUERY_CREATE_QUANTITY_TABLE);
			
			// Creating MENU table
			Log.i(TAG, "onCreate: Creating \"menu\" table");
			db.execSQL(QUERY_CREATE_MENU_TABLE);
			
			// Creating CUSTOMER table
			Log.i(TAG, "onCreate: Creating \"customer\" table");
			db.execSQL(QUERY_CREATE_CUSTOMER_TABLE);
			
			// Creating ORDER table
			Log.i(TAG, "onCreate: Creating \"order\" table");
			db.execSQL(QUERY_CREATE_ORDER_TABLE);
			
			// Creating ORDER_DETAILS table
			Log.i(TAG, "onCreate: Creating \"order_details\" table");
			db.execSQL(QUERY_CREATE_ORDER_DETAILS_TABLE);
			
			// Creating PAYMENT table
			Log.i(TAG, "onCreate: Creating \"payment\" table");
			db.execSQL(QUERY_CREATE_PAYMENT_TABLE);
			
		} catch (Throwable t) {
			Log.e(TAG, "onCreate: Error in creating table", t);
		}
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Returns the names of all the dishes
	 * @return
	 */
	public ArrayList<String> getAllDishes() {
		ArrayList<String> dishes = new ArrayList<String>();
		try {
			SQLiteDatabase db = getReadableDatabase();
			String[] col = new String[] {DishColoumnNames.NAME};
			Cursor c = db.query(TABLE_DISH_NAME, col, null, null, null, null, null);
			while (c.moveToNext()) {
				int index = c.getColumnIndex(DishColoumnNames.NAME);
				dishes.add(c.getString(index));
			}
			c.close();
		} catch (Throwable tr) {
			Log.e(TAG, "getAllDishes: Error in fetching dishes", tr);
		}
		return dishes;
	}
	
	/**
	 * Returns all the quantities
	 * @return
	 */
	public ArrayList<String> getAllQuantities() {
		ArrayList<String> quantities = new ArrayList<String>();
		try {
			SQLiteDatabase db = getReadableDatabase();
			String[] col = new String[] {QuantityColoumnNames.QUANTITY};
			Cursor c = db.query(TABLE_QUANTITY, col, null, null, null, null, null);
			while (c.moveToNext()) {
				int index = c.getColumnIndex(QuantityColoumnNames.QUANTITY);
				quantities.add(c.getString(index));
			}
			c.close();
		} catch (Throwable tr) {
			Log.e(TAG, "getAllQuantities: Error in fetching dishes", tr);
		}
		return quantities;
	}
	
	/**
	 * Gets id for the dish name passed as argument, otherwise returns -1
	 * @param dish
	 * @return
	 */
	public int getDishIdByName(String dish) {
		int dishId = -1;
		try {
			Log.i(TAG, "getDishIdByName: Dish Name: " + dish);
			SQLiteDatabase db = getReadableDatabase();
			String[] col = new String[] {DishColoumnNames.ID};
			String selection = DishColoumnNames.NAME + "=?";
			String[] selArgs = new String[] {dish};
			Cursor c = db.query(true, TABLE_DISH_NAME, col, selection, selArgs, null, null, null, null);
			Log.i(TAG, "getDishIdByName: Num Dishes found with this name in db: " + c.getCount());
			if (c.getCount() > 0) {
				c.moveToFirst();
				dishId = c.getInt(c.getColumnIndex(DishColoumnNames.ID));
			}
			c.close();
		} catch (Throwable tr) {
			Log.e(TAG, "getDishIdByName: Error in fetching id for dish " + dish, tr);
		}
		Log.i(TAG, "getDishIdByName: Dish Id for dish: " + dishId);
		return dishId;
	}
	
	/**
	 * Gets id for the quantity passed as argument, otherwise returns -1
	 * @param quantity
	 * @return
	 */
	public int getQuantityIdByName(String quantity) {
		int quantityId = -1;
		try {
			SQLiteDatabase db = getReadableDatabase();
			String[] col = new String[] {QuantityColoumnNames.ID};
			String selection = QuantityColoumnNames.QUANTITY + "=?";
			String[] selArgs = new String[] {quantity};
			Cursor c = db.query(true, TABLE_QUANTITY, col, selection, selArgs, null, null, null, null);
			if (c.getCount() > 0) {
				c.moveToFirst();
				quantityId = c.getInt(c.getColumnIndex(QuantityColoumnNames.ID));
			}
			c.close();
		} catch (Throwable tr) {
			Log.e(TAG, "getQuantityIdByName: Error in fetching id for quantity " + quantity, tr);
		}
		return quantityId;
	}
	
	/**
	 * Gets dish name for id passed as argument, otherwise null
	 * @param id
	 * @return
	 */
	public String getDishNameById(int id) {
		String dish = null;
		try {
			SQLiteDatabase db = getReadableDatabase();
			String[] col = new String[] {DishColoumnNames.NAME};
			String selection = DishColoumnNames.ID + "=?";
			String[] selArgs = new String[] {String.valueOf(id)};
			Cursor c = db.query(true, TABLE_DISH_NAME, col, selection, selArgs, null, null, null, null);
			if (c.getCount() > 0) {
				c.moveToFirst();
				dish = c.getString(c.getColumnIndex(DishColoumnNames.NAME));
			}
			c.close();
		} catch (Throwable tr) {
			Log.e(TAG, "getDishNameById: Error in fetching dish for id " + id, tr);
		}
		return dish;
	}
	
	/**
	 * Gets the quantity for id passed as argument, otherwise returns null
	 * @param id
	 * @return
	 */
	public String getQuantityNameById(int id) {
		String quantity = null;
		try {
			SQLiteDatabase db = getReadableDatabase();
			String[] col = new String[] {QuantityColoumnNames.QUANTITY};
			String selection = QuantityColoumnNames.ID + "=?";
			String[] selArgs = new String[] {String.valueOf(id)};
			Cursor c = db.query(true, TABLE_QUANTITY, col, selection, selArgs, null, null, null, null);
			if (c.getCount() > 0) {
				c.moveToFirst();
				quantity = c.getString(c.getColumnIndex(QuantityColoumnNames.QUANTITY));
			}
			c.close();
		} catch (Throwable tr) {
			Log.e(TAG, "getQuantityNameById: Error in fetching quantity for id " + id, tr);
		}
		return quantity;
	}
	
	/**
	 * Retrieve the menu from DB
	 * @return
	 */
	public ArrayList<ArrayList<String>> getMenu() {
		ArrayList<ArrayList<String>> menu = new ArrayList<ArrayList<String>>();
		try {
			SQLiteDatabase db = getReadableDatabase();
			Cursor menuCursor = db.query(TABLE_MENU, null, null, null, null, null, null);
			while (menuCursor.moveToNext()) {
				ArrayList<String> menuItem = new ArrayList<String>();
				String dishName = getDishNameById(menuCursor.getInt(menuCursor.getColumnIndex(MenuColoumnNames.DISH_ID)));
				if (dishName == null) {
					continue;
				}
				
				String quantity = getQuantityNameById(menuCursor.getInt(menuCursor.getColumnIndex(MenuColoumnNames.QUANTITY_ID)));
				if (quantity == null) {
					continue;
				}
				
				menuItem.add(dishName);
				menuItem.add(quantity);
				menuItem.add(menuCursor.getString(menuCursor.getColumnIndex(MenuColoumnNames.PRICE)));
				
				menu.add(menuItem);
			}
			menuCursor.close();
		} catch (Throwable tr) {
			Log.e(TAG, "getMenu: Error in fetching menu", tr);
		}
		return menu;
	}
	
	/**
	 * Retrieve the menu for a particular dish from DB
	 * @return
	 */
	public ArrayList<Menu> getMenuByDishId(int dish_id) {
		ArrayList<Menu> menu = new ArrayList<Menu>();
		try {
			SQLiteDatabase db = getReadableDatabase();
			String selection = MenuColoumnNames.DISH_ID + "=?";
			String[] selectionArgs = new String[] {String.valueOf(dish_id)};
			Cursor menuCursor = db.query(TABLE_MENU, null, selection, selectionArgs, null, null, null);
			while (menuCursor.moveToNext()) {
				
				long menu_id = menuCursor.getLong(menuCursor.getColumnIndex(MenuColoumnNames.ID));
				int quantityId = menuCursor.getInt(menuCursor.getColumnIndex(MenuColoumnNames.QUANTITY_ID));
				String price = menuCursor.getString(menuCursor.getColumnIndex(MenuColoumnNames.PRICE));

				menu.add(new Menu(menu_id, dish_id, quantityId, price));
			}
			menuCursor.close();
		} catch (Throwable tr) {
			Log.e(TAG, "getMenu: Error in fetching menu", tr);
		}
		return menu;
	}

	/**
	 * Returns all the customers
	 * @return
	 */
	public ArrayList<Customer> getAllCustomers() {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		try {
			SQLiteDatabase db = getReadableDatabase();
			Cursor c = db.query(TABLE_CUSTOMER, null, null, null, null, null, null);
			while (c.moveToNext()) {
				String name = c.getString(c.getColumnIndex(CustomerColoumnNames.NAME));
				String phone = c.getString(c.getColumnIndex(CustomerColoumnNames.PHONE));
				String balance = c.getString(c.getColumnIndex(CustomerColoumnNames.BALANCE));
				Customer customer = new Customer(name, phone, getFormattedMoney(Double.parseDouble(balance)));
				customer.setId(c.getInt(c.getColumnIndex(CustomerColoumnNames.ID)));
				customers.add(customer);
			}
			c.close();
		} catch (Throwable tr) {
			Log.e(TAG, "getAllDishes: Error in fetching dishes", tr);
		}
		return customers;
	}
	
	/**
	 * Returns the last added customer
	 * @param db 
	 * @return
	 */
	public Customer getLastAddedCustomer(SQLiteDatabase db) {
		Customer customer = null;
		try {
			String orderBy = CustomerColoumnNames.ID + " DESC";
			String limit = "1";
			Cursor c = db.query(TABLE_CUSTOMER, null, null, null, null, null, orderBy, limit);
			if (c.moveToFirst()) {
				customer = new Customer(c.getString(c.getColumnIndex(CustomerColoumnNames.NAME)), 
						c.getString(c.getColumnIndex(CustomerColoumnNames.PHONE)), 
						Double.parseDouble(c.getString(c.getColumnIndex(CustomerColoumnNames.BALANCE)))); 
				customer.setId(c.getLong(c.getColumnIndex(CustomerColoumnNames.ID)));
			}
			c.close();
		} catch (Throwable tr) {
			Log.e(TAG, "getLastAddedCustomer: Error in getting customer id");
		}
		return customer;
	}
	
	/**
	 * Checks whether a customer with passed phone exists in the database
	 * @param db 
	 * @param phone
	 * @return
	 */
	public Customer isCustomerWithPhoneExist(SQLiteDatabase db, String phone) {
		Customer exists = null;
		try {
			String selection = CustomerColoumnNames.PHONE + "=?";
			String[] selectionArgs = new String[] {phone};
			Cursor c = db.query(TABLE_CUSTOMER, null, selection, selectionArgs, null, null, null);
			if (c.getCount() == 1) {
				c.moveToFirst();
				exists = new Customer(c.getString(c.getColumnIndex(CustomerColoumnNames.NAME)), 
						c.getString(c.getColumnIndex(CustomerColoumnNames.PHONE)), 
						Double.parseDouble(c.getString(c.getColumnIndex(CustomerColoumnNames.BALANCE))));
				exists.setId(c.getLong(c.getColumnIndex(CustomerColoumnNames.ID)));
			} else {
				Log.e(TAG, "Multiple customers with same phone number in database exist");
			}
			c.close();
		} catch (Throwable tr) {
			Log.e(TAG, "Error in checking whether customer with phone exists", tr);
		}
		return exists;
	}
	
	/**
	 * Returns the last added order id from orders table
	 * @param db 
	 * @return
	 */
	public long getLastAddedOrderId(SQLiteDatabase db) {
		long id = -1;
		try {
			String orderBy = OrderColoumnNames.ID + " DESC";
			String limit = "1";
			Cursor c = db.query(TABLE_ORDER, null, null, null, null, null, orderBy, limit);
			if (c.moveToFirst()) {
				id = c.getLong(c.getColumnIndex(OrderColoumnNames.ID));
			}
			c.close();
		} catch (Throwable tr) {
			Log.e(TAG, "getLastAddedOrderId: Error in getting last added order id");
		}
		return id;
	}
	
	/**
	 * Gets Customer with passed id
	 * @param id
	 * @return
	 */
	public Customer getCustomerById(long id) {
		Customer customer = null;
		try {
			SQLiteDatabase db = getReadableDatabase();
			String selection = CustomerColoumnNames.ID + "=?";
			String[] selectionArgs = new String[] {String.valueOf(id)};
			Cursor c = db.query(TABLE_CUSTOMER, null, selection, selectionArgs, null, null, null);
			if (c.getCount() > 0) {
				c.moveToFirst();
				customer = new Customer(c.getString(c.getColumnIndex(CustomerColoumnNames.NAME)), 
						c.getString(c.getColumnIndex(CustomerColoumnNames.PHONE)), 
						Double.parseDouble(c.getString(c.getColumnIndex(CustomerColoumnNames.BALANCE))));
				customer.setId(id);
			}
			c.close();
		} catch (Throwable tr) {
			Log.e(TAG, "Error in retreiving customer for customer id " + id);
		}
		return customer;
	}
	
	/**
	 * fetches the menu by menu id from Menu table
	 * @param id
	 * @return
	 */
	public Menu getMenuByMenuId(long id) {
		Menu menu = null;
		try {
			SQLiteDatabase db = getReadableDatabase();
			String selection = MenuColoumnNames.ID + "=?";
			String[] selectionArgs = new String[] {String.valueOf(id)};
			Cursor c = db.query(TABLE_MENU, null, selection, selectionArgs, null, null, null);
			if (c.getCount() > 0) {
				c.moveToFirst();
				menu = new Menu(c.getLong(c.getColumnIndex(MenuColoumnNames.ID)), 
						c.getInt(c.getColumnIndex(MenuColoumnNames.DISH_ID)), 
						c.getInt(c.getColumnIndex(MenuColoumnNames.QUANTITY_ID)), 
						c.getString(c.getColumnIndex(MenuColoumnNames.PRICE)));
			}
		} catch (Throwable tr) {
			Log.e(TAG, "Error in retreiving Menu for menu id " + id);
		}
		return menu;
	}
	
	/**
	 * Fetches the Dishes in order by order id
	 * @param id
	 * @return
	 */
	public ArrayList<Dishes> getDishesInOrderByOrderId(long id) {
		ArrayList<Dishes> dishes = new ArrayList<ViewOrder.Dishes>();
		try {
			SQLiteDatabase db = getReadableDatabase();
			String selection = OrderDetailsColoumnNames.ORDER_ID + "=?";
			String[] selectionArgs = new String[] {String.valueOf(id)};
			Cursor c = db.query(TABLE_ORDER_DETAILS, null, selection, selectionArgs, null, null, null);
			while (c.moveToNext()) {
				Menu menu = getMenuByMenuId(c.getLong(c.getColumnIndex(OrderDetailsColoumnNames.MENU_ID)));
				if (menu != null) {
					dishes.add(new Dishes(menu,
							c.getInt(c.getColumnIndex(OrderDetailsColoumnNames.NUM_ITEMS))));
				}
			}
			c.close();
		} catch (Throwable tr) {
			Log.e(TAG, "Error in retreiving dishes from order_details for order id " + id);
		}
		return dishes;
	}

	/**
	 * Fetches the orders that are to be delivered on the passed date
	 * Performs these steps in this order
	 * - Fetches the orders from orders table based on the passed date
	 * - Fetches the customer information from customer table (retrieved from 1st step)
	 * - Fetches the dishes ordered from order_details table 
	 * @param startMillis
	 * @param endMillis 
	 * @return
	 */
	public ArrayList<ViewOrder> getOrdersForDate(long startMillis, long endMillis) {
		ArrayList<ViewOrder> orders = null;
		// Customer id -> ViewOrder map
		HashMap<Long, ViewOrder> ordersMap = new HashMap<Long, ViewOrder>(); 
		try {
			// Fetch the orders from orders table
			SQLiteDatabase db = getReadableDatabase();
			String[] columns = new String[] {OrderColoumnNames.ID, OrderColoumnNames.CUSTOMER_ID};
			String selection = OrderColoumnNames.DATE + ">=? AND " + OrderColoumnNames.DATE + "<=?";
			String[] selectionArgs = new String[] {String.valueOf(startMillis), String.valueOf(endMillis)};
			Cursor order_c = db.query(TABLE_ORDER, columns, selection, selectionArgs, null, null, null);
			while (order_c.moveToNext()) {
				// Get customer_id and fetch the customer object
				long customer_id = order_c.getLong(order_c.getColumnIndex(OrderColoumnNames.CUSTOMER_ID));
				Customer customer = getCustomerById(customer_id);
				
				// Fetch the stored ViewOrder object for this user or create one and then add it
				ViewOrder viewOrder = null;
				if (ordersMap.containsKey(customer_id)) {
					viewOrder = ordersMap.get(customer_id);
				} else {
					// ViewOrder for this Customer does not exist yet. Add now
					viewOrder = new ViewOrder(customer);
					ordersMap.put(customer_id, viewOrder);
				}
				
				// Get order_id from Cursor and get all the rows from the order_details table for this order_id
				long order_id = order_c.getLong(order_c.getColumnIndex(OrderColoumnNames.ID));
				viewOrder.addDishesToOrder(order_id, getDishesInOrderByOrderId(order_id));
			}
			order_c.close();
			
			// Getting the values of the hashmap (ViewOrder objects for all customers)
			orders = new ArrayList<ViewOrder>(ordersMap.values());
		} catch (Throwable tr) {
			Log.e(TAG, "Error in getting orders for the passed date: " + startMillis + " and " + endMillis);
		}
		return orders;
	}
	
	/**
	 * Returns the names of tables in database
	 * @return
	 */
	public ArrayList<String> getTablesInDatabase() {
		ArrayList<String> tables = new ArrayList<String>();
		try {
			SQLiteDatabase db = getReadableDatabase();
			Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
			while (c.moveToNext()) {
				tables.add(c.getString(0));
			}
		} catch (Throwable tr) {
			Log.e(TAG, "Error in fetching the table names in the database");
		}
		return tables;
	}
	
	/**
	 * Returns Cursor pointing to contents of the table
	 * @param table_name
	 * @return
	 */
	public Cursor getTableContents(String table_name) {
		Cursor c = null;
		try {
			SQLiteDatabase db = getReadableDatabase();
			c = db.rawQuery("SELECT * FROM " + table_name + ";", null);
		} catch (Throwable tr) {
			Log.e(TAG, "Error in fetching the table contents");
		}
		return c;
	}

	/**
	 * Replaces/Modifies a dish in the menu
	 * @param replacedDish
	 * @return
	 */
	public int replaceDishInMenu(Menu replacedDish) {
		int numRows = 0;
		try {
			SQLiteDatabase db = getWritableDatabase();
			String whereClause = MenuColoumnNames.DISH_ID + "=? AND " + MenuColoumnNames.QUANTITY_ID + "=?";
			String[] whereArgs = new String[] {String.valueOf(replacedDish.getDishId()), String.valueOf(replacedDish.getQuantityId())};
			ContentValues values = new ContentValues();
			values.put(MenuColoumnNames.DISH_ID, replacedDish.getDishId());
			values.put(MenuColoumnNames.QUANTITY_ID, replacedDish.getQuantityId());
			values.put(MenuColoumnNames.PRICE, replacedDish.getPrice());
			numRows = db.update(TABLE_MENU, values, whereClause, whereArgs);
		} catch (Throwable tr) {
			Log.e(TAG, "replaceDishInMenu: Error is replacing dish in Menu", tr);
		}
		return numRows;
	}
	
	/**
	 * Deletes the passed dish from the Menu
	 * @param dishToDelete
	 * @return
	 */
	public int deleteDishInMenu(Menu dishToDelete) {
		int rows = 0;
		try {
			SQLiteDatabase db = getWritableDatabase();
			String whereClause = MenuColoumnNames.DISH_ID + "=? AND " + MenuColoumnNames.QUANTITY_ID + "=?";
			String[] whereArgs = new String[] {String.valueOf(dishToDelete.getDishId()), String.valueOf(dishToDelete.getQuantityId())};
			rows = db.delete(TABLE_MENU, whereClause, whereArgs);
		} catch (Throwable tr) {
			Log.e(TAG, "deleteDishInMenu: Error is deleting dish in Menu", tr);
		}
		return rows;
	}
	
	/**
	 * Inserts the newly added dish in DB
	 * @param name
	 * @return
	 */
	public long storeDishName(String name) {
		long row = -1;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(DishColoumnNames.NAME, name);
			SQLiteDatabase db = getWritableDatabase();
			row = db.insert(TABLE_DISH_NAME, null, contentValues);
		} catch (Throwable tr) {
			Log.e(TAG, "storeDishName: Error is saving dish name in DB", tr);
		}
		return row;
	}
	
	/**
	 * Inserts the newly added dish in DB
	 * @param name
	 * @return
	 */
	public long storeQuantity(String quantity) {
		long row = -1;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(QuantityColoumnNames.QUANTITY, quantity);
			SQLiteDatabase db = getWritableDatabase();
			row = db.insert(TABLE_QUANTITY, null, contentValues);
		} catch (Throwable tr) {
			Log.e(TAG, "storeQuantity: Error is saving quantity in DB", tr);
		}
		return row;
	}
	
	/**
	 * Stores the dish in the menu
	 * @param dish
	 * @param quantity
	 * @param price
	 * @return
	 */
	public long storeInMenu(String dish, String quantity, String price) {
		long row = -1;
		try {
			int dishId = getDishIdByName(dish);
			int quantityId = getQuantityIdByName(quantity);
			if (dishId < 0 || quantityId < 0) {
				Log.e(TAG, "storeInMenu: Invalid dishId or quantityId. dishId: " + dishId + ", quantityId: " + quantityId);
				return row;
			}
			ContentValues contentValues = new ContentValues();
			contentValues.put(MenuColoumnNames.DISH_ID, dishId);
			contentValues.put(MenuColoumnNames.QUANTITY_ID, quantityId);
			contentValues.put(MenuColoumnNames.PRICE, price);
			SQLiteDatabase db = getWritableDatabase();
			row = db.insert(TABLE_MENU, null, contentValues);
		} catch (Throwable tr) {
			Log.e(TAG, "storeInMenu: Error is saving entry in Menu", tr);
		}
		return row;
	}
	
	/**
	 * Stores a new customer in the table
	 * @param db 
	 * @param name
	 * @param phone
	 * @param balance 
	 * @return customer id
	 */
	public Customer addNewCustomer(SQLiteDatabase db, String name, String phone, String balance) {
		Customer customer = null;
		boolean success = false;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(CustomerColoumnNames.NAME, name);
			contentValues.put(CustomerColoumnNames.PHONE, phone);
			contentValues.put(CustomerColoumnNames.BALANCE, balance);
			db.insert(TABLE_CUSTOMER, null, contentValues);
			success = true;
		} catch (Throwable tr) {
			Log.e(TAG, "addNewCustomer: Error in adding new customer", tr);
		}
		
		if (success) {
			customer = getLastAddedCustomer(db);
		}
		return customer;
	}
	
	/**
	 * Saves the Order in the table. It performs the following steps in this particular order
	 * All of these steps are performed in single transaction. Hence, they all succeed or nothing succeeds
	 * 1. Finds whether a customer with phone exists or not
	 * 2. If not, adds a new customer in DB else simply updates the balance of existing customer
	 * 3. For newly added customer or existing customer (customer id), it adds an entry in orders table
	 * 4. Retrieves the order_id for newly added order and then adds dishes in order_details table (one row for each dish in the order)
	 * @param order
	 * @return
	 */
	public boolean saveOrderInDB(PlaceOrder order) {
		boolean success = false;
		SQLiteDatabase db = null;
		try {
			db = getWritableDatabase();
			db.beginTransaction();

			// Check if customer exist or not
			Customer customer = isCustomerWithPhoneExist(db, order.getCustomerPhone());
			if (customer == null) {
				// Customer does not exist.
				// Make a new entry in customer table
				customer = addNewCustomer(db, order.getCustomerName(), order.getCustomerPhone(), order.getPriceOfOrder());
			} else {
				// Customer exists
				// Update the balance
				Double current_balance = Double.valueOf(customer.getBalance());
				double new_balance = getFormattedMoney(current_balance + Double.parseDouble(order.getPriceOfOrder()));
				Log.i(TAG, "New balance: " + new_balance);
				customer.setBalance(new_balance);
				updateExistingCustomer(db, customer);
			}
			
			if (customer != null) {
				// Make an entry for this customer in the orders table
				long order_id = addOrderInDB(db, customer.getId(), order.getOrderDate());
				if (order_id != -1) {
					// Insert each of the dishes in the order into the table in a separate row
					ArrayList<PlaceOrder.Dishes> dishesInOrder = order.getDishesInOrder();
					int numDishesInOrder = dishesInOrder.size();
					for (int i = 0; i < numDishesInOrder; i++) {
						ContentValues values = new ContentValues();
						values.put(OrderDetailsColoumnNames.ORDER_ID, order_id);
						values.put(OrderDetailsColoumnNames.MENU_ID, dishesInOrder.get(i).getDish().getMenuId());
						values.put(OrderDetailsColoumnNames.NUM_ITEMS, dishesInOrder.get(i).getNumItems());
						db.insert(TABLE_ORDER_DETAILS, null, values);
					}
					db.setTransactionSuccessful();
					success = true;
				} else {
					Log.e(TAG, "Problem in adding new order in orders table");
				}
			} else {
				Log.e(TAG, "Problem in adding new customer");
			}
		} catch (Throwable tr) {
			Log.e(TAG, "Error is saving order", tr);
			success = false;
		} finally {
			if (db != null) {
				db.endTransaction();
			}
		}
		return success;
	}

	private long addOrderInDB(SQLiteDatabase db, long id, long orderDate) {
		long order_id = -1;
		long rowId = -1;
		try {
			ContentValues values = new ContentValues();
			values.put(OrderColoumnNames.CUSTOMER_ID, id);
			values.put(OrderColoumnNames.DATE, orderDate);
			rowId = db.insert(TABLE_ORDER, null, values);
		} catch (Throwable tr) {
			Log.e(TAG, "Error in creating an entry in orders table", tr);
		}
		
		if (rowId != -1) {
			order_id = getLastAddedOrderId(db);
		}
		return order_id;
	}

	/**
	 * Updates existing customer
	 * @param db 
	 * @param newCustomer
	 * @return
	 */
	public long updateExistingCustomer(SQLiteDatabase db, Customer newCustomer) {
		int numRows = -1;
		try {
			ContentValues values = new ContentValues();
			values.put(CustomerColoumnNames.BALANCE, String.valueOf(newCustomer.getBalance()));
			String whereClause = CustomerColoumnNames.ID + "=?";
			String[] whereArgs = new String[] {String.valueOf(newCustomer.getId())};
			numRows = db.update(TABLE_CUSTOMER, values, whereClause, whereArgs);
		} catch (Throwable tr) {
			Log.e(TAG, "Error updating existing customer", tr);
		}
		return numRows;
	}

	/**
	 * Saves the payment made by customer and updates their balance in single transaction
	 * @param customer
	 * @param millis 
	 * @param payment
	 * @return
	 */
	public Customer savePaymentForCustomerInSingleTransaction(Customer customer, long millis, String payment) {
		Customer new_customer = null;
		SQLiteDatabase db = null;
		try {
			db = getWritableDatabase();
			db.beginTransaction();
			// Make and entry in payment table
			ContentValues values = new ContentValues();
			values.put(PaymentColoumnNames.CUSTOMER_ID, customer.getId());
			values.put(PaymentColoumnNames.PAYMENT, payment);
			values.put(PaymentColoumnNames.DATE, millis);
			long row_id = db.insert(TABLE_PAYMENT, null, values);
			if (row_id != -1) {
				double new_balance = getFormattedMoney(Double.valueOf(customer.getBalance()) - Double.parseDouble(payment));
				// Update the Customer table to reflect new balance on this customer
				values.clear();
				values.put(CustomerColoumnNames.BALANCE, String.valueOf(new_balance));
				String whereClause = CustomerColoumnNames.ID + "=?";
				String[] whereArgs = new String[] {String.valueOf(customer.getId())};
				int num_rows_affected = db.update(TABLE_CUSTOMER, values, whereClause, whereArgs);
				if (num_rows_affected > 0) {
					new_customer = new Customer(customer.getName(), customer.getPhone(), new_balance);
					new_customer.setId(customer.getId());
					db.setTransactionSuccessful();
				}
			}
		} catch (Throwable tr) {
			Log.e(TAG, "savePaymentForCustomerInSingleTransaction: Error saving payment for customer", tr);
		} finally {
			if (db != null) {
				db.endTransaction();
			}
		}
		return new_customer;
	}
	
	/**
	 * Closes the open DB
	 */
	public void closeDB() {
		close();
	}
	
	/**
	 * Returns money in proper number of digits
	 * @param money
	 * @return
	 */
	public double getFormattedMoney(double money) {
		return (Math.round(100.0 * money) / 100.0);
	}

	/**
	 * Coloumn names for "dishes" table
	 * @author agupta
	 *
	 */
	static final class DishColoumnNames {
		
		static final String ID = "id";
		static final String NAME = "name";
	}

	/**
	 * Coloumn names for "quantity" table
	 * @author agupta
	 *
	 */
	static final class QuantityColoumnNames {
		
		static final String ID = "id";
		static final String QUANTITY = "quantity";
	}

	/**
	 * Coloumn names for "menu" table
	 * @author agupta
	 *
	 */
	static final class MenuColoumnNames {
		
		static final String ID = "id";
		static final String DISH_ID = "dish_id";
		static final String QUANTITY_ID = "quantity_id";
		static final String PRICE = "price";
	}

	/**
	 * Coloumn names for "customer" table
	 * @author agupta
	 *
	 */
	static final class CustomerColoumnNames {
		
		static final String ID = "id";
		static final String NAME = "name";
		static final String PHONE = "phone";
		static final String BALANCE = "balance";
	}

	/**
	 * Coloumn names for "order" table
	 * @author agupta
	 *
	 */
	static final class OrderColoumnNames {
		
		static final String ID = "id";
		static final String CUSTOMER_ID = "customer_id";
		static final String DATE = "order_date";
	}

	/**
	 * Coloumn names for "order_details" table
	 * @author agupta
	 *
	 */
	static final class OrderDetailsColoumnNames {
		
		static final String ID = "id";
		static final String ORDER_ID = "order_id";
		static final String DATE = "order_date";
		static final String MENU_ID = "menu_id";
		static final String NUM_ITEMS = "num_items";
	}

	/**
	 * Coloumn names for "payment" table
	 * @author agupta
	 *
	 */
	static final class PaymentColoumnNames {
		
		static final String ID = "id";
		static final String CUSTOMER_ID = "customer_id";
		static final String DATE = "payment_date";
		static final String PAYMENT = "payment";
	}
}
